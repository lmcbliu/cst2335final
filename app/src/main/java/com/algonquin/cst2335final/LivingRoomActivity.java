package com.algonquin.cst2335final;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LivingRoomActivity extends AppCompatActivity {

    private Context ctx;
    private ListView livingroomlist;
    private String[] livingitems = {"Lamp On/Off", "Lamp Dimmable", "Lamp Colorful", "TV", "Window Blinds"};
    private String[] listitems = {"", "", "", "", ""};
    private int[] dynamicitems={0,0,0,0,0};
    private int itemscounter;
    protected Boolean isTablet;
    private int myLamp2Progress, myLamp3Progress, myLamp3Color, myTVChannel,myBlindsHeight;
    private String strLamp1Status;
    LivingFragmentActivity livingroomfrag;
    Bundle bundle;
    protected static LivingRoomDatabaseHelper livingDataHelper;
    protected SQLiteDatabase db;
    protected Cursor results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living_room);

        // user click refresh button could update the content of list items' status
        Button btrefresh = (Button) findViewById(R.id.livingroomrefreshbt);
        btrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBar livingProg = (ProgressBar) findViewById(R.id.livingProgressBar);
                livingProg.setVisibility(View.VISIBLE);
                LivingStatus livingStatus = new LivingStatus();
                livingStatus.execute();
            }
        });

        Button btintro = (Button)findViewById(R.id.livinghelpbutton);
        btintro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder livingIntro = new AlertDialog.Builder(ctx);
                LayoutInflater inflater = getLayoutInflater();
                final View view = inflater.inflate(R.layout.activity_living_help, null);
                livingIntro.setView(inflater.inflate(R.layout.activity_living_tv_dialog, null))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("OK", "OK");
                            }
                        });
                livingIntro.setView(view);
                livingIntro.create().show();
            }
        });

        ctx = this;
        bundle  = new Bundle();
        //Open a file for storing shared preferences:
        final SharedPreferences prefs = getSharedPreferences("livingroomFile", Context.MODE_PRIVATE);
        //Read the number of times run in the file:
        int ifirstrun = prefs.getInt("LivingRoomFirstRun", 0);
        if (ifirstrun <= 1){ // whether the database is run on first time

            livingDataHelper = new LivingRoomDatabaseHelper(ctx);
            db = livingDataHelper.getReadableDatabase();
            db.execSQL(LivingRoomDatabaseHelper.DROP_TABLE_MESSAGE);
            db.execSQL(LivingRoomDatabaseHelper.CREATE_TABLE_MESSAGE);

            ContentValues newValues1 = new ContentValues();
            newValues1.put(livingDataHelper.LIVINGITEM_KEY,"Lamp1Status");
            newValues1.put(livingDataHelper.LIVINGITEM_VALUE, "On");
            db.insert(livingDataHelper.TABLE_NAME, null, newValues1);

            ContentValues newValues2 = new ContentValues();
            newValues2.put(livingDataHelper.LIVINGITEM_KEY,"Lamp2Progress");
            newValues2.put(livingDataHelper.LIVINGITEM_VALUE, "50");
            db.insert(livingDataHelper.TABLE_NAME, null, newValues2);

            ContentValues newValues3 = new ContentValues();
            newValues3.put(livingDataHelper.LIVINGITEM_KEY,"Lamp3Progress");
            newValues3.put(livingDataHelper.LIVINGITEM_VALUE, "50");
            db.insert(livingDataHelper.TABLE_NAME, null, newValues3);

            ContentValues newValues4 = new ContentValues();
            newValues4.put(livingDataHelper.LIVINGITEM_KEY,"Lamp3Color");
            newValues4.put(livingDataHelper.LIVINGITEM_VALUE, "0");
            db.insert(livingDataHelper.TABLE_NAME, null, newValues4);

            ContentValues newValues5 = new ContentValues();
            newValues5.put(livingDataHelper.LIVINGITEM_KEY,"TVChannel");
            newValues5.put(livingDataHelper.LIVINGITEM_VALUE, "10");
            db.insert(livingDataHelper.TABLE_NAME, null, newValues5);

            ContentValues newValues6 = new ContentValues();
            newValues6.put(livingDataHelper.LIVINGITEM_KEY,"BlindsHeight");
            newValues6.put(livingDataHelper.LIVINGITEM_VALUE, "10");
            db.insert(livingDataHelper.TABLE_NAME, null, newValues6);

            // place the order of item
            dynamicitems[0] = 0;
            dynamicitems[1] = 1;
            dynamicitems[2] = 2;
            dynamicitems[3] = 3;
            dynamicitems[4] = 4;
            itemscounter = dynamicitems.length;
            //
            // when initialization, the counter is recorded into database
            ContentValues newValuesItemCounter = new ContentValues();
            newValuesItemCounter.put(livingDataHelper.LIVINGITEM_KEY,"itemscounter");
            newValuesItemCounter.put(livingDataHelper.LIVINGITEM_VALUE,itemscounter);
            db.insert(livingDataHelper.TABLE_NAME, null, newValuesItemCounter);

            ContentValues newValuesItem0 = new ContentValues();
            newValuesItem0.put(livingDataHelper.LIVINGITEM_KEY,"items0");
            newValuesItem0.put(livingDataHelper.LIVINGITEM_VALUE,dynamicitems[0]);
            db.insert(livingDataHelper.TABLE_NAME, null, newValuesItem0);

            ContentValues newValuesItem1 = new ContentValues();
            newValuesItem1.put(livingDataHelper.LIVINGITEM_KEY,"items1");
            newValuesItem1.put(livingDataHelper.LIVINGITEM_VALUE,dynamicitems[1]);
            db.insert(livingDataHelper.TABLE_NAME, null, newValuesItem1);

            ContentValues newValuesItem2 = new ContentValues();
            newValuesItem2.put(livingDataHelper.LIVINGITEM_KEY,"items2");
            newValuesItem2.put(livingDataHelper.LIVINGITEM_VALUE,dynamicitems[2]);
            db.insert(livingDataHelper.TABLE_NAME, null, newValuesItem2);

            ContentValues newValuesItem3 = new ContentValues();
            newValuesItem3.put(livingDataHelper.LIVINGITEM_KEY,"items3");
            newValuesItem3.put(livingDataHelper.LIVINGITEM_VALUE,dynamicitems[3]);
            db.insert(livingDataHelper.TABLE_NAME, null, newValuesItem3);

            ContentValues newValuesItem4 = new ContentValues();
            newValuesItem4.put(livingDataHelper.LIVINGITEM_KEY,"items4");
            newValuesItem4.put(livingDataHelper.LIVINGITEM_VALUE,dynamicitems[4]);
            db.insert(livingDataHelper.TABLE_NAME, null, newValuesItem4);
     }

        //Open a file for storing shared preferences:
        //final SharedPreferences prefs =  getSharedPreferences("livingroomFile", Context.MODE_PRIVATE);
        //Get an editor object for writing to the file:
        SharedPreferences.Editor writer = prefs.edit();
        writer.putInt("LivingRoomFirstRun", ++ifirstrun);
        //Write the file:
        writer.commit();
        // use getValue() method to retrieve counter
        dynamicitems[0] = Integer.parseInt(getValue("items0"));
        dynamicitems[1] = Integer.parseInt(getValue("items1"));
        dynamicitems[2] = Integer.parseInt(getValue("items2"));
        dynamicitems[3] = Integer.parseInt(getValue("items3"));
        dynamicitems[4] = Integer.parseInt(getValue("items4"));

        itemscounter = Integer.parseInt(getValue("itemscounter"));

        // By selecting Tablet or Phone display, the system choose ways to store default status
        isTablet = (findViewById(R.id.livingroomfragmentHolder)!=null); // boolean variable to tell if it's a tablet
        livingroomlist = (ListView)findViewById(R.id.livinglists);
        livingroomlist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //covert pos to itempos
                //adapter
                int itempos = dynamicitems[position];

                if(!isTablet) {//phone
                    switch (itempos) {
                        case 0: //lamp1
                            Intent lamp1Intent = new Intent(LivingRoomActivity.this, LivingRoomMessageDetails.class);
                            strLamp1Status = getValue("Lamp1Status");
                            lamp1Intent.putExtra("Lamp1Status",strLamp1Status);
                            lamp1Intent.putExtra("ItemID",0);
                            lamp1Intent.putExtra("IsTablet",0);
                            startActivityForResult(lamp1Intent, 5); //go to view fragment details
                            break;
                        case 1: //lamp2
                            Intent lamp2intent = new Intent(LivingRoomActivity.this, LivingRoomMessageDetails.class);
                            lamp2intent.putExtra("Lamp2Progress", myLamp2Progress);
                            lamp2intent.putExtra("ItemID",1);
                            lamp2intent.putExtra("IsTablet",0);
                            startActivityForResult(lamp2intent, 10);
                            break;
                        case 2: //lamp3
                            Intent lamp3intent= new Intent(LivingRoomActivity.this, LivingRoomMessageDetails.class);
                            lamp3intent.putExtra("Lamp3Progress", myLamp3Progress);
                            lamp3intent.putExtra("Lamp3Color", myLamp3Color);
                            lamp3intent.putExtra("ItemID",2);
                            lamp3intent.putExtra("IsTablet",0);
                            startActivityForResult(lamp3intent, 15);
                            break;
                        case 3: // tv
                            Intent tvActivity = new Intent(LivingRoomActivity.this, LivingRoomMessageDetails.class);
                            tvActivity.putExtra("TVChannel", myTVChannel);
                            tvActivity.putExtra("ItemID",3);
                            tvActivity.putExtra("IsTablet",0);
                            startActivityForResult(tvActivity, 20);
                            break;
                        case 4: // blinds
                            Intent blindsActivity = new Intent(LivingRoomActivity.this, LivingRoomMessageDetails.class);
                            blindsActivity.putExtra("BlindsHeight", myBlindsHeight);
                            blindsActivity.putExtra("ItemID",4);
                            blindsActivity.putExtra("IsTablet",0);
                            startActivityForResult(blindsActivity, 25);
                            break;
                    }
                }else{// tablet
                    //livingroomfrag = new LivingFragmentActivity(LivingRoomActivity.this);
                    //    livingroomfrag.setArguments(bundle);
                    switch (itempos) {
                        case 0: //lamp1
                            Lamp1Activity lamp1intent = new Lamp1Activity(LivingRoomActivity.this);
                            Bundle bundle = new Bundle();
                            bundle.putString("Lamp1Status",strLamp1Status);
                            bundle.putInt("ItemID",0);
                            bundle.putInt("IsTablet",1);
                            lamp1intent.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.livingroomfragmentHolder, lamp1intent).commit();
                            break;
                        case 1://lamp2

                            Lamp2Activity lamp2intent = new Lamp2Activity(LivingRoomActivity.this);
                            bundle = new Bundle();
                            bundle.putInt("Lamp2Progress", myLamp2Progress);
                            bundle.putInt("ItemID",1);
                            bundle.putInt("IsTablet",1);
                            lamp2intent.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.livingroomfragmentHolder, lamp2intent).commit();
                            break;

                        case 2://lamp3

                            Lamp3Activity lamp3intent = new Lamp3Activity(LivingRoomActivity.this);
                            bundle = new Bundle();
                            bundle.putInt("Lamp3Progress", myLamp3Progress);
                            bundle.putInt("Lamp3Color", myLamp3Color);
                            bundle.putInt("ItemID",2);
                            bundle.putInt("IsTablet",1);
                            lamp3intent.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.livingroomfragmentHolder, lamp3intent).commit();
                            break;
                        case 3://tv
                            TVActivity tvintent = new TVActivity(LivingRoomActivity.this);
                            bundle = new Bundle();
                            bundle.putInt("TVChannel", myTVChannel);
                            bundle.putInt("ItemID",3);
                            bundle.putInt("IsTablet",1);
                            tvintent.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.livingroomfragmentHolder, tvintent).commit();
                            break;
                        case 4: // blinds
                            BlindsActivity blindsintent = new BlindsActivity(LivingRoomActivity.this);
                            bundle = new Bundle();
                            bundle.putInt("BlindsHeight", myBlindsHeight);
                            bundle.putInt("ItemID",4);
                            bundle.putInt("IsTablet",1);
                            blindsintent.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.livingroomfragmentHolder, blindsintent).commit();
                            break;

                    }
                }
            }
        });

        strLamp1Status = getValue("Lamp1Status");
        myLamp2Progress = Integer.parseInt(getValue("Lamp2Progress"));
        myLamp3Progress = Integer.parseInt(getValue("Lamp3Progress"));
        myLamp3Color = Integer.parseInt(getValue("Lamp3Color"));
        myTVChannel = Integer.parseInt(getValue("TVChannel"));
        myBlindsHeight = Integer.parseInt(getValue("BlindsHeight"));

        livingitems[0] = "Lamp1 is " + strLamp1Status;
        livingitems[1] = "Lamp2 is " + myLamp2Progress;
        livingitems[2] = "Lamp3 is " + myLamp3Progress + " and color is " + myLamp3Color;
        livingitems[3] = "TV is tuned to channel " + myTVChannel;
        livingitems[4] = "Blinds is tuned to  " + myBlindsHeight + "  cm high";

        displayList();
        Log.i("LivingRoomActivity", "OnCreate");

        // pop out customer dialog
        Button btdialog = (Button) findViewById(R.id.livingroomcustombt);
        btdialog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent customIntent = new Intent(LivingRoomActivity.this, LivingCustomizingActivity.class);
                startActivityForResult(customIntent, 100); //go to view fragment details

            }
        });

    }

    public void displayList(){
        for(int i = 0; i<listitems.length; i++)
            listitems[i]="";
        for(int i = 0; i<itemscounter; i ++){
            listitems[i]=livingitems[dynamicitems[i]];
        }

        livingroomlist.setAdapter(new ArrayAdapter<>(this, R.layout.living_row_layout, listitems ));
    }
    public void onStart(){
        super.onStart();
        Log.i("LivingRoomActivity", "onStart");
    }

    public void onResume(){

        super.onResume();
        Log.i("LivingRoomActivity", "onResume");
    }

    public void onPause(){

        super.onPause();
        Log.i("LivingRoomActivity", "onPause");
    }

    public void onStop()
    {
        super.onStop();

        Log.i("LivingRoomActivity", "onStop");
    }

    public void onDestroy(){

        super.onDestroy();
        Log.i("LivingRoomActivity", "ondestroy");
    }
    //This function gets called when another activity has finished and this activity is resuming:
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(data != null)
        {
            String fromOtherActivity = data.getStringExtra("Status");
            Log.d("LivingRoomActivity", "Back from  other activity: " + fromOtherActivity);


            if(requestCode == 5 && resultCode == 0){
                strLamp1Status = data.getStringExtra("Lamp1Status");
            }else if(requestCode == 10 && resultCode == 0){
                myLamp2Progress = data.getIntExtra("Lamp2Progress",0);
            }else if (requestCode == 15 && resultCode == 0){
                myLamp3Progress = data.getIntExtra("Lamp3Progress", 0);
                myLamp3Color = data.getIntExtra("Lamp3Color",0);
            }else if (requestCode == 20 && resultCode == 0){
                myTVChannel = data.getIntExtra("TVChannel", 0);
            }else if(requestCode == 25 && resultCode == 0) {
                myBlindsHeight = data.getIntExtra("BlindsHeight", 0);
            }else if(requestCode == 100 && resultCode == 0){
                int AddOrDel = data.getIntExtra("AddOrDel",0);
                int ItemSelected = data.getIntExtra("ItemSelected",0);
                changeItems(ItemSelected,AddOrDel);

                // update database table
                putValue("itemscounter",""+itemscounter);
                putValue("items0",""+dynamicitems[0]);
                putValue("items1",""+dynamicitems[1]);
                putValue("items2",""+dynamicitems[2]);
                putValue("items3",""+dynamicitems[3]);
                putValue("items4",""+dynamicitems[4]);
            }


            putValue("Lamp1Status",strLamp1Status);
            putValue("Lamp2Progress",""+myLamp2Progress);
            putValue("Lamp3Progress",""+myLamp3Progress);
            putValue("Lamp3Color",""+myLamp3Color);
            putValue("TVChannel",""+myTVChannel);
            putValue("BlindsHeight",""+myBlindsHeight);

            livingitems[0] = "Lamp1 is " + strLamp1Status;
            livingitems[1] = "Lamp2 is " + myLamp2Progress + " degree";
            livingitems[2] = "Lamp3 is " + myLamp3Progress + " and color is " + myLamp3Color;
            livingitems[3] = "TV is tuned to channel " + myTVChannel;
            livingitems[4] = "Blinds is tuned to  " + myBlindsHeight + "  cm high";

            displayList();
        }
    }

    public void synclamp1(String lamp1status) {
        putValue("Lamp1Status",strLamp1Status);

        strLamp1Status = lamp1status;
        livingitems[0] = "Lamp1 is " + strLamp1Status;

        displayList();
    }


    public void synclamp2(int lamp2progress) {
        myLamp2Progress = lamp2progress;
        putValue("Lamp2Progress",""+myLamp2Progress);

        livingitems[1] = "Lamp2 is " + myLamp2Progress + " degree";

        displayList();
    }

    public void synclamp3(int lamp3progress, int color) {
        myLamp3Progress = lamp3progress;
        myLamp3Color = color;
        putValue("Lamp3Progress",""+myLamp3Progress);
        putValue("Lamp3Color",""+myLamp3Color);

        livingitems[2] = "Lamp3 is " + myLamp3Progress + " and color is " + myLamp3Color;

        displayList();
    }

    public void synctv(int tvChannel) {
        myTVChannel = tvChannel;
        putValue("TVChannel",""+myTVChannel);

        livingitems[3] = "TV is tuned to channel " + myTVChannel;

        displayList();
    }

    public void syncblinds(int blindsHeight) {
        myBlindsHeight = blindsHeight;
        putValue("BlindsHeight",""+myBlindsHeight);

        livingitems[4] = "Blinds is tuned to  " + myBlindsHeight + "  cm high";

        displayList();
    }

    public void removeFragmentLamp1(Lamp1Activity  lamp1){
        getSupportFragmentManager().beginTransaction().remove(lamp1).commit();
    }

    public void removeFragmentLamp2(Lamp2Activity  lamp2){
        getSupportFragmentManager().beginTransaction().remove(lamp2).commit();
    }

    public void removeFragmentLamp3(Lamp3Activity  lamp3){
        getSupportFragmentManager().beginTransaction().remove(lamp3).commit();
    }

    public void removeFragmentTV(TVActivity tv){
        getSupportFragmentManager().beginTransaction().remove(tv).commit();
    }

    public void removeFragmentBlinds(BlindsActivity blinds){
        getSupportFragmentManager().beginTransaction().remove(blinds).commit();
    }

    public String getValue(String strname){
        livingDataHelper = new LivingRoomDatabaseHelper(this);
        db = livingDataHelper.getReadableDatabase();

        //String query = String
        //      .format("SELECT * FROM %s WHERE %s=%s", livingDataHelper.TABLE_NAME, livingDataHelper.LIVINGITEM_KEY,strname);
        //results = db.rawQuery(query, null);
        // query existed data in table
        results = db.query(false, livingDataHelper.TABLE_NAME,
                new String[]{ livingDataHelper.LVINGITEM_ID, livingDataHelper.LIVINGITEM_KEY, livingDataHelper.LIVINGITEM_VALUE},
                livingDataHelper.LVINGITEM_ID + " not null",
                null, null, null, null, null);

        int count = results.getCount();

        results.moveToFirst();
        while( ! results.isAfterLast() ){
            String str1 = results.getString(results.getColumnIndex(livingDataHelper.LVINGITEM_ID));
            String str2 = results.getString(results.getColumnIndex(livingDataHelper.LIVINGITEM_KEY));
            String str3 = results.getString(results.getColumnIndex(livingDataHelper.LIVINGITEM_VALUE));
            if(strname.compareTo(str2)==0){
                return str3;
            }
            results.moveToNext();
        }
        return null;
    }

    public void putValue(String strname, String value){
        livingDataHelper = new LivingRoomDatabaseHelper(this);
        db = livingDataHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(livingDataHelper.LIVINGITEM_KEY,strname);
        newValues.put(livingDataHelper.LIVINGITEM_VALUE, value);
        //db.insert(livingDataHelper.TABLE_NAME, null, newValues);

        try {
            db.update(livingDataHelper.TABLE_NAME, newValues, livingDataHelper.LIVINGITEM_KEY + "='" + strname+"'", null);
        }catch(Exception e) {
            //db.insert(livingDataHelper.TABLE_NAME, null, newValues);
        }
    }

    /**
     * Accordiing to add or delete actions, re-adjust the counter and total number of items
     * @param itemid, Type int
     * @param addordel, Type int
     */
    public void changeItems(int itemid,int addordel) {
        int dynaitemid = -1;
        for (int i = 0; i < itemscounter; i++){
            if (dynamicitems[i] == itemid) {
                dynaitemid = i;
                break;
            }
        }
        if (addordel == 1 && dynaitemid < 0) {
            if (itemscounter <= 4)
                dynamicitems[itemscounter++] = itemid;
        } else if (addordel == -1 && dynaitemid >= 0) {
            if (itemscounter >= 0)
                itemscounter--;
            for (int i = dynaitemid; i < itemscounter; i++)
                dynamicitems[i] = dynamicitems[i + 1];
        }

    }


    public class LivingStatus extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... args){
            String in = "";
            try{
                livingDataHelper = new LivingRoomDatabaseHelper(ctx);
                db = livingDataHelper.getReadableDatabase();

                //String query = String
                //      .format("SELECT * FROM %s WHERE %s=%s", livingDataHelper.TABLE_NAME, livingDataHelper.LIVINGITEM_KEY,strname);
                //results = db.rawQuery(query, null);

                results = db.query(false, livingDataHelper.TABLE_NAME,
                        new String[]{ livingDataHelper.LVINGITEM_ID, livingDataHelper.LIVINGITEM_KEY, livingDataHelper.LIVINGITEM_VALUE},
                        livingDataHelper.LVINGITEM_ID + " not null",
                        null, null, null, null, null);

                int count = results.getCount();

                results.moveToFirst();
                while( ! results.isAfterLast() ){
                    String str2 = results.getString(results.getColumnIndex(livingDataHelper.LIVINGITEM_KEY));
                    if(str2.compareTo("Lamp1Status")==0){
                        publishProgress(25);
                        Thread.sleep(1000);
                    }
                    if(str2.compareTo("Lamp3Color")==0){
                        publishProgress(50);
                        Thread.sleep(1000);
                    }
                    if(str2.compareTo("BlindsHeight")==0){
                        publishProgress(75);
                        Thread.sleep(1000);
                    }
                    results.moveToNext();
                }
                publishProgress(100);
                Thread.sleep(1000);
            } catch (Exception me) {
                Log.e("AsyncTask", "Malformed URL:" + me.getMessage());
            }

            return in;
        }

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }


        public void onProgressUpdate(Integer... values) {

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.livingProgressBar);
            progressBar.setProgress(values[0]);
            progressBar.setVisibility(View.VISIBLE);
        }

        public void onPostExecute(String result) {

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.livingProgressBar);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}

