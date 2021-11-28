package com.example.doan_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.doan_mobile.Model.SanPham;
import com.example.doan_mobile.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ProductDetailsActivity extends AppCompatActivity {

    ImageView image;
    ElegantNumberButton numberButton;
    TextView price, description, name;
    Button addToCart;

    String productID = "", state = "Thông thường";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("MaSP");

        matching();

        getProductDetails(productID);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (state.equals("Chờ xác nhận") || state.equals("Đang giao")){
                    Toast.makeText(ProductDetailsActivity.this, "Bạn có thể mua thêm sản phẩm khi đơn hàng của bạn đã hoàn thành", Toast.LENGTH_SHORT).show();
                }
                else {
                    addingToCartList();
                }
                
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderState();
    }

    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("ddMMyyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("GioHang");

        String sname = name.getText().toString().trim();
        String sprice = price.getText().toString().trim();

        String newStr = sprice.replaceAll("[,]", "");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("MaSP", productID);
        cartMap.put("TenSP", sname);
        cartMap.put("GiaGoc", newStr);
        cartMap.put("Ngay", saveCurrentDate);
        cartMap.put("ThoiGian", saveCurrentTime);
        cartMap.put("SoLuongMua", numberButton.getNumber());
        cartMap.put("GiamGia", "");

        cartRef.child("ViewKhachHang").child(Prevalent.currentOnlineUser.getDienThoai())
                .child("SanPham").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cartRef.child("ViewQuanTri").child(Prevalent.currentOnlineUser.getDienThoai())
                                    .child("SanPham").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ProductDetailsActivity.this, "Giỏ hàng đã được cập nhật!", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });


    }

    private void getProductDetails(String productID) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("SanPham");
        ref.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    SanPham sanPham = snapshot.getValue(SanPham.class);

                    double dPrice = Double.parseDouble(sanPham.getGiaGoc());
                    NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
                    String sPrice = nf.format(dPrice);

                    name.setText(sanPham.getTenSP());
                    description.setText(sanPham.getThongTinChiTietSP());
                    price.setText(sPrice);
                    Picasso.get().load(sanPham.getHinhAnhSP()).into(image);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

                    if (shippingState.equals("Đang giao")){
                        state ="Hoàn thành";
                         }
                    else if (shippingState.equals("Chờ xác nhận")){
                        state ="Đang giao";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void matching() {
        image = (ImageView) findViewById(R.id.productDetails_iv_image);
        numberButton = (ElegantNumberButton) findViewById(R.id.productDetails_btn_number);
        price = (TextView) findViewById(R.id.productDetails_tv_price);
        description = (TextView) findViewById(R.id.productDetails_tv_description);
        name = (TextView) findViewById(R.id.productDetails_tv_name);
        addToCart = (Button) findViewById(R.id.productDetails_btn_addToCart);
    }
}