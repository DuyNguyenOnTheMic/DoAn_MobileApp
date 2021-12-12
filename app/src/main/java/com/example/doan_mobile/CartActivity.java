package com.example.doan_mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_mobile.Model.GioHang;
import com.example.doan_mobile.Prevalent.Prevalent;
import com.example.doan_mobile.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button next;
    TextView total, mslg;
    ImageView thanks;

    int overTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        matching();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerView.getChildCount() == 0) {
                    Toast.makeText(CartActivity.this, "Bạn chưa thêm gì vào giỏ hàng mà", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(CartActivity.this, ConfimFinalOrderActivity.class);
                    intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderState();

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


                int oneTypeProductTPrice = Integer.valueOf(gioHang.getGiaGoc()) * Integer.valueOf(gioHang.getSoLuongMua());
                overTotalPrice += oneTypeProductTPrice;
                NumberFormat nf2 = NumberFormat.getInstance(new Locale("en", "US"));
                String sPrice2 = nf.format(overTotalPrice);

                total.setText("Tổng tiền: " + sPrice2 + " VNĐ");


                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Chỉnh sửa",
                                        "Xóa"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Lựa chọn: ");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if ( i == 0){
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("MaSP", gioHang.getMaSP());
                                    startActivity(intent);
                                }
                                if ( i == 1){
                                    cartListRef.child("ViewKhachHang")
                                            .child(Prevalent.currentOnlineUser.getDienThoai())
                                            .child("SanPham")
                                            .child(gioHang.getMaSP())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(CartActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                        overridePendingTransition(0, 0);
                                                        startActivity(getIntent());
                                                        overridePendingTransition(0, 0);
                                                    }
                                                }
                                            });

                                    cartListRef.child("ViewQuanTri")
                                            .child(Prevalent.currentOnlineUser.getDienThoai())
                                            .child("SanPham")
                                            .child(gioHang.getMaSP())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        finish();
                                                        overridePendingTransition(0, 0);
                                                        startActivity(getIntent());
                                                        overridePendingTransition(0, 0);
                                                    }
                                                }
                                            });

                                }
                            }
                        });
                        builder.show();
                    }
                });
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

    private void checkOrderState(){
        DatabaseReference orderRef;

        orderRef = FirebaseDatabase.getInstance().getReference()
                .child("DonHang")
                .child(Prevalent.currentOnlineUser.getDienThoai());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String shippingState = snapshot.child("TinhTrang").getValue().toString();

                    if (shippingState.equals("Đang giao") | shippingState.equals("Chờ xác nhận")){
                        total.setText("Tình trạng: " + shippingState);
                        recyclerView.setVisibility(View.GONE);

                        thanks.setVisibility(View.VISIBLE);
                        mslg.setVisibility(View.VISIBLE);
                        next.setVisibility(View.GONE);
                        //Toast.makeText(CartActivity.this, "Bạn có thể mua nhiều sản phẩm hơn sau khi đã hoàn thành đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void matching() {
        recyclerView = (RecyclerView) findViewById(R.id.cart_rv_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        next = (Button) findViewById(R.id.cart_btn_next);
        total = (TextView) findViewById(R.id.cart_tv_total);
        mslg = (TextView) findViewById(R.id.cart_tv_mslg);
        thanks = (ImageView) findViewById(R.id.cart_iv_thanks);
    }
}