package com.example.gravitysensor;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_GYROSCOPE;
import static android.hardware.SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    //private static final String TAG = "MainActivity";
    private DecimalFormat decimalFormat = new DecimalFormat(".00");
    private DecimalFormat decimalFormatG = new DecimalFormat(".000");
    private SensorManager sensorManager;
    private SensorManager sensorManagerG;
    private Sensor GyosensorManager;
    private Sensor accelerometer;
    TextView sensorviewX;
    TextView sensorviewY;
    TextView sensorviewZ;

    TextView GyoX;
    TextView GyoY;
    TextView GyoZ;

    Button Start;
    Button Stop;
    Button Next;
    Button Save;

    List listX;
    List listY;
    List listZ;
    List listC;
    List listT;
    List listGX;
    List listGY;
    List listGZ;
    List listGC;
    List listGT;

    private int savecount = 0;
    private int savecountG = 0;
    public SharedPreferences sharedPreGravityX;
    public SharedPreferences.Editor editorGX;
    public SharedPreferences sharedPreGravityY;
    public SharedPreferences.Editor editorGY;
    public SharedPreferences sharedPreGravityZ;
    public SharedPreferences.Editor editorGZ;
    public SharedPreferences sharedPreCount;
    public SharedPreferences.Editor editorGC;
    public SharedPreferences sharedPreGX;
    public SharedPreferences.Editor editorX;
    public SharedPreferences sharedPreGY;
    public SharedPreferences.Editor editorY;
    public SharedPreferences sharedPreGZ;
    public SharedPreferences.Editor editorZ;
    public SharedPreferences sharedPreGCount;
    public SharedPreferences.Editor editorC;
    public SharedPreferences sharedPreT;
    public SharedPreferences.Editor editorT;
    public SharedPreferences sharedPreGT;
    public SharedPreferences.Editor editorGT;

    public Calendar calendar;
    public SimpleDateFormat sdf;
    public Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorviewX = (TextView) findViewById(R.id.sensorviewx);
        sensorviewY = (TextView) findViewById(R.id.sensorviewy);
        sensorviewZ = (TextView) findViewById(R.id.sensorviewz);

        GyoX = (TextView) findViewById(R.id.gx);
        GyoY = (TextView) findViewById(R.id.gy);
        GyoZ = (TextView) findViewById(R.id.gz);

        Start = (Button) findViewById(R.id.startb);
        Stop = (Button) findViewById(R.id.stopb);
        Next = (Button) findViewById(R.id.nextb);
        Save = (Button) findViewById(R.id.saveb);
        listX = new ArrayList<Float>();
        listC = new ArrayList<Float>();
        listY = new ArrayList<Float>();
        listZ = new ArrayList<Float>();
        listT = new ArrayList<>();
        listGX = new ArrayList<Float>();
        listGY = new ArrayList<Float>();
        listGZ = new ArrayList<Float>();
        listGC = new ArrayList<Float>();
        listGT = new ArrayList<>();



        sharedPreGravityX = this.getSharedPreferences("SensorDataX", MODE_PRIVATE);
        editorX = sharedPreGravityX.edit();
        sharedPreGravityY = this.getSharedPreferences("SensorDataY", MODE_PRIVATE);
        editorY = sharedPreGravityY.edit();
        sharedPreGravityZ = this.getSharedPreferences("SensorDataZ", MODE_PRIVATE);
        editorZ = sharedPreGravityZ.edit();
        sharedPreCount = this.getSharedPreferences("SensorDataC", MODE_PRIVATE);
        editorC = sharedPreCount.edit();
        sharedPreT = this.getSharedPreferences("SensorDataT", MODE_PRIVATE);
        editorT = sharedPreT.edit();
        sharedPreGX = this.getSharedPreferences("SensorDataGX", MODE_PRIVATE);
        editorGX = sharedPreGX.edit();
        sharedPreGY = this.getSharedPreferences("SensorDataGY", MODE_PRIVATE);
        editorGY = sharedPreGY.edit();
        sharedPreGZ = this.getSharedPreferences("SensorDataGZ", MODE_PRIVATE);
        editorGZ = sharedPreGZ.edit();
        sharedPreGCount = this.getSharedPreferences("SensorDataGC", MODE_PRIVATE);
        editorGC = sharedPreGCount.edit();
        sharedPreGT = this.getSharedPreferences("SensorDataGT", MODE_PRIVATE);
        editorGT = sharedPreGT.edit();


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManagerG = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER);

//        sensorManager.registerListener(MainActivity.this, accelerometer, 1000000000 * 100);//完成Sensor註冊
//        Log.d(TAG, "onCreate: Registered accelerometer listener");
//
        GyosensorManager = sensorManagerG.getDefaultSensor(TYPE_GYROSCOPE);
//        sensorManager.registerListener(MainActivity.this, GyosensorManager, 1000000000 * 100);//完成Sensor註冊
//        Log.d(TAG, "onCreate: Registered Gyo listener");


        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager.unregisterListener(MainActivity.this, accelerometer);
                sensorManagerG.unregisterListener(MainActivity.this, GyosensorManager);
                Toast.makeText(MainActivity.this, "Stop", Toast.LENGTH_SHORT).show();
            }
        });
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewDataActivity.class);//移到ViewData
               startActivity(intent);
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                for (int i = 1; i <listX.size(); i++) {//= savecount
//                    editorC.putInt(String.valueOf(i), Integer.valueOf(listC.get(i-1).toString()));
//                    editorC.commit();
//                    editorT.putString(String.valueOf(i), listT.get(i - 1).toString());
//                    editorT.commit();
//                    editorX.putFloat(String.valueOf(i), Float.valueOf(listX.get(i - 1).toString()));
//                    editorX.commit();
//                    editorY.putFloat(String.valueOf(i), Float.valueOf(listY.get(i - 1).toString()));
//                    editorY.commit();
//                    editorZ.putFloat(String.valueOf(i), Float.valueOf(listZ.get(i - 1).toString()));
//                    editorZ.commit();
//                }
                for (int i = 1; i < listGX.size(); i++) {
                    editorGC.putInt(String.valueOf(i), Integer.valueOf(listGC.get(i-1).toString()));
                    editorGC.commit();
                    editorGT.putString(String.valueOf(i), listGT.get(i - 1).toString());
                    editorGT.commit();
                    editorGX.putFloat(String.valueOf(i), Float.valueOf(listGX.get(i - 1).toString()));
                    editorGX.commit();
                    editorGY.putFloat(String.valueOf(i), Float.valueOf(listGY.get(i - 1).toString()));
                    editorGY.commit();
                    editorGZ.putFloat(String.valueOf(i), Float.valueOf(listGZ.get(i - 1).toString()));
                    editorGZ.commit();
                }
//                listX.clear();
//                listC.clear();
//                listY.clear();
//                listZ.clear();
                listGX.clear();
                listGY.clear();
                listGZ.clear();
                listGC.clear();
                Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }

            ;
        });
    }


    public void StartSensor(View view) {
        sensorManager.registerListener(MainActivity.this, accelerometer,50000000|SENSOR_STATUS_ACCURACY_MEDIUM);//SensorManager.SENSOR_DELAY_UI
        sensorManagerG.registerListener(MainActivity.this, GyosensorManager, 50000000|SENSOR_STATUS_ACCURACY_MEDIUM);
        Toast.makeText(MainActivity.this, "Start", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("ss.SSSS");
        date=new Date();
        String time=sdf.format(date);
        Sensor sensor = event.sensor;
        if (sensor.getType() == TYPE_GYROSCOPE) {

            savecount++;
            GyoX.setText("GX: " + String.valueOf(event.values[0]));
            GyoY.setText("GY: " + String.valueOf(event.values[1]));
            GyoZ.setText("GZ: " + String.valueOf(event.values[2]));
            listGX.add(event.values[0]);
            listGY.add(event.values[1]);
            listGZ.add(event.values[2]);
            listGC.add(savecount);
            //listGT.add(calendar.getTime().getSeconds());
            listGT.add(time);
        }
//        if (sensor.getType() == TYPE_ACCELEROMETER) {
//            savecount++;
//            sensorviewX.setText("X: " + String.valueOf(event.values[0]));
//            sensorviewY.setText("Y: " + String.valueOf(event.values[1]));
//            sensorviewZ.setText("Z: " + String.valueOf(event.values[2]));
//            listX.add(event.values[0]);
//            listY.add(event.values[1]);
//            listZ.add(event.values[2]);
//            listC.add(savecount);
//           // listT.add(calendar.getTime().getSeconds());
//            listT.add(time);
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }






}