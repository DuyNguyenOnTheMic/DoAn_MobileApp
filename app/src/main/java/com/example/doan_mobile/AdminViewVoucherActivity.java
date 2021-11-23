package com.example.doan_mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_mobile.Model.Voucher;
import com.example.doan_mobile.ViewHolder.VoucherViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminViewVoucherActivity extends AppCompatActivity {

    private DatabaseReference VouchersRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton add;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_voucher);

        VouchersRef = FirebaseDatabase.getInstance().getReference().child("Voucher");

        matching();

        recyclerView = findViewById(R.id.adminViewVoucher_recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(AdminViewAdminActivity.this, AdminAddNewAdminActivity.class));*/
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Voucher> options =
                new FirebaseRecyclerOptions.Builder<Voucher>()
                        .setQuery(VouchersRef, Voucher.class)
                        .build();

        FirebaseRecyclerAdapter<Voucher, VoucherViewHolder> adapter =
                new FirebaseRecyclerAdapter<Voucher, VoucherViewHolder>(options) {
                    @NonNull
                    @Override
                    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_vouchers_items_layout, parent, false);
                        VoucherViewHolder holder = new VoucherViewHolder(view);
                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull VoucherViewHolder voucherViewHolder, int i, @NonNull Voucher voucher) {
                        voucherViewHolder.voucherCode.setText(voucher.getMaVoucher());
                        voucherViewHolder.voucherDiscount.setText("Mức giảm: " + voucher.getMucGiam());
                        voucherViewHolder.voucherBegin.setText(voucher.getThoiGianBatDau());
                        voucherViewHolder.voucherEnd.setText(voucher.getThoiGianKetThuc());

                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void matching() {
        add = (FloatingActionButton) findViewById(R.id.adminViewVoucher_fab_add);
        back = (ImageView) findViewById(R.id.adminViewVoucher_iv_back);
    }
}