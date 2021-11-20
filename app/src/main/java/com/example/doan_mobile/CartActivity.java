package com.example.doan_mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_mobile.Model.GioHang;
import com.example.doan_mobile.Prevalent.Prevalent;
import com.example.doan_mobile.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button next;
    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        matching();

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("GioHang");

        FirebaseRecyclerOptions<GioHang> options =
                new FirebaseRecyclerOptions.Builder<GioHang>()
                        .setQuery(cartListRef.child("ViewKhachHang")
                                .child(Prevalent.currentOnlineUser.getDienThoai()).
                                        child("SanPham"), GioHang.class)
                        .build();

        FirebaseRecyclerAdapter<GioHang, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<GioHang, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull GioHang gioHang) {
                double dPrice = Double.parseDouble(gioHang.getGiaGoc());
                NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
                String sPrice = nf.format(dPrice);

                cartViewHolder.txtPQuantity.setText("Số lượng: " + gioHang.getSoLuongMua());
                cartViewHolder.txtPPrice.setText("Đơn giá: " + sPrice);
                cartViewHolder.txtPName.setText(gioHang.getTenSP());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void matching() {
        recyclerView = (RecyclerView) findViewById(R.id.cart_rv_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        next = (Button) findViewById(R.id.cart_btn_next);
        total = (TextView) findViewById(R.id.cart_tv_total);
    }
}