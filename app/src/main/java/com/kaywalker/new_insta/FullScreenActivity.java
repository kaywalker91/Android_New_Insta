package com.kaywalker.new_insta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kaywalker.new_insta.Adapter.ShareAdapter;

import java.io.File;
import java.io.FileOutputStream;

public class FullScreenActivity extends AppCompatActivity {

    private FileOutputStream outputStream;
    private BitmapDrawable drawable;
    private Bitmap bitmap;
    private File file,dir;
    private Intent intent;
    private static int REQUEST_CODE = 100;

    private ImageView imageView;
    private Button share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        imageView = findViewById(R.id.image_view);
        share = findViewById(R.id.share);

        Intent i = getIntent();
        int position = i.getExtras().getInt("id");
        ShareAdapter shareAdapter = new ShareAdapter(this);
        imageView.setImageResource(shareAdapter.imageArray[position]);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });

    }

    private void shareImage() {

        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Uri uri = getImageToShare(bitmap);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent,"Shared Image"));

    }

    private Uri getImageToShare(Bitmap bitmap) {

        File folder = new File(getCacheDir(),"images");
        Uri uri = null;

        try {
            folder.mkdirs();
            File file = new File(folder,"shared_image.jpg");

            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();

            uri = FileProvider.getUriForFile(this,"com.kaywalker.new_insta", file);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return uri;
    }
}