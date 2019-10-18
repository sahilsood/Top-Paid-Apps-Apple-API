package com.example.itunestoppaidapps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
        ArrayList<App> mData= new ArrayList<>();

public RecyclerAdapter(ArrayList<App> mData) {
        this.mData = mData;
        }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
        }

@Override
public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final App app = mData.get(position);

    holder.name.setText(app.name);
    holder.price.setText(app.price);
    if (app.imageUrl.isEmpty()) {
        holder.image.getLayoutParams().height = 300;
        holder.image.getLayoutParams().width = 300;
    }
    else{
        Picasso.get().load(app.imageUrl).resize(300, 300).centerCrop().into((holder.image));
    }

    float priceFloat = Float.parseFloat(app.price.replace("$",""));
    if(priceFloat<=1.99){
        holder.dollarImage.setImageResource(R.drawable.price_low);
    }
    else if(priceFloat>=2.00 && priceFloat <= 5.99){
        holder.dollarImage.setImageResource(R.drawable.price_medium);
    }
    else if(priceFloat>=6.00){
        holder.dollarImage.setImageResource(R.drawable.price_high);
    }

    holder.deleteImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mData.remove(app);
            MainActivity.result.add(app);
            MainActivity.mAdapter.notifyDataSetChanged();
        }
    });

}

@Override
public int getItemCount() {
        return mData.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder{
    TextView name;
    TextView price;
    ImageView image, dollarImage;
    ImageButton deleteImage;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.textViewName);
        price = (TextView) itemView.findViewById(R.id.textViewPrice);
        image = (ImageView) itemView.findViewById(R.id.imageViewMain);
        dollarImage = (ImageView) itemView.findViewById(R.id.imageViewD);
        deleteImage = (ImageButton) itemView.findViewById(R.id.ibtn_delete);
    }
}
}