package com.mytrail.android;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Fade;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.SalesforceActivity;
import java.util.HashMap;
import java.util.Map;

public class caseraise extends SalesforceActivity {
    private Button msubmit;
    private EditText msubject;
    private EditText mstatus;
    private TextView mtest;
    private Button mcrcancel;

    private RestClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);


        getWindow().setExitTransition(new Fade());
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setEnterTransition(new Fade());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caseraise);
        mcrcancel=findViewById(R.id.crcancel);
        mcrcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caseraise.super.onBackPressed();
            }
        });

        msubmit=findViewById(R.id.submit);
        msubject=findViewById(R.id.subject);
        mstatus=findViewById(R.id.status);
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
        Intent intent = getIntent();
        final String id = intent.getStringExtra("ID");
        msubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = msubject.getText().toString().trim();
                String status = mstatus.getText().toString().trim();
                if(!TextUtils.isEmpty(subject)||!TextUtils.isEmpty(status)){
                    Map<String, Object> fields = new HashMap<String, Object>();
                    fields.put("Origin", "Phone");
                    fields.put("Subject", subject);
                    fields.put("Status",status);
                    fields.put("ContactId",id );
                    saveData(fields);
                    caseraise.super.onBackPressed();
                    Toast.makeText(getApplicationContext(),"Caise Raised",Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getApplicationContext(),"Fill all the Fields",Toast.LENGTH_LONG).show();

                }

            }
        });
    }
    private void saveData(Map<String, Object> fields)  {
        RestRequest restRequest;
        try {
            restRequest = RestRequest.getRequestForCreate(ApiVersionStrings.getVersionNumber(this),"Case",fields);
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

    @Override
    public void onResume(RestClient client) {
        this.client=client;


    }
}
