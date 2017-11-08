package ch.epfl.sweng.groupup.lib.fileStorage;

import android.graphics.Bitmap;

import java.util.List;

public interface FileProxy {

    void uploadFile(String uuid, Bitmap bitmap);

    List<Bitmap> downloadFromDatabase();

}
