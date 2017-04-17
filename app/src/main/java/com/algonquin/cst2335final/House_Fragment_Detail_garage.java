package com.algonquin.cst2335final;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class House_Fragment_Detail_garage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_fragment_holder);
        ListDetailHouseFragment fragment = new ListDetailHouseFragment();
        Bundle bun = getIntent().getExtras();
        fragment.setArguments(bun);
        getFragmentManager().beginTransaction().replace(R.id.housefragmentholder,fragment).commit();


    }
}
