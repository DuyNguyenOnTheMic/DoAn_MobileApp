package com.example.doan_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfimFinalOrderActivity extends AppCompatActivity {
    EditText name, phone, address, city;
    Button confirm;
    String total = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confim_final_order);
        maching();

        total = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Tổng tiền = " + total, Toast.LENGTH_SHORT).show();
    }

    private void maching() {
        confirm = (Button) findViewById(R.id.Confirm_btn_confirm);
        name = (EditText) findViewById(R.id.Confirm_et_name);
        phone = (EditText) findViewById(R.id.Confirm_et_phone);
        address = (EditText) findViewById(R.id.Confirm_et_address);
        city = (EditText) findViewById(R.id.Confirm_et_city);
    }
}