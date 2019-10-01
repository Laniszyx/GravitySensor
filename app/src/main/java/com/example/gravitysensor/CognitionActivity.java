package com.example.gravitysensor;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CognitionActivity extends AppCompatActivity {
    Dialog dialog;//popup視窗

    public static double NEW_DATA[][][] = new double[][][]{{{0, 0, 0, 0, 0, 0, 0, 0}, {0}}};
    public static double NEW_DATAY[][][] = new double[][][]{{{0, 0, 0, 0, 0, 0, 0, 0}, {0}}};
    public static double NEW_DATAX[][][] = new double[][][]{{{0, 0, 0, 0, 0, 0, 0, 0}, {0}}};
    public static double NEW_DATAXUP[][][] = new double[][][]{{{0, 0, 0, 0}, {0}}};
    public static double NEW_DATAYUP[][][] = new double[][][]{{{0, 0, 0, 0}, {0}}};
    public static double NEW_DATAZUP[][][] = new double[][][]{{{0, 0, 0, 0}, {0}}};
    Button finalb;
    Button finalupb;
    Button loadxyzG;
    TextView txtlifttime;
    TextView viewdata;

    SharedPreferences sharedPreGravityX;
    SharedPreferences sharedPreGravityY;
    SharedPreferences sharedPreGravityZ;
    SharedPreferences sharedPreCount;
    SharedPreferences sharedPreT;
    SharedPreferences sharedPreGX;
    SharedPreferences sharedPreGY;
    SharedPreferences sharedPreGZ;
    SharedPreferences sharedPreGCount;
    SharedPreferences sharedPreGT;

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

    List Xindex;
    List GZGYindex;
    List GYindex;
    List GZindex;
    List GXindex;
    List GZupindex;
    List GXupindex;
    List GYupindex;
    StringBuilder stringBuilderMAIN;

    NeuralNetwork neuralNetwork;
    NeuralNetwork2 neuralNetwork2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cognition);

        dialog=new Dialog(this);
        dialog.setContentView(R.layout.liftpopout);

        txtlifttime=(TextView)dialog.findViewById(R.id.lifttext);

        neuralNetwork = new NeuralNetwork(8);//HiddenNeuronAmount
        neuralNetwork2 = new NeuralNetwork2(8);//HiddenNeuronAmount
        viewdata = (TextView) findViewById(R.id.textviewdatac);
        viewdata.setMovementMethod(ScrollingMovementMethod.getInstance());//make it scroll
        registerForContextMenu(viewdata);//copy and override onCreateContextMenu

        finalb = (Button) findViewById(R.id.finalb);
        finalb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Finalconsort();
                    }
                }).run();
            }
        });

        loadxyzG = (Button) findViewById(R.id.loadxyzgb);
        loadxyzG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LoagDataG();
                    }
                }).run();
            }
        });

        finalupb = (Button) findViewById(R.id.finalupb);
        finalupb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FinalconsortUP();
                    }
                }).run();
            }
        });


        stringBuilderMAIN = new StringBuilder();

        listX = new ArrayList<Float>();
        listC = new ArrayList<>();
        listY = new ArrayList<Float>();
        listZ = new ArrayList<Float>();
        listT = new ArrayList<>();
        listGX = new ArrayList<Float>();
        listGY = new ArrayList<Float>();
        listGZ = new ArrayList<Float>();
        listGC = new ArrayList<Float>();
        listGT = new ArrayList<>();

        Xindex = new ArrayList<>();
        GZGYindex = new ArrayList<>();
        GYindex = new ArrayList<>();
        GXindex = new ArrayList<>();
        GZindex = new ArrayList<>();
        GZupindex = new ArrayList<>();
        GYupindex = new ArrayList<>();
        GXupindex = new ArrayList<>();

//        sharedPreGravityX = this.getSharedPreferences("SensorDataX", MODE_PRIVATE);
//        sharedPreGravityY = this.getSharedPreferences("SensorDataY", MODE_PRIVATE);
//        sharedPreGravityZ = this.getSharedPreferences("SensorDataZ", MODE_PRIVATE);
//        sharedPreCount = this.getSharedPreferences("SensorDataC", MODE_PRIVATE);
//        sharedPreT = this.getSharedPreferences("SensorDataT", MODE_PRIVATE);

        sharedPreGX = this.getSharedPreferences("SensorDataGX", MODE_PRIVATE);
        sharedPreGY = this.getSharedPreferences("SensorDataGY", MODE_PRIVATE);
        sharedPreGZ = this.getSharedPreferences("SensorDataGZ", MODE_PRIVATE);
        sharedPreGCount = this.getSharedPreferences("SensorDataGC", MODE_PRIVATE);
        sharedPreGT = this.getSharedPreferences("SensorDataGT", MODE_PRIVATE);


    }

    public void LoagData() {

        Map<String, ?> countmap = sharedPreCount.getAll();//以下取出count
        Set countset = countmap.keySet();
        List countarray = Arrays.asList(countset.toArray());
        Collections.sort(countarray, new Comparator() {//將count排序
            @Override
            public int compare(Object o1, Object o2) {
                return -(Integer.valueOf(o1.toString()).compareTo(Integer.valueOf(o2.toString())));
            }
        });
        int count = 0;
        for (int i = 0; i < countarray.size(); i++) {
            count = sharedPreCount.getInt(String.valueOf(countarray.get(i)), 99);
            String Tvalue = sharedPreT.getString(String.valueOf(countarray.get(i)), "99");
            Float Xvalue = sharedPreGravityX.getFloat(String.valueOf(countarray.get(i)), 99);
            Float Yvalue = sharedPreGravityY.getFloat(String.valueOf(countarray.get(i)), 99);
            Float Zvalue = sharedPreGravityZ.getFloat(String.valueOf(countarray.get(i)), 99);

            listC.add(i, count);
            listT.add(i, Tvalue);//index=原先資料count-1
            listX.add(i, Xvalue);
            listY.add(i, Yvalue);
            listZ.add(i, Zvalue);
        }

    }

    public void LoagDataG() {
        Map<String, ?> countmap = sharedPreGCount.getAll();//以下取出count原本沒有用Gcount
        Set countset = countmap.keySet();
        List countarray = Arrays.asList(countset.toArray());
        //Object[] countarray=countset.toArray();
        Collections.sort(countarray, new Comparator() {//將count排序
            @Override
            public int compare(Object o1, Object o2) {
                return -(Integer.valueOf(o1.toString()).compareTo(Integer.valueOf(o2.toString())));
            }
        });
        int Gcount = 0;
        for (int i = 0; i < countarray.size(); i++) {
            Gcount = sharedPreGCount.getInt(String.valueOf(countarray.get(i)), 99);
            String GTvalue = sharedPreGT.getString(String.valueOf(countarray.get(i)), "99");
            Float GXvalue = sharedPreGX.getFloat(String.valueOf(countarray.get(i)), 99);
            Float GYvalue = sharedPreGY.getFloat(String.valueOf(countarray.get(i)), 99);
            Float GZvalue = sharedPreGZ.getFloat(String.valueOf(countarray.get(i)), 99);

            listGC.add(i, Gcount);
            listGT.add(i, GTvalue);//index=原先資料count-1
            listGX.add(i, GXvalue);
            listGY.add(i, GYvalue);
            listGZ.add(i, GZvalue);
        }
//        checkGZbyweighted();
//        checkGYbyweighted();
//        checkGXbyweighted();
        checkGZupbyweighted();
        checkGYupbyweighted();
        checkGXupbyweighted();
    }


    private void checkGXbyweighted() {
        for (int i = 0; i < listGX.size(); i++) {
            double[] tresult = new double[NEW_DATAX.length];
            if (i + 8 > listGX.size()) {
                break;
            } else {
                for (int x = 0; x < 8; x++) {
                    NEW_DATAX[0][0][x] = Double.valueOf(listGX.get(i + x).toString());
                }
                double tTA = 0.98;
                for (int y = 0; y < NEW_DATAX.length; y++) {
                    tresult[y] = neuralNetwork.testforwardpropX(NEW_DATAX[y][0])
                            .getLayers()[2].getNeuron()[0].getOutput();
                    if (tresult[y] > tTA) {
                        GXindex.add(i);
                    }
                }
            }
        }

        if(GXindex.isEmpty()==false){
            for (int n = 0; n <GXindex.size(); n++) {
                if (n + 2 > GXindex.size()) {
                    break;
                } else {
                    if (Integer.valueOf(GXindex.get(n + 1).toString()) < Integer.valueOf((GXindex.get(n).toString()) + 4)){
                        GXindex.remove(n);
                    }
                }
            }
        }
        stringBuilderMAIN.append(/*"GX:"+GXindex.toString() +*/ "GX:you lifted " + GXindex.size() + ". " + "\n");
    }

    private void checkGXupbyweighted() {
        for (int i = 0; i < listGX.size(); i++) {
            double[] tresult = new double[NEW_DATAXUP.length];
            if (i + 4 > listGX.size()) {
                break;
            } else {
                for (int x = 0; x < 4; x++) {
                    NEW_DATAXUP[0][0][x] = Double.valueOf(listGX.get(i + x).toString());
                }
                double tTA = 0.98;
                for (int y = 0; y < NEW_DATAXUP.length; y++) {
                    tresult[y] = neuralNetwork2.testforwardpropXup(NEW_DATAXUP[y][0])
                            .getLayers()[3].getNeuron()[0].getOutput();
                    if (tresult[y] > tTA) {
                        GXupindex.add(i);
                    }
                }
            }
        }
//        if(GXupindex.isEmpty()==false){
//            for (int n = 0; n <GXupindex.size(); n++) {
//                if (n + 2 > GXupindex.size()) {
//                    break;
//                } else {
//                    if (Integer.valueOf(GXupindex.get(n + 1).toString()) < Integer.valueOf((GXupindex.get(n).toString()) + 4)){
//                        GXupindex.remove(n);
//                    }
//                }
//            }
//        }
        stringBuilderMAIN.append("GXUP: " + GXupindex.size() + ". " + "\n");
    }

    private void checkGYbyweighted() {
        for (int i = 0; i < listGY.size(); i++) {
            double[] tresult = new double[NEW_DATAY.length];
            if (i + 8 > listGY.size()) {
                break;
            } else {
                for (int x = 0; x < 8; x++) {
                    NEW_DATAY[0][0][x] = Double.valueOf(listGY.get(i + x).toString());
                }
                double tTA = 0.98;
                for (int y = 0; y < NEW_DATAY.length; y++) {
                    tresult[y] = neuralNetwork.testforwardpropY(NEW_DATAY[y][0])
                            .getLayers()[2].getNeuron()[0].getOutput();
                    if (tresult[y] > tTA) {
                        GYindex.add(i);
                    }
                }
            }
        }
        if(GYindex.isEmpty()==false){
            for (int n = 0; n <GYindex.size(); n++) {
                if (n + 2 > GYindex.size()) {
                    break;
                } else {
                    if (Integer.valueOf(GYindex.get(n + 1).toString()) < Integer.valueOf((GYindex.get(n).toString()) + 4)){
                        GYindex.remove(n);
                    }
                }
            }
        }
        stringBuilderMAIN.append(/*"GY:"+GYindex.toString() +*/ "GY:you lifted " + GYindex.size() + ". " + "\n");
    }

    private void checkGYupbyweighted() {
        for (int i = 0; i < listGY.size(); i++) {
            double[] tresult = new double[NEW_DATAYUP.length];
            if (i + 4 > listGY.size()) {
                break;
            } else {
                for (int x = 0; x < 4; x++) {
                    NEW_DATAYUP[0][0][x] = Double.valueOf(listGY.get(i + x).toString());
                }
                double tTA = 0.98;
                for (int y = 0; y < NEW_DATAYUP.length; y++) {
                    tresult[y] = neuralNetwork2.testforwardpropYup(NEW_DATAYUP[y][0])
                            .getLayers()[3].getNeuron()[0].getOutput();
                    if (tresult[y] > tTA) {
                        GYupindex.add(i);
                    }
                }
            }
        }

//        if(GYupindex.isEmpty()==false){
//            for (int n = 0; n <GYupindex.size(); n++) {
//                if (n + 2 > GYupindex.size()) {
//                    break;
//                } else {
//                    if (Integer.valueOf(GYupindex.get(n + 1).toString()) < Integer.valueOf((GYupindex.get(n).toString()) + 4)){
//                        GYupindex.remove(n);
//                    }
//                }
//            }
//        }


        stringBuilderMAIN.append("GYUP: " + GYupindex.size() + ". " + "\n");
    }

    private void checkGZbyweighted() {
        for (int i = 0; i < listGZ.size(); i++) {
            double[] tresult = new double[NEW_DATA.length];
            if (i + 8 > listGZ.size()) {
                break;
            } else {
                for (int x = 0; x < 8; x++) {
                    NEW_DATA[0][0][x] = Double.valueOf(listGZ.get(i + x).toString());
                }
                double tTA = 0.98;
                for (int y = 0; y < NEW_DATA.length; y++) {
                    tresult[y] = neuralNetwork.testforwardprop(NEW_DATA[y][0])
                            .getLayers()[2].getNeuron()[0].getOutput();
                    if (tresult[y] > tTA) {
                        GZindex.add(i);
                    }
                }
            }

        }
        if(GZindex.isEmpty()==false){
            for (int n = 0; n <GZindex.size(); n++) {
                if (n + 2 > GZindex.size()) {
                    break;
                } else {
                    if (Integer.valueOf(GZindex.get(n + 1).toString()) < Integer.valueOf((GZindex.get(n).toString()) + 4)){
                        GZindex.remove(n);
                    }
                }
            }
        }

        stringBuilderMAIN.append("GZ:" + GZindex.toString() + "\n" + "GZ:you lifted " + GZindex.size() + ". " + "\n");
    }

    private void checkGZupbyweighted() {
        for (int i = 0; i < listGZ.size(); i++) {
            double[] tresult = new double[NEW_DATAZUP.length];
            if (i + 4 > listGZ.size()) {
                break;
            } else {
                for (int x = 0; x < 4; x++) {
                    NEW_DATAZUP[0][0][x] = Double.valueOf(listGZ.get(i + x).toString());
                }
                double tTA = 0.98;
                for (int y = 0; y < NEW_DATAZUP.length; y++) {
                    tresult[y] = neuralNetwork2.testforwardpropZup(NEW_DATAZUP[y][0])
                            .getLayers()[3].getNeuron()[0].getOutput();
                    if (tresult[y] > tTA) {
                        GZupindex.add(i);
                    }
                }
            }
        }

//        if(GZupindex.isEmpty()==false){
//            for (int n = 0; n <GZupindex.size(); n++) {
//                if (n + 2 > GZupindex.size()) {
//                    break;
//                } else {
//                    if (Integer.valueOf(GZupindex.get(n + 1).toString()) < Integer.valueOf((GZupindex.get(n).toString()) + 4)){
//                        GZupindex.remove(n);
//                    }
//                }
//            }
//        }

        stringBuilderMAIN.append(/*"GZUP:" + GZupindex.toString() + "\n" +*/ "GZUP: " + GZupindex.size() + ". " + "\n");
    }

    public void Finalconsort() {
        List list = new ArrayList();
        for (int i = 0; i < GZindex.size(); i++) {
            for (int j = 0; j < GYindex.size(); j++) {
                if (GYindex.get(j).equals(GZindex.get(i))) {
                    for (int x = 0; x < GXindex.size(); x++) {
                        if (GXindex.get(x).equals(GZindex.get(i))) {
                            list.add(GZindex.get(i));
                        }
                    }
                }
            }
        }

        stringBuilderMAIN.append("FinalConsort:" + list.toString() + "\n");
    }

    public void FinalconsortUP() {
        List list2 = new ArrayList();
        for (int i = 0; i < GZupindex.size(); i++) {
            for (int j = 0; j < GYupindex.size(); j++) {
                if (GYupindex.get(j).equals(GZupindex.get(i))) {
                    for (int x = 0; x < GXupindex.size(); x++) {
                        if (GXupindex.get(x).equals(GZupindex.get(i))) {
                            list2.add(GZupindex.get(i));
                        }
                    }
                }
            }
        }
        if(list2.isEmpty()==false){
            Boolean b1=true;
            for (int n = 0; n <list2.size(); n++) {
                if (n + 2 > list2.size()) {
                    break;
                } else {
                    if (Integer.valueOf(list2.get(n + 1).toString()) - Integer.valueOf(list2.get(n).toString()) < 3){
                        list2.set(n,"?");
                    }
                }
            }
            removeDuplicate(list2);
            list2.remove("?");

//            for (int n = 0; n <list2.size(); n++) {//   排除休息置放啞鈴那一下舉起
//                if (n + 2 > list2.size()) {
//                    break;
//                } else {
//                    if (Integer.valueOf(list2.get(n + 1).toString()) - Integer.valueOf(list2.get(n).toString()) > 12){
//                        list2.remove(n+1);
//                    }
//                }
//            }


        }


        txtlifttime.setText((list2.size()-1)+"~"+list2.size() + " times");

        stringBuilderMAIN.append("FinalConsortUP:" + list2.toString() + "\n");
        //stringBuilderMAIN.append("You lifted " + list2.size() + " times\n");
    }


    public void setViewon(View v) {
        dialog.show();
        viewdata.setText(stringBuilderMAIN);
    }
    public static void removeDuplicate(List list) {
        for ( int i = 0 ; i < list.size()-1 ; i ++ )
        { for ( int j = list.size()-1 ; j > i; j--) {
            if (list.get(j).equals(list.get(i))) {
                list.remove(j); }
        }
        }
    }



}


