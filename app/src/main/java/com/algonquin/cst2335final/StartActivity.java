package com.algonquin.cst2335final;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**the start page of the application
 * @version 1.0
 * */
public class StartActivity extends AppCompatActivity {

    /**
     * Override the onCreate and start the application
     * @param savedInstanceState
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**create the toolbar
     * @param m*/
    public boolean onCreateOptionsMenu (Menu m){

        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;

    }

    /**
     * action for the menu Item
     * @param mi*/
    public boolean onOptionsItemSelected(MenuItem mi){

        int id =mi.getItemId();

        switch( id ){

            case R.id.livingroom:
                Intent intentLivingRoom = new Intent(StartActivity.this, LivingRoomActivity.class);
                startActivity(intentLivingRoom);

                break;

            case R.id.kitchen:

      
              Intent intentKitchen= new Intent(StartActivity.this, KitchenActivity.class);
              startActivity(intentKitchen);


                break;

            case R.id.house:
                Intent intentHouse= new Intent(StartActivity.this, HouseActivity.class);
                startActivity(intentHouse);

                break;

            case R.id.auto:
                Intent intentAuto= new Intent(StartActivity.this, AutoActivity.class);
                startActivity(intentAuto);

                break;

            case R.id.about:

              Toast.makeText(this, "CST2355 Final Project Version 1.0 by Bo Liu/WangTao/WangCheng/MinLuo", Toast.LENGTH_LONG).show();
               break;
        }

        return true;
    }

}
