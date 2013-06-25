package com.example.yuxiflashlight;

import java.util.Timer;
import java.util.TimerTask;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * 国际求救信号 三短三长
 * @author Administrator
 *
 */
public class SOSFlashLightActivity extends Activity {
	private View view;
	private Button exit;
	private boolean Flashing=false;
    private Camera camera;  
    private boolean isOpen = false;   
    private int flashTime=0;
    private Timer timer;
    private  Handler myHandler;
	private IntentFilter intentFilter_flashtime;
	private BroadcastReceiver flashTimeReceiver;

    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        //去掉标题栏   
	    requestWindowFeature(Window.FEATURE_NO_TITLE);  
	    setContentView(R.layout.activity_sosflash_light);  
	    view=(View)findViewById(R.id.background_sos);
	    exit=(Button)findViewById(R.id.exit_sos);
	    //设置监听器
	    exit.setOnClickListener(new ExitOnClickListner());
	    view.setOnClickListener(new FlashLightClickListener());
	    //注册broadcast,用以接收从handler发出的信息
	    flashTimeReceiver=new FlashLightBroadcast();
		registerReceiver(flashTimeReceiver,getFlashTimerIntentFileter());

}  
 //求救信号实现
 class FlashLightClickListener implements OnClickListener {
	 @Override  
     public void onClick(View v) {  
         if(!isOpen){  
	           //打开照相机  
             camera = Camera.open();  
          	 //改变背景图片  
		      view.setBackgroundResource(R.drawable.flashlight_on); 
		      //开始设置每隔1秒自动更新一次
             timer = new Timer(true);
             timer.schedule(new TimerTask() {			
 				@Override
 				public void run() {
 					// TODO Auto-generated method stub
 					Message msg=new Message();
 					msg.what=0x1222;
 					//当主线程有多个handler的时候，就需要使用dispatch分发message。
 					 myHandler.dispatchMessage(msg);
 				}
 			 },0, 1000);
             isOpen = true;  
             //打开notification
             
             //handler,用以更新摄像头闪关灯闪烁开关。
             myHandler =new Handler(){
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					if(msg.what==0x1222){
						if(Flashing==false){
			             //获取照相机参数
						  Parameters params = camera.getParameters();    
				           //设置照相机参数，FLASH_MODE_TORCH  持续的亮灯，FLASH_MODE_ON 只闪一下  
				          params.setFlashMode(Parameters.FLASH_MODE_TORCH);   
				             camera.setParameters(params);  
				             //开始亮灯  
				             camera.startPreview();  
				             Flashing=true;		            
						}else if(Flashing==true){
							 Flashing=false;
							 //flashTime用以记录闪关灯次数
							 ++flashTime;
				           //关掉亮灯 
				             camera.stopPreview();  
				             //当闪关灯闪了三次，发送广播，通知timer更改循环等待时间（由一秒改为三秒）
							  if(flashTime==3) {
								  try {Thread.sleep(3000);
								} catch (InterruptedException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								  Intent intent=new Intent();
								  intent.putExtra("flashTime_Three", flashTime);
								  intent.setAction("yuxiflashlight.time.com");
								  sendBroadcast(intent);
						             //当闪关灯共闪了6次，发送广播，通知timer更改循环等待时间（由三秒改为一秒，并把闪关灯次数重置为0）
							    }else if(flashTime==6) {
								  try {
									Thread.sleep(1000);
								}catch (InterruptedException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								  Intent intent=new Intent();
								  intent.putExtra("flashTime_Three", flashTime);
								  intent.setAction("yuxiflashlight.time.com");
								  sendBroadcast(intent);
							  }
						}
					}
				}	 
             };
             notificationOpen();
         }else{ 
        	 timer.cancel();
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
 //接收广播，通知运行哪个timer
class FlashLightBroadcast extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		flashTime=intent.getIntExtra("flashTime_Three", 0);
		if(flashTime==3){	
		   timer.cancel();
	       timer = new Timer(true);
           timer.schedule(new TimerTask() {			
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Message msg=new Message();
					msg.what=0x1222;
					 myHandler.dispatchMessage(msg);
				}
			},0, 3000);
		}else if(flashTime==6){
			//当闪光灯次数为6时，重置该次数为0
			flashTime=0;
			timer.cancel();
            timer = new Timer(true);
            timer.schedule(new TimerTask() {			
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Message msg=new Message();
					msg.what=0x1222;
					 myHandler.dispatchMessage(msg);
				}
			},0, 1000);
		
		}
		
	}
	}
 class   ExitOnClickListner implements OnClickListener{
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		unregisterReceiver(flashTimeReceiver);
        if(camera != null&&isOpen){  
        	 timer.cancel();
            //关掉亮灯 
            camera.stopPreview();   
            camera.release();  
            notificationCancel();
            finish();  
        }else{
            finish();  
        }
	}
 }
private void notificationOpen(){
	Intent intent=new Intent(this,SOSFlashLightActivity.class);
	//在创建PendingIntent的时候需要注意参数PendingIntent.FLAG_CANCEL_CURRENT
	//这个标志位用来指示：如果当前的Activity和PendingIntent中设置的intent一样，那么就先取消当前的Activity，用PendingIntent中指定的Activity取代之。
	PendingIntent pi=PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	Notification notify=new Notification();
	notify.icon=R.drawable.flashlighticon;
	//点击后自动消失
	//notify.flags |= Notification.FLAG_AUTO_CANCEL;
	notify.when=System.currentTimeMillis();
	//notify.defaults=Notification.DEFAULT_ALL;
	notify.setLatestEventInfo(this, "SOS求救", "正在开启",pi);
	NotificationManager notifyManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	notifyManager.notify(0x0124, notify);
}
private void notificationCancel(){
	NotificationManager notifyManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	notifyManager.cancel(0x0124);
}
@Override
public void onConfigurationChanged(Configuration newConfig) {
	// TODO Auto-generated method stub
	super.onConfigurationChanged(newConfig);
}

@Override  
public boolean onKeyDown(int keyCode, KeyEvent event) {  
    if(keyCode == KeyEvent.KEYCODE_BACK){       //按back键的时候 释放照相机  
    	unregisterReceiver(flashTimeReceiver);
    	if(camera != null&&isOpen){  
    	   timer.cancel();
           //关掉亮灯 
           camera.stopPreview();   
           camera.release();  
           notificationCancel();
           finish();  
    	}else{
    	   finish();  
    	}
    }  
    return super.onKeyDown(keyCode, event);  
} 
//闪关灯次数broadcast过滤器
private IntentFilter getFlashTimerIntentFileter(){
	if(intentFilter_flashtime==null){
		intentFilter_flashtime=new IntentFilter();
		intentFilter_flashtime.addAction("yuxiflashlight.time.com");
	}
	return intentFilter_flashtime;
	}
}

