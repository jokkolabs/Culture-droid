package com.yeleman.culture_droid;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class TagElementsAdapter extends BaseAdapter {

    private static final String TAG = Constants.getLogTag("TagElementsAdapter");
    private Context context;
    private ArrayList<TagElement> tagElements;

    public TagElementsAdapter(Context context, ArrayList<TagElement> tagElements) {
        this.context = context;
        this.tagElements = tagElements;
    }

    @Override
    public int getCount() {
        return tagElements.size();
    }

    @Override
    public Object getItem(int position) {
        return tagElements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (View) inflater.inflate(
                    R.layout.tag_list_item, null);
        }

        TextView tagName = (TextView)convertView.findViewById(R.id.tagName);
        tagName.setText(tagElements.get(position).getTagName());
        /*ImageView image = (ImageView) convertView.findViewById(R.id.tagImage);
         if( position > 1) {
             image.setImageResource(R.drawable.downicon);
         }*/
        return convertView;
    }
}