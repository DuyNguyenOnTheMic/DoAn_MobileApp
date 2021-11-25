package com.example.doan_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_mobile.Model.HangSP;
import com.example.doan_mobile.ViewHolder.ProductCategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
                int i_id =  recyclerView.getChildCount();
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
                    protected void onBindViewHolder(@NonNull ProductCategoryViewHolder productCategoryViewHolder, int i, @NonNull HangSP hangSP) {
                        productCategoryViewHolder.productCategoryName.setText(hangSP.getTenHangSP());
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