package com.example.learn_map;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.learn_map.helper.PermissionUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends BaseActivity  {
    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        fragment = new MenuMapFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.base_fragment,fragment).commit();



        replaceFragment(new MenuMapFragment());
    }


    public void replaceFragment(Fragment fragment){

        getSupportFragmentManager().beginTransaction().replace(R.id.base_fragment,fragment)
                .addToBackStack("map")
                .commit();
    }




}