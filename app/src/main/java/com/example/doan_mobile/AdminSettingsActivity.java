package com.example.doan_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan_mobile.Prevalent.AdminPrevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminSettingsActivity extends AppCompatActivity {

    CircleImageView profileImage;
    EditText fullName, password;
    TextView change, close, save;
    ImageView show_password;

    Uri imageUri;
    String myUrl = "";
    StorageTask uploadTask;
    StorageReference storageReference;
    String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        storageReference = FirebaseStorage.getInstance().getReference().child("HinhAvatarAdmin");

        matching();

        userInfoDisplay(profileImage, fullName, password);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")) {
                    userInfoSaved();
                } else {
                    updateOnlyUserInfo();
                }
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(AdminSettingsActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImage.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Có lỗi đã xảy ra, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminSettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().
                getReference().child("QuanTri");

        String sName = fullName.getText().toString().trim();
        String sPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(sName)) {
            Toast.makeText(this, "Họ và tên không được rỗng", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(sPassword)) {
            Toast.makeText(this, "Mật khẩu không được rỗng", Toast.LENGTH_SHORT).show();
        } else if (sPassword.length() <= 5) {
            Toast.makeText(this, "Mật khẩu tối thiểu là 6 kí tự", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("HoTen", sName);
            userMap.put("MatKhau", sPassword);
            ref.child(AdminPrevalent.currentOnlineAdmin.getDienThoai()).updateChildren(userMap);


            Toast.makeText(AdminSettingsActivity.this, "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }



    private void userInfoSaved() {
        String sName = fullName.getText().toString().trim();
        String sPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(sName)) {
            Toast.makeText(this, "Họ và tên không được rỗng", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(sPassword)) {
            Toast.makeText(this, "Mật khẩu không được rỗng", Toast.LENGTH_SHORT).show();
        } else if (sPassword.length() <= 5) {
            Toast.makeText(this, "Mật khẩu tối thiểu là 6 kí tự", Toast.LENGTH_SHORT).show();
        } else if (checker.equals("clicked")) {
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Cập nhật hồ sơ");
        progressDialog.setMessage("Vui lòng đợi trong khi chúng tôi đang cập nhật tài khoản của bạn");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageReference.child
                    (AdminPrevalent.currentOnlineAdmin.getDienThoai() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    String sName = fullName.getText().toString().trim();
                    String sPassword = password.getText().toString().trim();

                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().
                                getReference().child("QuanTri");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("HoTen", sName);
                        userMap.put("MatKhau", sPassword);
                        userMap.put("Avatar", myUrl);
                        ref.child(AdminPrevalent.currentOnlineAdmin.getDienThoai()).updateChildren(userMap);

                        progressDialog.dismiss();

                        Toast.makeText(AdminSettingsActivity.this, "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(AdminSettingsActivity.this, "Lỗi cập nhật hồ sơ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(AdminSettingsActivity.this, "Ảnh đại diện chưa được chọn", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(CircleImageView profileImage, EditText fullName, EditText password) {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().
                child("QuanTri").child(AdminPrevalent.currentOnlineAdmin.getDienThoai());
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String sName = snapshot.child("HoTen").getValue().toString();
                    String sPassword = snapshot.child("MatKhau").getValue().toString();

                    if (snapshot.child("Avatar").exists()) {

                        String sImage = snapshot.child("Avatar").getValue().toString();

                        Picasso.get().load(sImage).into(profileImage);
                        fullName.setText(sName);
                        password.setText(sPassword);

                    } else {

                        fullName.setText(sName);
                        password.setText(sPassword);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void matching() {
        profileImage = (CircleImageView) findViewById(R.id.adminSettings_ap_profileImage);
        fullName = (EditText) findViewById(R.id.adminSettings_et_fullName);
        password = (EditText) findViewById(R.id.adminSettings_et_password);
        change = (TextView) findViewById(R.id.adminSettings_tv_profileImageChange);
        close = (TextView) findViewById(R.id.adminSettings_tv_close);
        save = (TextView) findViewById(R.id.adminSettings_tv_update);
        show_password = (ImageView) findViewById(R.id.show_pass_btn);
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