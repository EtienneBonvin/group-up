package ch.epfl.sweng.groupup.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;


/**
 * CompressedBitmap class.
 * Allows to store a bitmap under the form of a compressed byte array, reducing the amount of space
 * needed to store it.
 */
public class CompressedBitmap {

    private final byte[] compressed;

    /**
     * Constructor for the compressed bitmap taking a bitmap as an argument.
     * @param bitmap the bitmap the object should represent.
     */
    public CompressedBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        compressed = baos.toByteArray();
    }


    /**
     * Constructor for the compressed bitmap taking a byte array as an argument.
     * @param compressed the compressed byte array representing a bitmap.
     */
    public CompressedBitmap(byte[] compressed) {
        this.compressed = compressed.clone();
    }


    /**
     * Returns the object as a Bitmap object.
     * @return a Bitmap of the object.
     */
    public Bitmap asBitmap() {
        return BitmapFactory.decodeByteArray(compressed, 0, compressed.length);
    }


    /**
     * Returns the object as a compressed byte array.
     * @return the Bitmap as a byte array.
     */
    public byte[] asByteArray() {
        return compressed.clone();
    }


    /**
     * Overrides equals method.
     * @param o the object to be compared to.
     * @return true if this and the object o are equals, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CompressedBitmap that = (CompressedBitmap) o;

        return Arrays.equals(compressed, that.compressed);
    }
}
