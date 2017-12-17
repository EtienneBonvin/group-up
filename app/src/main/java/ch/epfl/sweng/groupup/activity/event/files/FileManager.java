package ch.epfl.sweng.groupup.activity.event.files;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.event.description.EventDescriptionActivity;
import ch.epfl.sweng.groupup.lib.AndroidHelper;

import ch.epfl.sweng.groupup.lib.CompressedBitmap;
import ch.epfl.sweng.groupup.lib.Watcher;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.event.Event;

import static android.app.Activity.RESULT_OK;

/**
 * FileManager class.
 * Contains all methods and attributes relative to the file management of an event.
 */
@SuppressWarnings("WeakerAccess")
public class FileManager implements Watcher {


    private EventDescriptionActivity activity;

    private final int COLUMNS = 3;
    private final int ROWS = 4;
    private int columnWidth;
    private int rowHeight;
    private final Event event;
    private int imagesAdded = 0;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_VIDEO_CAPTURE = 2;
    private String mCurrentFilePath = "";

    /**
     * Creates a FileManager for a particular EventDescription activity.
     *
     * @param activity the activity this FileManager is linked to.
     */
    public FileManager(final EventDescriptionActivity activity) {
        this.activity = activity;

        initializeTakePicture();
        initializeTakeVideo();
        Intent i = activity.getIntent();
        final int eventIndex = i.getIntExtra(activity.getString(R.string.event_listing_extraIndex), -1);
        if (eventIndex > -1) {
            event = Account.shared.getEvents().get(eventIndex);
        } else {
            event = null;
        }

        if (event != null)
            event.addWatcher(this);

        // Set onClickListeners to add files
        // TODO adding videos.
        activity.findViewById(R.id.add_files).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                /*Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activity.startActivityForResult(intent, 0);*/
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("*/*");
                activity.startActivityForResult(intent, 0);
            }
        });

        // Set onClickListener to create aftermovie
        activity.findViewById(R.id.create_aftermovie).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(activity, SlideshowActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(activity.getString(R.string.event_listing_extraIndex), eventIndex);
                activity.startActivity(i);
            }
        });

        // Set the GridLayout and initially get the height and width of the rows and columns.
        final GridLayout grid = activity.findViewById(R.id.image_grid);
        final RelativeLayout container = activity.findViewById(R.id.scroll_view_container);
        ViewTreeObserver vto = container.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                columnWidth = container.getMeasuredWidth() / COLUMNS;
                rowHeight = container.getMeasuredHeight() / ROWS;
            }
        });

        ViewGroup.LayoutParams params = grid.getLayoutParams();
        params.height = rowHeight;
        grid.setLayoutParams(params);

        // Add the event pictures as soon as possible to the Grid.
        ViewTreeObserver vto_grid = container.getViewTreeObserver();
        vto_grid.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (event != null) {
                    for (CompressedBitmap bitmap : event.getPictures()) {
                        addImageToGrid(bitmap, false);
                    }
                    //TODO addvideotoGrid, on click listener to play the videoView

                }
            }
        });
    }

    /**
     * Closes the FileManager.
     * This method should be called when the activity using the FileManager is paused or destroyed
     * to avoid unnecessary network communications.
     */
    public void close() {
        event.removeWatcher(this);
    }

    /**
     * Override of onActivityResult method.
     * Define the behavior when the user finished selecting the picture he wants to add or
     * taking a picture.
     * @param resultCode  indicate if the operation succeeded.
     * @param data        the data returned by the previous activity.
     */
    public void onActivityResult(int resultCode, Intent data) {
        event.addWatcher(this);
        if (resultCode == RESULT_OK) {
            galleryAddPic();
            String type;
            Uri targetUri;

            //Apparently some phone (including mine) doesn't return intent
            try {
                type = data.getType();
            } catch (NullPointerException n) {
                type = "";
            }
            try {
                targetUri = data.getData();
            } catch (NullPointerException n) {
                targetUri = Uri.fromFile(new File(mCurrentFilePath));
            }

            if (type.contains("image") || mCurrentFilePath.contains("Pictures")) {
                recoverAndUploadImage(targetUri);
            } else {
                recoverAndUploadVideo(targetUri);
            }
            mCurrentFilePath="";
        }
    }

    /**
     * Recover a video from the user's phone from its uri and upload it on the database.
     *
     * @param targetUri the uri of the video.
     */
    private void recoverAndUploadVideo(Uri targetUri) {
        addVideoToGrid(targetUri);
        event.addVideo(Account.shared.getUUID().getOrElse("Default ID"), targetUri);
    }

    /**
     * Recover an image from the user's phone from its uri and download it on the database.
     *
     * @param targetUri the uri of the image.
     */
    private void recoverAndUploadImage(Uri targetUri) {
        Bitmap bitmap;

        try {
            bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(targetUri));

        } catch (FileNotFoundException e) {
            AndroidHelper.showToast(activity.getApplicationContext(),
                    activity.getString(R.string.file_management_toast_error_file_uri),
                    Toast.LENGTH_SHORT);
            return;
        }

        CompressedBitmap compressedBitmap = new CompressedBitmap(bitmap);
        addImageToGrid(compressedBitmap, true);
    }

    //TODO TAKE VIDEOS FROM APP

    private void initializeTakeVideo() {
        final Button takeVideo = activity.findViewById(R.id.take_video);
        final Context thisContext = activity.getApplicationContext();
        takeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(activity.getPackageManager()) != null) {
                    File video = null;
                    try {
                        video = createFile(false);
                    } catch (IOException e) {
                        AndroidHelper.showToast(activity.getApplicationContext(),
                                activity.getString(R.string.file_management_toast_error_file_not_created),
                                Toast.LENGTH_SHORT);
                    }
                    if (video != null) {
                        Uri videoUri = FileProvider.getUriForFile(thisContext, "com.example.android.fileprovider", video);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                        activity.startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                    }
                }
            }
        });
    }

    /**
     * Initialize the camera button and open the camera
     */
    private void initializeTakePicture() {
        final Button takePicture = activity.findViewById(R.id.take_picture);
        final Context thisContext = activity.getApplicationContext();
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                    File photo = null;
                    try {
                        photo = createFile(true);
                    } catch (IOException e) {
                        AndroidHelper.showToast(activity.getApplicationContext(),
                                activity.getString(R.string.file_management_toast_error_file_not_created),
                                Toast.LENGTH_SHORT);
                    }
                    if (photo != null) {
                        Uri photoUri = FileProvider.getUriForFile(thisContext, "com.example.android.fileprovider", photo);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });
    }

    private File createFile(boolean image) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(new Date());
        String FileName = timeStamp + "_";
        String extension;
        File storageDir;
        if (image) {
            storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            extension = ".jpg";
        } else {
            storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            extension = ".mp4";
        }
        File file = File.createTempFile(
                FileName,  /* prefix */
                extension,         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentFilePath = file.getAbsolutePath();
        return file;
    }

    /**
     * Add the photos to the gallery on the phone
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentFilePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);
    }


    /**
     * Helper method to clear all the images of the grid.
     */
    private void clearImages() {
        ((GridLayout) activity.findViewById(R.id.image_grid))
                .removeAllViews();
        imagesAdded = 0;
    }

    private void addVideoToGrid(Uri uri) {
        MediaMetadataRetriever mMMR = new MediaMetadataRetriever();
        mMMR.setDataSource(activity, uri);
        CompressedBitmap thumb = new CompressedBitmap(mMMR.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC));
        addImageToGrid(thumb, false);
    }

    /**
     * Add an image to the grid and to the Firebase storage.
     *
     * @param bitmap the image to add.
     */
    private void addImageToGrid(final CompressedBitmap bitmap, boolean addToDatabase) {
        ImageView image = new ImageView(activity);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = columnWidth;
        layoutParams.height = rowHeight;
        image.setLayoutParams(layoutParams);

        Bitmap trimed = trimBitmap(bitmap.asBitmap());

        image.setImageBitmap(trimed);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ImageView) activity.findViewById(R.id.show_image))
                        .setImageBitmap(bitmap.asBitmap());

                activity.findViewById(R.id.image_grid)
                        .setVisibility(View.INVISIBLE);

                activity.findViewById(R.id.show_image)
                        .setVisibility(View.VISIBLE);

                activity.findViewById(R.id.show_image)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                activity.findViewById(R.id.show_image)
                                        .setVisibility(View.INVISIBLE);

                                activity.findViewById(R.id.image_grid)
                                        .setVisibility(View.VISIBLE);
                            }
                        });
            }
        });

        ((GridLayout) activity.findViewById(R.id.image_grid))
                .addView(image, imagesAdded++);

        if (addToDatabase)
            event.addPicture(Account.shared.getUUID().getOrElse("Default ID"),
                    bitmap);
    }

    /**
     * Helper method to trim an image, the resulting image will be centered, with a width of a
     * column and the height of a row in the grid.
     *
     * @param bitmap the bitmap to trim.
     * @return the trimmed image.
     */
    private Bitmap trimBitmap(Bitmap bitmap) {

        //Scaling bitmap
        Bitmap scaled;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            int nh = (int) (bitmap.getWidth() * (1.0 * rowHeight / bitmap.getHeight()));
            scaled = Bitmap.createScaledBitmap(bitmap, nh, rowHeight, true);
        } else {
            int nh = (int) (bitmap.getHeight() * (1.0 * columnWidth / bitmap.getWidth()));
            scaled = Bitmap.createScaledBitmap(bitmap, columnWidth, nh, true);
        }

        int cutOnSide = (scaled.getWidth() - columnWidth) / 2;
        int cutOnTop = (scaled.getHeight() - rowHeight) / 2;

        if (cutOnSide > 0 || cutOnTop > 0)
            scaled = Bitmap.createBitmap(scaled, cutOnSide, cutOnTop,
                    columnWidth, rowHeight);

        return scaled;
    }

    /**
     * Override of notifyWatcher.
     * When notified, synchronise the grid images with the event images.
     */
    @Override
    public void notifyWatcher() {
        clearImages();
        for (CompressedBitmap bitmap : event.getPictures()) {
            addImageToGrid(bitmap, false);
        }
        for (Uri f : event.getEventVideos()) {
            addVideoToGrid(f);
        }
    }

}
