package com.example.doan_mobile.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_mobile.Interface.ItemClickListener;
import com.example.doan_mobile.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView adminName, adminPhone, adminRole;
    public CircleImageView adminImage;
    public ItemClickListener listener;

    public AdminViewHolder(@NonNull View itemView) {
        super(itemView);

        adminName = (TextView) itemView.findViewById(R.id.adminItems_tv_name);
        adminPhone = (TextView) itemView.findViewById(R.id.adminItems_tv_phone);
        adminRole = (TextView) itemView.findViewById(R.id.adminItems_tv_role);
        adminImage = (CircleImageView) itemView.findViewById(R.id.adminItems_iv_avatar);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
