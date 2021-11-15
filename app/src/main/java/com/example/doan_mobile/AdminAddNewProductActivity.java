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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    String categoryID, dowloadImageUri;
    String sName, sDescription,sPrice;
    ImageView inputImage;
    Button addNewProduct;
    EditText pName, pDescription, pPrice;
    private  static  final int GalleryPick = 1;
    Uri ImageUri;
    StorageReference ProductImagesRef;
    DatabaseReference ProductRef;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        matching();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("HinhAnhSP");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("SanPham");
        categoryID = getIntent().getExtras().get("HangSP").toString();
        Intent intent = getIntent();

        inputImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProduct();

                /*FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("SanPham");
                String sID = reference.push().getKey();

                reference.child(sID).child("ID").setValue(sID);
                reference.child(sID).child("MaHangSP").setValue(categoryName);
                reference.child(sID).child("TenSP").setValue(sName);
                reference.child(sID).child("ThongTinChiTietSP").setValue(sDescription);
                reference.child(sID).child("GiaGoc").setValue(sPrice);
                Toast.makeText(getApplicationContext(),"Thêm thành công contact",Toast.LENGTH_LONG).show();
                finish();*/
            }
        });

    }

    private void ValidateProduct() {
         sName = pName.getText().toString().trim();
         sDescription = pDescription.getText().toString().trim();
         sPrice = pPrice.getText().toString().trim();

        if (ImageUri == null){
            Toast.makeText(getApplicationContext(),"Hình ảnh đâu ??-.-??",Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(sDescription)){
            Toast.makeText(getApplicationContext(),"Chưa có mô tả kìa -.-",Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(sName)){
            Toast.makeText(getApplicationContext(),"Bán cái gì mà hong có tên ??-.-??",Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(sPrice)){
            Toast.makeText(getApplicationContext(),"Rồi bán mà hong có giá hả ??-.-??",Toast.LENGTH_LONG).show();
        }else{
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {
        loadingBar.setTitle("Thêm mới sản phẩm");
        loadingBar.setMessage("Gửi admin,  vui lòng đợi, chúng tôi đang thêm sản phẩm mới");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() +".png");
        final UploadTask uploadTask = filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminAddNewProductActivity.this,"Error: "+e.toString(),Toast.LENGTH_LONG).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this,"Có hình ảnh rồi đó ^^: ",Toast.LENGTH_LONG).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()){
                            throw task.getException();

                        }
                         dowloadImageUri = filePath.getDownloadUrl().toString();
                         return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            dowloadImageUri = task.getResult().toString();
                            Toast.makeText(AdminAddNewProductActivity.this,"Lấy Url hình ảnh sản phẩm thành công",Toast.LENGTH_LONG).show();

                        SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        String sID = ProductRef.push().getKey();
        productMap.put("ID",sID);
        productMap.put("MaHangSP",categoryID);
        productMap.put("TenSP",sName);
        productMap.put("ThongTinChiTietSP",sDescription);
        productMap.put("GiaGoc",sPrice);
        productMap.put("HinhAnhSP",dowloadImageUri);
        ProductRef.child(sID).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class));

                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, "Thêm sản phẩm thành công ^^", Toast.LENGTH_SHORT).show();
                }else{
                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, "Error: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data!=null){
            ImageUri = data.getData();
            inputImage.setImageURI(ImageUri);
        }
    }

    private void matching() {
        addNewProduct = (Button) findViewById(R.id.adminAddNewProduct_btn_add);
        pName = (EditText) findViewById(R.id.adminAddNewProduct_et_name);
        pDescription = (EditText) findViewById(R.id.adminAddNewProduct_et_productDescription);
        pPrice = (EditText) findViewById(R.id.adminAddNewProduct_et_price);
        inputImage = (ImageView) findViewById(R.id.adminAddNewProduct_iv_selectImg);
        loadingBar = new ProgressDialog(this);
    }
}