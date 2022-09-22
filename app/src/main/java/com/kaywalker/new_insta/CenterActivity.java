package com.kaywalker.new_insta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.kaywalker.new_insta.Fragment.Frag_Board;
import com.kaywalker.new_insta.Fragment.Frag_Home;
import com.kaywalker.new_insta.Fragment.Frag_List;
import com.kaywalker.new_insta.Fragment.Frag_Profile;

public class CenterActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag_Home frag_home;
    private Frag_Board frag_board;
    private Frag_Profile frag_profile;
    private Frag_List frag_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
    }
}