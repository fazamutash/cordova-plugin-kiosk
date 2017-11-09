package jk.cordova.plugin.kiosk;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import org.apache.cordova.*;

import android.util.TypedValue;
import android.view.Gravity;
import android.widget.*;
import android.view.Window;
import android.view.View;
import android.view.WindowManager;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;

import com.mitrais.pybar.*;

import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (KioskActivity.running) {
            System.out.println("HomeActivity closing because already running");
            finish(); // prevent more instances of kiosk activity
        }

        RelativeLayout layout = new RelativeLayout(this);
        layout.setBackgroundColor(Color.parseColor("#003C8D"));
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);

        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        // ImageView pybarImageView = new ImageView(this);
        // pybarImageView.setImageResource(com.mitrais.pybar.R.drawable.screen);
        // pybarImageView.setScaleType(ImageView.ScaleType.CENTER);
        // pybarImageView.setLayoutParams(relativeParams);

        LinearLayout insideLinearLayout = new LinearLayout(this);
        LayoutParams LLParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        insideLinearLayout.setLayoutParams(LLParams);
        insideLinearLayout.setOrientation(LinearLayout.VERTICAL);
        insideLinearLayout.setLayoutParams(relativeParams);

        TextView textIPLOD = new TextView(this);
        textIPLOD.setGravity(Gravity.CENTER_HORIZONTAL);
        textIPLOD.setText("IPLOD");
        textIPLOD.setTextColor(Color.parseColor("#ffffff"));
        textIPLOD.setTextSize(TypedValue.COMPLEX_UNIT_SP, 70);
        textIPLOD.setTypeface(null, Typeface.BOLD);
        textIPLOD.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        TextView textDesc = new TextView(this);
        textDesc.setGravity(Gravity.CENTER_HORIZONTAL);
        textDesc.setText("Click or press any key to begin...");
        textDesc.setTextColor(Color.parseColor("#ffffff"));
        textDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        textIPLOD.setTypeface(null, Typeface.BOLD);
        textIPLOD.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        insideLinearLayout.addView(textIPLOD);
        insideLinearLayout.addView(textDesc);

        layout.addView(insideLinearLayout);

//        layout.addView(pybarImageView);

        layout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            HomeActivity.this.startKioskActivity();
          }
        });

//        FrameLayout.LayoutParams imageLayoutParams = new LayoutParams

//        Button button = new Button(this);
//        button.setText("Click or press any key to begin...");
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                HomeActivity.this.startKioskActivity();
//            }
//        });
//        layout.addView(button, params);

        setContentView(layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            public void run() {
                HomeActivity.this.startKioskActivity();
            }
        }, 20000); // 20 seconds
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        startKioskActivity();
        return true; // prevent event from being propagated
    }

    // http://www.andreas-schrade.de/2015/02/16/android-tutorial-how-to-create-a-kiosk-mode-in-android/
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);

            ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            am.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);

            // sametime required to close opened notification area
            Timer timer = new Timer();
            timer.schedule(new TimerTask(){
                public void run() {
                    Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    sendBroadcast(closeDialog);
                }
            }, 500); // 0.5 second
        }
    }

    private void startKioskActivity() {
        Intent serviceIntent = new Intent(this, KioskActivity.class);
        startActivity(serviceIntent);
    }
}

