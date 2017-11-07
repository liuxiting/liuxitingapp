package com.sdk.lxting.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements  GestureDetector.OnGestureListener{
    private TextView text;
    GestureDetector detector;
    private  float fontSize=30;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text= (TextView) findViewById(R.id.text);
        text.setTextSize(fontSize);
        detector=new GestureDetector(this,this);

    }
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        return detector.onTouchEvent(e);
    }
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        showShortToast("The method has been called - onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        showShortToast("The method has been called - onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        showShortToast("The method has been called - onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float vx, float vy)
    {
        showShortToast("The method has been called - onScroll");
        if (vx>0){
            if (fontSize>10){
                fontSize-=5;
                text.setTextSize(fontSize);

            }
        }else {
            if (fontSize<60){
                fontSize+=5;
                text.setTextSize(fontSize);

            }

        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        showShortToast("The method has been called - onFling");
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        showShortToast("The method has been called - onFling");
        return false;
    }
    //我们将Toast封装一下，以便调用时只需要传入待显示的消息，而略去了填写Context和持续时间等参数。
    public void showShortToast(String message){
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


}
