package com.kaywalker.new_insta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kaywalker.new_insta.Model.Image;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class AddActivity extends AppCompatActivity {

    StorageReference sRef;
    FirebaseStorage fStore;
    FirebaseDatabase fDatabase;
    DatabaseReference dRef;

    Button chooser,uploader,viewListimg;
    ImageView imgLoader;
    int PIKE_IMAGE_CODE = 200;
    private Uri filepath;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        fStore = FirebaseStorage.getInstance();
        sRef = fStore.getReference().child("Images");

        fDatabase = FirebaseDatabase.getInstance();
        dRef = fDatabase.getReference("Images").push();

        chooser = findViewById(R.id.createChooser);
        uploader = findViewById(R.id.upload);
        viewListimg = findViewById(R.id.viewList);
        imgLoader = findViewById(R.id.loadImage);

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        chooser.setOnClickListener(view ->{
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent,"select image"),PIKE_IMAGE_CODE);
        });

        uploader.setOnClickListener(view -> ImageUploadProgress());

        viewListimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddActivity.this, CenterActivity.class));
            }
        });

    }

    private void ImageUploadProgress() {
        dialog.show();
        if(filepath != null){
            StorageReference imgFile = sRef.child(UUID.randomUUID().toString());
            imgFile.putFile(filepath).addOnSuccessListener(taskSnapshot -> {
                imgFile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        dialog.dismiss();
                        Image image = new Image();
                        image.setImageUrl(String.valueOf(uri));
                        image.setStorageKey(imgFile.getName());
                        dRef.setValue(image);
                        Toast.makeText(AddActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddActivity.this, CenterActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(AddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }).addOnFailureListener(e -> {
                dialog.dismiss();
                Toast.makeText(AddActivity.this, "Failure!", Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(snapshot -> {
                double progress = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount() );
                dialog.setMessage("uploading: "+((int)progress)+"%..");
            });
        }else{
            dialog.dismiss();
            Toast.makeText(AddActivity.this, "이미지를 선택해주세요!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PIKE_IMAGE_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            filepath = data.getData();
            Picasso.get().load(filepath).into(imgLoader);
        }
    }

}