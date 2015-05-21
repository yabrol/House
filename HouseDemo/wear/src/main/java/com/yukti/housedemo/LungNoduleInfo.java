package com.yukti.housedemo;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Yukti on 5/21/15.
 */
public class LungNoduleInfo {

    private int[] noduleSizes;
    private ArrayList<String> lowRisk;
    private ArrayList<String> highRisk;
    private Context context;

    public LungNoduleInfo(Context c){
        context = c;
        noduleSizes = c.getResources().getIntArray(R.array.Nodule_Size);
        lowRisk = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.Low_Risk_Patient)));
        highRisk = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.High_Risk_Patient)));
    }

    public String getResult(boolean isLowRisk, int nodSize){
        for(int i =0; i < noduleSizes.length-1; i++){
            if(nodSize < noduleSizes[i]){
                if(isLowRisk){
                    return lowRisk.get(i);
                }else {
                    return highRisk.get(i);
                }
            }
        }
        if(isLowRisk){
            return lowRisk.get(noduleSizes.length);
        }else {
            return highRisk.get(noduleSizes.length);
        }
    }


}
