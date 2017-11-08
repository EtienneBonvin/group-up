package ch.epfl.sweng.groupup.lib.fileStorage;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.groupup.object.account.Member;
import ch.epfl.sweng.groupup.object.event.Event;

public class FirebaseFileProxy implements FileProxy {

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef = storage.getReference();
    private final Event event;
    private List<Bitmap> recoveredImages;
    private Map<String, Counter> memberCounter;
    private boolean recovering;

    public FirebaseFileProxy(Event event){
        this.event = event;
        recoveredImages = new ArrayList<>();
        memberCounter = new HashMap<>();
        for(Member member : event.getEventMembers()){
            memberCounter.put(member.getUUID().getOrElse("Default ID"), new Counter());
        }
        AsyncDownloadFileTask adft = new AsyncDownloadFileTask();
        adft.execute();
        getNewImages();
    }

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

    @Override
    public List<Bitmap> getFromDatabase() {
        AsyncDownloadFileTask adft = new AsyncDownloadFileTask();
        adft.execute();
        return new ArrayList<>(recoveredImages);
    }

    private boolean getNewImages(){
        if(recovering){
            return true;
        }
        recovering = true;
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
                    final String finalId = memberId;
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
        recovering = false;
        return true;
    }

    private class Counter {
        private int count;

        private Counter(){
            count = 0;
        }

        private Counter(int count){
            this.count = count;
        }

        private void increment(){
            count ++;
        }

        private void decrement(){count --;}

        private int getCount(){
            return count;
        }
    }

    private class AsyncDownloadFileTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            getNewImages();
            return true;
        }
    }
}
