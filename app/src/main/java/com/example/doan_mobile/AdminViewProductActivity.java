package com.example.doan_mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_mobile.Model.SanPham;
import com.example.doan_mobile.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class AdminViewProductActivity extends AppCompatActivity {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton add;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_product);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("SanPham");

        matching();

        recyclerView = findViewById(R.id.adminViewProduct_recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminViewProductActivity.this, AdminAddNewProductActivity.class));
                finish();
            }
        });

    }

    private void matching() {
        add = (FloatingActionButton) findViewById(R.id.adminViewProduct_fab_add);
        back = (ImageView) findViewById(R.id.adminViewProduct_iv_back);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<SanPham> options =
                new FirebaseRecyclerOptions.Builder<SanPham>()
                        .setQuery(ProductsRef,SanPham.class)
                        .build();

        FirebaseRecyclerAdapter<SanPham, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<SanPham, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull SanPham sanPham) {

                        double dPrice = Double.parseDouble(sanPham.getGiaGoc());
                        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
                        String sPrice = nf.format(dPrice);

                        productViewHolder.productName.setText(sanPham.getTenSP());
                        productViewHolder.productDecription.setText(sanPham.getThongTinChiTietSP());
                        productViewHolder.productPrice.setText(sPrice  + " VNĐ");
                        Picasso.get().load(sanPham.getHinhAnhSP()).into(productViewHolder.productImage);

                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Chỉnh sửa",
                                                "Xóa"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewProductActivity.this);
                                builder.setTitle("Lựa chọn: ");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if( i == 0){
                                            Intent intent = new Intent(AdminViewProductActivity.this, AdminMaintainProductsActivity.class);
                                            intent.putExtra("MaSP", sanPham.getID());
                                            intent.putExtra("TenHangSP", sanPham.getTenHangSP());
                                            startActivity(intent);
                                        }
                                        if(i==1){
                                            AlertDialog.Builder confirm_dialog = new AlertDialog.Builder(AdminViewProductActivity.this);
                                            AlertDialog alert = confirm_dialog.create();

                                            confirm_dialog.setTitle("Thông báo");
                                            confirm_dialog.setMessage("Bạn có chắc muốn xoá " + sanPham.getTenSP() + "?");
                                            confirm_dialog.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    String key = getRef(productViewHolder.getAdapterPosition()).getKey();

                                                    ProductsRef.child(key)
                                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(AdminViewProductActivity.this, "Xóa sản phẩm thành công rồi nè :)", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                                overridePendingTransition(0, 0);
                                                                startActivity(getIntent());
                                                                overridePendingTransition(0, 0);
                                                            }
                                                        }
                                                    });

                                                }
                                            });
                                            confirm_dialog.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    alert.dismiss();
                                                }
                                            });
                                            confirm_dialog.show();

                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}