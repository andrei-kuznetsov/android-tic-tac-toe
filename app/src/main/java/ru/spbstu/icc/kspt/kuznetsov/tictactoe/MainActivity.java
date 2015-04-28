package ru.spbstu.icc.kspt.kuznetsov.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends Activity {
    final private static String TAG = "MainActivity";
    final public static String KEY_LAUNCH_MODE = "launch.mode";
    public static enum LAUNCH_MODES {
        PLAYER_VS_COMPUTER,
        COMPUTER_VS_PLAYER,
        PLAYER_VS_PLAYER
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate");

//        setContentView(R.layout.test);
        setContentView(R.layout.activity_main);

        final Button bnAbout = (Button) findViewById(R.id.bnAbout);
        bnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, AboutActivity.class);
                startActivity(i);
            }
        });

        ((Button) findViewById(R.id.bnRules)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, RulesActivity.class);
                startActivity(i);
            }
        });
    }

    public void onBnStart(View v){
        Intent i = new Intent();

        switch (v.getId()){
            case R.id.bnStartComputerVsPlayer:
                i.putExtra(KEY_LAUNCH_MODE, LAUNCH_MODES.COMPUTER_VS_PLAYER);
                break;
            case R.id.bnStartPlayerVsComputer:
                i.putExtra(KEY_LAUNCH_MODE, LAUNCH_MODES.PLAYER_VS_COMPUTER);
                break;
            case R.id.bnStartPlayerVsPlayer:
                i.putExtra(KEY_LAUNCH_MODE, LAUNCH_MODES.PLAYER_VS_PLAYER);
                break;
        }
        i.setClass(MainActivity.this, GameActivity.class);
        startActivity(i);
    }
}
