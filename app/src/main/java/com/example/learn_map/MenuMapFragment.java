package com.example.learn_map;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;


public class MenuMapFragment extends Fragment {
View view;
Button hq_bt;
Button campus_bt;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu_map, container, false);
        hq_bt = view.findViewById(R.id.hq_bt);
        campus_bt = view.findViewById(R.id.campus_bt);
        clickInit();

        return view;
    }

    private void clickInit(){
        hq_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(new MapsFragment(new LatLng(11.573459, 104.892941),"VP.Start Technology Co., Ltd."));
            }
        });

        campus_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(new MapsFragment(new LatLng(11.584735, 104.881824),"VP.Start Sen Sok Valley Campus"));

            }
        });
    }
}