package com.example.doan_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.doan_mobile.Model.NguoiDung;
import com.example.doan_mobile.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button signup, login;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        matching();

        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != "") {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                allowAccess(UserPhoneKey, UserPasswordKey);

                loadingBar.setTitle("Đã đăng nhập");
                loadingBar.setMessage("Vui lòng đợi...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }


    }

    private void allowAccess(String sphone, String spassword) {
        final DatabaseReference Ref;
        Ref = FirebaseDatabase.getInstance().getReference();

        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("NguoiDung").child(sphone).exists()) {

                    NguoiDung usersData = snapshot.child("NguoiDung").child(sphone).getValue(NguoiDung.class);

                    if (usersData.getDienThoai().equals(sphone)) {
                        if (usersData.getMatKhau().equals(spassword)) {
                            loadingBar.dismiss();

                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Sai mật khẩu, vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Số điện thoại này chưa được đăng ký, vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("LOI DANG NHAP", error.toString());
            }
        });
    }

    private void matching() {
        signup = (Button) findViewById(R.id.main_btn_signup);
        login = (Button) findViewById(R.id.main_btn_login);
        loadingBar = new ProgressDialog(this);
    }
}