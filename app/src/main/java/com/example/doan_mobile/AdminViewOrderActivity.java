package com.example.doan_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_mobile.Model.DonHang;
import com.example.doan_mobile.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Locale;

public class AdminViewOrderActivity extends AppCompatActivity {

    RecyclerView ordersList;
    DatabaseReference ordersRef;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_order);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("DonHang");

        matching();

        ordersList.setLayoutManager(new LinearLayoutManager(this));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<DonHang> options =
                new FirebaseRecyclerOptions.Builder<DonHang>()
                .setQuery(ordersRef, DonHang.class)
                .build();

        FirebaseRecyclerAdapter<DonHang, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<DonHang, OrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, final int position, @NonNull DonHang donHang) {

                        double dPrice = Double.parseDouble(donHang.getTongTien());
                        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
                        String sPrice = nf.format(dPrice);

                        orderViewHolder.userName.setText("Tên: " + donHang.getTenKH());
                        orderViewHolder.userPhone.setText("SĐT: " + donHang.getSDT());
                        orderViewHolder.userTotalPrice.setText("Tổng tiền: " + sPrice);
                        orderViewHolder.userDateTime.setText("Ngày đặt hàng:\n" + donHang.getNgay() + " " +  donHang.getThoiGian());
                        orderViewHolder.userAddress.setText("Địa chỉ: " + donHang.getDiaChi() + ", " + donHang.getThanhPho());

                        orderViewHolder.showProducts.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String MaKH = getRef(orderViewHolder.getAdapterPosition()).getKey();

                                Intent intent = new Intent(AdminViewOrderActivity.this, AdminUserProductActivity.class);
                                intent.putExtra("MaKH", MaKH);
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_orders_layout, parent, false);
                        OrderViewHolder holder = new OrderViewHolder(view);
                        return holder;
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void matching() {
        ordersList = (RecyclerView) findViewById(R.id.adminViewOrder_recycler_menu);
        back = (ImageView) findViewById(R.id.adminViewOrder_iv_back);
    }
}