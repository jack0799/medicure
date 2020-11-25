package com.mytrail.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.SalesforceActivity;

public class calendarall extends SalesforceActivity implements MyRecyclerViewAdapter.ItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendarall);
    }

    @Override
    public void onResume(RestClient client) {

    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
