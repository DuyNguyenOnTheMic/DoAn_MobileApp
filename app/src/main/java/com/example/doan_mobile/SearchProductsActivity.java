package com.example.doan_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_mobile.Model.SanPham;
import com.example.doan_mobile.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.EditText;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class SearchProductsActivity extends AppCompatActivity {
    Button search;
    com.rey.material.widget.EditText input;
    RecyclerView list;
    String SearchLInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);
        matching();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchLInput = input.getText().toString();

                onStart();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("SanPham");



        FirebaseRecyclerOptions<SanPham> options = new FirebaseRecyclerOptions.Builder<SanPham>()
                .setQuery(reference.orderByChild("TenSP").startAt(SearchLInput).endAt(SearchLInput + "\uf8ff"), SanPham.class)
                .build();

        FirebaseRecyclerAdapter<SanPham, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<SanPham, ProductViewHolder>(options) {
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
                        Intent intent = new Intent(SearchProductsActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("MaSP", sanPham.getID());
                        startActivity(intent);
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
        list.setAdapter(adapter);
        adapter.startListening();
    }

    private void matching() {
        search = (Button) findViewById(R.id.searchproducts_btn_search);
        input =(EditText) findViewById(R.id.searchproducts_et_name);
        list=(RecyclerView) findViewById(R.id.searchproducts_list);
        list.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));
    }
}