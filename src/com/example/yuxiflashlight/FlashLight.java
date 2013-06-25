package com.example.yuxiflashlight;


import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FlashLight extends Activity {
		private View view;
		private Button exit;
	    private Camera camera;  
	    private boolean isOpen = false;   
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	      //设置全屏 
	    //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
	     //去掉标题栏   
	       requestWindowFeature(Window.FEATURE_NO_TITLE);  
	        setContentView(R.layout.activity_flash_light);  
	    view=(View)findViewById(R.id.background);
	    exit=(Button)findViewById(R.id.exit);
	    exit.setOnClickListener(new ExitOnClickListner());
        view.setOnClickListener(new FlashLightClickListener());
    }  
	 class FlashLightClickListener implements OnClickListener {
		 @Override  
         public void onClick(View v) {  
             if(!isOpen){  
 
               //打开照相机  
                 camera = Camera.open();  
             	 //改变背景图片  
                 view.setBackgroundResource(R.drawable.flashlight_on);  
                 //获取照相机参数
                 Parameters params = camera.getParameters();    
               //设置照相机参数，FLASH_MODE_TORCH  持续的亮灯，FLASH_MODE_ON 只闪一下  
                 params.setFlashMode(Parameters.FLASH_MODE_TORCH);   
                 camera.setParameters(params);  
                 //开始亮灯  
                 camera.startPreview();   
                 isOpen=true;
                 //打开notification
                 notificationOpen();
             }else{  
                 //改变背景图片  
                 view.setBackgroundResource(R.drawable.flashlight_off);    
                 //关闭notification
               //关掉亮灯 
                 camera.stopPreview();   
                 //关掉照相机  
                 camera.release();  
                 isOpen = false;  
                 notificationCancel();
             }  
         }  
     }

	 class   ExitOnClickListner implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
	        if(camera != null){  
                camera.release();  
                notificationCancel();
                finish();  
            }else{
                finish();  
            }  
		}
	 }
    private void notificationOpen(){
    	Intent intent=new Intent(this,FlashLight.class);
		//在创建PendingIntent的时候需要注意参数PendingIntent.FLAG_CANCEL_CURRENT
		//这个标志位用来指示：如果当前的Activity和PendingIntent中设置的intent一样，那么就先取消当前的Activity，用PendingIntent中指定的Activity取代之。
		PendingIntent pi=PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		Notification notify=new Notification();
		notify.icon=R.drawable.flashlighticon;
		//点击后自动消失
		//notify.flags |= Notification.FLAG_AUTO_CANCEL;
		notify.when=System.currentTimeMillis();
		//notify.defaults=Notification.DEFAULT_ALL;
		notify.setLatestEventInfo(this, "手电筒", "正在开启",pi);
		NotificationManager notifyManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		notifyManager.notify(0x0123, notify);
    }
    private void notificationCancel(){
		NotificationManager notifyManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		notifyManager.cancel(0x0123);
    }
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if(keyCode == KeyEvent.KEYCODE_BACK){       //按back键的时候 释放照相机  
            if(camera != null){  
                camera.release();  
                notificationCancel();
                finish();  
            }else{
                finish();  
            }
        }  
        return super.onKeyDown(keyCode, event);  
    }  
}

