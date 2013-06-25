package com.example.yuxiflashlight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {
	private ListView functionList;
	private ImageView functionTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initCreate();
		functionListView();
	}
	private void initCreate(){
		 functionList=(ListView)findViewById(R.id.function_listview);
		 functionTitle=(ImageView)findViewById(R.id.function_title);
	}
	private void functionListView()
	{
		ArrayList<HashMap<String,Object>> arraylist=new ArrayList<HashMap<String,Object>>();
		  HashMap<String,Object> map1=new HashMap<String,Object>();
		  HashMap<String,Object> map2=new HashMap<String,Object>();
		  HashMap<String,Object> map3=new HashMap<String,Object>();
		  HashMap<String,Object> map4=new HashMap<String,Object>();
		  HashMap<String,Object> map5=new HashMap<String,Object>();
		  map1.put("function_imageview", R.drawable.ic_launcher);
		  map1.put("function_textview", "LED手电筒");
		  map2.put("function_imageview", R.drawable.ic_launcher);
		  map2.put("function_textview", "LED求救信号");
		  map3.put("function_imageview", R.drawable.ic_launcher);
		  map3.put("function_textview", "屏幕手电筒");
		  map4.put("function_imageview", R.drawable.ic_launcher);
		  map4.put("function_textview", "退出");
		  arraylist.add(map1);
		  arraylist.add(map2);
		  arraylist.add(map3);
		  arraylist.add(map4);
		  arraylist.add(map5);
		  SimpleAdapter simpleadapter=new SimpleAdapter(MainActivity.this, arraylist, R.layout.function_list, new String[]{"function_imageview","function_textview"},new int[]{R.id.function_imageview,R.id.function_textview});
		  functionList.setAdapter(simpleadapter);
	}
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		functionList.setOnItemClickListener(new ListOnClickListener());
	}
	
	class ListOnClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			switch (arg2)
			{
			case 0:
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, FlashLight.class);
				startActivity(intent);
				break;
			case 1:
				Intent intent1=new Intent();
				intent1.setClass(MainActivity.this, SOSFlashLightActivity.class);
				startActivity(intent1);
				break;
			case 2:
				Intent intent2=new Intent();
				intent2.setClass(MainActivity.this, ScreenLight.class);
				startActivity(intent2);
				break;
			case 3:
				finish();
				break;
			}
		}
	
	}
	}

