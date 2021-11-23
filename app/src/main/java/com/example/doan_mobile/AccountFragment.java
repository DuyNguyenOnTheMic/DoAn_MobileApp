package com.example.doan_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.doan_mobile.Prevalent.AdminPrevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountFragment extends Fragment {

    CircleImageView profileImage;
    TextView fullName, phone, role;
    Button update;

    StorageReference storageReference;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AccountFragment() {

    }


    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        storageReference = FirebaseStorage.getInstance().getReference().child("HinhAvatar");

        matching(view);

        userInfoDisplay(profileImage, fullName, phone);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdminSettingsActivity.class));
            }
        });

        return view;
    }

    private void userInfoDisplay(CircleImageView profileImage, TextView fullName, TextView phone) {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().
                child("QuanTri").child(AdminPrevalent.currentOnlineAdmin.getDienThoai());
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("Avatar").exists()) {
                        String sImage = snapshot.child("Avatar").getValue().toString();
                        String sName = snapshot.child("HoTen").getValue().toString();
                        String sPhone = snapshot.child("DienThoai").getValue().toString();
                        String sRole = snapshot.child("VaiTro").getValue().toString();


                        Picasso.get().load(sImage).into(profileImage);
                        fullName.setText(sName);
                        role.setText(sRole);
                        phone.setText(sPhone);
                    } else {
                        String sName = snapshot.child("HoTen").getValue().toString();
                        String sPhone = snapshot.child("DienThoai").getValue().toString();
                        String sRole = snapshot.child("VaiTro").getValue().toString();

                        fullName.setText(sName);
                        phone.setText(sPhone);
                        role.setText(sRole);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void matching(View view) {
        profileImage = (CircleImageView) view.findViewById(R.id.adminAccount_ap_profileImage);
        fullName = (TextView) view.findViewById(R.id.adminAccount_et_fullName);
        phone = (TextView) view.findViewById(R.id.adminAccount_et_phone);
        update = (Button) view.findViewById(R.id.adminAccount_btn_update);
        role = (TextView) view.findViewById(R.id.adminAccount_et_role);
    }

}