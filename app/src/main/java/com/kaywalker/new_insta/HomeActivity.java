package com.kaywalker.new_insta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class HomeActivity extends AppCompatActivity {

    private ImageView iv_profile;
    private TextView tv_name;
    private Button bn_Enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        String nickName = intent.getStringExtra("nickName");
        String photoUrl = intent.getStringExtra("photoUrl");

        tv_name = findViewById(R.id.tv_name);
        tv_name.setText(nickName);

        iv_profile = findViewById(R.id.iv_profile);
        Glide.with(this).load(photoUrl).into(iv_profile);

        bn_Enter = findViewById(R.id.bn_Enter);
        bn_Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),CenterActivity.class);
                startActivity(intent1);
            }
        });
    }
}