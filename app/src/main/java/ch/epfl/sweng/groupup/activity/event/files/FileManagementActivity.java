package ch.epfl.sweng.groupup.activity.event.files;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.List;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.Helper;
import ch.epfl.sweng.groupup.lib.Watcher;
import ch.epfl.sweng.groupup.lib.fileStorage.FirebaseFileProxy;
import ch.epfl.sweng.groupup.object.account.Account;
import ch.epfl.sweng.groupup.object.event.Event;

public class FileManagementActivity extends ToolbarActivity implements Watcher {

    private final int COLUMNS = 3;
    private final int ROWS = 4;
    private int columnWidth;
    private int rowHeight;
    private Event event;
    private int eventIndex;
    private Watcher meAsWatcher;

    private int imagesAdded = 0;

    public static final String FILE_EXTRA_NAME = "File";
    public static final String EVENT_INDEX = "EventIndex";

    /**
     * Override onCreate method of ToolbarActivity.
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_management);
        super.initializeToolbarActivity();

        //Recover event and add ourselves as listeners.
        Intent intent = getIntent();
        eventIndex = intent.getIntExtra(EVENT_INDEX, -1);
        if (eventIndex >-1) {
            event = Account.shared.getEvents().get(eventIndex);
        }
        event.addWatcher(this);
        meAsWatcher = this;

        // Set onClickListeners to add files
        // TODO adding videos.
        findViewById(R.id.add_files).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }});

        // Set the GridLayout and initially get the height and width of the rows and columns.
        final GridLayout grid = findViewById(R.id.image_grid);
        final RelativeLayout container = findViewById(R.id.scroll_view_container);
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
                for(Bitmap bitmap : event.getPictures()){
                    addImageToGrid(bitmap);
                }
            }
        });
    }

    /**
     * Override onPause method, remove the activity from the watchers of the event to avoid
     * exceptions.
     **/
    @Override
    protected void onPause(){
        super.onPause();
        event.removeWatcher(this);
    }

    /**
     * Override onStop method, remove the activity from the watchers of the event to avoid
     * exceptions.
     **/
    @Override
    public void onStop(){
        super.onStop();
        event.removeWatcher(this);
    }

    /**
     * Override onDestroy method, remove the activity from the watchers of the event to avoid
     * exceptions.
     **/
    @Override
    public void onDestroy(){
        super.onDestroy();
        event.removeWatcher(this);
    }

    /**
     * Override of onActivityResult method.
     * Define the behavior when the user finished selecting the picture he wants to add.
     * @param requestCode unused.
     * @param resultCode indicate if the operation succeeded.
     * @param data the data returned by the previous activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        event.addWatcher(this);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();

            if(targetUri == null){
                Helper.showToast(getApplicationContext(),
                        getString(R.string.file_management_toast_error_file_uri),
                        Toast.LENGTH_SHORT);
                return;
            }

            if(imagesAdded % COLUMNS == 0){
                ((GridLayout)findViewById(R.id.image_grid))
                        .setRowCount(imagesAdded / ROWS + 1);
                ViewGroup.LayoutParams params = findViewById(R.id.image_grid).getLayoutParams();
                params.height = Math.round(rowHeight * (imagesAdded / ROWS + 1));
                findViewById(R.id.image_grid)
                        .setLayoutParams(params);
            }

            Bitmap bitmap;
            try {

                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

            } catch (FileNotFoundException e) {
                Helper.showToast(getApplicationContext(),
                        getString(R.string.file_management_toast_error_file_uri),
                        Toast.LENGTH_SHORT);
                return;
            }

            if(bitmap.getByteCount()/8 > FirebaseFileProxy.MAX_FILE_SIZE){
                Helper.showToast(getApplicationContext(),
                        getString(R.string.file_management_toast_error_file_too_big),
                        Toast.LENGTH_SHORT);
            }else {
                addImageToGrid(bitmap);
                event.addPicture(Account.shared.getUUID().getOrElse("Default ID"), bitmap);
            }
        }
    }

    /**
     * Helper method to clear all the images of the grid.
     */
    private void clearImages(){
        ((GridLayout)findViewById(R.id.image_grid))
                .removeAllViews();
        imagesAdded = 0;
    }

    /**
     * Add an image to the grid and to the Firebase storage.
     * @param bitmap the image to add.
     */
    private void addImageToGrid(final Bitmap bitmap){
        ImageView image = new ImageView(this);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = columnWidth;
        layoutParams.height = rowHeight;
        image.setLayoutParams(layoutParams);

        image.setImageBitmap(trimBitmap(bitmap));

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FullScreenFile.class);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                intent.putExtra(FILE_EXTRA_NAME, data);
                intent.putExtra(EVENT_INDEX, eventIndex);
                event.removeWatcher(meAsWatcher);
                startActivity(intent);
            }
        });

        ((GridLayout)findViewById(R.id.image_grid))
                .addView(image, imagesAdded++);
    }

    /**
     * Helper method to trim an image, the resulting image will be centered, with a width of a
     * column and the height of a row in the grid.
     * @param bitmap the bitmap to trim.
     * @return the trimmed image.
     */
    private Bitmap trimBitmap(Bitmap bitmap) {

        //Scaling bitmap
        Bitmap scaled;

        if(bitmap.getWidth() > bitmap.getHeight()) {
            int nh = (int) (bitmap.getWidth() * (1.0 * rowHeight / bitmap.getHeight()));
            scaled = Bitmap.createScaledBitmap(bitmap, nh, rowHeight, true);
        }else{
            int nh = (int) (bitmap.getHeight() * (1.0 * columnWidth / bitmap.getWidth()));
            scaled = Bitmap.createScaledBitmap(bitmap, columnWidth, nh, true);
        }

        int cutOnSide = (scaled.getWidth() - columnWidth) / 2;
        int cutOnTop = (scaled.getHeight() - rowHeight) / 2;

        return Bitmap.createBitmap(scaled, cutOnSide, cutOnTop,
                columnWidth, rowHeight);
    }

    /**
     * Override of notifyWatcher.
     * When notified, synchronise the grid images with the event images.
     */
    @Override
    public void notifyWatcher() {
        clearImages();
        List<Bitmap> eventPictures = event.getPictures();
        for(Bitmap bitmap : eventPictures){
            addImageToGrid(bitmap);
        }
    }
}
