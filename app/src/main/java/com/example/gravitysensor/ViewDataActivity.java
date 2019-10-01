package com.example.gravitysensor;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ViewDataActivity extends AppCompatActivity {
    private DecimalFormat decimalFormat = new DecimalFormat(".00");
    private DecimalFormat decimalFormatG = new DecimalFormat(".000");
    Button Clear;
    Button Load;
    Button LoadG;

    TextView viewdata;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {//for copy
        super.onCreateContextMenu(menu, v, menuInfo);
        //user has long pressed your TextView
        menu.add(0, v.getId(), 0, "Copied the data");
        //cast the received View to TextView so that you can get its text
        TextView yourTextView = (TextView) v;
        //place your TextView's text in clipboard
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setText(yourTextView.getText());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        viewdata = (TextView) findViewById(R.id.textviewdata);
        viewdata.setMovementMethod(ScrollingMovementMethod.getInstance());//make it scroll
        registerForContextMenu(viewdata);//copy and override onCreateContextMenu


        Clear=findViewById(R.id.clearb);
        Load = (Button) findViewById(R.id.loadb);
        LoadG=(Button)findViewById(R.id.loadgb);
        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialize();
            }
        });
        Load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });
        LoadG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadG();
            }
        });

    }

        public void next(View v) {
            Intent intent = new Intent(this.getBaseContext(), CognitionActivity.class);
            startActivity(intent);
        }


    public void initialize(){
        SharedPreferences sharedPreGravityX = this.getSharedPreferences("SensorDataX", MODE_PRIVATE);
         SharedPreferences.Editor editorX = sharedPreGravityX.edit();
        SharedPreferences sharedPreGravityY = this.getSharedPreferences("SensorDataY", MODE_PRIVATE);
         SharedPreferences.Editor editorY = sharedPreGravityY.edit();
        SharedPreferences sharedPreGravityZ = this.getSharedPreferences("SensorDataZ", MODE_PRIVATE);
         SharedPreferences.Editor editorZ = sharedPreGravityZ.edit();
        SharedPreferences sharedPreCount = this.getSharedPreferences("SensorDataC", MODE_PRIVATE);
        SharedPreferences.Editor editorC = sharedPreCount.edit();
        editorC.clear();
        editorC.commit();
        editorX.clear();
        editorX.commit();
        editorY.clear();
        editorY.commit();
        editorZ.clear();
        editorZ.commit();


        SharedPreferences sharedPreGX = this.getSharedPreferences("SensorDataGX", MODE_PRIVATE);
        final SharedPreferences.Editor editorGX = sharedPreGX.edit();
        SharedPreferences sharedPreGY = this.getSharedPreferences("SensorDataGY", MODE_PRIVATE);
        final SharedPreferences.Editor editorGY = sharedPreGY.edit();
        SharedPreferences sharedPreGZ = this.getSharedPreferences("SensorDataGZ", MODE_PRIVATE);
        final SharedPreferences.Editor editorGZ = sharedPreGZ.edit();
        SharedPreferences sharedPreGCount = this.getSharedPreferences("SensorDataGC", MODE_PRIVATE);
        final SharedPreferences.Editor editorGC = sharedPreGCount.edit();
        editorGX.clear();
        editorGX.commit();
        editorGY.clear();
        editorGY.commit();
        editorGZ.clear();
        editorGZ.commit();
        editorGC.clear();
        editorGC.commit();


        Toast.makeText(this.getBaseContext(),"已清除記錄",Toast.LENGTH_LONG).show();
        Restart();
    }

    public void load() {
        SharedPreferences sharedPreGravityX = this.getSharedPreferences("SensorDataX", MODE_PRIVATE);
        SharedPreferences sharedPreGravityY = this.getSharedPreferences("SensorDataY", MODE_PRIVATE);
        SharedPreferences sharedPreGravityZ = this.getSharedPreferences("SensorDataZ", MODE_PRIVATE);
        SharedPreferences sharedPreCount = this.getSharedPreferences("SensorDataC", MODE_PRIVATE);
        SharedPreferences sharedPreT = this.getSharedPreferences("SensorDataT", MODE_PRIVATE);
        StringBuilder stringBuilder = new StringBuilder();

        Map<String, ?> countmap = sharedPreCount.getAll();//以下取出count
        Set countset = countmap.keySet();
        List countarray = Arrays.asList(countset.toArray());
        //Object[] countarray=countset.toArray();
        Collections.sort(countarray, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {

                return -(Integer.valueOf(o1.toString()).compareTo(Integer.valueOf(o2.toString())));
            }
        });
        int count = 0;
        for (int i = 0; i < countarray.size(); i++) {
            count = sharedPreCount.getInt(String.valueOf(countarray.get(i)), 99);
            String Tvalue=sharedPreT.getString(String.valueOf(countarray.get(i)), "99");
            Float Xvalue = sharedPreGravityX.getFloat(String.valueOf(countarray.get(i)),99 );
            Float Yvalue = sharedPreGravityY.getFloat(String.valueOf(countarray.get(i)), 99);
            Float Zvalue = sharedPreGravityZ.getFloat(String.valueOf(countarray.get(i)), 99);

            stringBuilder.append(count +", "+Tvalue+ " ,X=" + decimalFormat.format(Xvalue) + " ," + "Y=" + decimalFormat.format(Yvalue) + " ,Z=" + decimalFormat.format(Zvalue) + "\n");
        }
        if (count == 0) {

            Toast.makeText(this, "尚無記錄", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "讀取成功", Toast.LENGTH_LONG).show();
            viewdata.setText(stringBuilder);
        }
    }

    public void loadG() {
        SharedPreferences sharedPreGX = this.getSharedPreferences("SensorDataGX", MODE_PRIVATE);
        SharedPreferences sharedPreGY = this.getSharedPreferences("SensorDataGY", MODE_PRIVATE);
        SharedPreferences sharedPreGZ = this.getSharedPreferences("SensorDataGZ", MODE_PRIVATE);
        SharedPreferences sharedPreGCount = this.getSharedPreferences("SensorDataGC", MODE_PRIVATE);
        SharedPreferences sharedPreGT = this.getSharedPreferences("SensorDataGT", MODE_PRIVATE);
        StringBuilder stringBuilder = new StringBuilder();

        Map<String, ?> countmap = sharedPreGCount.getAll();//以下取出count
        Set countset = countmap.keySet();
        List countarray = Arrays.asList(countset.toArray());
        //Object[] countarray=countset.toArray();
        Collections.sort(countarray, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {

                return -(Integer.valueOf(o1.toString()).compareTo(Integer.valueOf(o2.toString())));
            }
        });
        int count = 0;
        for (int i = 0; i < countarray.size(); i++) {
            count = sharedPreGCount.getInt(String.valueOf(countarray.get(i)), 99);
            String Tvalue=sharedPreGT.getString(String.valueOf(countarray.get(i)), "99");
            Float Xvalue = sharedPreGX.getFloat(String.valueOf(countarray.get(i)),99 );
            Float Yvalue = sharedPreGY.getFloat(String.valueOf(countarray.get(i)), 99);
            Float Zvalue = sharedPreGZ.getFloat(String.valueOf(countarray.get(i)), 99);

            stringBuilder.append(count +", "+Tvalue+ " ,GX=" + decimalFormatG.format(Xvalue) + " ," + "GY=" + decimalFormatG.format(Yvalue) + " ,GZ=" + decimalFormatG.format(Zvalue) + "\n");
        }
        if (count == 0) {

            Toast.makeText(this, "尚無記錄", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "讀取成功", Toast.LENGTH_LONG).show();
            viewdata.setText(stringBuilder);
        }
    }

    public void GXtoString(View v) {
        SharedPreferences sharedPreGX = this.getSharedPreferences("SensorDataGX", MODE_PRIVATE);
        SharedPreferences sharedPreGY = this.getSharedPreferences("SensorDataGY", MODE_PRIVATE);
        SharedPreferences sharedPreGZ = this.getSharedPreferences("SensorDataGZ", MODE_PRIVATE);
        SharedPreferences sharedPreGCount = this.getSharedPreferences("SensorDataGC", MODE_PRIVATE);
        SharedPreferences sharedPreGT = this.getSharedPreferences("SensorDataGT", MODE_PRIVATE);

        StringBuilder stringBuilderX = new StringBuilder();


        Map<String, ?> countmap = sharedPreGCount.getAll();//以下取出count
        Set countset = countmap.keySet();
        List countarray = Arrays.asList(countset.toArray());
        //Object[] countarray=countset.toArray();
        Collections.sort(countarray, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {

                return -(Integer.valueOf(o1.toString()).compareTo(Integer.valueOf(o2.toString())));
            }
        });
        int count = 0;
        for (int i = 0; i < countarray.size(); i++) {
            count = sharedPreGCount.getInt(String.valueOf(countarray.get(i)), 99);
            String Tvalue=sharedPreGT.getString(String.valueOf(countarray.get(i)), "99");
            Float Xvalue = sharedPreGX.getFloat(String.valueOf(countarray.get(i)),99 );
            Float Yvalue = sharedPreGY.getFloat(String.valueOf(countarray.get(i)), 99);
            Float Zvalue = sharedPreGZ.getFloat(String.valueOf(countarray.get(i)), 99);
            stringBuilderX.append(decimalFormatG.format(Xvalue)+"x");


           // stringBuilder.append(count +", "+Tvalue+ " ,GX=" + decimalFormatG.format(Xvalue) + " ," + "GY=" + decimalFormatG.format(Yvalue) + " ,GZ=" + decimalFormatG.format(Zvalue) + "\n");
        }
        if (count == 0) {

            Toast.makeText(this, "尚無記錄", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "讀取成功", Toast.LENGTH_LONG).show();

            viewdata.setText(stringBuilderX);
        }
    }
    public void GYtoString(View v) {
        SharedPreferences sharedPreGX = this.getSharedPreferences("SensorDataGX", MODE_PRIVATE);
        SharedPreferences sharedPreGY = this.getSharedPreferences("SensorDataGY", MODE_PRIVATE);
        SharedPreferences sharedPreGZ = this.getSharedPreferences("SensorDataGZ", MODE_PRIVATE);
        SharedPreferences sharedPreGCount = this.getSharedPreferences("SensorDataGC", MODE_PRIVATE);
        SharedPreferences sharedPreGT = this.getSharedPreferences("SensorDataGT", MODE_PRIVATE);

        StringBuilder stringBuilderY = new StringBuilder();


        Map<String, ?> countmap = sharedPreGCount.getAll();//以下取出count
        Set countset = countmap.keySet();
        List countarray = Arrays.asList(countset.toArray());
        //Object[] countarray=countset.toArray();
        Collections.sort(countarray, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {

                return -(Integer.valueOf(o1.toString()).compareTo(Integer.valueOf(o2.toString())));
            }
        });
        int count = 0;
        for (int i = 0; i < countarray.size(); i++) {
            count = sharedPreGCount.getInt(String.valueOf(countarray.get(i)), 99);
            String Tvalue=sharedPreGT.getString(String.valueOf(countarray.get(i)), "99");
            Float Xvalue = sharedPreGX.getFloat(String.valueOf(countarray.get(i)),99 );
            Float Yvalue = sharedPreGY.getFloat(String.valueOf(countarray.get(i)), 99);
            Float Zvalue = sharedPreGZ.getFloat(String.valueOf(countarray.get(i)), 99);

            stringBuilderY.append(decimalFormatG.format(Yvalue)+"y");


            // stringBuilder.append(count +", "+Tvalue+ " ,GX=" + decimalFormatG.format(Xvalue) + " ," + "GY=" + decimalFormatG.format(Yvalue) + " ,GZ=" + decimalFormatG.format(Zvalue) + "\n");
        }
        if (count == 0) {

            Toast.makeText(this, "尚無記錄", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "讀取成功", Toast.LENGTH_LONG).show();

            viewdata.setText(stringBuilderY);
        }
    }
    public void GZtoString(View v) {
        SharedPreferences sharedPreGX = this.getSharedPreferences("SensorDataGX", MODE_PRIVATE);
        SharedPreferences sharedPreGY = this.getSharedPreferences("SensorDataGY", MODE_PRIVATE);
        SharedPreferences sharedPreGZ = this.getSharedPreferences("SensorDataGZ", MODE_PRIVATE);
        SharedPreferences sharedPreGCount = this.getSharedPreferences("SensorDataGC", MODE_PRIVATE);
        SharedPreferences sharedPreGT = this.getSharedPreferences("SensorDataGT", MODE_PRIVATE);

        StringBuilder stringBuilderZ = new StringBuilder();

        Map<String, ?> countmap = sharedPreGCount.getAll();//以下取出count
        Set countset = countmap.keySet();
        List countarray = Arrays.asList(countset.toArray());
        //Object[] countarray=countset.toArray();
        Collections.sort(countarray, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {

                return -(Integer.valueOf(o1.toString()).compareTo(Integer.valueOf(o2.toString())));
            }
        });
        int count = 0;
        for (int i = 0; i < countarray.size(); i++) {
            count = sharedPreGCount.getInt(String.valueOf(countarray.get(i)), 99);
            String Tvalue=sharedPreGT.getString(String.valueOf(countarray.get(i)), "99");
            Float Xvalue = sharedPreGX.getFloat(String.valueOf(countarray.get(i)),99 );
            Float Yvalue = sharedPreGY.getFloat(String.valueOf(countarray.get(i)), 99);
            Float Zvalue = sharedPreGZ.getFloat(String.valueOf(countarray.get(i)), 99);

            stringBuilderZ.append(decimalFormatG.format(Zvalue)+"z");

            // stringBuilder.append(count +", "+Tvalue+ " ,GX=" + decimalFormatG.format(Xvalue) + " ," + "GY=" + decimalFormatG.format(Yvalue) + " ,GZ=" + decimalFormatG.format(Zvalue) + "\n");
        }
        if (count == 0) {

            Toast.makeText(this, "尚無記錄", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "讀取成功", Toast.LENGTH_LONG).show();

            viewdata.setText(stringBuilderZ);
        }
    }

    public void Restart(){
        Intent intent=this.getBaseContext().getPackageManager().getLaunchIntentForPackage(this.getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        this.finish();
    }
}
