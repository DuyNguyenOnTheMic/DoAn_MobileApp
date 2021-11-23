package com.example.doan_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.doan_mobile.Prevalent.AdminPrevalent;


public class ManageFragment extends Fragment {

    ImageView admin, category, product, order, voucher;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ManageFragment() {
    }


    public static ManageFragment newInstance(String param1, String param2) {
        ManageFragment fragment = new ManageFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage, container, false);

        matching(view);

        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AdminViewProductActivity.class));
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AdminPrevalent.currentOnlineAdmin.getVaiTro().equals("Admin")) {
                    startActivity(new Intent(getActivity(), AdminViewAdminActivity.class));
                } else {
                    Toast.makeText(getActivity(), "Chỉ có quản trị mới được quyền truy cập mục này!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdminViewProductCategoryActivity.class));
            }
        });

        return view;
    }

    private void matching(View view) {
        admin = (ImageView) view.findViewById(R.id.adminManage_iv_admin);
        category = (ImageView) view.findViewById(R.id.adminManage_iv_category);
        product = (ImageView) view.findViewById(R.id.adminManage_iv_product);
        order = (ImageView) view.findViewById(R.id.adminManage_iv_order);
        voucher = (ImageView) view.findViewById(R.id.adminManage_iv_voucher);
    }
}