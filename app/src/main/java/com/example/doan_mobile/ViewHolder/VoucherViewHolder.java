package com.example.doan_mobile.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_mobile.Interface.ItemClickListener;
import com.example.doan_mobile.R;

public class VoucherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView voucherCode, voucherDiscount, voucherBegin, voucherEnd;
    public ItemClickListener listener;

    public VoucherViewHolder(@NonNull View itemView) {
        super(itemView);

        voucherCode = (TextView) itemView.findViewById(R.id.voucherItems_tv_voucherCode);
        voucherDiscount = (TextView) itemView.findViewById(R.id.voucherItems_tv_voucherDiscount);
        voucherBegin = (TextView) itemView.findViewById(R.id.voucherItems_tv_voucherBegin);
        voucherEnd = (TextView) itemView.findViewById(R.id.voucherItems_tv_voucherEnd);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
