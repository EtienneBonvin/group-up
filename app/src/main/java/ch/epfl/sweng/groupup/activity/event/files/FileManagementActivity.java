package ch.epfl.sweng.groupup.activity.event.files;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.FileNotFoundException;

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.Helper;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class FileManagementActivity extends ToolbarActivity {

    private final int COLUMNS = 3;
    private final int ROWS = 4;
    private int columnWidth;
    private int rowHeight;

    int imagesAdded = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_management);
        super.initializeToolbarActivity();

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

            Bitmap bitmap;
            try {

                if(imagesAdded % COLUMNS == 0){
                    ((GridLayout)findViewById(R.id.image_grid))
                            .setRowCount(imagesAdded / ROWS + 1);
                    ViewGroup.LayoutParams params = findViewById(R.id.image_grid).getLayoutParams();
                    params.height = Math.round(rowHeight * (imagesAdded / ROWS + 1));
                    findViewById(R.id.image_grid)
                            .setLayoutParams(params);
                }

                ScrollView displayerVer = new ScrollView(this);
                displayerVer.setLayoutParams(
                        new ViewGroup.LayoutParams(columnWidth, WRAP_CONTENT)
                );

                HorizontalScrollView displayerHor = new HorizontalScrollView(this);
                displayerHor.setLayoutParams(
                        new ViewGroup.LayoutParams(WRAP_CONTENT, rowHeight)
                );

                ImageView image = new ImageView(this);

                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.width = columnWidth;
                layoutParams.height = rowHeight;
                image.setLayoutParams(layoutParams);

                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                //Scaling bitmap
                //TODO : Manage problem between two vertical scrollviews
                Bitmap scaled;
                if(bitmap.getWidth() > bitmap.getHeight()) {
                    int nh = (int) (bitmap.getWidth() * (1.0 * rowHeight / bitmap.getHeight()));
                    scaled = Bitmap.createScaledBitmap(bitmap, nh, rowHeight, true);

                    displayerVer.addView(displayerHor);
                    displayerHor.addView(image);

                    ((GridLayout)findViewById(R.id.image_grid))
                            .addView(displayerVer, imagesAdded++);
                }else{
                    int nh = (int) (bitmap.getHeight() * (1.0 * columnWidth / bitmap.getWidth()));
                    scaled = Bitmap.createScaledBitmap(bitmap, columnWidth, nh, true);

                    displayerHor.addView(displayerVer);
                    displayerVer.addView(image);

                    ((GridLayout)findViewById(R.id.image_grid))
                            .addView(displayerHor, imagesAdded++);
                }
                image.setImageBitmap(scaled);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
