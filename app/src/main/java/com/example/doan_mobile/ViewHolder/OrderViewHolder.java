package com.example.doan_mobile.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_mobile.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    public TextView userName, userPhone, userTotalPrice, userDateTime, userAddress, orderStatus;
    public Button showProducts;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.adminOrders_tv_name);
        userPhone = itemView.findViewById(R.id.adminOrders_tv_phone);
        userTotalPrice = itemView.findViewById(R.id.adminOrders_tv_price);
        userDateTime = itemView.findViewById(R.id.adminOrders_tv_date);
        userAddress = itemView.findViewById(R.id.adminOrders_tv_address);
        showProducts = itemView.findViewById(R.id.adminOrders_btn_showProducts);
        orderStatus = itemView.findViewById(R.id.adminOrders_tv_status);
    }
}
