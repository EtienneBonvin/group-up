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

import java.io.FileNotFoundException;
import java.util.List;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.Helper;
import ch.epfl.sweng.groupup.lib.fileStorage.FirebaseFileProxy;

public class FileManagementActivity extends ToolbarActivity {

    private final int COLUMNS = 3;
    private final int ROWS = 4;
    private int columnWidth;
    private int rowHeight;

    private FirebaseFileProxy proxy;
    private List<Bitmap> images;

    int imagesAdded = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_management);
        super.initializeToolbarActivity();

        Intent intent = getIntent();
        proxy = new FirebaseFileProxy(intent.getStringExtra("EventId"));

        findViewById(R.id.add_files).setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }});

        final GridLayout grid = findViewById(R.id.image_grid);
        final RelativeLayout container = findViewById(R.id.scroll_view_container);
        ViewTreeObserver vto = container.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                columnWidth = container.getMeasuredWidth() / COLUMNS;
                rowHeight = container.getMeasuredHeight() / ROWS;
                // TODO : import images
                //importImages(height, width);

            }
        });
        ViewGroup.LayoutParams params = grid.getLayoutParams();
        params.height = rowHeight;
        grid.setLayoutParams(params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

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

            addImageToGrid(bitmap);
            proxy.uploadFile(bitmap);
        }
    }

    private void addImageToGrid(Bitmap bitmap){
        ImageView image = new ImageView(this);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = columnWidth;
        layoutParams.height = rowHeight;
        image.setLayoutParams(layoutParams);

        image.setImageBitmap(trimBitmap(bitmap));

        ((GridLayout)findViewById(R.id.image_grid))
                .addView(image, imagesAdded++);
    }

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
}
