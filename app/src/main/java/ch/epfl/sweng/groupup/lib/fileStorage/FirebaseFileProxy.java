package ch.epfl.sweng.groupup.lib.fileStorage;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import ch.epfl.sweng.groupup.activity.event.files.CompressedBitmap;
import ch.epfl.sweng.groupup.lib.Watchee;
import ch.epfl.sweng.groupup.lib.Watcher;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;

/**
 * FirebaseFileProxy
 * Manages the communications between the application and the Firebase Storage.
 */
public class FirebaseFileProxy implements FileProxy, Watchee {

    //TODO check if the download and upload is stable on low internet connectivity

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef = storage.getReference();

    private final Event event;

    private List<CompressedBitmap> recoveredImages;
    private Map<String, Counter> memberCounter;
    private Set<Watcher> watchers;
    private static Queue<AsyncUploadFileTask> queuedUploads;

    private final SuperBoolean operating;
    private final TimeBomb ticker;
    private final SuperBoolean killed;
    private final SuperBoolean allRecovered;

    // One MB is the maximum file size while using Firebase Storage.
    public final static  int MAX_FILE_SIZE = 1000*1000;

    /**
     * Constructor for FirebaseFileProxy
     * @param event the event to which the proxy is linked.
     */
    public FirebaseFileProxy(Event event){
        this.event = event;
        killed = new SuperBoolean(false);
        ticker = new TimeBomb(event.getEventMembers().size());
        watchers = new HashSet<>();
        recoveredImages = new ArrayList<>();
        memberCounter = new HashMap<>();
        for(Member member : event.getEventMembers()){
            memberCounter.put(member.getUUID().getOrElse("Default ID"), new Counter());
        }
        operating = new SuperBoolean(false);
        allRecovered = new SuperBoolean(false);
        queuedUploads = new ArrayDeque<>();
        AsyncDownloadFileTask adft = createAsyncDownloadTask();
        adft.execute();
    }

    /**
     * Upload a file to the Firebase storage.
     * @param uuid the id of the uploader.
     * @param bitmap the bitmap to upload.
     */
    @Override
    public void uploadFile(String uuid, CompressedBitmap bitmap) {
        queuedUploads.offer(new AsyncUploadFileTask(this, uuid, bitmap));
    }

    /**
     * Indicates whether all files have been recovered from the Firebase Storage.
     * @return true if all files have been recovered, false otherwise.
     */
    @Override
    public boolean isAllRecovered(){
        return allRecovered.get();
    }

    /**
     * Kill the proxy, no other operations will be emitted from it.
     */
    public void kill(){
        killed.set(true);
    }

    private void effectivelyUploadFile(String uuid, CompressedBitmap bitmap){
        Counter memberCount = memberCounter.get(uuid);
        StorageReference imageRef = storageRef.child(event.getUUID()+"/"+uuid+"/"+memberCount.getCount());

        Bitmap scaledBitmap = scaleBitmap(bitmap.asBitmap());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        imageRef.putBytes(data);
        scaledBitmap.recycle();
    }

    //TODO this method does nothing for now (the bitmap given is always < 1MB)
    //This can be changed to blur all images that are too big.
    private Bitmap scaleBitmap(Bitmap bitmap){
        Bitmap scaledBitmap = bitmap;
        while(scaledBitmap.getByteCount()/8 > MAX_FILE_SIZE){
            Log.e("Alpha", scaledBitmap.getByteCount()+"");
            Bitmap result = fastblur(scaledBitmap);
            scaledBitmap = result.copy(result.getConfig(), false);
            Log.e("Zoulou", scaledBitmap.getByteCount()+"");
        }
        return scaledBitmap;
    }

    /**
     * Return the last images recovered from the storage.
     * @return a list of bitmap of the pictures of the event.
     */
    @Override
    public List<CompressedBitmap> getFromDatabase() {
        return new ArrayList<>(recoveredImages);
    }

    private AsyncDownloadFileTask createAsyncDownloadTask(){
        return new AsyncDownloadFileTask(this);
    }

    /**
     * Refreshes the images of the proxy with the content of the database.
     */
    private void getNewImages(){
        if(operating.get()){
            return;
        }
        operating.set(true);
        String memberId;
        StorageReference folderRef = storageRef.child(event.getUUID());
        StorageReference imageRef;

        for(Member member : event.getEventMembers()) {
            memberId = member.getUUID().get();
            final Counter memberCount = memberCounter.get(memberId);
            try {
                imageRef = folderRef.child(memberId+"/"+memberCount.getCount());

                imageRef.getBytes(Long.MAX_VALUE)
                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                recoveredImages.add(
                                        new CompressedBitmap(bytes));
                                memberCount.increment();
                                notifyAllWatchers();
                                ticker.tick(true);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            ticker.tick(false);
                            }
                        });
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    @Override
    public void notifyAllWatchers() {
        for(Watcher w : watchers){
            w.notifyWatcher();
        }
    }

    @Override
    public void addWatcher(Watcher newWatcher) {
        watchers.add(newWatcher);
    }

    @Override
    public void removeWatcher(Watcher watcher) {
        if(watchers.contains(watcher))
            watchers.remove(watcher);
    }

    /**
     * Private class Counter
     * Useful to count the files added by each members of the group.
     */
    private class Counter {
        private int count;

        /**
         * Default constructor of the counter.
         * Initialize the count to 0.
         */
        private Counter(){
            count = 0;
        }

        /**
         * Increment the counter of one unit.
         */
        private void increment(){
            count ++;
        }

        /**
         * Get the actual count of the counter.
         * @return the actual count of the counter.
         */
        private int getCount(){
            return count;
        }
    }

    private static class SuperBoolean {
        private boolean value;

        private SuperBoolean(boolean initialValue){
            value = initialValue;
        }

        private boolean get(){
            return value;
        }

        private void set(boolean newValue){
            value = newValue;
        }
    }

    /**
     * Private class AsyncDownloadTask.
     * Allow the Proxy to execute tasks in background, thus not slowing down the
     * whole application.
     */
    private static class AsyncDownloadFileTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<FirebaseFileProxy> wproxy;

        /**
         * Constructor for AsyncDownloadFileTask.
         * Assign a weak reference to the proxy to be able to verify its presence later.
         * Avoids leaks.
         * @param proxy the FirebaseFileProxy asking for the asynchronous task.
         */
        private AsyncDownloadFileTask(FirebaseFileProxy proxy){
            wproxy = new WeakReference<>(proxy);
        }

        /**
         * Get the new images in background.
         * @param voids not used.
         */
        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseFileProxy proxy = wproxy.get();
            if(proxy != null) {
                try {
                    proxy.getNewImages();
                }catch(Exception e){
                    // TODO check if exceptions needs to e caught
                }
            }
            return null;
        }
    }

    private static class AsyncUploadFileTask extends AsyncTask<Void, Void, Void>{

        private final WeakReference<FirebaseFileProxy> wproxy;
        private final String uuid;
        private final CompressedBitmap image;

        /**
         * Constructor for AsyncDownloadFileTask.
         * Assign a weak reference to the proxy to be able to verify its presence later.
         * Avoids leaks.
         * @param proxy the FirebaseFileProxy asking for the asynchronous task.
         * @param uuid the user's id.
         * @param image the image to upload.
         */
        private AsyncUploadFileTask(FirebaseFileProxy proxy, String uuid, CompressedBitmap image){
            wproxy = new WeakReference<>(proxy);
            this.uuid = uuid;
            this.image = image;
        }

        /**
         * Upload a new image in background.
         * @param voids unused.
         * @return nothing.
         */
        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseFileProxy proxy = wproxy.get();
            if(proxy != null) {
                try {
                    proxy.effectivelyUploadFile(uuid, image);
                }catch(Exception e){
                    //TODO check if exceptions should be caught
                }
            }
            return null;
        }
    }

    private class TimeBomb {

        private final int timer;
        private int count;
        private int errorCount;

        private TimeBomb(int timer){
            this.timer = timer;
            count = 0;
            errorCount = 0;
        }

        private void tick(boolean success){
            count++;
            if(!success)
                errorCount++;
            if(count == timer){
                operating.set(false);
                if(errorCount == timer)
                    boom(true);
                else
                    boom(false);
                count = 0;
                errorCount = 0;
            }
        }

        private void boom(boolean allFails){
            if(allFails) {
                allRecovered.set(true);
                if (!queuedUploads.isEmpty()) {
                    queuedUploads.poll().execute();
                }
            }else{
                allRecovered.set(false);
            }
            if(!killed.get()) {
                createAsyncDownloadTask().execute();
            }
        }
    }

    /*
     * Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>
     */
    private Bitmap fastblur(Bitmap sentBitmap) {
        Log.e("Bravo", "");

        // Settings of the blurring algorithm.
        int radius = 1;
        float scale = 1f;

        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
}
