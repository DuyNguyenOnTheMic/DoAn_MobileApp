package com.example.doan_mobile;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AdminAddNewProductActivity extends AppCompatActivity {

    String categoryName, dowloadImageUri, ID;
    String sName, sDescription, sPrice, sQuantity;
    ImageView inputImage, back;
    Button addNewProduct;
    Spinner pCategoryName;
    EditText pName, pDescription, pPrice, pQuantity;
    private static final int GalleryPick = 1;
    List<String> pCategoryList;

    Uri ImageUri;
    StorageReference ProductImagesRef;
    DatabaseReference ProductRef, ProductCategoryRef;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        matching();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("HinhAnhSP");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("SanPham");

        pCategoryList = new ArrayList<>();

        ProductCategoryRef = FirebaseDatabase.getInstance().getReference();
        ProductCategoryRef.child("HangSP").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mySnapShot : snapshot.getChildren()) {
                    String spinnerpCategory = mySnapShot.child("TenHangSP").getValue(String.class);
                    pCategoryList.add(spinnerpCategory);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AdminAddNewProductActivity.this, android.R.layout.simple_spinner_item, pCategoryList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                pCategoryName.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //pCategoryName.setEnabled(false);
        //pCategoryName.setText(categoryName);
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

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminAddNewProductActivity.this, AdminViewProductActivity.class));
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(AdminAddNewProductActivity.this, AdminViewProductActivity.class));
        finish();
    }

    private void ValidateProduct() {
        sName = pName.getText().toString().trim();
        sDescription = pDescription.getText().toString().trim();
        sPrice = pPrice.getText().toString().trim();
        sQuantity = pQuantity.getText().toString().trim();

        if (ImageUri == null) {
            Toast.makeText(getApplicationContext(), "Ch??a c?? h??nh ???nh s???n ph???m n?? :(", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(sName)) {
            Toast.makeText(getApplicationContext(), "Ch??a c?? t??n s???n ph???m n?? :(", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(sDescription)) {
            Toast.makeText(getApplicationContext(), "Ch??a c?? m?? t??? s???n ph???m n?? :(", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(sPrice)) {
            Toast.makeText(getApplicationContext(), "Ch??a c?? gi?? s???n ph???m n?? :(", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(sQuantity)) {
            Toast.makeText(getApplicationContext(), "Ch??a c?? s??? l?????ng t???n kho s???n ph???m n?? :(", Toast.LENGTH_LONG).show();
        } else {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {
        loadingBar.setTitle("Th??m m???i s???n ph???m");
        loadingBar.setMessage("Vui l??ng ?????i, ch??ng t??i ??ang th??m s???n ph???m m???i");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("ddMMyyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        ID = saveCurrentDate + " - " + saveCurrentTime;

        StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + ".png");
        final UploadTask uploadTask = filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminAddNewProductActivity.this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
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

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {

        categoryName = pCategoryName.getSelectedItem().toString();

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("ID", ID);
        productMap.put("TenHangSP", categoryName);
        productMap.put("TenSP", sName);
        productMap.put("ThongTinChiTietSP", sDescription);
        productMap.put("GiaGoc", sPrice);
        productMap.put("SoLuongTonKho", sQuantity);
        productMap.put("HinhAnhSP", dowloadImageUri);
        ProductRef.child(ID).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(AdminAddNewProductActivity.this, AdminViewProductActivity.class));

                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, "Th??m s???n ph???m th??nh c??ng ^^", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
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

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            inputImage.setImageURI(ImageUri);
        }
    }

    private void matching() {
        addNewProduct = (Button) findViewById(R.id.adminAddNewProduct_btn_add);
        pName = (EditText) findViewById(R.id.adminAddNewProduct_et_name);
        pDescription = (EditText) findViewById(R.id.adminAddNewProduct_et_productDescription);
        pPrice = (EditText) findViewById(R.id.adminAddNewProduct_et_price);
        pCategoryName = (Spinner) findViewById(R.id.adminAddNewProduct_spinner_categoryName);
        pQuantity = (EditText) findViewById(R.id.adminAddNewProduct_et_quantity);
        inputImage = (ImageView) findViewById(R.id.adminAddNewProduct_iv_selectImg);
        back = (ImageView) findViewById(R.id.adminAddNewProduct_iv_back);
        loadingBar = new ProgressDialog(this);
    }
}