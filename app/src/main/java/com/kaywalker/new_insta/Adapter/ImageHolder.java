package com.kaywalker.new_insta.Adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaywalker.new_insta.R;

public class ImageHolder extends RecyclerView.ViewHolder
{

    ImageView igView;
    ImageView delete,update;

    public ImageHolder(@NonNull View itemView) {
        super(itemView);

        igView = itemView.findViewById(R.id.imgLoader);
        delete = itemView.findViewById(R.id.deletButton);
        update = itemView.findViewById(R.id.updateButton);

    }

}

