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

import com.example.doan_mobile.Model.Voucher;
import com.example.doan_mobile.ViewHolder.VoucherViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminViewVoucherActivity extends AppCompatActivity {

    private DatabaseReference VouchersRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton add;
    ImageView back;

    Integer lastesKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_voucher);

        VouchersRef = FirebaseDatabase.getInstance().getReference().child("Voucher");

        Query last = VouchersRef.orderByKey().limitToLast(1);
        last.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnap: dataSnapshot.getChildren()){
                    lastesKey = Integer.valueOf(childSnap.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        matching();

        recyclerView = findViewById(R.id.adminViewVoucher_recycler_menu);
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
                Intent intent = new Intent(AdminViewVoucherActivity.this, AdminAddNewVoucherActivity.class);
                intent.putExtra("ID", lastesKey);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Voucher> options =
                new FirebaseRecyclerOptions.Builder<Voucher>()
                        .setQuery(VouchersRef, Voucher.class)
                        .build();

        FirebaseRecyclerAdapter<Voucher, VoucherViewHolder> adapter =
                new FirebaseRecyclerAdapter<Voucher, VoucherViewHolder>(options) {
                    @NonNull
                    @Override
                    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_vouchers_items_layout, parent, false);
                        VoucherViewHolder holder = new VoucherViewHolder(view);
                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull VoucherViewHolder voucherViewHolder, int i, @NonNull Voucher voucher) {
                        voucherViewHolder.voucherCode.setText(voucher.getMaVoucher());
                        voucherViewHolder.voucherDiscount.setText("Mức giảm: " + voucher.getMucGiam());
                        voucherViewHolder.voucherBegin.setText(voucher.getThoiGianBatDau());
                        voucherViewHolder.voucherEnd.setText(voucher.getThoiGianKetThuc());

                        voucherViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Chỉnh sửa",
                                                "Xóa"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewVoucherActivity.this);
                                builder.setTitle("Lựa chọn: ");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {

                                            Intent intent = new Intent(AdminViewVoucherActivity.this, AdminEditVoucherActivity.class);
                                            String i_id = getRef(voucherViewHolder.getAdapterPosition()).getKey();
                                            intent.putExtra("ID", i_id);
                                            intent.putExtra("ThoiGianBD", voucher.getThoiGianBatDau());
                                            intent.putExtra("ThoiGianKT", voucher.getThoiGianKetThuc());
                                            startActivity(intent);
                                            finish();

                                        }
                                        if (which == 1) {

                                            AlertDialog.Builder confirm_dialog = new AlertDialog.Builder(AdminViewVoucherActivity.this);
                                            AlertDialog alert = confirm_dialog.create();

                                            confirm_dialog.setTitle("Thông báo");
                                            confirm_dialog.setMessage("Bạn có chắc muốn xoá Voucher " + voucher.getMaVoucher() + "?");
                                            confirm_dialog.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    String key = getRef(voucherViewHolder.getAdapterPosition()).getKey();

                                                    VouchersRef.child(key)
                                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(AdminViewVoucherActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
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
        add = (FloatingActionButton) findViewById(R.id.adminViewVoucher_fab_add);
        back = (ImageView) findViewById(R.id.adminViewVoucher_iv_back);
    }
}