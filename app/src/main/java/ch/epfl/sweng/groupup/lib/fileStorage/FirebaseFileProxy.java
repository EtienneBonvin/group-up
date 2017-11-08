package ch.epfl.sweng.groupup.lib.fileStorage;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;

/**
 * FirebaseFileProxy
 * Manages the communications between the application and the Firebase Storage.
 */
public class FirebaseFileProxy implements FileProxy {

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef = storage.getReference();
    private final Event event;
    private List<Bitmap> recoveredImages;
    private Map<String, Counter> memberCounter;
    private boolean operating;

    /**
     * Constructor for FirebaseFileProxy
     * @param event the event to which the proxy is linked.
     */
    public FirebaseFileProxy(Event event){
        this.event = event;
        recoveredImages = new ArrayList<>();
        memberCounter = new HashMap<>();
        for(Member member : event.getEventMembers()){
            memberCounter.put(member.getUUID().getOrElse("Default ID"), new Counter());
        }
        AsyncDownloadFileTask adft = new AsyncDownloadFileTask(this);
        adft.execute();
        operating = false;
        getNewImages();
    }

    /**
     * Upload a file to the Firebase storage.
     * @param uuid the id of the uploader.
     * @param bitmap the bitmap to upload.
     */
    @Override
    public void uploadFile(String uuid, Bitmap bitmap) {
        Counter memberCount = memberCounter.get(uuid);
        StorageReference imageRef = storageRef.child(event.getUUID()+"/"+uuid+"/"+memberCount.getCount());
        memberCounter.remove(uuid);
        memberCount.increment();
        memberCounter.put(uuid, new Counter(memberCount.getCount()));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

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

    /**
     * Refreshes the images of the proxy with the content of the database.
     */
    private void getNewImages(){
        if(operating){
            return;
        }
        operating = true;
        boolean noErrors;
        String memberId;
        StorageReference folderRef = storageRef.child(event.getUUID());
        StorageReference imageRef;

        for(Member member : event.getEventMembers()) {
            memberId = member.getUUID().get();
            noErrors = true;
            final Counter memberCount = memberCounter.get(memberId);
            final Counter imageCount = new Counter(memberCount.getCount());
            while (noErrors && memberCount.getCount() < 100) {
                try {
                    imageRef = folderRef.child(memberId+"/"+imageCount.getCount());
                    imageCount.increment();
                    imageRef.getBytes(Long.MAX_VALUE)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    recoveredImages.add(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                    memberCount.increment();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Catch the failures
                                }
                            });
                } catch (Exception e) {
                    //e.printStackTrace();
                    noErrors = false;
                }
            }
        }
        operating = false;
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

    /**
     * Private class AsyncDownloadTask.
     * Allow the Proxy to execute tasks in background, thus not slowing down the
     * whole application.
     */
    private class AsyncDownloadFileTask extends AsyncTask<Void, Integer, Void> {
        private final WeakReference<FirebaseFileProxy> wRef;

        /**
         * Constructor for AsyncDownloadFileTask.
         * Assign a weak reference to the proxy to be able to verify its presence later.
         * Avoids leaks.
         * @param proxy the FirebaseFileProxy asking for the asynchronous task.
         */
        private AsyncDownloadFileTask(FirebaseFileProxy proxy){
            wRef = new WeakReference<>(proxy);
        }

        /**
         * Get the new images in background.
         * @param voids not used.
         * @return true.
         */
        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseFileProxy proxy = wRef.get();
            if(proxy != null) {
                getNewImages();
            }
            return null;
        }
    }
}
