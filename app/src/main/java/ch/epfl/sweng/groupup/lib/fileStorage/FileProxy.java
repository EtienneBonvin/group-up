package ch.epfl.sweng.groupup.lib.fileStorage;

import ch.epfl.sweng.groupup.lib.CompressedBitmap;
import java.io.File;
import java.util.List;


/**
 * FileProxy interface.
 * Describes the behavior of a proxy communicating with some external storage.
 */
public interface FileProxy {

    /**
     * Get all the files from the external storage.
     *
     * @return a list of the stored images in the external storage.
     */
    List<CompressedBitmap> getImagesFromDatabase();

    List<File> getVideosFromDatabase();

    /**
     * Indicates whether all the files have been recovered.
     *
     * @return true if all files have been recovered from the external storage, false otherwise.
     */
    boolean isAllRecovered();

    /**
     * Upload a file into the external storage.
     *
     * @param uuid   the uuid of the user uploading the file.
     * @param bitmap the image to upload.
     */
    void uploadFile(String uuid, CompressedBitmap bitmap);
}
