package com.example.doan_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan_mobile.Model.NguoiDung;
import com.example.doan_mobile.Model.QuanTri;
import com.example.doan_mobile.Prevalent.AdminPrevalent;
import com.example.doan_mobile.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    EditText phone, password;
    Button login;
    ProgressDialog loadingBar;
    CheckBox rememberMe;
    TextView adminLink, notAdminLink;

    String parentDbName = "NguoiDung";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        matching();

        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sphone = phone.getText().toString().trim();
                String spassword = password.getText().toString().trim();

                if (TextUtils.isEmpty(sphone) || TextUtils.isEmpty(spassword)) {
                    Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Đăng nhập tài khoản");
                    loadingBar.setMessage("Vui lòng đợi trong khi chúng tôi đang xác thực thông tin");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    allowAccessToAccount(sphone, spassword);
                }
            }

            private void allowAccessToAccount(String sphone, String spassword) {
                if (rememberMe.isChecked()) {
                    Paper.book().write(Prevalent.UserPhoneKey, sphone);
                    Paper.book().write(Prevalent.UserPasswordKey, spassword);
                }

                final DatabaseReference Ref;
                Ref = FirebaseDatabase.getInstance().getReference();

                Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(parentDbName).child(sphone).exists()) {

                            NguoiDung usersData = snapshot.child(parentDbName).child(sphone).getValue(NguoiDung.class);
                            QuanTri adminsData = snapshot.child(parentDbName).child(sphone).getValue(QuanTri.class);

                            if (usersData.getDienThoai().equals(sphone)) {
                                if (usersData.getMatKhau().equals(spassword)) {
                                    if (parentDbName.equals("QuanTri")) {
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        AdminPrevalent.currentOnlineAdmin = adminsData;
                                        startActivity(new Intent(LoginActivity.this, AdminCategoryActivity.class));
                                    } else if (parentDbName.equals("NguoiDung")){
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Prevalent.currentOnlineUser = usersData;
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    }
                                } else {
                                    loadingBar.dismiss();
                                    Toast.makeText(LoginActivity.this, "Sai mật khẩu, vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Số điện thoại này chưa được đăng ký, vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("LOI DANG NHAP", error.toString());
                    }
                });
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setText("Đăng nhập quản trị");
                adminLink.setVisibility(view.INVISIBLE);
                notAdminLink.setVisibility(view.VISIBLE);
                parentDbName = "QuanTri";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setText("Đăng nhập");
                adminLink.setVisibility(view.VISIBLE);
                notAdminLink.setVisibility(view.INVISIBLE);
                parentDbName = "NguoiDung";
            }
        });
    }

    private void matching() {
        phone = (EditText) findViewById(R.id.login_et_phone);
        password = (EditText) findViewById(R.id.login_et_password);
        login = (Button) findViewById(R.id.login_btn_login);
        loadingBar = new ProgressDialog(this);
        rememberMe = (CheckBox) findViewById(R.id.login_cb_remmemberMe);
        adminLink = (TextView) findViewById(R.id.login_tv_adminLogin);
        notAdminLink = (TextView) findViewById(R.id.login_tv_notAdmin);
    }

    public void ShowHidePass(View view) {
        if(view.getId()==R.id.show_pass_btn){

            if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.hidden);

                //Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.eye);

                //Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
}