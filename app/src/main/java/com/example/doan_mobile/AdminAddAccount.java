package com.example.doan_mobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminAddAccount extends AppCompatActivity {

    String dowloadImageUri, sRole;
    String sname, sphone, spass;
    EditText fullname, phone, pass;
    ImageView inputImage, back;
    Uri ImageUri;
    Button add;
    Spinner spRole;
    TextView change;
    DatabaseReference AdminAccountRef;
    StorageReference AdminImagesRef;
    ProgressDialog loadingBar;
    private static final int GalleryPick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_account);

        matching();

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Vai trò của bạn");
        arrayList.add("Admin");
        arrayList.add("Sale");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(arrayAdapter);

        AdminImagesRef = FirebaseStorage.getInstance().getReference().child("HinhAnhSP");
        AdminAccountRef = FirebaseDatabase.getInstance().getReference().child("QuanTri");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        inputImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(fullname.getText().toString())) {
                    Toast.makeText(AdminAddAccount.this, "Vui lòng nhập họ và tên", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(phone.getText().toString())) {
                    Toast.makeText(AdminAddAccount.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(pass.getText().toString())) {
                    Toast.makeText(AdminAddAccount.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    AdminAddAccount();
                }
            }
        });
    }

    private void OpenGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GalleryPick);
    }


    private void AdminAddAccount() {
        sname = fullname.getText().toString().trim();
        sphone = phone.getText().toString().trim();
        spass = pass.getText().toString().trim();

        if (ImageUri == null) {
            Toast.makeText(getApplicationContext(), "Chưa có hình ảnh", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(sname)) {
            Toast.makeText(getApplicationContext(), "Chưa nhập họ tên", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(sphone)) {
            Toast.makeText(getApplicationContext(), "Chưa nhập số điện thoại", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(spass)) {
            Toast.makeText(getApplicationContext(), "Chưa nhập mật khẩu", Toast.LENGTH_LONG).show();
        }  else {
            AdminAccountInformation();
        }


    }

    private void AdminAccountInformation() {
        loadingBar.setTitle("Thêm tài khoản quản trị thành công");
        loadingBar.setMessage("Vui lòng đợi, chúng tôi đang thêm mới tài khoản");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        StorageReference filePath = AdminImagesRef.child(ImageUri.getLastPathSegment() + ".png");
        final UploadTask uploadTask = filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminAddAccount.this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }
                        dowloadImageUri = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            dowloadImageUri = task.getResult().toString();

                            SaveAccountInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveAccountInfoToDatabase() {

        sRole = spRole.getSelectedItem().toString();

        HashMap<String, Object> accountMap = new HashMap<>();
        accountMap.put("HoTen", sname);
        accountMap.put("DienThoai", sphone);
        accountMap.put("MatKhau", spass);
        accountMap.put("VaiTro", sRole);
        accountMap.put("Avatar", dowloadImageUri);
        AdminAccountRef.child(sphone).updateChildren(accountMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(AdminAddAccount.this, AdminViewAdminActivity.class));

                    loadingBar.dismiss();
                    Toast.makeText(AdminAddAccount.this, "Thêm tài khoản thành công ^^", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(AdminAddAccount.this, "Error: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            inputImage.setImageURI(ImageUri);
        }
    }


    private void matching() {
        change = (TextView) findViewById(R.id.adminAddAccount_tv_profileImageChange);
        fullname = (EditText) findViewById(R.id.adminAddAccount_et_name);
        phone = (EditText) findViewById(R.id.adminAddAccount_et_phone);
        pass = (EditText) findViewById(R.id.adminAddAccount_et_pass);
        inputImage = (ImageView) findViewById(R.id.adminAddAccount_iv_selectImg);
        back = (ImageView) findViewById(R.id.adminAddAccount_iv_back);
        add = (Button) findViewById(R.id.adminAddAccount_btn_add);
        spRole = (Spinner) findViewById(R.id.adminAddAccount_spinner_Role);
        loadingBar = new ProgressDialog(this);

    }
}