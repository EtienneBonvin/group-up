package ch.epfl.sweng.groupup.lib.fileStorage;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
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
     * Upload a bitmap to the Firebase storage.
     * @param uuid the id of the uploader.
     * @param bitmap the bitmap to upload.
     */
    @Override
    public void uploadFile(String uuid, CompressedBitmap bitmap) {
        queuedUploads.offer(new AsyncUploadFileTask(this, uuid, bitmap));
    }

    /**
     * Upload a video to the firebase storage
     * @param uuid
     * @param file
     */
    public void uploadFile(String uuid, File file){
        //TODO
        queuedUploads.offer(new AsyncUploadFileTask(this,uuid, file));
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
     * Removes all images a member uploaded on the database.
     * @param uuid the uuid of the member.
     */
    public void removeImageFromUser(String uuid){
        removeOneImageFromUser(uuid, 0);
    }

    /**
     * Remove the index-th image uploaded by the user.
     * @param uuid the uuid of the member.
     * @param index the index of the image to remove.
     */
    private void removeOneImageFromUser(final String uuid, final int index){
        StorageReference imageRef = storageRef.child(event.getUUID()+"/"+uuid+"/"+index);

        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                removeOneImageFromUser(uuid, index + 1);
            }
        });
    }

    /**
     * Kill the proxy, no other operations will be emitted from it.
     */
    public void kill(){
        killed.set(true);
    }

    private void effectivelyUploadFile(String uuid, CompressedBitmap bitmap){
        final Counter memberCount = memberCounter.get(uuid);
        StorageReference imageRef = storageRef.child(event.getUUID()+"/"+uuid+"/"+memberCount.getCount());
        imageRef.putBytes(bitmap.asByteArray())
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    memberCount.increment();
                    if(!queuedUploads.isEmpty()){
                        queuedUploads.poll().execute();
                    }else{
                        createAsyncDownloadTask().execute();
                    }
                }
            });
    }

    private void effectivelyUploadFile(String uuid, File file){
        //TODO
        final Counter memberCount=memberCounter.get(uuid);
        Uri uri = Uri.fromFile(file);
        StorageReference videoRef = storageRef.child(event.getUUID()+"/"+uuid+"/"+memberCount.getCount());
        videoRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                memberCount.increment();
                if(!queuedUploads.isEmpty()){
                    queuedUploads.poll().execute();
                }else{
                    createAsyncDownloadTask().execute();
                }
            }
        });
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
        private  File file;
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
            this.file=null;
        }
        /**
         * Constructor for AsyncDownloadFileTask.
         * Assign a weak reference to the proxy to be able to verify its presence later.
         * Avoids leaks.
         * @param proxy the FirebaseFileProxy asking for the asynchronous task.
         * @param uuid the user's id.
         * @param file the file to upload.
         */
        private AsyncUploadFileTask(FirebaseFileProxy proxy, String uuid, File file){
            wproxy = new WeakReference<>(proxy);
            this.uuid = uuid;
            this.image = null;
            this.file=file;
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
                    if (image!=null){
                    proxy.effectivelyUploadFile(uuid, image);
                    }
                    else {
                        proxy.effectivelyUploadFile(uuid,file);
                    }
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
                } else if(!killed.get()) {
                    createAsyncDownloadTask().execute();
                }
            }else{
                allRecovered.set(false);
                if(!killed.get()) {
                    createAsyncDownloadTask().execute();
                }
            }
        }
    }
}
