package com.example.doan_mobile.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_mobile.Interface.ItemClickListener;
import com.example.doan_mobile.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtPName, txtPPrice, txtPQuantity;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtPName = itemView.findViewById(R.id.cartItems_tv_name);
        txtPPrice = itemView.findViewById(R.id.cartItems_tv_price);
        txtPQuantity = itemView.findViewById(R.id.cartItems_tv_quantity);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
