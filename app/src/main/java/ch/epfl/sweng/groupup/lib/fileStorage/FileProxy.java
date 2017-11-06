package ch.epfl.sweng.groupup.lib.fileStorage;

import android.graphics.Bitmap;

import java.util.List;

public interface FileProxy {

    void uploadFile(Bitmap bitmap);

    List<Bitmap> downloadFromDatabase();

}
