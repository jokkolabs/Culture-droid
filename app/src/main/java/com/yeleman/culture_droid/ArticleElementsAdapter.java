package com.yeleman.culture_droid;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ArticleElementsAdapter extends BaseAdapter {

    private static final String TAG = Constants.getLogTag("ArticleElementsAdapter");
    private Context context;
    private ArrayList<ArticleElement> articleElements;

    public ArticleElementsAdapter(Context context, ArrayList<ArticleElement> articleElements) {
        this.context = context;
        this.articleElements = articleElements;
    }

    @Override
    public int getCount() {
        return articleElements.size();
    }

    @Override
    public Object getItem(int position) {
        return articleElements.get(position);
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
                    R.layout.basic_list_item, null);
        }

        TextView title = (TextView)convertView.findViewById(R.id.title);
        TextView publishedOn = (TextView)convertView.findViewById(R.id.publishedOn);
        final TextView contentSize = (TextView)convertView.findViewById(R.id.contentSize);
        ImageView thumbnail = (ImageView)convertView.findViewById(R.id.thumbnail);

        title.setText(articleElements.get(position).getTitle());
        publishedOn.setText(articleElements.get(position).getPublishedOn());
        contentSize.setText(articleElements.get(position).getContentSize());

        byte[] decodedString = Base64.decode(articleElements.get(position).getEncodedThumbnail(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        thumbnail.setImageBitmap(decodedByte);

        final int articleId = articleElements.get(position).getArticleId();

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //contentSize.setText("✔");
                Intent intent = new Intent(context, Details.class);
                intent.putExtra("articleId", String.valueOf(articleId));
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}