package com.example.itunestoppaidapps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends ArrayAdapter<App> {


    public AppAdapter(@NonNull Context context, int resource, @NonNull List<App> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final App app = getItem(position);
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.app_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_appName);
            viewHolder.price = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_app);
            viewHolder.dollarImage = (ImageView) convertView.findViewById(R.id.dollarImage);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(app.name);
        viewHolder.price.setText(app.price);
        if (app.imageUrl.isEmpty()) {
            viewHolder.image.getLayoutParams().height = 300;
            viewHolder.image.getLayoutParams().width = 300;
        }
        else{
            Picasso.get().load(app.imageUrl).resize(300, 300).centerCrop().into((viewHolder.image));
        }

        float priceFloat = Float.parseFloat(app.price.replace("$",""));
        if(priceFloat<=1.99){
            viewHolder.dollarImage.setImageResource(R.drawable.price_low);
        }
        else if(priceFloat>=2.00 && priceFloat <= 5.99){
            viewHolder.dollarImage.setImageResource(R.drawable.price_medium);
        }
        else if(priceFloat>=6.00){
            viewHolder.dollarImage.setImageResource(R.drawable.price_high);
        }

        return convertView;

    }

    public static class ViewHolder{
        TextView name;
        TextView price;
        ImageView image, dollarImage;
    }
}