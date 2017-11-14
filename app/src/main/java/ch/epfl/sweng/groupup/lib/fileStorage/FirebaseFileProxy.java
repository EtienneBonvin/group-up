package ch.epfl.sweng.groupup.lib.fileStorage;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import ch.epfl.sweng.groupup.lib.Watchee;
import ch.epfl.sweng.groupup.lib.Watcher;
import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;

/**
 * FirebaseFileProxy
 * Manages the communications between the application and the Firebase Storage.
 */
public class FirebaseFileProxy implements FileProxy, Watchee {

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef = storage.getReference();
    private final Event event;
    private Set<Bitmap> recoveredImages;
    private Map<String, Counter> memberCounter;
    private final SuperBoolean operating;
    private Set<Watcher> watchers;
    private final TimeBomb ticker;
    private static Queue<AsyncUploadFileTask> queuedUploads;
    private final SuperBoolean killed;

    /**
     * Constructor for FirebaseFileProxy
     * @param event the event to which the proxy is linked.
     */
    public FirebaseFileProxy(Event event){
        this.event = event;
        killed = new SuperBoolean(false);
        ticker = new TimeBomb(event.getEventMembers().size());
        watchers = new HashSet<>();
        recoveredImages = new HashSet<>();
        memberCounter = new HashMap<>();
        for(Member member : event.getEventMembers()){
            memberCounter.put(member.getUUID().getOrElse("Default ID"), new Counter());
        }
        operating = SuperBoolean.getSuperBooleanInstance();
        queuedUploads = new PriorityQueue<>();
        AsyncDownloadFileTask adft = new AsyncDownloadFileTask(this);
        adft.execute();
    }

    /**
     * Upload a file to the Firebase storage.
     * @param uuid the id of the uploader.
     * @param bitmap the bitmap to upload.
     */
    @Override
    public void uploadFile(String uuid, Bitmap bitmap) {
        queuedUploads.offer(new AsyncUploadFileTask(this, uuid, bitmap));
        Log.e("added", " in queue");
    }

    public void kill(){
        killed.set(true);
    }

    private void effectivelyUploadFile(String uuid, Bitmap bitmap){
        // TODO put in queue and manage when all image are recovered.
        Counter memberCount = memberCounter.get(uuid);
        StorageReference imageRef = storageRef.child(event.getUUID()+"/"+uuid+"/"+memberCount.getCount());
        memberCounter.remove(uuid);
        memberCount.increment();
        memberCounter.put(uuid, new Counter(memberCount.getCount()));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        //TODO verify : seems like big files makes the application bug
        try {
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //TODO : manage failure
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //TODO : manage success
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });
        }catch(OutOfMemoryError ex){
            //TODO : catch the case when the bucket is full ?
        }
    }

    /**
     * Return the last images recovered from the storage.
     * The proxy refreshes its content asynchronously each time this
     * method is called.
     * @return a list of bitmap of the pictures of the event.
     */
    @Override
    public List<Bitmap> getFromDatabase() {
        AsyncDownloadFileTask adft = new AsyncDownloadFileTask(this);
        adft.execute();
        return new ArrayList<>(recoveredImages);
    }

    @Override
    public boolean isRecovering(){
        return operating.get();
    }

    private Queue<AsyncUploadFileTask> getQueudTasks(){
        return new PriorityQueue<>(queuedUploads);
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
            final Counter imageCount = new Counter(memberCount.getCount());
            try {
                imageRef = folderRef.child(memberId+"/"+imageCount.getCount());
                imageCount.increment();

                imageRef.getBytes(Long.MAX_VALUE)
                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                recoveredImages.add(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
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
         * Constructor with one argument for the counter.
         * Initialize the counter to the given value.
         * @param count the value to initialize the count.
         */
        private Counter(int count){
            this.count = count;
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
        private static SuperBoolean instance;
        private boolean value;

        private SuperBoolean(boolean initialValue){
            value = initialValue;
        }

        private static SuperBoolean getSuperBooleanInstance(){
            if(instance == null){
                instance = new SuperBoolean(false);
            }
            return instance;
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
    private class AsyncDownloadFileTask extends AsyncTask<Void, Void, Void> {
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
                    //TODO check if exceptions should be managed
                }
            }
            return null;
        }
    }

    private class AsyncUploadFileTask extends AsyncTask<Void, Void, Void>{

        private final WeakReference<FirebaseFileProxy> wproxy;
        private final String uuid;
        private final Bitmap image;

        /**
         * Constructor for AsyncDownloadFileTask.
         * Assign a weak reference to the proxy to be able to verify its presence later.
         * Avoids leaks.
         * @param proxy the FirebaseFileProxy asking for the asynchronous task.
         * @param uuid the user's id.
         * @param image the image to upload.
         */
        private AsyncUploadFileTask(FirebaseFileProxy proxy, String uuid, Bitmap image){
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
                    //TODO check if exceptions should be managed
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
            operating.set(false);
            if(count == timer){
                if(errorCount == timer)
                    boom(true);
                else
                    boom(false);
                count = 0;
                errorCount = 0;
            }
        }

        private void boom(boolean allFails){
            if(!killed.get()) {
                if(allFails) {
                    while (queuedUploads.size() > 0) {
                        queuedUploads.poll().execute();
                    }
                }
                createAsyncDownloadTask().execute();
            }
        }
    }
}
