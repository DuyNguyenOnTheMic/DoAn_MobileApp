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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    Button signup;
    EditText name, phone, password;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        matching();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sname = name.getText().toString().trim();
                String sphone = phone.getText().toString().trim();
                String spassword = password.getText().toString().trim();

                if (TextUtils.isEmpty(sname) || TextUtils.isEmpty(sphone) || TextUtils.isEmpty(spassword) ) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (spassword.length() <= 5) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu tối thiểu là 6 kí tự", Toast.LENGTH_SHORT).show();
                } else if (sphone.length() != 10) {
                    Toast.makeText(RegisterActivity.this, "Số điện thoại phải có 10 số", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Tạo tài khoản");
                    loadingBar.setMessage("Vui lòng đợi trong khi chúng tôi đang tạo tài khoản");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    createAccount(sname, sphone, spassword);
                }

            }

            private void createAccount(String sname, String sphone, String spassword) {
                DatabaseReference Ref;
                Ref = FirebaseDatabase.getInstance().getReference();


                Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!(snapshot.child("NguoiDung").child(sphone).exists())) {
                            HashMap<String, Object> userdataMap = new HashMap<>();
                            userdataMap.put("HoTen", sname);
                            userdataMap.put("DienThoai", sphone);
                            userdataMap.put("MatKhau", spassword);

                            Ref.child("NguoiDung").child(sphone).updateChildren(userdataMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                loadingBar.dismiss();
                                                Toast.makeText(RegisterActivity.this, "Lỗi đăng ký, vui lòng kiểm tra lại đường truyền", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Số điện thoại này đã được đăng ký", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("LOI TAO TK", error.toString());
                    }
                });
            }

        });
    }

    private void matching() {
        signup = (Button) findViewById(R.id.register_btn_register);
        name = (EditText) findViewById(R.id.register_et_name);
        phone = (EditText) findViewById(R.id.register_et_phone);
        password = (EditText) findViewById(R.id.register_et_password);
        loadingBar = new ProgressDialog(this);
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