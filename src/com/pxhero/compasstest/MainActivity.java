package com.pxhero.compasstest;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private ImageView m_ImageCompass;
	private TextView m_TextValue;
	
	private SensorManager m_SensorManager;
	private Sensor m_accelerometer; //加速度传感器
	private Sensor m_magneticSensor; //地磁传感器
	
	private SensorEventListener m_listener = new SensorEventListener() {
		
		private float[] m_accelerometerValues=new float[3];
		private float[] m_magneticValues = new float[3];
		private float m_lastRotateDegree;
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				 //若是加速度传感器
				m_accelerometerValues = event.values.clone(); // 深拷贝
			}
			else if( event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				m_magneticValues = event.values.clone(); //深拷贝
			}
			
			float[] R = new float[9];
			float[] values = new float[3];
			SensorManager.getRotationMatrix(R, null, m_accelerometerValues, m_magneticValues);
			SensorManager.getOrientation(R, values);  //获取XYZ三个方向上的旋转角度
			
			m_TextValue.setText("The totation of Z is " + Math.toDegrees(values[0]) );
			
			//将计算出的旋转角度取反，用于旋转指南针背景图
			float rotateDegree = - (float)Math.toDegrees(values[0]);
			
			if(Math.abs(rotateDegree- m_lastRotateDegree) > 1) {
				RotateAnimation animation = new RotateAnimation(m_lastRotateDegree, rotateDegree, Animation.RELATIVE_TO_SELF,0.5f,
						Animation.RELATIVE_TO_SELF,0.5f);
				animation.setFillAfter(true);
				m_ImageCompass.startAnimation(animation);
				m_lastRotateDegree = rotateDegree;
			}
			
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		m_ImageCompass = (ImageView)findViewById(R.id.compassImage);
		m_TextValue = (TextView)findViewById(R.id.OrientationText);
		
		m_SensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		m_accelerometer = m_SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); //获取加速度传感器实例
		m_magneticSensor = m_SensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD); //获取地磁传感器实例
		
		m_SensorManager.registerListener(m_listener, m_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);  //注册加速度事件监听器
		m_SensorManager.registerListener(m_listener, m_magneticSensor, SensorManager.SENSOR_DELAY_NORMAL); //注册地磁事件监听器
	}
	
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(m_SensorManager != null) {
			m_SensorManager.unregisterListener(m_listener);
		}
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
