package com.urbannightdev.cardiopp.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.urbannightdev.cardiopp.R;
import com.urbannightdev.cardiopp.helper.SessionManager;

public class SplashScreen extends AppCompatActivity {

    private static final int GOTOLOGINPAGE = 1;
    private static final int GOTOHOMEPAGE = 2;

    //time in milliseconds
    private static final long SPLASHTIME = 1200;

    private ImageView splash_mid;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        setContentView(R.layout.activity_splash_screen);
        splash_mid = (ImageView) findViewById(R.id.splashscreen_mid);

        splash_mid.setAlpha(1f);
        splash_mid.animate().scaleX(0.5f).scaleY(0.5f).setDuration(SPLASHTIME);

        // Session manager
        session = new SessionManager(getApplicationContext());

        Message msg = new Message();

        if (session.isLoggedIn())
            msg.what = GOTOHOMEPAGE;
        else
            msg.what = GOTOLOGINPAGE;

        splashHandler.sendMessageDelayed(msg, SPLASHTIME);
    }

    //handler for splash screen
    private Handler splashHandler = new Handler() {
        /* (non-Javadoc)
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message msg) {
            Intent intentpindah;
            switch (msg.what) {
                case GOTOLOGINPAGE:
                    //remove SplashScreen from view
                    splash_mid.setVisibility(View.GONE);
                    intentpindah = new Intent(SplashScreen.this, LoginActivity.class);
                    SplashScreen.this.startActivity(intentpindah);
                    SplashScreen.this.finish();
                    break;
                case GOTOHOMEPAGE:
                    splash_mid.setVisibility(View.GONE);
                    intentpindah = new Intent(SplashScreen.this, HomePage.class);
                    SplashScreen.this.startActivity(intentpindah);
                    SplashScreen.this.finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
