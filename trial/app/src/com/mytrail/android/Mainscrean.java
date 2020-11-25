package com.mytrail.android;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.SalesforceActivity;
import org.json.JSONArray;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Mainscrean extends SalesforceActivity {

    ArrayList<String> tasksubject = new ArrayList<>();
    private RestClient client;
    private TextView mnumbertask;
    private Button mbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscrean);

    }

    public void onStop() {

        tasksubject.clear();
        super.onStop();

    }

    @Override
    public void onStart() {

        super.onStart();
        super.onResume();
        String k = client.getClientInfo().userId;
        String j =client.getClientInfo().displayName;
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>Hey "+j+"!"+"</font>"));
        tasksubject.clear();
        mnumbertask = findViewById(R.id.numbertask);
        mbutton = findViewById(R.id.button);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String date = mdformat.format(calendar.getTime());

        try {

            sendRequest("SELECT id FROM Task WHERE ActivityDate=" + date + "and ownerid='" + k + "'");

        }

        catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }

        mbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent mainIntent1 = new Intent(Mainscrean.this, Main3Activity.class);
                mainIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent1);

            }
        });

    }

    @Override
    public void onResume(RestClient client) {
        this.client = client;
    }

    private void sendRequest(String soql) throws UnsupportedEncodingException {

        RestRequest restRequest = RestRequest.getRequestForQuery(ApiVersionStrings.getVersionNumber(this), soql);

        client.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {

            @Override
            public void onSuccess(RestRequest request, final RestResponse result) {

                result.consumeQuietly(); // consume before going back to main thread

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        try {

                            JSONArray records = result.asJSONObject().getJSONArray("records");
                            tasksubject.clear();

                            for (int i = 0; i < records.length(); i++) {

                                tasksubject.add(records.getJSONObject(0).getString("Id"));

                            }

                            String l = String.valueOf(tasksubject.size()).trim();
                            mnumbertask.setText(l);
                            mnumbertask.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 75);

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

                        Toast.makeText(Mainscrean.this,Mainscrean.this.getString(R.string.sf__generic_error, exception.toString()),Toast.LENGTH_LONG).show();

                    }

                });

            }

        });

    }

}