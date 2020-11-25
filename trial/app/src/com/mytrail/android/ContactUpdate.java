package com.mytrail.android;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;

public class ContactUpdate extends SalesforceActivity {
    private RestClient client;
    private EditText motherphone;
    private EditText mbirth;
    private EditText memail;
    private EditText mname;
    private EditText mphone;
    Button firstFragment, secondFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_update);
    }
    public void onResume() {
        super.onResume();
        motherphone = findViewById(R.id.otherphone);
        mbirth = findViewById(R.id.birth);
        memail = findViewById(R.id.email);
        mname = findViewById(R.id.name);
        mphone = findViewById(R.id.phone);
        firstFragment = findViewById(R.id.firstFragment);
        secondFragment = findViewById(R.id.secondFragment);
        Intent intent = getIntent();
        final String k = intent.getStringExtra("Id");
        try {
            sendRequest("select Birthdate,Email,Name,OtherPhone,MobilePhone from contact where id ='" + k + "'");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        firstFragment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // load First Fragment
//                loadFragment(new FirstFragment());
//            }
//        });
//        secondFragment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // load Second Fragment
//                loadFragment(new SecondFragment());
//            }
//        });
    }

    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();

    }

    private void sendRequest(String soql) throws UnsupportedEncodingException {
        RestRequest restRequest = RestRequest.getRequestForQuery(ApiVersionStrings.getVersionNumber(this), soql);
        client.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
            @Override
            public void onSuccess(RestRequest request, final RestResponse result1) {
                result1.consumeQuietly(); // consume before going back to main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray records1 = result1.asJSONObject().getJSONArray("records");
                            String a ;
                            String b ;
                            String c ;
                            String d ;
                            String e ;
                            if(records1.getJSONObject(0).getString("Birthdate").equals(null)){
                                a ="No Data";
                            }
                            else {
                                a =records1.getJSONObject(0).getString("Birthdate");
                            }
                            if(records1.getJSONObject(0).getString("Email").equals("null")){
                                b ="";
                            }
                            else {
                                b =records1.getJSONObject(0).getString("Email");
                            }
                            if(records1.getJSONObject(0).getString("Name").equals("null")){
                                c ="";
                            }
                            else {
                                c =records1.getJSONObject(0).getString("Name");
                            }
                            if(records1.getJSONObject(0).getString("OtherPhone").equals("null")){
                                d ="";
                            }
                            else {
                                d =records1.getJSONObject(0).getString("OtherPhone");
                            }
                            if(records1.getJSONObject(0).getString("MobilePhone").equals("null")){
                                e ="";
                            }
                            else {
                                e =records1.getJSONObject(0).getString("MobilePhone");
                            }

                              mbirth.setText(a);
                              memail.setText(b);
                              mname.setText(c);
                              motherphone.setText(d);
                              mphone.setText(e);
                        }

                        catch (Exception e) {
                            onError(e);
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ContactUpdate.this,ContactUpdate.this.getString(R.string.sf__generic_error, exception.toString()),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onResume(RestClient client) {
        this.client = client;
    }

}