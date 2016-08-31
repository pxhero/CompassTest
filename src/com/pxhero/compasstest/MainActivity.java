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
	private Sensor m_accelerometer; //���ٶȴ�����
	private Sensor m_magneticSensor; //�شŴ�����
	
	private SensorEventListener m_listener = new SensorEventListener() {
		
		private float[] m_accelerometerValues=new float[3];
		private float[] m_magneticValues = new float[3];
		private float m_lastRotateDegree;
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				 //���Ǽ��ٶȴ�����
				m_accelerometerValues = event.values.clone(); // ���
			}
			else if( event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				m_magneticValues = event.values.clone(); //���
			}
			
			float[] R = new float[9];
			float[] values = new float[3];
			SensorManager.getRotationMatrix(R, null, m_accelerometerValues, m_magneticValues);
			SensorManager.getOrientation(R, values);  //��ȡXYZ���������ϵ���ת�Ƕ�
			
			m_TextValue.setText("The totation of Z is " + Math.toDegrees(values[0]) );
			
			//�����������ת�Ƕ�ȡ����������תָ���뱳��ͼ
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
		m_accelerometer = m_SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); //��ȡ���ٶȴ�����ʵ��
		m_magneticSensor = m_SensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD); //��ȡ�شŴ�����ʵ��
		
		m_SensorManager.registerListener(m_listener, m_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);  //ע����ٶ��¼�������
		m_SensorManager.registerListener(m_listener, m_magneticSensor, SensorManager.SENSOR_DELAY_NORMAL); //ע��ش��¼�������
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
