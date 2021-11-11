package com.example.doan_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ListView nguoiDungList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        matching();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        nguoiDungList.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("NguoiDung");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();
                for (DataSnapshot data:snapshot.getChildren()) {
                    String key = data.getKey();
                    String value = data.getValue().toString();
                    adapter.add(key + "\n" + value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase", "loadPost:OnCancelled", error.toException());
            }
        });
    }

    private void matching() {
        nguoiDungList = (ListView) findViewById(R.id.lv_NguoiDung);
    }
}