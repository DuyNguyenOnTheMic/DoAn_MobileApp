package com.example.doan_mobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class AdminAddNewVoucherActivity extends AppCompatActivity {
    Integer ID = 0;
    Button addNewVoucher;
    EditText voucherCode, discount, note, startDay, endDay;
    ImageView back;
    String rdvoucherCode, sdiscount, snote, sstartDay, sendDay;
    DatabaseReference VoucherRef;
    Calendar myCalendarS = Calendar.getInstance();
    Calendar myCalendarE = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_voucher);

        matching();

        VoucherRef = FirebaseDatabase.getInstance().getReference().child("Voucher");

        //ID tự động tăng
        ID = getIntent().getIntExtra("ID", 0);

        //Set Text Mã Voucher
        rdvoucherCode = randomCode(10);
        voucherCode.setText(rdvoucherCode);

        //Chọn ngày bắt đầu
        DatePickerDialog.OnDateSetListener dateS = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                myCalendarS.set(Calendar.YEAR, year);
                myCalendarS.set(Calendar.MONTH, month);
                myCalendarS.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        startDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AdminAddNewVoucherActivity.this, dateS,myCalendarS
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
                updateLabel02();
            }
        };

        endDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AdminAddNewVoucherActivity.this, dateE,myCalendarE
                        .get(Calendar.YEAR), myCalendarE.get(Calendar.MONTH),
                        myCalendarE.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Gán sự kiện button Thêm mới Voucher
        addNewVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateVoucherData();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminAddNewVoucherActivity.this, AdminViewVoucherActivity.class));
                finish();
            }
        });

    }

    //Hiển thị ngày bắt đầu
    public void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDay.setText(sdf.format(myCalendarS.getTime()));
    }

    //Hiển thị ngày kết thúc
    public void updateLabel02() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDay.setText(sdf.format(myCalendarE.getTime()));
    }

    //Tạo random Mã Voucher
    private String randomCode(int length){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVW0123456789".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i<length; i++){
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    //Kiểm tra hợp lệ dữ liệu không rỗng
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
            SaveVoucherInfoToDatabase();
        }
    }

    //Lưu dữ liệu thêm mới vào Firebase
    private void SaveVoucherInfoToDatabase() {


        HashMap<String, Object> voucherMap = new HashMap<>();
        voucherMap.put("MaVoucher", rdvoucherCode);
        voucherMap.put("MucGiam", sdiscount);
        voucherMap.put("GhiChu", snote);
        voucherMap.put("ThoiGianBatDau", sstartDay);
        voucherMap.put("ThoiGianKetThuc", sendDay);

        VoucherRef.child(String.valueOf(ID + 1)).
                updateChildren(voucherMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(AdminAddNewVoucherActivity.this, AdminViewVoucherActivity.class));

                    Toast.makeText(AdminAddNewVoucherActivity.this, "Thêm Voucher thành công ^^", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(AdminAddNewVoucherActivity.this, "Error: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void matching() {
        addNewVoucher = (Button) findViewById(R.id.adminAddNewVoucher_btn_addVoucher);
        voucherCode = (EditText) findViewById(R.id.adminAddNewVoucher_et_voucherCode);
        discount = (EditText) findViewById(R.id.adminAddNewVoucher_et_discount);
        note = (EditText) findViewById(R.id.adminAddNewVoucher_et_note);
        startDay = (EditText) findViewById(R.id.adminAddNewVoucher_et_startDay);
        endDay = (EditText) findViewById(R.id.adminAddNewVoucher_et_endDay);
        back = (ImageView) findViewById(R.id.adminAddNewVoucher_iv_back);
    }
}