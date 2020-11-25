/*
 * Copyright (c) 2012-present, salesforce.com, inc.
 * All rights reserved.
 * Redistribution and use of this software in source and binary forms, with or
 * without modification, are permitted provided that the following conditions
 * are met:
 * - Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * - Neither the name of salesforce.com, inc. nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission of salesforce.com, inc.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.mytrail.android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Main activity
 */
public class MainActivity extends SalesforceActivity {

	private RestClient client;


	private Button mtesting;
	private Button mbutton3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup view
		setContentView(R.layout.main);
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
	public void onResume() {
		if (!checkIfAlreadyhavePermission()) {
			requestForSpecificPermission();
		}
		mtesting=findViewById(R.id.testing);
		mbutton3=findViewById(R.id.button3);
		mtesting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


				Intent maineIntent = new Intent(MainActivity.this, Mainscrean.class);
				maineIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(maineIntent);
				//finish();
			}
		});
		super.onResume();


		super.onResume();


	}



    public void onLogoutClick(View v) {
		SalesforceSDKManager.getInstance().logout(this);
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






	@Override
	public void onResume(RestClient client) {
		this.client=client;
	}
}
