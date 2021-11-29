package com.example.doan_mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_mobile.Model.DonHang;
import com.example.doan_mobile.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

public class AdminViewOrderActivity extends AppCompatActivity {

    RecyclerView ordersList;
    DatabaseReference ordersRef, cartRef, productRef;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_order);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("DonHang");
        cartRef = FirebaseDatabase.getInstance().getReference().child("GioHang");
        productRef = FirebaseDatabase.getInstance().getReference().child("SanPham");

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
                        orderViewHolder.userDateTime.setText("Ngày đặt hàng:\n" + donHang.getNgay() + " " + donHang.getThoiGian());
                        orderViewHolder.userAddress.setText("Địa chỉ: " + donHang.getDiaChi() + ", " + donHang.getThanhPho());
                        orderViewHolder.orderStatus.setText("Tình trạng: " + donHang.getTinhTrang());

                        orderViewHolder.showProducts.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String MaKH = getRef(orderViewHolder.getAdapterPosition()).getKey();

                                Intent intent = new Intent(AdminViewOrderActivity.this, AdminUserProductActivity.class);
                                intent.putExtra("MaKH", MaKH);
                                startActivity(intent);
                            }
                        });

                        orderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]{
                                        "Cập nhật tình trạng đơn hàng"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewOrderActivity.this);
                                builder.setTitle("Lựa chọn:");

                                AlertDialog alert = builder.create();

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {

                                            CharSequence order_status[] = new CharSequence[]{
                                                    "Chờ xác nhận",
                                                    "Đang giao",
                                                    "Hoàn thành"
                                            };

                                            AlertDialog.Builder orderStatusBuilder = new AlertDialog.Builder(AdminViewOrderActivity.this);
                                            orderStatusBuilder.setTitle("Cập nhật tình trạng đơn hàng:");

                                            orderStatusBuilder.setItems(order_status, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String MaKH = getRef(orderViewHolder.getAdapterPosition()).getKey();

                                                    if (which == 0) {

                                                        updateOrderStatus_ChoXacNhan(MaKH);

                                                    }
                                                    if (which == 1) {

                                                        updateOrderStatus_DangGiao(MaKH);

                                                    }
                                                    if (which == 2) {

                                                        AlertDialog.Builder confirm_dialog = new AlertDialog.Builder(AdminViewOrderActivity.this);
                                                        AlertDialog alert = confirm_dialog.create();

                                                        confirm_dialog.setTitle("Thông báo");
                                                        confirm_dialog.setMessage("Bạn có chắc muốn hoàn thành đơn hàng của " + donHang.getTenKH() + " ?" + "\nĐiều này sẽ dẫn đến đơn hàng sẽ bị xoá đi?");
                                                        confirm_dialog.setPositiveButton("Hoàn thành", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                removeOrder(MaKH);
                                                                removeAdminCart(MaKH);
                                                                Toast.makeText(AdminViewOrderActivity.this, "Đơn hàng đã xoá thành công!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        confirm_dialog.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                alert.dismiss();
                                                            }
                                                        });
                                                        confirm_dialog.show();
                                                    }
                                                }
                                            });
                                            orderStatusBuilder.show();

                                        }
                                    }
                                });
                                builder.show();

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

    private void updateOrderStatus_DangGiao(String maKH) {
        HashMap<String, Object> OrderMap = new HashMap<>();
        OrderMap.put("TinhTrang", "Đang giao");

        ordersRef.child(maKH).updateChildren(OrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminViewOrderActivity.this, "Cập nhật tình trạng đơn hàng thành công ^^", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminViewOrderActivity.this, "Error: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateOrderStatus_ChoXacNhan(String maKH) {

        HashMap<String, Object> OrderMap = new HashMap<>();
        OrderMap.put("TinhTrang", "Chờ xác nhận");

        ordersRef.child(maKH).updateChildren(OrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminViewOrderActivity.this, "Cập nhật tình trạng đơn hàng thành công ^^", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminViewOrderActivity.this, "Error: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void removeAdminCart(String maKH) {
        cartRef.child("ViewQuanTri").child(maKH).removeValue();
    }

    private void removeOrder(String maKH) {
        ordersRef.child(maKH).removeValue();
    }

    private void matching() {
        ordersList = (RecyclerView) findViewById(R.id.adminViewOrder_recycler_menu);
        back = (ImageView) findViewById(R.id.adminViewOrder_iv_back);
    }
}