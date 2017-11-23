package ch.epfl.sweng.groupup.activity.event.files;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * CompressedBitmap class.
 * Allows to store a bitmap under the form of a compressed byte array, reducing the amount of space
 * needed to store it.
 */
public class CompressedBitmap {
    private final byte[] compressed;

    public CompressedBitmap(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        compressed = baos.toByteArray();
    }

    public CompressedBitmap(byte[] compressed){
        this.compressed = compressed.clone();
    }

    public Bitmap asBitmap(){
        return BitmapFactory.decodeByteArray(compressed, 0, compressed.length);
    }

    public byte[] asByteArray(){
        return compressed.clone();
    }
}
