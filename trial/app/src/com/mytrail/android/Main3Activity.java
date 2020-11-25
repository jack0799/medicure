package com.mytrail.android;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

//import androidx.appcompat.app.ActionBar;


public class Main3Activity extends SalesforceActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,MyRecyclerViewAdapter.ItemClickListener {



    private RestClient client;
    private RecyclerView mrec;
    MyRecyclerViewAdapter adapter;
    ArrayList<String> tasksubject  = new ArrayList<>();
    ArrayList<String> tasksubject1 = new ArrayList<>();
    ArrayList<String> tasksubject2 = new ArrayList<>();
    ArrayList<String> tasksubject3 = new ArrayList<>();
    ArrayList<String> tasksubject4 = new ArrayList<>();
    ArrayList<String> tasksubject5 = new ArrayList<>();
    final Map<String, Object> fields = new HashMap<String, Object>();
    SwipeRefreshLayout pullToRefresh;
    private ProgressBar mProgress;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Fade());
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setEnterTransition(new Fade());
        super.onCreate(savedInstanceState);
        getWindow().setAllowEnterTransitionOverlap(true);
        setContentView(R.layout.activity_main3);
        tasksubject1.clear();
        tasksubject.clear();
        tasksubject3.clear();
        tasksubject2.clear();
        tasksubject4.clear();
        tasksubject5.clear();
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);
        ActionBar actionBar = getActionBar();
       // actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE))

        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Today's Tasks </font>"));


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            int Refreshcounter = 1; //Counting how many times user have refreshed the layout

            @Override
            public void onRefresh() {
                tasksubject1.clear();
                tasksubject.clear();
                tasksubject3.clear();
                tasksubject2.clear();
                tasksubject4.clear();
                tasksubject5.clear();

                String k = client.getClientInfo().userId;
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
                String date = mdformat.format(calendar.getTime());

                try {
                    sendRequest("SELECT Subject,status,WhoId,id FROM Task WHERE ActivityDate=" + date + " and status ='Not Started' and ownerid='" + k + "'");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                pullToRefresh.setRefreshing(false);
            }
        });

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
//        switch (position) {
//            case 0:
//                Intent maineIntent = new Intent(Main3Activity.this, Calendar.class);
//                maineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(maineIntent);
//            case 1:
//
//            case 2:
//
//        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                Intent maineIntent = new Intent(Main3Activity.this, Calendar.class);
                maineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(maineIntent,ActivityOptions.makeSceneTransitionAnimation(Main3Activity.this).toBundle());
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                Intent maineIntent1 = new Intent(Main3Activity.this, NCT.class);
                maineIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(maineIntent1,ActivityOptions.makeSceneTransitionAnimation(Main3Activity.this).toBundle());
                break;
            case 4:
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void onPause() {
//        tasksubject1.clear();
//        tasksubject.clear();
//        tasksubject3.clear();
//        tasksubject2.clear();
//        tasksubject4.clear();
//        tasksubject5.clear();
        super.onPause();
    }
    public void onStop() {
        tasksubject1.clear();
        tasksubject.clear();
        tasksubject3.clear();
        tasksubject2.clear();
        tasksubject4.clear();
        tasksubject5.clear();
        super.onStop();
    }
    public void onRestart() {
        super.onRestart();
//        tasksubject1.clear();
//        tasksubject.clear();
//        tasksubject3.clear();
//        tasksubject2.clear();
//        tasksubject4.clear();
//        tasksubject5.clear();

    }


    public void onStart() {
        final TelephonyManager telephonyManager =
                (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

       // findViewById(R.id.drawer_layout).setVisibility(View.INVISIBLE);

        tasksubject.clear();
        tasksubject1.clear();
        tasksubject2.clear();
        tasksubject3.clear();
        tasksubject4.clear();
        tasksubject5.clear();
        mProgress = findViewById(R.id.prog);
        mProgress.setVisibility(View.VISIBLE);
        super.onResume();
        String k = client.getClientInfo().userId;
        mrec = findViewById(R.id.rec);
        mrec.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, tasksubject, tasksubject1, tasksubject3, new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                //int commandType = intent.getIntExtra("commandType", STATE_INCOMING_NUMBER);
                Uri u = Uri.parse("tel:" + tasksubject2.get(position));

                Intent i = new Intent(Intent.ACTION_CALL, u);
                try {

                    //TimeUnit.SECONDS.sleep(1);
                    Intent maineIntent1 = new Intent(Main3Activity.this, logacall.class);
                    maineIntent1.putExtra("ID", tasksubject5.get(position));
                    startActivity(maineIntent1,ActivityOptions.makeSceneTransitionAnimation(Main3Activity.this).toBundle());
                    startActivity(i);

                }
                catch (SecurityException s) {

                }

                PhoneStateListener callStateListener = new PhoneStateListener() {
                    public void onCallStateChanged(int state, String incomingNumber)
                    {
                        if(state==TelephonyManager.CALL_STATE_RINGING){
                            Toast.makeText(getApplicationContext(),"Phone Is Riging",
                                    Toast.LENGTH_LONG).show();
                        }
                        if(state==TelephonyManager.CALL_STATE_OFFHOOK){

                            Toast.makeText(getApplicationContext(),"Phone Is on call",
                                    Toast.LENGTH_LONG).show();
                        }

                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(state==TelephonyManager.CALL_STATE_IDLE){

                            String id = tasksubject5.get(position);
                            fields.put("Status", "Completed");
                            saveData(fields,id);


//
//                            Toast.makeText(getApplicationContext(),"Phone Is not on call",
//                                    Toast.LENGTH_LONG).show();

                        }
                    }
                };

                telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);


            }
        }, new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                Intent maineIntent = new Intent(Main3Activity.this, Popmssg.class);
                maineIntent.putExtra("Number", tasksubject2.get(position));
                startActivity(maineIntent,ActivityOptions.makeSceneTransitionAnimation(Main3Activity.this).toBundle());

            }
        }, new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent maineIntent = new Intent(Main3Activity.this, caseraise.class);
                maineIntent.putExtra("ID", tasksubject4.get(position));
                startActivity(maineIntent,ActivityOptions.makeSceneTransitionAnimation(Main3Activity.this).toBundle());

            }
        },new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                Intent maineIntent = new Intent(Main3Activity.this, ContactUpdate.class);
                maineIntent.putExtra("Id", tasksubject4.get(position));
                startActivity(maineIntent);

            }
        });

        adapter.setClickListener(this);

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
        String date = mdformat.format(calendar.getTime());
        try {
            sendRequest("SELECT Subject,status,WhoId,id FROM Task WHERE ActivityDate=" + date + " and status ='Not Started' and ownerid='" + k + "'");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
                            tasksubject1.clear();
                            tasksubject.clear();
                            tasksubject3.clear();
                            tasksubject2.clear();
                            tasksubject4.clear();
                            tasksubject5.clear();

                            for (int i = 0; i < records.length(); i++) {

                                String l=records.getJSONObject(i).getString("WhoId");
                                String l1=records.getJSONObject(i).getString("Subject");
                                String l2=records.getJSONObject(i).getString("Status");
                                String l3=records.getJSONObject(i).getString("Id");
                                sendRequest1("SELECT id,name,mobilephone FROM contact where id='"+l+"'",l1,l2,l3);
                            }

                            mProgress.setVisibility(View.GONE);

                        } catch (Exception e) {
                            onError(e);
                            mProgress.setVisibility(View.GONE);
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Main3Activity.this,Main3Activity.this.getString(R.string.sf__generic_error, exception.toString()),
                                Toast.LENGTH_LONG).show();
                        mProgress.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void sendRequest1(String soql1,final String l,final String q,final String p) throws UnsupportedEncodingException {

        RestRequest restRequest1 = RestRequest.getRequestForQuery(ApiVersionStrings.getVersionNumber(this), soql1);

        client.sendAsync(restRequest1, new RestClient.AsyncRequestCallback() {
            @Override
            public void onSuccess(RestRequest request1, final RestResponse result1) {
                result1.consumeQuietly(); // consume before going back to main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONArray records1 = result1.asJSONObject().getJSONArray("records");

                            tasksubject.add(records1.getJSONObject(0).getString("Name"));
                            tasksubject1.add(l);
                            tasksubject3.add(q);
                            tasksubject5.add(p);
                            tasksubject2.add(records1.getJSONObject(0).getString("MobilePhone"));
                            tasksubject4.add(records1.getJSONObject(0).getString("Id"));
                            adapter.notifyDataSetChanged();
                            mrec.invalidate();
                            mrec.setAdapter(adapter);

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
                        Toast.makeText(Main3Activity.this,Main3Activity.this.getString(R.string.sf__generic_error, exception.toString()),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onResume(RestClient client) {
        this.client=client;

    }
    @Override
    public void onItemClick(View view, int position) {

    }


    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {

            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main3, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Main3Activity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


    private void saveData(Map<String, Object> fields, final String id)  {

        RestRequest restRequest;
        RestRequest restRequest1;

        try {

            restRequest = RestRequest.getRequestForUpdate(getString(R.string.api_version),"Task", id, fields);
            //restRequest = RestRequest.getRequestForCreate(ApiVersionStrings.getVersionNumber(this),"Task",fields);
            //restRequest = RestRequest.getRequestForUpdate(getString(R.string.api_version), "Merchandise__c", id, fields);
        }
        catch (Exception e) {
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

                        }
                        catch (Exception e) {
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