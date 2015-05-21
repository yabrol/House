package com.yukti.housedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

public class WearActivity extends Activity  implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks{

    private TextView mTextView;
    private String TAG = "WEAR";
    private GoogleApiClient mGoogleApiClient;
    private RelativeLayout wearLayout;
    private Button lungNoduleButton;
    private Button fetalDemiseButton;


    private TopicModule module;
    private Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);

        //  Is needed for communication between the wearable and the device.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "onConnectionFailed: " + result);
                    }
                })
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mTextView.setText("Select a module");

                lungNoduleButton = (Button) stub.findViewById(R.id.lungNoduleButton);
                lungNoduleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        module = TopicModule.LUNG_NODULE;
                        myIntent = new Intent(WearActivity.this, LungNoduleActivity.class);
                        myIntent.putExtra("TopicModule", module);
                        WearActivity.this.startActivity(myIntent);
                    }
                });

                fetalDemiseButton = (Button) stub.findViewById(R.id.fetalDemiseButton);
                fetalDemiseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        module = TopicModule.FETAL_DEMISE;
                        myIntent = new Intent(WearActivity.this, FetalDemiseActivity.class);
                        myIntent.putExtra("TopicModule", module);
                        WearActivity.this.startActivity(myIntent);
                    }
                });
            }
        });
    }




    @Override
    public void onConnected(Bundle bundle) {
        Log.v(TAG, "connected to Google Play Services on Wear!");
        Wearable.MessageApi.addListener(mGoogleApiClient, this).setResultCallback(resultCallback);

    }

    /**
     * Not needed, but here to show capabilities. This callback occurs after the MessageApi
     * listener is added to the Google API Client.
     */
    private ResultCallback<Status> resultCallback =  new ResultCallback<Status>() {
        @Override
        public void onResult(Status status) {
            Log.v(TAG, "Status: " + status.getStatus().isSuccess());
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    sendToPhone("/START");
                    return null;
                }
            }.execute();
        }
    };

    /**
     * receives message from watch
     * @param str
     */
    public void sendToPhone(final String str){
        new AsyncTask<Void, Void, List<Node>>() {

            @Override
            protected List<Node> doInBackground(Void... params) {
                return getNodes();
            }

            @Override
            protected void onPostExecute(List<Node> nodeList) {
                for (final Node node : nodeList) {
                    Log.v(TAG, "Node: " + node.getId());

                    PendingResult<MessageApi.SendMessageResult> result = Wearable.MessageApi.sendMessage(
                            mGoogleApiClient,
                            node.getId(),
                            str,
                            null
                    );

                    result.setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            Log.v(TAG, "From watch to phone:" + str);
//                            changeBackgroundColor(Color.BLACK);
                        }
                    });
                }
            }
        }.execute();
    }

    private List<Node> getNodes() {
        List<Node> nodes = new ArrayList<Node>();
        NodeApi.GetConnectedNodesResult rawNodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        for (Node node : rawNodes.getNodes()) {
            nodes.add(node);
        }
        return nodes;
    }

    @Override
    public void onConnectionSuspended(int i) {
        //do nothing
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        /*
        This method apparently runs in a background thread.
         */

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(messageEvent.getPath().endsWith("/START")){
                    Log.d(TAG,"CONNECTED TO PHONE");
                }
                else if(messageEvent.getPath().endsWith("/ALARMON")){
//                    changeBackgroundColor(Color.RED);
                    Log.d(TAG,"alarm ON");
                }
                else {
                    Log.d(TAG,"no proper suffix in msg");
                }
            }
        });

        Log.v(TAG, "Message received on wear: " + messageEvent.getPath());

    }

    @Override
    protected void onStop() {
        super.onStop();
        Wearable.MessageApi.removeListener(mGoogleApiClient, this);
    }
}
