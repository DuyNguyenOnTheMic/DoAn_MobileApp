package com.example.doan_mobile.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_mobile.Interface.ItemClickListener;
import com.example.doan_mobile.R;

public class ProductCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productCategoryName;
    public ItemClickListener listener;

    public ProductCategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        productCategoryName = (TextView) itemView.findViewById(R.id.adminCategoryItems_tv_name);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
