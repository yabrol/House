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

public class LungNoduleActivity extends Activity {

    private TextView mTextView, mSpeechTextView;
    private Button mIncorrect;
    private Intent mIntent;
    private TopicModule module;
    private static final int SPEECH_RECOGNIZER_REQUEST_CODE_RISK = 0;
    private static final int SPEECH_RECOGNIZER_REQUEST_CODE_NODE = 1;
    private boolean isLowRisk;
    private int noduleSize;
    private String TAG = "LungNodAct";
    private Handler mHandler = new Handler();
    private LungNoduleInfo lungNoduleInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lung_nodule);

        mIntent = getIntent();
        module = (TopicModule) mIntent.getSerializableExtra("TopicModule");

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                lungNoduleInfo = new LungNoduleInfo(LungNoduleActivity.this);
                mTextView = (TextView) stub.findViewById(R.id.textView);
//                mTextView = new TextView(LungNoduleActivity.this.getApplicationContext());
                mTextView.setText("<Low> or <High> Risk Patient?");

//                mSpeechTextView = new TextView(LungNoduleActivity.this.getApplicationContext());
                mSpeechTextView = (TextView) stub.findViewById(R.id.speechTextView);

//                mIncorrect = new Button(LungNoduleActivity.this.getApplicationContext());
                mIncorrect = (Button) stub.findViewById(R.id.pauseButton);
                mIncorrect.setText("Incorrect");
                mIncorrect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTextView.getText().toString().equalsIgnoreCase("<Low> or <High> Risk Patient?")) {
//                            startSpeechRecognition(SPEECH_RECOGNIZER_REQUEST_CODE_RISK);
                        } else if (mTextView.getText().toString().equalsIgnoreCase("Nodule Size (mm)")) {
//                            startSpeechRecognition(SPEECH_RECOGNIZER_REQUEST_CODE_NODE);
                        }
                    }
                });
//                startSpeechRecognition(SPEECH_RECOGNIZER_REQUEST_CODE_RISK);
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
        if (requestCode == SPEECH_RECOGNIZER_REQUEST_CODE_NODE) {
            // When the speech recognizer finishes its work, Android invokes this callback with requestCode equal to SPEECH_RECOGNIZER_REQUEST_CODE
            if (resultCode == RESULT_OK) {
                results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                recognizedText = results.get(0);
                // Display the recognized text
                mSpeechTextView.setText(recognizedText);
                noduleSize = Integer.getInteger(recognizedText).intValue();
                Log.d(TAG,"nodule size:" + noduleSize);
            }
        }else if(requestCode == SPEECH_RECOGNIZER_REQUEST_CODE_RISK){
            // When the speech recognizer finishes its work, Android invokes this callback with requestCode equal to SPEECH_RECOGNIZER_REQUEST_CODE
            if (resultCode == RESULT_OK) {
                results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                recognizedText = results.get(0);
                // Display the recognized text
                mSpeechTextView.setText(recognizedText);
                if(recognizedText.equalsIgnoreCase("low")){
                    isLowRisk = true;
                }else if(recognizedText.equalsIgnoreCase("high")){
                    isLowRisk = false;
                }else{
                    mSpeechTextView.setText("try again");
                    startSpeechRecognition(SPEECH_RECOGNIZER_REQUEST_CODE_RISK);
                }
            }
        }
//        mHandler.postDelayed(new Runnable() {
//            public void run() {
//                //wait if they click button
//                if(requestCode == SPEECH_RECOGNIZER_REQUEST_CODE_NODE){
//                    lungNoduleInfo.getResult(isLowRisk,noduleSize);
//                }
//            }
//        }, 3000);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
