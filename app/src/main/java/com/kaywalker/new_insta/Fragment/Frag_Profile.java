package com.kaywalker.new_insta.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.kaywalker.new_insta.ImageTestActivity;
import com.kaywalker.new_insta.MainActivity;
import com.kaywalker.new_insta.R;
import com.kaywalker.new_insta.StartActivity;
import com.kaywalker.new_insta.UserActivity;

public class Frag_Profile extends Fragment {

    private View view;

    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView add_profile = view.findViewById(R.id.add_profile);
        add_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserActivity.class);
                startActivity(intent);
            }
        });

        ImageView addBtnChat = view.findViewById(R.id.add_chat);
        addBtnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        ImageView share_image = view.findViewById(R.id.share_image);
        share_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ImageTestActivity.class);
                startActivity(intent);
            }
        });

        Button bn_logout = view.findViewById(R.id.bn_logout);
        bn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                firebaseAuth.signOut();
                ActivityCompat.finishAffinity(getActivity());
            }
        });

        return view;
    }

}

