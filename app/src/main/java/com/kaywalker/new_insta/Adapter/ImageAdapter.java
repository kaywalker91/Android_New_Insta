package com.kaywalker.new_insta.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaywalker.new_insta.CenterActivity;
import com.kaywalker.new_insta.Model.Image;
import com.kaywalker.new_insta.NewActivity;
import com.kaywalker.new_insta.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {

    Context ctx;
    List<Image> imgList;

    public static ImageView LoadChangedImage;
    public static Button openChooser;
    public static int PIKE_IMAGE_CODE = 200;
    public static int PIKE_STORAGE_PERMISTIONS_CODE = 300;
    public static Uri filepath;
    public static ProgressDialog pDialog;

    public ImageAdapter(Context context, List<Image> imageList) {
        this.ctx = context;
        this.imgList = imageList;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageloader,parent,false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, @SuppressLint("RecyclerView") int position) {

        Image image = imgList.get(position);
        pDialog = new ProgressDialog(ctx);
        pDialog.setMessage("Loading...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Picasso.get().load(image.getImageUrl()).into(holder.igView);

        holder.igView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, NewActivity.class);
                intent.putExtra("image@#",imgList.get(position).getImageUrl());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
                DatabaseReference dRef = fDatabase.getReference("Images").child(image.getKey());
                dRef.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if(ref == null){
                            Toast.makeText(ctx, "delete: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }else{

                            FirebaseStorage fStorage = FirebaseStorage.getInstance();
                            StorageReference sRef = fStorage.getReference("Images/").child(image.getStorageKey());

                            sRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    int pos = imgList.indexOf(image);
                                    imgList.remove(pos);
                                    notifyItemRemoved(pos);
                                    Toast.makeText(ctx, "deleted", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(ctx, "error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                });

            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View layout = LayoutInflater.from(ctx).inflate(R.layout.changeimage, null);
                LoadChangedImage = layout.findViewById(R.id.changeImage);
                openChooser = layout.findViewById(R.id.changeImageButton);
                openChooser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        ((Activity) ctx).startActivityForResult(Intent.createChooser(intent,"choose img"), PIKE_IMAGE_CODE);

                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Change Image");
                builder.setView(layout);

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pDialog.show();

                        FirebaseStorage fStore = FirebaseStorage.getInstance();
                        StorageReference sRef = fStore.getReference("Images").child(image.getStorageKey());

                        sRef.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        pDialog.dismiss();

                                        FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
                                        DatabaseReference dRef = fDatabase.getReference("Images").child(image.getKey());

                                        Image img = new Image();

                                        img.setImageUrl(String.valueOf(uri));
                                        img.setStorageKey(sRef.getName());
                                        img.setKey(dRef.getKey());

                                        dRef.setValue(img);
                                        refresh(img);
                                        notifyDataSetChanged();

                                        Toast.makeText(ctx, "Image Successfully Changed!!", Toast.LENGTH_SHORT).show();

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable(){
                                            public void run(){
                                                Intent main = new Intent(ctx, CenterActivity.class);
                                                ctx.startActivity(main);
                                            }
                                        },400);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pDialog.dismiss();
                                        Toast.makeText(ctx, "url: " +e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pDialog.dismiss();
                                Toast.makeText(ctx, "storage: " +e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    private void refresh(Image img){
        imgList.clear();
        int i = imgList.size();
        imgList.add(i,img);
    }

}
