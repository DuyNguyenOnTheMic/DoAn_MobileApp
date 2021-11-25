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

import com.example.doan_mobile.Model.HangSP;
import com.example.doan_mobile.ViewHolder.ProductCategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminViewProductCategoryActivity extends AppCompatActivity {

    private DatabaseReference ProductCategoryRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton add;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_product_category);

        ProductCategoryRef = FirebaseDatabase.getInstance().getReference().child("HangSP");

        matching();

        recyclerView = findViewById(R.id.adminViewProductCategory_recycler_menu);
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
            public void onClick(View view) {
                Intent intent = new Intent(AdminViewProductCategoryActivity.this, AdminAddNewProductCategoryActivity.class);
                int i_id = recyclerView.getChildCount();
                intent.putExtra("MaHangSP", i_id);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<HangSP> options =
                new FirebaseRecyclerOptions.Builder<HangSP>()
                        .setQuery(ProductCategoryRef, HangSP.class)
                        .build();

        FirebaseRecyclerAdapter<HangSP, ProductCategoryViewHolder> adapter =
                new FirebaseRecyclerAdapter<HangSP, ProductCategoryViewHolder>(options) {
                    @NonNull
                    @Override
                    public ProductCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_productcategory_items_layout, parent, false);
                        ProductCategoryViewHolder holder = new ProductCategoryViewHolder(view);
                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull ProductCategoryViewHolder productCategoryViewHolder, int position, @NonNull HangSP hangSP) {
                        productCategoryViewHolder.productCategoryName.setText(hangSP.getTenHangSP());

                        productCategoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Chỉnh sửa",
                                                "Xóa"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewProductCategoryActivity.this);
                                builder.setTitle("Lựa chọn: ");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {

                                            Intent intent = new Intent(AdminViewProductCategoryActivity.this, AdminEditProductCategoryActivity.class);
                                            String i_id = getRef(productCategoryViewHolder.getAdapterPosition()).getKey();
                                            intent.putExtra("MaHangSP", i_id);
                                            startActivity(intent);
                                            finish();

                                        }
                                        if (which == 1) {

                                            AlertDialog.Builder confirm_dialog = new AlertDialog.Builder(AdminViewProductCategoryActivity.this);
                                            AlertDialog alert = confirm_dialog.create();

                                            confirm_dialog.setTitle("Thông báo");
                                            confirm_dialog.setMessage("Bạn có chắc muốn xoá danh mục " + hangSP.getTenHangSP() + "?");
                                            confirm_dialog.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    String key = getRef(productCategoryViewHolder.getAdapterPosition()).getKey();

                                                    ProductCategoryRef.child(key)
                                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(AdminViewProductCategoryActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
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
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void matching() {
        add = (FloatingActionButton) findViewById(R.id.adminViewProductCategory_fab_add);
        back = (ImageView) findViewById(R.id.adminViewProductCategory_iv_back);
    }
}