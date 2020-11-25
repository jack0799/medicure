package com.mytrail.android;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.Toast;
import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.SalesforceActivity;
import org.json.JSONArray;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Calendar extends SalesforceActivity implements MyRecyclerViewAdapter.ItemClickListener,NavigationDrawerFragment.NavigationDrawerCallbacks {
    private CalendarView mcalendar;
    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    private RestClient client;
    private ProgressDialog mProgress;
    ArrayList<String> tasksubject = new ArrayList<>();
    ArrayList<String> tasksubject1 = new ArrayList<>();
    ArrayList<String> tasksubject2 = new ArrayList<>();
    ArrayList<String> tasksubject3 = new ArrayList<>();
    ArrayList<String> tasksubject4 = new ArrayList<>();
    ArrayList<String> tasksubject5 = new ArrayList<>();
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private MediaRecorder recorder = null;
    private int currentFormat = 0;
    private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4,
            MediaRecorder.OutputFormat.THREE_GPP };
    private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4,
            AUDIO_RECORDER_FILE_EXT_3GP };
    AudioManager audioManager;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Fade());
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setEnterTransition(new Fade());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        actionBar.setTitle(Html.fromHtml("<font color='#2699FB'>Sales Companion </font>"));
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout1));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, Calendar.PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {

        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                Intent maineIntent2 = new Intent(Calendar.this, Main3Activity.class);
                maineIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(maineIntent2,ActivityOptions.makeSceneTransitionAnimation(Calendar.this).toBundle());
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
//                Intent maineIntent = new Intent(Calendar.this, Calendar.class);
//                maineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(maineIntent);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                Intent maineIntent1 = new Intent(Calendar.this, NCT.class);
                maineIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(maineIntent1,ActivityOptions.makeSceneTransitionAnimation(Calendar.this).toBundle());
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
    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
            }
            return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
        }
        private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Toast.makeText(Calendar.this,
                    "Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
            }
            };
    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Toast.makeText(Calendar.this,
                    "Warning: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
            }
            };
    public void onResume() {
        if (!checkIfAlreadyhavePermission()) {
            requestForSpecificPermission();
        }
        final TelephonyManager telephonyManager =
                (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        mcalendar=findViewById(R.id.calendarView);

    mProgress = new ProgressDialog(this);
    recyclerView = findViewById(R.id.tasklist);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, tasksubject, tasksubject1,tasksubject3, new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Uri u = Uri.parse("tel:" + tasksubject2.get(position));
                Intent i = new Intent(Intent.ACTION_CALL, u);
                try
                {
                    startActivity(i);
                    //TimeUnit.SECONDS.sleep(1);
                    Intent maineIntent1 = new Intent(Calendar.this,logacall.class);
                    maineIntent1.putExtra("ID",tasksubject5.get(position));
                    startActivity(maineIntent1,ActivityOptions.makeSceneTransitionAnimation(Calendar.this).toBundle());

                }
                catch (SecurityException s)
                {
                }

                PhoneStateListener callStateListener = new PhoneStateListener() {
                    public void onCallStateChanged(int state, String incomingNumber)
                    {
                        if(state==TelephonyManager.CALL_STATE_RINGING){
                            Toast.makeText(getApplicationContext(),"Phone Is Riging",
                                    Toast.LENGTH_LONG).show();
                        }
                        if(state==TelephonyManager.CALL_STATE_OFFHOOK){

//                                    try {
//                                        recorder = new MediaRecorder();
//                                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                                        //recorder.setOutputFormat(output_formats[currentFormat]);
//                                        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//                                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                                        recorder.setOutputFile(getFilename());
//                                        recorder.setOnErrorListener(errorListener);
//                                        recorder.setOnInfoListener(infoListener);
//                                        recorder.prepare();
//                                        recorder.start();
//                                    } catch (IllegalStateException e) {
//                                        Log.e("REDORDING :: ",e.getMessage());
//                                        e.printStackTrace();
//                                    } catch (IOException e) {
//                                        Log.e("REDORDING :: ",e.getMessage());
//                                        e.printStackTrace();
//                                    }
                            Toast.makeText(getApplicationContext(),"Phone Is on call",
                                    Toast.LENGTH_LONG).show();
                        }

                        if(state==TelephonyManager.CALL_STATE_IDLE){

//                                    recorder.stop();
//                                    recorder.reset();
//                                    recorder.release();
//                                    recorder = null;
                            Toast.makeText(getApplicationContext(),"Phone Is not on call",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                };
                telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);

            }
        }, new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent maineIntent = new Intent(Calendar.this,Popmssg.class);
                maineIntent.putExtra("Number",tasksubject2.get(position));
                startActivity(maineIntent,ActivityOptions.makeSceneTransitionAnimation(Calendar.this).toBundle());

            }
        },new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent maineIntent = new Intent(Calendar.this,caseraise.class);
                maineIntent.putExtra("ID",tasksubject4.get(position));
                startActivity(maineIntent,ActivityOptions.makeSceneTransitionAnimation(Calendar.this).toBundle());

            }
        });

        adapter.setClickListener(this);
        super.onResume();

        mcalendar.setOnDateChangeListener (new CalendarView.OnDateChangeListener()  {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                int month1=month+1;
                tasksubject1.clear();
                tasksubject.clear();
                tasksubject3.clear();
                tasksubject2.clear();
                tasksubject4.clear();
                recyclerView.setAdapter(adapter);
                String k = client.getClientInfo().userId;
                mProgress.setMessage("Fetching Data....");
                mProgress.show();
                String date = year + "-" +(month1<10?("0"+month1):(month1)) + "-" +(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth));
                try {
                    sendRequest("SELECT Subject,status,WhoId,id FROM Task WHERE ActivityDate=" +date+"and ownerid='"+k+"'" );
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    mProgress.dismiss();
                }
            }
        });

    }
        @Override
        public void onResume(RestClient client) {
            this.client = client;
        }

    private boolean checkIfAlreadyhavePermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(this,
                Manifest.permission.PROCESS_OUTGOING_CALLS)
                != PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(this,
                Manifest.permission.MODIFY_AUDIO_SETTINGS)
                != PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG,Manifest.permission.CALL_PHONE,Manifest.permission.READ_SMS,Manifest.permission.PROCESS_OUTGOING_CALLS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,Manifest.permission.MODIFY_AUDIO_SETTINGS,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAPTURE_AUDIO_OUTPUT}, 101);
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
                                    for (int i = 0; i < records.length(); i++) {
                                        String l=records.getJSONObject(i).getString("WhoId");
                                        String l1=records.getJSONObject(i).getString("Subject");
                                        String l2=records.getJSONObject(i).getString("Status");
                                        String l3=records.getJSONObject(i).getString("Id");
                                        sendRequest1("SELECT id,name,mobilephone FROM contact where id='"+l+"'",l1,l2,l3);
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
                                Toast.makeText(Calendar.this,Calendar.this.getString(R.string.sf__generic_error, exception.toString()),
                                        Toast.LENGTH_LONG).show();
                                mProgress.dismiss();
                            }
                        });
                    }
                        });
                /*public void is{
                functionality
                nn
                fragment


                }
                 */
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
                                                recyclerView.invalidate();
                                                recyclerView.setAdapter(adapter);

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
                                            Toast.makeText(Calendar.this,Calendar.this.getString(R.string.sf__generic_error, exception.toString()),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });
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
        public static Calendar.PlaceholderFragment newInstance(int sectionNumber) {

            Calendar.PlaceholderFragment fragment = new Calendar.PlaceholderFragment();
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
            ((Calendar) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}