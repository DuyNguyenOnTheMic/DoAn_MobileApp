package com.example.doan_mobile;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AdminEditVoucherActivity extends AppCompatActivity {
    Button editVoucher;
    EditText voucherCode, discount, note, startDay, endDay;
    ImageView back;
    String sdiscount, snote, sstartDay, sendDay, ID;
    DatabaseReference VoucherRef;
    ProgressDialog loadingBar;
    Calendar myCalendarS = Calendar.getInstance();
    Calendar myCalendarE = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_voucher);

        matching();

        //ID tăng tự động
        ID = getIntent().getStringExtra("ID");
        //Kết nối với firebase Voucher
        VoucherRef = FirebaseDatabase.getInstance().getReference().child("Voucher");

        getVoucherDetail();

        //Chọn ngày bắt đầu
        DatePickerDialog.OnDateSetListener dateS = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                myCalendarS.set(Calendar.YEAR, year);
                myCalendarS.set(Calendar.MONTH, month);
                myCalendarS.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                startDay.setText(sdf.format(myCalendarS.getTime()));
            }
        };

        startDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AdminEditVoucherActivity.this, dateS,myCalendarS
                        .get(Calendar.YEAR), myCalendarS.get(Calendar.MONTH),
                        myCalendarS.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Chọn ngày kết thúc
        DatePickerDialog.OnDateSetListener dateE = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                myCalendarE.set(Calendar.YEAR, year);
                myCalendarE.set(Calendar.MONTH, month);
                myCalendarE.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                endDay.setText(sdf.format(myCalendarE.getTime()));
            }
        };

        endDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AdminEditVoucherActivity.this, dateE,myCalendarE
                        .get(Calendar.YEAR), myCalendarE.get(Calendar.MONTH),
                        myCalendarE.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    //Lấy dữ liệu hiển thị lên giao diện
    private void getVoucherDetail() {
        Intent intent = getIntent();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference voucherRef = database.getReference("Voucher");

        voucherRef.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    String sMaVoucher = dataSnapshot.child("MaVoucher").getValue().toString();
                    String sMucGiam = dataSnapshot.child("MucGiam").getValue().toString();
                    String sGhiChu = dataSnapshot.child("GhiChu").getValue().toString();
                    String sThoiGianBD = dataSnapshot.child("ThoiGianBatDau").getValue().toString();
                    String sThoiGianKT = dataSnapshot.child("ThoiGianKetThuc").getValue().toString();

                    voucherCode.setText(sMaVoucher);
                    discount.setText(sMucGiam);
                    note.setText(sGhiChu);
                    startDay.setText(sThoiGianBD);
                    endDay.setText(sThoiGianKT);

                }catch (Exception e){
                    Log.d("Error", e.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminEditVoucherActivity.this, AdminViewVoucherActivity.class));
                finish();
            }
        });

        //Gán sự kiện button Sửa Voucher
        editVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateVoucherData();
            }
        });
    }

    private void ValidateVoucherData() {
        sdiscount = discount.getText().toString();
        snote = note.getText().toString();
        sstartDay = startDay.getText().toString();
        sendDay = endDay.getText().toString();

        if (TextUtils.isEmpty(sdiscount)){
            Toast.makeText(getApplicationContext(),"Voucher này chưa có mức giảm",Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(snote)){
            Toast.makeText(getApplicationContext(),"Voucher này cần có ghi chú",Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(sstartDay)){
            Toast.makeText(getApplicationContext(),"Voucher này sẽ bắt đầu từ ngày bao nhiêu thế?",Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(sendDay)){
            Toast.makeText(getApplicationContext(),"Voucher này sẽ kết thúc vào ngày bao nhiêu thế?",Toast.LENGTH_LONG).show();
        }else{
            UpdateDataVoucher();
        }
    }

    private void UpdateDataVoucher() {

        loadingBar.setTitle("Sửa voucher");
        loadingBar.setMessage("Vui lòng đợi, chúng tôi đang sửa voucher");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        HashMap<String, Object> voucherMap = new HashMap<>();
        voucherMap.put("MaVoucher",voucherCode.getText().toString());
        voucherMap.put("MucGiam", sdiscount);
        voucherMap.put("GhiChu", snote);
        voucherMap.put("ThoiGianBatDau", sstartDay);
        voucherMap.put("ThoiGianKetThuc", sendDay);

        VoucherRef.child(String.valueOf(ID)).
                updateChildren(voucherMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(AdminEditVoucherActivity.this, AdminViewVoucherActivity.class));

                    loadingBar.dismiss();
                    Toast.makeText(AdminEditVoucherActivity.this, "Sửa Voucher thành công ^^", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    loadingBar.dismiss();
                    Toast.makeText(AdminEditVoucherActivity.this, "Error: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void matching() {
        editVoucher = (Button) findViewById(R.id.adminEditVoucher_btn_editVoucher);
        back = (ImageView) findViewById(R.id.adminEditVoucher_iv_back);
        voucherCode = (EditText) findViewById(R.id.adminEditVoucher_et_voucherCode);
        discount = (EditText) findViewById(R.id.adminEditVoucher_et_discount);
        note = (EditText) findViewById(R.id.adminEditVoucher_et_note);
        startDay = (EditText) findViewById(R.id.adminEditVoucher_et_startDay);
        endDay = (EditText) findViewById(R.id.adminEditVoucher_et_endDay);
        loadingBar = new ProgressDialog(this);
    }

}