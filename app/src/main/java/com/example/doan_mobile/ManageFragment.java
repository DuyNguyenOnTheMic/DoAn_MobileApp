package com.example.doan_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;


public class ManageFragment extends Fragment {

    ImageView lenovo, acer, asus, hp, apple, dell, msi, lg;

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

        lenovo = (ImageView) view.findViewById(R.id.adminCategory_iv_lenovo);
        acer = (ImageView) view.findViewById(R.id.adminCategory_iv_acer);
        asus = (ImageView) view.findViewById(R.id.adminCategory_iv_asus);
        hp = (ImageView) view.findViewById(R.id.adminCategory_iv_hp);
        apple = (ImageView) view.findViewById(R.id.adminCategory_iv_apple);
        dell = (ImageView) view.findViewById(R.id.adminCategory_iv_dell);
        msi = (ImageView) view.findViewById(R.id.adminCategory_iv_msi);
        lg = (ImageView) view.findViewById(R.id.adminCategory_iv_lg);

        lenovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdminAddNewProductActivity.class);
                intent.putExtra("HangSP", "Lenovo");
                startActivity(intent);
            }
        });

        return view;
    }
}