package ru.spbstu.icc.kspt.kuznetsov.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URLEncoder;

/**
 * Created by qnf863 on 25.02.2015.
 */
public class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        final ImageView imgv = (ImageView) findViewById(R.id.android_logo);
        imgv.setOnClickListener(new View.OnClickListener() {
            private int imgs[] = {R.drawable.blue, R.drawable.red, R.drawable.androidlogo};
            private int lastImg = -1;

            @Override
            public void onClick(View view) {
                int rnd = (int) Math.round(Math.random() * (imgs.length - 1));
                if (rnd == lastImg) rnd = (rnd + 1) % imgs.length;
                imgv.setImageResource(imgs[rnd]);
                lastImg = rnd;
            }
        });
    }

    public void onSendEmail(View v){
        Toast.makeText(this, "Prepare to send e-mail", Toast.LENGTH_SHORT).show();

        String uriText =
                "mailto:icc2013.android@gmail.com" +
                        "?subject=" + URLEncoder.encode("Feedback on tic-tac-toe") +
                        "&body=" + URLEncoder.encode("Dear Andrei Nikolaevich,\n\n");

        Intent i = new Intent();
        i.setAction(Intent.ACTION_SENDTO);
        i.setData(Uri.parse(uriText));

        startActivity(i);
    }
}
