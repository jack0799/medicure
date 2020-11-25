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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class calendarpop extends SalesforceActivity {
    private RestClient client;
    private CalendarView mcalendar;
    private Button mresch1;
    private Button mcancel;
    private EditText mreason;

    TextView p;

    @Override
    public void onBackPressed() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);


        getWindow().setExitTransition(new Fade());
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setEnterTransition(new Fade());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendarpop);
        mcalendar=findViewById(R.id.calendarView1);
        mresch1=findViewById(R.id.resch1);
        mcancel=findViewById(R.id.cancel);
        mreason=findViewById(R.id.reason);






        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width =dm.widthPixels;
        int height =dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.7));

        Intent intent = getIntent();
        final String id = intent.getStringExtra("ID");
        final String uid = intent.getStringExtra("UID");
        final String subject = intent.getStringExtra("Subject");
        final String name = intent.getStringExtra("Name");
        java.util.Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");

        String strDate =mdformat.format(calendar.getTime());
        //final int result = Integer.parseInt(strDate);


        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x=0;
        params.y=-10;
        getWindow().setAttributes(params);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getWindow().setElevation(20);
        }
        mcalendar.setMinDate(System.currentTimeMillis());



        mcalendar.setOnDateChangeListener (new CalendarView.OnDateChangeListener()  {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                int month1=month+1;
                final String date = year + "-" +(month1<10?("0"+month1):(month1)) + "-" +(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth));
                mresch1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> fields = new HashMap<String, Object>();
                        Map<String, Object> fields1 = new HashMap<String, Object>();

                        if(mreason.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(),"Provide Reason",Toast.LENGTH_LONG).show();

                        }
                        else{
                            fields.put("ActivityDate", date);
                            fields.put("OwnerId", uid);
                            fields.put("Status","Not Started");
                            fields.put("Subject",subject);
                            fields.put("WhoId",name);
                            fields1.put("rescheduled__c","yes");
                            fields1.put("Reason__c",mreason.getText().toString().trim());

                            saveData(fields,fields1,id);
                            Intent maineIntent = new Intent(calendarpop.this, NCT.class);
                            startActivity(maineIntent);

                            Toast.makeText(getApplicationContext(),"Task Rescheduled",Toast.LENGTH_LONG).show();
                        }






                    }
                });





            }
        });
        mcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarpop.super.onBackPressed();

            }
        });




//                DatePickerDialog.OnDateSetListener myDateListener = new
//                        DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker arg0,
//                                                  int year, int month, int dayOfMonth) {
//                                int month1=month+1;
//                                String date = year + "-" +(month1<10?("0"+month1):(month1)) + "-" +(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth));
//
//
//
//
//                            }
//                        };











    }
                                    private void saveData(Map<String, Object> fields,Map<String, Object> fields1,final String id)  {
                                        RestRequest restRequest;
                                        RestRequest restRequest1;
                                        try {
                                            restRequest1 = RestRequest.getRequestForUpdate(getString(R.string.api_version),"Task", id, fields1);
                                            restRequest = RestRequest.getRequestForCreate(ApiVersionStrings.getVersionNumber(this),"Task",fields);
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

    @Override
    public void onResume(RestClient client) {
        this.client=client;

    }
}
