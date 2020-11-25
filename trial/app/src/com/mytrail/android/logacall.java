package com.mytrail.android;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import java.util.HashMap;
import java.util.Map;

public class logacall extends SalesforceActivity {
    private RestClient client;
    private EditText mcalllog;
    private Button mlogcall;
    private Button mtime;
    private EditText mtimet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);


        getWindow().setExitTransition(new Fade());
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setEnterTransition(new Fade());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logacall);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width =dm.widthPixels;
        int height =dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.6));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getWindow().setElevation(20);
        }

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x=0;
        params.y=-10;
        getWindow().setAttributes(params);

    }

    @Override
    public void onResume(RestClient client) {
        mcalllog=findViewById(R.id.calllog);
        mlogcall=findViewById(R.id.logcall);
        mtime=findViewById(R.id.time);
        mtimet=findViewById(R.id.timet);
        this.client=client;
        final Map<String, Object> fields = new HashMap<String, Object>();
        Intent intent = getIntent();
        final String id = intent.getStringExtra("ID");

        mlogcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mcalllog.getText().toString().equals("")) {

                    String log = mcalllog.getText().toString();
                    fields.put("Description", log);
                    saveData(fields,id);
                    Toast.makeText(logacall.this,"Call logged",Toast.LENGTH_LONG).show();
                    logacall.super.onBackPressed();

                }
                else {

                    Toast.makeText(logacall.this,"Please enter comments",Toast.LENGTH_LONG).show();

                }
            }
        });

        mtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mtimet.getText().toString().equals("")) {


                    fields.put("Time__c", mtimet.getText().toString().trim());
                    fields.put("Status", "Not Started");
                    saveData1(fields,id);
                    Toast.makeText(logacall.this,"Rescheduled",Toast.LENGTH_LONG).show();
                    logacall.super.onBackPressed();

                }
                else {

                    Toast.makeText(logacall.this,"Please enter new time",Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    private void saveData(Map<String, Object> fields,final String id)  {
        RestRequest restRequest;
        RestRequest restRequest1;
        try {
           // restRequest1 = RestRequest.getRequestForUpdate(getString(R.string.api_version),"Task", id, fields);
            restRequest = RestRequest.getRequestForUpdate(getString(R.string.api_version),"Task", id, fields);
           //restRequest = RestRequest.getRequestForCreate(ApiVersionStrings.getVersionNumber(this),"Task",fields);
            //restRequest = RestRequest.getRequestForUpdate(getString(R.string.api_version), "Merchandise__c", id, fields);
        } catch (Exception e) {
            // You might want to log the error or show it to the user
            return;
        }

        client.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
            @Override
            public void onSuccess(RestRequest request, RestResponse result) {
                // Consume before going back to main thread
                // Not required if you don't do main (UI) thread tasks here
                result.consumeQuietly();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Network component doesn’t report app layer status.
                        // Use the Mobile SDK RestResponse.isSuccess() method to check
                        // whether the REST request itself succeeded.

                        try {
                            Log.d("APITest", "success entered");
                        } catch (Exception e) {
                            //showError(MainActivity.this, e);
                        }

                    }
                });
            }



            @Override
            public void onError(Exception e) {
                // You might want to log the error
                // or show it to the user
            }
        });


    }

    private void saveData1(Map<String, Object> fields,final String id)  {
        RestRequest restRequest;
        RestRequest restRequest1;
        try {
            restRequest1 = RestRequest.getRequestForUpdate(getString(R.string.api_version),"Task", id, fields);
            //restRequest = RestRequest.getRequestForUpdate(getString(R.string.api_version),"Task", id, fields);
            //restRequest = RestRequest.getRequestForCreate(ApiVersionStrings.getVersionNumber(this),"Task",fields);
            //restRequest = RestRequest.getRequestForUpdate(getString(R.string.api_version), "Merchandise__c", id, fields);
        } catch (Exception e) {
            // You might want to log the error or show it to the user
            return;
        }


        client.sendAsync(restRequest1, new RestClient.AsyncRequestCallback() {
            @Override
            public void onSuccess(RestRequest request, RestResponse result) {
                // Consume before going back to main thread
                // Not required if you don't do main (UI) thread tasks here
                result.consumeQuietly();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Network component doesn’t report app layer status.
                        // Use the Mobile SDK RestResponse.isSuccess() method to check
                        // whether the REST request itself succeeded.

                        try {
                            Log.d("APITest", "success entered");
                        } catch (Exception e) {
                            //showError(MainActivity.this, e);
                        }

                    }
                });
            }

            @Override
            public void onError(Exception e) {
                // You might want to log the error
                // or show it to the user
            }
        });

    }
}