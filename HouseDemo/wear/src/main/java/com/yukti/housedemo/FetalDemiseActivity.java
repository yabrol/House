package com.yukti.housedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class FetalDemiseActivity extends Activity {

    private TextView mTextView,mSpeechTextView;
    private Button mIncorrect;
    private Intent mIntent;
    private TopicModule module;
    private static final int SPEECH_RECOGNIZER_REQUEST_CODE_FHB = 0;
    private static final int SPEECH_RECOGNIZER_REQUEST_CODE_CRL = 1;
    private static final int SPEECH_RECOGNIZER_REQUEST_CODE_FPP = 2;
    private static final int SPEECH_RECOGNIZER_REQUEST_CODE_MSD = 3;
    private static final int SPEECH_RECOGNIZER_REQUEST_CODE_YSP = 4;
    private static final int SPEECH_RECOGNIZER_REQUEST_CODE_EP = 5;
    private static final int SPEECH_RECOGNIZER_REQUEST_CODE_FPMTM = 6;
    private static final int SPEECH_RECOGNIZER_REQUEST_CODE_SSG = 7;
    private static final int SPEECH_RECOGNIZER_REQUEST_CODE_IGS = 8;
    private static final int SPEECH_RECOGNIZER_REQUEST_CODE_LPGS = 9;
    private String TAG = "FetalDemAct";
    private Handler mHandler = new Handler();

    private boolean Fetal_Heart_Beat;
    private int CRL;
    private boolean Fetal_Pole_Present;
    private int MSD;
    private boolean Yolk_Sac_Present;
    private boolean Embryo_Present;
    private boolean Fetal_Pole_Measurement_To_MSD;
    private boolean Serial_Scan_Growth;
    private boolean Irregular_Gestation_Sac;
    private boolean Low_Position_Gestational_Sac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetal_demise);

        mIntent = getIntent();
        module = (TopicModule) mIntent.getSerializableExtra("TopicModule");

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mTextView.setText("Fetal Heart Beat Present? Y/N");

                mSpeechTextView = (TextView) stub.findViewById(R.id.speechTextView);
                mSpeechTextView.setText("");

                mIncorrect = (Button) stub.findViewById(R.id.pauseButton);
                mIncorrect.setText("Incorrect");
                mIncorrect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTextView.getText().toString().equalsIgnoreCase("Fetal Heart Beat Present? Y/N")) {
                            startSpeechRecognition(SPEECH_RECOGNIZER_REQUEST_CODE_FHB);
                        } else if (mTextView.getText().toString().equalsIgnoreCase("CRL (mm)")) {
                            startSpeechRecognition(SPEECH_RECOGNIZER_REQUEST_CODE_CRL);
                        } else if (mTextView.getText().toString().equalsIgnoreCase("Fetal Pole Present? Y/N")) {
                            startSpeechRecognition(SPEECH_RECOGNIZER_REQUEST_CODE_FPP);
                        } else if (mTextView.getText().toString().equalsIgnoreCase("MSD (mm)")) {
                            startSpeechRecognition(SPEECH_RECOGNIZER_REQUEST_CODE_MSD);
                        } else if (mTextView.getText().toString().equalsIgnoreCase("Yolk Sac Present? Y/N")) {
                            startSpeechRecognition(SPEECH_RECOGNIZER_REQUEST_CODE_YSP);
                        } else if (mTextView.getText().toString().equalsIgnoreCase("Embryo Present? Y/N")) {
                            startSpeechRecognition(SPEECH_RECOGNIZER_REQUEST_CODE_EP);
                        } else if (mTextView.getText().toString().equalsIgnoreCase("Fetal Pole Measurement > MSD? Y/N")) {
                            startSpeechRecognition(SPEECH_RECOGNIZER_REQUEST_CODE_FPMTM);
                        } else if (mTextView.getText().toString().equalsIgnoreCase("Serial Scan Growth? Y/N")) {
                            startSpeechRecognition(SPEECH_RECOGNIZER_REQUEST_CODE_SSG);
                        } else if (mTextView.getText().toString().equalsIgnoreCase("Irregular Gestation Sac? Y/N")) {
                            startSpeechRecognition(SPEECH_RECOGNIZER_REQUEST_CODE_IGS);
                        } else if (mTextView.getText().toString().equalsIgnoreCase("Low Position Gestational Sac? Y/N")) {
                            startSpeechRecognition(SPEECH_RECOGNIZER_REQUEST_CODE_LPGS);
                        }
                    }
                });
                pause(SPEECH_RECOGNIZER_REQUEST_CODE_FHB);
            }
        });
    }


    private void startSpeechRecognition(int codeType) {
        // Create an intent to start the Speech Recognizer
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity
        startActivityForResult(intent, codeType);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        List<String> results;
        String recognizedText;
        if (requestCode == SPEECH_RECOGNIZER_REQUEST_CODE_FHB) {
            // When the speech recognizer finishes its work, Android invokes this callback with requestCode equal to SPEECH_RECOGNIZER_REQUEST_CODE
            if (resultCode == RESULT_OK) {
                results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                recognizedText = results.get(0);
                // Display the recognized text
                mSpeechTextView.setText(recognizedText);
                if(yesOrNo(recognizedText, requestCode)){
                    Fetal_Heart_Beat = true;
                    mTextView.setText("Fetal Pole Present? Y/N");
                    pause(SPEECH_RECOGNIZER_REQUEST_CODE_FPP);
                }else {
                    Fetal_Heart_Beat = false;
                    mTextView.setText("CRL (mm)");
                    pause(SPEECH_RECOGNIZER_REQUEST_CODE_CRL);
                }
            }
        }else if(requestCode == SPEECH_RECOGNIZER_REQUEST_CODE_CRL){
            // When the speech recognizer finishes its work, Android invokes this callback with requestCode equal to SPEECH_RECOGNIZER_REQUEST_CODE
            if (resultCode == RESULT_OK) {
                results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                recognizedText = results.get(0);
                // Display the recognized text
                mSpeechTextView.setText(recognizedText);
                CRL = numberRecognition(recognizedText, requestCode);
                if( CRL >= 7 && !Fetal_Heart_Beat){
                    pause("Non-viable");
                }else
                {
                    mTextView.setText("Fetal Pole Present? Y/N");
                    pause(SPEECH_RECOGNIZER_REQUEST_CODE_FPP);
                }
            }
        }else if(requestCode == SPEECH_RECOGNIZER_REQUEST_CODE_FPP){
            // When the speech recognizer finishes its work, Android invokes this callback with requestCode equal to SPEECH_RECOGNIZER_REQUEST_CODE
            if (resultCode == RESULT_OK) {
                results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                recognizedText = results.get(0);
                // Display the recognized text
                mSpeechTextView.setText(recognizedText);
                if(yesOrNo(recognizedText, requestCode)){
                    Fetal_Pole_Present = true;
                    mTextView.setText("Fetal Pole Measurement > MSD? Y/N");
                    pause(SPEECH_RECOGNIZER_REQUEST_CODE_FPMTM);
                }else {
                    Fetal_Pole_Present = false;
                    mTextView.setText("Embryo Present? Y/N");
                    pause(SPEECH_RECOGNIZER_REQUEST_CODE_EP);
                }
            }
        }else if(requestCode == SPEECH_RECOGNIZER_REQUEST_CODE_MSD){
            // When the speech recognizer finishes its work, Android invokes this callback with requestCode equal to SPEECH_RECOGNIZER_REQUEST_CODE
            if (resultCode == RESULT_OK) {
                results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                recognizedText = results.get(0);
                // Display the recognized text
                mSpeechTextView.setText(recognizedText);
                MSD = numberRecognition(recognizedText, requestCode);
                if( MSD > 8 && !Yolk_Sac_Present){
                    pause("Suspicion");
                }else if(MSD <= 8)
                {
                    mTextView.setText("Irregular Gestation Sac? Y/N");
                    pause(SPEECH_RECOGNIZER_REQUEST_CODE_IGS);
                }else if(MSD > 25 && !Embryo_Present && !Fetal_Pole_Present){
                    pause("Non-viable");
                }else {
                    mTextView.setText("Serial Scan Growth? Y/N");
                    pause(SPEECH_RECOGNIZER_REQUEST_CODE_SSG);
                }
            }
        }else if(requestCode == SPEECH_RECOGNIZER_REQUEST_CODE_YSP){
            // When the speech recognizer finishes its work, Android invokes this callback with requestCode equal to SPEECH_RECOGNIZER_REQUEST_CODE
            if (resultCode == RESULT_OK) {
                results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                recognizedText = results.get(0);
                // Display the recognized text
                mSpeechTextView.setText(recognizedText);
                Yolk_Sac_Present = yesOrNo(recognizedText, requestCode);
                if(Yolk_Sac_Present){
                    pause("ALL WELL");
                }else {
                    mTextView.setText("MSD (mm)");
                    pause(SPEECH_RECOGNIZER_REQUEST_CODE_MSD);
                }
            }
        }else if(requestCode == SPEECH_RECOGNIZER_REQUEST_CODE_EP){
            // When the speech recognizer finishes its work, Android invokes this callback with requestCode equal to SPEECH_RECOGNIZER_REQUEST_CODE
            if (resultCode == RESULT_OK) {
                results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                recognizedText = results.get(0);
                // Display the recognized text
                mSpeechTextView.setText(recognizedText);
                Embryo_Present = yesOrNo(recognizedText, requestCode);
                if(Embryo_Present){
                    pause("Suspicion");
                }else {
                    mTextView.setText("MSD (mm)");
                    pause(SPEECH_RECOGNIZER_REQUEST_CODE_MSD);
                }
            }
        }else if(requestCode == SPEECH_RECOGNIZER_REQUEST_CODE_FPMTM){
            // When the speech recognizer finishes its work, Android invokes this callback with requestCode equal to SPEECH_RECOGNIZER_REQUEST_CODE
            if (resultCode == RESULT_OK) {
                results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                recognizedText = results.get(0);
                // Display the recognized text
                mSpeechTextView.setText(recognizedText);
                Fetal_Pole_Measurement_To_MSD = yesOrNo(recognizedText, requestCode);
                if(Fetal_Pole_Measurement_To_MSD){
                    pause("Non-viable");
                }else {
                    mTextView.setText("Serial Scan Growth? Y/N");
                    pause(SPEECH_RECOGNIZER_REQUEST_CODE_SSG);
                }
            }
        }else if(requestCode == SPEECH_RECOGNIZER_REQUEST_CODE_SSG){
            // When the speech recognizer finishes its work, Android invokes this callback with requestCode equal to SPEECH_RECOGNIZER_REQUEST_CODE
            if (resultCode == RESULT_OK) {
                results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                recognizedText = results.get(0);
                // Display the recognized text
                mSpeechTextView.setText(recognizedText);
                Serial_Scan_Growth = yesOrNo(recognizedText, requestCode);
                if(Serial_Scan_Growth){
                    mTextView.setText("Irregular Gestation Sac? Y/N");
                    pause(SPEECH_RECOGNIZER_REQUEST_CODE_IGS);
                }else {
                    pause("Non-viable");
                }
            }
        }else if(requestCode == SPEECH_RECOGNIZER_REQUEST_CODE_IGS){
            // When the speech recognizer finishes its work, Android invokes this callback with requestCode equal to SPEECH_RECOGNIZER_REQUEST_CODE
            if (resultCode == RESULT_OK) {
                results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                recognizedText = results.get(0);
                // Display the recognized text
                mSpeechTextView.setText(recognizedText);
                Irregular_Gestation_Sac = yesOrNo(recognizedText, requestCode);
                if(Irregular_Gestation_Sac){
                    pause("Suspicion");
                }else {
                    mTextView.setText("Low Position Gestational Sac? Y/N");
                    pause(SPEECH_RECOGNIZER_REQUEST_CODE_LPGS);
                }
            }
        }else if(requestCode == SPEECH_RECOGNIZER_REQUEST_CODE_LPGS){
            // When the speech recognizer finishes its work, Android invokes this callback with requestCode equal to SPEECH_RECOGNIZER_REQUEST_CODE
            if (resultCode == RESULT_OK) {
                results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                recognizedText = results.get(0);
                // Display the recognized text
                mSpeechTextView.setText(recognizedText);
                Low_Position_Gestational_Sac = yesOrNo(recognizedText, requestCode);
                if(Low_Position_Gestational_Sac){
                    pause("Suspicion");
                }else {
                    mTextView.setText("Yolk Sac Present? Y/N");
                    pause(SPEECH_RECOGNIZER_REQUEST_CODE_YSP);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean yesOrNo(String recognizedText, int code){
        if(recognizedText.equalsIgnoreCase("yes")){
            return true;
        }else if(recognizedText.equalsIgnoreCase("no")){
            return false;
        }else{
            mSpeechTextView.setText("try again");
            pause(code);
        }
        return false;
    }

    private int numberRecognition(String recognizedText, int code){
        try{
            return Integer.parseInt(recognizedText);
        } catch(NumberFormatException e){
            mSpeechTextView.setText("try again");
            pause(code);
        }
        return -1;
    }

    private void pause(final int code){
        mHandler.postDelayed(new Runnable() {
            public void run() {
                //wait if they click button
                startSpeechRecognition(code);
            }
        }, 2000);
    }

    private void pause(final String str){
        mHandler.postDelayed(new Runnable() {
            public void run() {
                //wait if they click button
                mTextView.setText(str);
            }
        }, 2000);
    }
}
