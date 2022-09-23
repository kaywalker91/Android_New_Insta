package com.kaywalker.new_insta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kaywalker.new_insta.Adapter.ShareAdapter;

public class ImageTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_test);

        GridView gridView;
        gridView = (GridView)findViewById(R.id.gridView);
        gridView.setAdapter(new ShareAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplication(),FullScreenActivity.class);
                intent.putExtra("id",position);
                startActivity(intent);

            }
        });

    }
}