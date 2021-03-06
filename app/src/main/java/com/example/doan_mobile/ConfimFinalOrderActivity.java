package com.example.doan_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan_mobile.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ConfimFinalOrderActivity extends AppCompatActivity {
    EditText name, phone, address, city;
    Button confirm;
    String total = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confim_final_order);
        maching();

        total = getIntent().getStringExtra("Total Price");
        double dPrice = Double.parseDouble(total);
        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
        String sPrice = nf.format(dPrice);
        Toast.makeText(this, "Vui lòng điền thông tin giao hàng để tiếp tục", Toast.LENGTH_SHORT).show();

        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().
                child("NguoiDung").child(Prevalent.currentOnlineUser.getDienThoai());
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                if (snapshot.exists()) {

                    String sName = snapshot.child("HoTen").getValue().toString();
                    String sPhone = snapshot.child("DienThoai").getValue().toString();

                    if (snapshot.child("DiaChi").exists()) {
                        String sAddress = snapshot.child("DiaChi").getValue().toString();

                        name.setText(sName);
                        phone.setText(sPhone);
                        address.setText(sAddress);

                    } else {

                        name.setText(sName);
                        phone.setText(sPhone);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                check();
            }
        });
    }

    private void check() {
        if (TextUtils.isEmpty(name.getText().toString())){
            Toast.makeText(this, "Vui lòng nhập tên !", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone.getText().toString())){
            Toast.makeText(this, "Vui lòng nhập số điện thoại !", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(address.getText().toString())){
            Toast.makeText(this, "Vui lòng nhập địa chỉ !", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(city.getText().toString())){
            Toast.makeText(this, "Vui lòng nhập thành phố !", Toast.LENGTH_SHORT).show();
        } else {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        String saveCurrentTime, saveCurrentDate;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                .child("DonHang")
                .child(Prevalent.currentOnlineUser.getDienThoai());

        final HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("TongTien", total);
        orderMap.put("TenKH", name.getText().toString().trim());
        orderMap.put("SDT", phone.getText().toString().trim());
        orderMap.put("DiaChi", address.getText().toString().trim());
        orderMap.put("ThanhPho", city.getText().toString().trim());
        orderMap.put("Ngay", saveCurrentDate);
        orderMap.put("ThoiGian", saveCurrentTime);
        orderMap.put("TinhTrang", "Chờ xác nhận");
        
        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference()
                            .child("GioHang")
                            .child("ViewKhachHang")
                            .child(Prevalent.currentOnlineUser.getDienThoai())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ConfimFinalOrderActivity.this, "Đặt hàng thành công!!!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ConfimFinalOrderActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    }
                                }
                            });
                }
            }
        });
    }

    private void maching() {
        confirm = (Button) findViewById(R.id.Confirm_btn_confirm);
        name = (EditText) findViewById(R.id.Confirm_et_name);
        phone = (EditText) findViewById(R.id.Confirm_et_phone);
        address = (EditText) findViewById(R.id.Confirm_et_address);
        city = (EditText) findViewById(R.id.Confirm_et_city);
    }
}