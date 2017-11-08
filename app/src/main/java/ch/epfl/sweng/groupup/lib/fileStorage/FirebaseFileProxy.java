package ch.epfl.sweng.groupup.lib.fileStorage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

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

    public FirebaseFileProxy(Event event){
        this.event = event;
        recoveredImages = new ArrayList<>();
        memberCounter = new HashMap<>();
        for(Member member : event.getEventMembers()){
            memberCounter.put(member.getUUID().getOrElse("Default ID"), new Counter());
        }

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
    public List<Bitmap> downloadFromDatabase() {
        return recoveredImages;
    }

    private boolean getNewImages(){
        boolean noErrors;
        String memberId;
        Counter memberCount;
        StorageReference folderRef = storageRef.child(event.getUUID());
        StorageReference imageRef;

        for(Member member : event.getEventMembers()) {
            memberId = member.getUUID().get();
            noErrors = true;
            memberCount = memberCounter.get(memberId);
            final Counter finalCounter = new Counter(memberCount.getCount());
            while (noErrors && memberCount.getCount() < 100) {
                try {
                    final String finalId = memberId;
                    imageRef = folderRef.child(memberId+"/"+memberCount.getCount());
                    memberCount.increment();
                    Log.e("Yolo", imageRef.getPath());
                    imageRef.getBytes(Long.MAX_VALUE)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    recoveredImages.add(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                    memberCounter.remove(finalId);
                                    finalCounter.increment();
                                    memberCounter.put(finalId, new Counter(finalCounter.getCount()));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Catch the failures
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                    noErrors = false;
                }
                if(noErrors){

                }
            }
        }
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

        private int getCount(){
            return count;
        }
    }
}
