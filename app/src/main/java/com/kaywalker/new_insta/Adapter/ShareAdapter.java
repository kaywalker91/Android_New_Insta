package com.kaywalker.new_insta.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kaywalker.new_insta.R;

public class ShareAdapter extends BaseAdapter {

    private Context mContext;

    public ShareAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public int[] imageArray = {R.drawable.shareimg1,R.drawable.shareimg2,R.drawable.shareimg3,R.drawable.shareimg4,
            R.drawable.shareimg5,R.drawable.shareimg6,R.drawable.shareimg7,R.drawable.shareimg8,R.drawable.shareimg9,R.drawable.shareimg10};

    @Override
    public int getCount() {
        return imageArray.length;
    }

    @Override
    public Object getItem(int position) {
        return imageArray[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(imageArray[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(700, 700));

        return imageView;
    }
}
