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

import com.example.doan_mobile.Model.QuanTri;
import com.example.doan_mobile.ViewHolder.AdminViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminViewAdminActivity extends AppCompatActivity {

    private DatabaseReference AdminsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton add;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_admin);

        AdminsRef = FirebaseDatabase.getInstance().getReference().child("QuanTri");

        matching();

        recyclerView = findViewById(R.id.adminViewAdmin_recycler_menu);
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
                startActivity(new Intent(AdminViewAdminActivity.this, AdminAddAccount.class));
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<QuanTri> options =
                new FirebaseRecyclerOptions.Builder<QuanTri>()
                        .setQuery(AdminsRef, QuanTri.class)
                        .build();

        FirebaseRecyclerAdapter<QuanTri, AdminViewHolder> adapter =
                new FirebaseRecyclerAdapter<QuanTri, AdminViewHolder>(options) {
                    @NonNull
                    @Override
                    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admins_item_layout, parent, false);
                        AdminViewHolder holder = new AdminViewHolder(view);
                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull AdminViewHolder adminViewHolder, int i, @NonNull QuanTri quanTri) {

                        if (quanTri.getAvatar() == (null)) {
                            adminViewHolder.adminName.setText(quanTri.getHoTen());
                            adminViewHolder.adminPhone.setText(quanTri.getDienThoai());
                            adminViewHolder.adminRole.setText(quanTri.getVaiTro());
                        } else {
                            adminViewHolder.adminName.setText(quanTri.getHoTen());
                            adminViewHolder.adminPhone.setText(quanTri.getDienThoai());
                            adminViewHolder.adminRole.setText(quanTri.getVaiTro());
                            Picasso.get().load(quanTri.getAvatar()).into(adminViewHolder.adminImage);
                        }

                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void matching() {
        add = (FloatingActionButton) findViewById(R.id.adminViewAdmin_fab_add);
        back = (ImageView) findViewById(R.id.adminViewAdmin_iv_back);
    }
}