package com.kaywalker.new_insta.Fragment;

import static android.app.appsearch.AppSearchResult.RESULT_OK;

import static com.kaywalker.new_insta.Adapter.ImageAdapter.LoadChangedImage;
import static com.kaywalker.new_insta.Adapter.ImageAdapter.PIKE_IMAGE_CODE;
import static com.kaywalker.new_insta.Adapter.ImageAdapter.PIKE_STORAGE_PERMISTIONS_CODE;
import static com.kaywalker.new_insta.Adapter.ImageAdapter.filepath;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaywalker.new_insta.Adapter.ImageAdapter;
import com.kaywalker.new_insta.AddActivity;
import com.kaywalker.new_insta.Model.Image;
import com.kaywalker.new_insta.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Frag_Home extends Fragment {

    private View view;
    private FloatingActionButton floatingActionButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dRef;

    RecyclerView recyclerView;
    ImageAdapter adapter;
    List<Image> imgList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViews);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dRef = firebaseDatabase.getReference("Images");

        imgList = new ArrayList<>();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()){
                    Image image = ds.getValue(Image.class);
                    image.setKey(ds.getKey());
                    imgList.add(image);
                }

                adapter = new ImageAdapter(getContext(), imgList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        dRef.addListenerForSingleValueEvent(valueEventListener);

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PIKE_IMAGE_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            filepath = data.getData();
            Picasso.get().load(filepath).into(LoadChangedImage);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PIKE_STORAGE_PERMISTIONS_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
