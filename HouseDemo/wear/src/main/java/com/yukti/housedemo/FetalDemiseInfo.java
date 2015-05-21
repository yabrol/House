package com.yukti.housedemo;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Yukti on 5/21/15.
 */
public class FetalDemiseInfo {
    private ArrayList<String> Measurements;
    private ArrayList<String> Fetal_Heart_Beat;
    private ArrayList<String> CRL;
    private ArrayList<String> Fetal_Pole_Present;
    private ArrayList<String> MSD;
    private ArrayList<String> Yolk_Sac_Present;
    private ArrayList<String> Embryo_Present;
    private ArrayList<String> Fetal_Pole_Measurement_To_MSD;
    private ArrayList<String> Serial_Scan_Growth;
    private ArrayList<String> Irregular_Gestation_Sac;
    private ArrayList<String> Low_Position_Gestational_Sac;
    private Context context;

    public FetalDemiseInfo(Context c){
        context=c;

        Measurements = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.Measurements)));
        Fetal_Heart_Beat = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.Fetal_Heart_Beat)));
        CRL = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.CRL)));
        Fetal_Pole_Present = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.Fetal_Pole_Present)));
        MSD = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.MSD)));
        Yolk_Sac_Present = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.Yolk_Sac_Present)));
        Embryo_Present = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.Embryo_Present)));
        Fetal_Pole_Measurement_To_MSD = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.Fetal_Pole_Measurement_To_MSD)));
        Serial_Scan_Growth = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.Serial_Scan_Growth)));
        Irregular_Gestation_Sac = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.Irregular_Gestation_Sac)));
        Low_Position_Gestational_Sac = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.Low_Position_Gestational_Sac)));
    }

//    public String getResult(){
//    }


}
