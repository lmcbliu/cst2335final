package com.algonquin.cst2335final;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class TVActivity extends Fragment {
    private int tvChannel, tvCounter;
    private View gui;
    private  int isTablet;
    private LivingRoomActivity livingroomwindow;

    public  TVActivity () {}

    public TVActivity(LivingRoomActivity cw){
        livingroomwindow = cw;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bun = getArguments();
        tvChannel = bun.getInt("TVChannel", 0);
        tvCounter = bun.getInt("TVCounter", 0);
        isTablet = bun.getInt("IsTablet", 0);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        gui = inflater.inflate(R.layout.activity_tv, null);

        EditText input = (EditText)gui.findViewById(R.id.tvchannel);
        input.setText(String.valueOf(tvChannel));

        Button tvButton = (Button) gui.findViewById(R.id.tvbutton);
        tvButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText input = (EditText)gui.findViewById(R.id.tvchannel);
                tvChannel = Integer.parseInt(input.getText().toString());
                /*
                final SharedPreferences prefs = getActivity().getSharedPreferences("livingroomFile", Context.MODE_PRIVATE);
                //Get an editor object for writing to the file:
                SharedPreferences.Editor writer = prefs.edit();
                writer.putInt("TVChannel", tvChannel);
                writer.putInt("TVCounter",++ tvCounter);

                //Write the file:
                writer.commit();*/
                if(isTablet == 0) {
                    Intent dataBack = new Intent();
                    dataBack.putExtra("TVChannel", tvChannel);
                    dataBack.putExtra("TVCounter", tvCounter);
                    getActivity().setResult(0, dataBack);
                    getActivity().finish();
                }else{
                    livingroomwindow.synctv(tvChannel);
                    livingroomwindow.removeFragmentTV(TVActivity.this);
                }
            }
        });

        Switch lampSwitch = (Switch) gui.findViewById(R.id.switchTV);
        lampSwitch.setSelected(true);
        lampSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CharSequence text;
                int duration;

                if(isChecked){
                    text = "Home TV is On";
                    duration = Toast.LENGTH_SHORT;
                }else{
                    text = "Home TV is Off";
                    duration = Toast.LENGTH_SHORT;
                }

                Toast toast = Toast.makeText(getActivity(), text, duration);
                toast.show();
            }

        });
        return gui;
    }

    public void onStart(){
        super.onStart();
        Log.i("TVActivity", "onStart");
    }

    public void onResume(){

        super.onResume();
        Log.i("TVActivity", "onResume");
    }

    public void onPause(){

        super.onPause();
        Log.i("TVActivity", "onPause");
    }

    public void onStop()
    {
        super.onStop();
        Log.i("TVActivity", "onStop");
    }

    public void onDestroy(){

        super.onDestroy();
        Log.i("TVActivity", "ondestroy");
    }
}