package com.mytrail.android;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NCT extends SalesforceActivity implements adapter2.ItemClickListener,NavigationDrawerFragment.NavigationDrawerCallbacks {


    private RestClient client;
    adapter2 adapter;
    RecyclerView recyclerView;
    private ProgressDialog mProgress;
    ArrayList<String> tasksubject = new ArrayList<>();
    ArrayList<String> tasksubject1 = new ArrayList<>();
    ArrayList<String> tasksubject2 = new ArrayList<>();
    ArrayList<String> tasksubject3 = new ArrayList<>();
    ArrayList<String> tasksubject4 = new ArrayList<>();
    ArrayList<String> tasksubject5 = new ArrayList<>();
    ArrayList<String> tasksubject6 = new ArrayList<>();
    private Button mbutton3;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setExitTransition(new Fade());
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setEnterTransition(new Fade());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nct);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout2));

    }


    @Override
    public void onBackPressed() {
        Intent maineIntent = new Intent(NCT.this, Main3Activity.class);

        startActivity(maineIntent);


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
    @Override
    public void onStart() {
        super.onResume();
        final String k = client.getClientInfo().userId;
        tasksubject1.clear();
        tasksubject.clear();
        tasksubject3.clear();

        recyclerView = findViewById(R.id.tasklist1);
        mProgress = new ProgressDialog(this);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new adapter2(this, tasksubject, tasksubject1, tasksubject3, new adapter2.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent maineIntent = new Intent(NCT.this, calendarpop.class);
                maineIntent.putExtra("ID", tasksubject5.get(position));
                maineIntent.putExtra("Subject", tasksubject1.get(position));
                maineIntent.putExtra("Name", tasksubject6.get(position));
                maineIntent.putExtra("UID", k);
                startActivity(maineIntent,ActivityOptions.makeSceneTransitionAnimation(NCT.this).toBundle());

            }
        });
        adapter.setClickListener1(this);


        //recyclerView.setAdapter(adapter);

        mProgress.setMessage("Fetching Data....");
        mProgress.show();


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
        String strDate = mdformat.format(calendar.getTime());
        // mtest1.setText(strDate);
        try {
            tasksubject1.clear();
            tasksubject.clear();
            tasksubject3.clear();
            sendRequest("SELECT Subject,status,id,WhoId,activitydate FROM Task WHERE ActivityDate<" + strDate + "and status ='Not Started' and ownerid='" + k + "' and rescheduled__c='no'");
            //  sendRequest("SELECT Subject,status,WhoId FROM Task " );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void sendRequest(String soql) throws UnsupportedEncodingException {

        RestRequest restRequest = RestRequest.getRequestForQuery(ApiVersionStrings.getVersionNumber(this), soql);
        // RestRequest restreques1 = RestRequest.getRequestForUpdate(ApiVersionStrings.getVersionNumber((this),"Query"));
        //RestRequest restRequest2 =RestRequest.getRequestForUpdate()


        client.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
            @Override
            public void onSuccess(RestRequest request, final RestResponse result) {
                result.consumeQuietly(); // consume before going back to main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONArray records = result.asJSONObject().getJSONArray("records");
                            //  tasksubject.clear();
                            //tasksubject1.clear();
                            tasksubject1.clear();
                            tasksubject.clear();
                            tasksubject3.clear();
                            for (int i = 0; i < records.length(); i++) {

                                // tasksubject.add(records.getJSONObject(i).getString("WhoId"));
                                String l = records.getJSONObject(i).getString("WhoId");
                                String l1 = records.getJSONObject(i).getString("Subject");
                                String l2 = records.getJSONObject(i).getString("Status");
                                String l3 = records.getJSONObject(i).getString("Id");
                                //tasksubject.add(records.getJSONObject(i).getString("Status"));

                                sendRequest1("SELECT id,name,mobilephone FROM contact where id='" + l + "'", l1, l2, l3, l);
                                //adapter.notifyDataSetChanged();
                                //recyclerView.invalidate();
                                //recyclerView.setAdapter(adapter);
                                //  sendRequest1("SELECT id,name,mobilephone FROM contact where id='"+l+"'",l1,l2);
                            }

                            mProgress.dismiss();

                        } catch (Exception e) {
                            onError(e);
                            mProgress.dismiss();
                        }
                    }
                });
            }


            @Override
            public void onError(final Exception exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NCT.this, NCT.this.getString(R.string.sf__generic_error, exception.toString()),
                                Toast.LENGTH_LONG).show();
                        mProgress.dismiss();
                    }
                });
            }
        });
    }


    private void sendRequest1(String soql1, final String l, final String q, final String p, final String o) throws UnsupportedEncodingException {

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
                            //  tasksubject.clear();
                            //  tasksubject1.clear();
                            // for (int i = 0; i < records.length(); i++) {


                            //tasksubject.add(records.getJSONObject(i).getString("MobilePhone"));
                            // if(!l.equals(null)){


                            tasksubject.add(records1.getJSONObject(0).getString("Name"));
                            tasksubject1.add(l);
                            tasksubject3.add(q);
                            tasksubject5.add(p);
                            tasksubject6.add(o);
                            tasksubject2.add(records1.getJSONObject(0).getString("MobilePhone"));
                            tasksubject4.add(records1.getJSONObject(0).getString("Id"));
                            adapter.notifyDataSetChanged();
                            recyclerView.invalidate();
                            recyclerView.setAdapter(adapter);


                            //}
                        } catch (Exception e) {
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
                        Toast.makeText(NCT.this, NCT.this.getString(R.string.sf__generic_error, exception.toString()),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    @Override
    public void onResume(RestClient client) {
        // Keeping reference to rest client
        this.client = client;


    }

    @Override
    public void onItemClick1(View view, int position) {

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, NCT.PlaceholderFragment.newInstance(position + 1))
                .commit();

    }

    public void onSectionAttached(int number) {

        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                Intent maineIntent2 = new Intent(NCT.this, Main3Activity.class);
                maineIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(maineIntent2,ActivityOptions.makeSceneTransitionAnimation(NCT.this).toBundle());
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                Intent maineIntent = new Intent(NCT.this, com.mytrail.android.Calendar.class);
                maineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(maineIntent,ActivityOptions.makeSceneTransitionAnimation(NCT.this).toBundle());
                break;
            case 3:

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
        public static NCT.PlaceholderFragment newInstance(int sectionNumber) {

            NCT.PlaceholderFragment fragment = new NCT.PlaceholderFragment();
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
            ((NCT) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}