package com.example.doan_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminMaintainProductsActivity extends AppCompatActivity {
    private Button applyChange;
    private EditText name, price, des, quantity;
    private ImageView imageView;
    Uri ImageUri;
    StorageReference ProductImagesRef;
    private  static  final int GalleryPick = 1;

    String category, dowloadImageUri;
    List<String> CategoryList;
    Spinner categoryname;
    DatabaseReference ProductCategoryRef;
    ProgressDialog loadingBar;

    private String productID, productCategoryName;
    private DatabaseReference productRef;
    String pName,pDes,pQuantity,pPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);
        matching();

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("HinhAnhSP");
        productID = getIntent().getStringExtra("MaSP");
        productCategoryName = getIntent().getStringExtra("TenHangSP");
        productRef = FirebaseDatabase.getInstance().getReference().child("SanPham").child(productID);

        CategoryList = new ArrayList<>();

        ProductCategoryRef = FirebaseDatabase.getInstance().getReference();
        ProductCategoryRef.child("HangSP").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mySnapShot:snapshot.getChildren()) {
                    String spinnerpCategory = mySnapShot.child("TenHangSP").getValue(String.class);
                    CategoryList.add(spinnerpCategory);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AdminMaintainProductsActivity.this, android.R.layout.simple_spinner_item, CategoryList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                categoryname.setAdapter(arrayAdapter);

                for (int i = 0; i < categoryname.getAdapter().getCount(); i++) {
                    if (categoryname.getAdapter().getItem(i).toString().contains(productCategoryName)) {
                        categoryname.setSelection(i);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        displaySpecificProductInfo();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        applyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Change();
            }
        });
    }

    private void matching() {
        applyChange = (Button) findViewById(R.id.adminMaintainProduct_btn_add);
        quantity = (EditText) findViewById(R.id.adminMaintainProduct_et_quantity);
        name = (EditText) findViewById(R.id.adminMaintainProduct_et_name);
        des = (EditText) findViewById(R.id.adminMaintainProduct_et_productDescription);
        price = (EditText) findViewById(R.id.adminMaintainProduct_et_price);
        categoryname = (Spinner) findViewById(R.id.adminMaintainProduct_spinner_categoryName);
        imageView = (ImageView) findViewById(R.id.adminMaintainProduct_iv_Img);
        loadingBar = new ProgressDialog(this);
    }


    private void Change() {
        pName = name.getText().toString();
        pPrice = price.getText().toString();
        pDes = des.getText().toString();
        pQuantity = quantity.getText().toString();
        if (pName.equals("")) {
            Toast.makeText(AdminMaintainProductsActivity.this, "Chưa có tên sản phẩm nè :(", Toast.LENGTH_SHORT).show();
        } else if (pPrice.equals("")) {
            Toast.makeText(AdminMaintainProductsActivity.this, "Chưa có giá sản phẩm nè :(", Toast.LENGTH_SHORT).show();
        } else if (pDes.equals("")) {
            Toast.makeText(AdminMaintainProductsActivity.this, "Chưa có mô tả sản phẩm nè :(", Toast.LENGTH_SHORT).show();
        } else if (pQuantity.equals("")) {
            Toast.makeText(AdminMaintainProductsActivity.this, "Chưa có số lượng tồn kho sản phẩm nè :(", Toast.LENGTH_SHORT).show();
        }else {

            StoreProductInformation();

        }
    }

    private void StoreProductInformation() {
        loadingBar.setTitle("Cập nhật sản phẩm");
        loadingBar.setMessage("Vui lòng đợi, chúng tôi đang cập nhật sản phẩm");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        if (ImageUri == null){
            newChange();
        }
        else{
            StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() +".png");
            final UploadTask uploadTask = filePath.putFile(ImageUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminMaintainProductsActivity.this,"Error: "+e.toString(),Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

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

                                newChange();
                            }
                        }
                    });
                }
            });
        }
        }


    private void newChange() {
        category= categoryname.getSelectedItem().toString();

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("TenSP", pName);
        productMap.put("ThongTinChiTietSP", pDes);
        productMap.put("GiaGoc", pPrice);
        productMap.put("SoLuongTonKho", pQuantity);
        productMap.put("TenHangSP",category);
        productMap.put("HinhAnhSP",dowloadImageUri);

        productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AdminMaintainProductsActivity.this, "Cập nhật sản phẩm thành công rồi nè :)", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminViewProductActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    private void displaySpecificProductInfo() {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String pName = snapshot.child("TenSP").getValue().toString();
                    String pPrice = snapshot.child("GiaGoc").getValue().toString();
                    String pDes = snapshot.child("ThongTinChiTietSP").getValue().toString();
                    dowloadImageUri = snapshot.child("HinhAnhSP").getValue().toString();
                    String pQuantity = snapshot.child("SoLuongTonKho").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    des.setText(pDes);
                    quantity.setText(pQuantity);
                    Picasso.get().load(dowloadImageUri).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data!=null){
            ImageUri = data.getData();
            imageView.setImageURI(ImageUri);
        }
    }

    private void OpenGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GalleryPick);
    }
}

