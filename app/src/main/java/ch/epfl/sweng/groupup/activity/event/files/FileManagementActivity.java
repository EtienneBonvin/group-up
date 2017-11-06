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

import ch.epfl.sweng.groupup.R;
import ch.epfl.sweng.groupup.activity.toolbar.ToolbarActivity;
import ch.epfl.sweng.groupup.lib.Helper;

public class FileManagementActivity extends ToolbarActivity {

    private final int COLUMNS = 3;
    private final int ROWS = 3;
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

                ImageView image = new ImageView(this);
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                //Scaling bitmap
                int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                image.setImageBitmap(scaled);

                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.width = columnWidth;
                layoutParams.height = rowHeight;
                image.setLayoutParams(layoutParams);

                if(imagesAdded % COLUMNS == 0 && imagesAdded > 9){
                    ((GridLayout)findViewById(R.id.image_grid))
                            .setRowCount(imagesAdded / ROWS + 1);
                    ViewGroup.LayoutParams params = findViewById(R.id.image_grid).getLayoutParams();
                    params.height = Math.round(rowHeight * (imagesAdded / ROWS + 1));
                    findViewById(R.id.image_grid)
                            .setLayoutParams(params);
                }

                ((GridLayout)findViewById(R.id.image_grid))
                        .addView(image, imagesAdded++);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
