package ch.epfl.sweng.groupup.lib.fileStorage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FirebaseFileProxy implements FileProxy {

    List<DataBufferObserver> observers;

    private int counter = 0;

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private final String eventId;
    private List<Bitmap> recoveredImages;

    public FirebaseFileProxy(String eventId){
        this.eventId = eventId;
        recoveredImages = new ArrayList<>();
        getInitialImages();
        observers = new ArrayList<>();
    }

    @Override
    public void uploadFile(Bitmap bitmap) {
        StorageReference imageRef = storageRef.child(eventId+"/"+(counter++));

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

    private void getInitialImages(){

        /*StorageReference imageRef;
        boolean noErrors = true;
        imageRef = storageRef.child(eventId+"/0");
        try {
            imageRef.getBytes(1024*1024)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            recoveredImages.add(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                            counter++;
                        }
                    });
        }catch(Exception e){
            e.printStackTrace();
            noErrors = false;
        }*/
    }
}
