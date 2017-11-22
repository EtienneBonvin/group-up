package ch.epfl.sweng.groupup.lib.fileStorage;

import java.util.List;

import ch.epfl.sweng.groupup.activity.event.files.CompressedBitmap;

/**
 * FileProxy interface.
 * Describes the behavior of a proxy communicating with some external storage.
 */
public interface FileProxy {

    /**
     * Upload a file into the external storage.
     * @param uuid the uuid of the user uploading the file.
     * @param bitmap the image to upload.
     */
    void uploadFile(String uuid, CompressedBitmap bitmap);

    /**
     * Get all the files from the external storage.
     * @return a list of the stored images in the external storage.
     */
    List<CompressedBitmap> getFromDatabase();

    /**
     * Indicates whether all the files have been recovered.
     * @return true if all files have been recovered from the external storage, false otherwise.
     */
    boolean isAllRecovered();
}
