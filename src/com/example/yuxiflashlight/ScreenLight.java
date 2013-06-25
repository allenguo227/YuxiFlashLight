package com.example.yuxiflashlight;

import android.os.Bundle;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ScreenLight extends Activity implements  android.view.GestureDetector.OnGestureListener {
	private GestureDetector detector;
	private float screenBrightness=150f;
	private RelativeLayout screen_light;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_light);
		setScreenMode(0);
		screen_light=(RelativeLayout)findViewById(R.id.backgroundcolor);
		detector=new GestureDetector(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.screen_light, menu);
		return true;
	}
	/** 
     * 获得当前屏幕亮度的模式     
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度 
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度 
     */  
      private int getScreenMode(){  
        int screenMode=0;  
        try{  
            screenMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);  
        }  
        catch (Exception localException){  
              
        }  
        return screenMode;  
      }  
        
     /** 
     * 获得当前屏幕亮度值  0--255 
     */  
      private int getScreenBrightness(){  
        int screenBrightness=255;  
        try{  
            screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);  
        }  
        catch (Exception localException){  
            
        }  
        return screenBrightness;  
      }  
    /** 
     * 设置当前屏幕亮度的模式     
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度 
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度 
     */  
      private void setScreenMode(int paramInt){  
        try{  
          Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);  
        }catch (Exception localException){  
          localException.printStackTrace();  
        }  
      }  
      /** 
       * 设置当前屏幕亮度值  0--255 
       * 
       */  
      private void saveScreenBrightness(int paramInt){  
        try{  
          Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);  
        }  
        catch (Exception localException){  
          localException.printStackTrace();  
        }  
      }  
      /** 
       * 保存当前的屏幕亮度值，并使之生效 
       */  
      private void setScreenBrightness(float screenBrightness2){  
        Window localWindow = getWindow();  
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();  
        float f = screenBrightness2 / 255; 
        localLayoutParams.screenBrightness = f;  
        localWindow.setAttributes(localLayoutParams);  
      }

      
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return detector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	//滑动屏幕做出屏幕亮度改变
	@SuppressLint("ShowToast")
	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
			System.out.println("arg2---->"+arg2);
			System.out.println("arg3---->"+arg3);
			System.out.println("getScreenMode获取当前亮度模式0为手动，1为自动---->"+getScreenMode());
			//往上滑动，arg3的参数为正数
			if(arg3<=0){
				screenBrightness+=50;
				if(screenBrightness>=255)
				{
					screenBrightness=255;
				}
			}else{
				screenBrightness-=50;
				if(screenBrightness<=25)
				{
					screenBrightness=25.5f;
				}
				}		
			setScreenBrightness(screenBrightness);
			Toast.makeText(ScreenLight.this, "当前屏幕亮度为："+(int)(100*(screenBrightness/255))+"%", 200).show();
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

     public void redScreen(View v){
    	 screen_light.setBackgroundResource(R.color.red);
     }
     public void orangeScreen(View v){
    	 screen_light.setBackgroundResource(R.color.orange);
     }
     public void yellowScreen(View v){
    	 screen_light.setBackgroundResource(R.color.yellow);
     }
     public void greenScreen(View v){
    	 screen_light.setBackgroundResource(R.color.green);
     }
     public void blueScreen(View v){
    	 screen_light.setBackgroundResource(R.color.blue);
     }
     public void cyanScreen(View v){
    	 screen_light.setBackgroundResource(R.color.cyan);
     }
     public void purpleScreen(View v){
    	 screen_light.setBackgroundResource(R.color.purple);
     }
     public void whiteScreen(View v){
    	 screen_light.setBackgroundResource(R.color.white);
     }
     public void blackScreen(View v){
    	 screen_light.setBackgroundResource(R.color.black);
     }

}
