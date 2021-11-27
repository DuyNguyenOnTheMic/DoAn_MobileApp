package com.example.doan_mobile.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_mobile.R;

public class AdminCartViewHolder extends RecyclerView.ViewHolder {

    public TextView pName, pQuantity, pPrice;

    public AdminCartViewHolder(@NonNull View itemView) {
        super(itemView);

        pName = (TextView) itemView.findViewById(R.id.userProductItems_tv_name);
        pQuantity = (TextView) itemView.findViewById(R.id.userProductItems_tv_quantity);
        pPrice = (TextView) itemView.findViewById(R.id.userProductItems_tv_price);
    }
}
