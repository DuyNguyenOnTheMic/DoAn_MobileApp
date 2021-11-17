package com.example.doan_mobile.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_mobile.Interface.ItemClickListener;
import com.example.doan_mobile.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productName, productDecription, productPrice;
    public ImageView productImage;
    public ItemClickListener listener;

    public ProductViewHolder(View itemView) {
        super(itemView);

        productImage = (ImageView) itemView.findViewById(R.id.im_productImage);
        productName = (TextView) itemView.findViewById(R.id.tv_productName);
        productDecription = (TextView) itemView.findViewById(R.id.tv_productDescription);
        productPrice = (TextView) itemView.findViewById(R.id.tv_productPrice);
    }
    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
    listener.onClick(view, getAdapterPosition(), false);
    }
}
