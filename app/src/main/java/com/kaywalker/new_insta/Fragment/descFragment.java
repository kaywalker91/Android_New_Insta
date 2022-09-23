package com.kaywalker.new_insta.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.kaywalker.new_insta.R;

public class descFragment extends Fragment {

    String name, place, email, purl;

    public descFragment() {

    }

    public descFragment(String name, String place, String email, String purl) {
        this.name = name;
        this.place = place;
        this.email = email;
        this.purl = purl;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_desc, container, false);

        ImageView imageholder=view.findViewById(R.id.imagegholder);
        TextView nameholder=view.findViewById(R.id.nameholder);
        TextView courseholder=view.findViewById(R.id.placeholder);
        TextView emailholder=view.findViewById(R.id.emailholder);

        nameholder.setText(name);
        courseholder.setText(place);
        emailholder.setText(email);
        Glide.with(getContext()).load(purl).into(imageholder);

        return view;
    }
}
