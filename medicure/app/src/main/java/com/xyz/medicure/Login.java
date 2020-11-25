package com.xyz.medicure;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private AuthStateListener mAuthListener;
    private EditText memail;
    private Button mlogin;
    private EditText mpassword;
    private Button msignup;
    private Button motp;
    String codesent;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        this.memail = (EditText) findViewById(R.id.Email);
        this.mpassword = (EditText) findViewById(R.id.pswd);
        this.mlogin = (Button) findViewById(R.id.Login);
        this.msignup = (Button) findViewById(R.id.signup);
        motp=(Button) findViewById(R.id.otp);
        motp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = "+91" + memail.getText().toString().trim();
                DatabaseReference mDatabase3= mDatabase.child(number).child("flag");

                mDatabase3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String n = dataSnapshot.getValue(String.class);

                        if(n!=null){

                            sendveri();
                            mlogin.setOnClickListener(new OnClickListener() {
                                public void onClick(View view) {
                                    onCodesent();
                                }
                            });

                        }
                        else{


                            Toast.makeText(getApplicationContext(),"First Register",Toast.LENGTH_LONG).show();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        this.mAuthListener = new AuthStateListener() {
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Login.this.startActivity(new Intent(Login.this, Home.class));
                }
            }
        };

        this.msignup.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Login.this.startActivity(new Intent(Login.this, Register.class));
            }
        });
        if (!checkIfAlreadyhavePermission()) {
            requestForSpecificPermission();
        }
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Log.d(TAG, "onVerificationCompleted:");
           // Toast.makeText(Login.this, "Verification Complete", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Login.this, "Verification Failed", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codesent =s;
            Toast.makeText(Login.this, "OTP Sent", Toast.LENGTH_SHORT).show();
            motp.setVisibility(View.GONE);
            Toast.makeText(Login.this, "You can resend OTP after 60 seconds", Toast.LENGTH_SHORT).show();
            new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {
                   // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                   // mTextField.setText("done!");
                    motp.setVisibility(View.VISIBLE);
                }
            }.start();

        }
    };

    private void onCodesent(){

        final String number = "+91" + memail.getText().toString().trim();

        final String codesent1 = mpassword.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent, codesent1);
        signInWithPhoneAuthCredential(credential,number);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential,final String number) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String user_id = mAuth.getCurrentUser().getUid();


                           // mProgress.dismiss();
                            Intent maineIntent = new Intent(Login.this,Login.class);
                            maineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(maineIntent);
                            Toast.makeText(getApplicationContext(),"Successfully Logged In ",Toast.LENGTH_LONG).show();

                            //FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(getApplicationContext(),"Wrong OTP",Toast.LENGTH_LONG).show();

                            return;

//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                // The verification code entered was invalid
//                                Toast.makeText(getApplicationContext(),"The verification code entered was invalid",Toast.LENGTH_LONG).show();
//                                mProgress.dismiss();
//                                return;
//                            }
                        }
                    }
                });
    }
    private void sendveri(){

        final String number = "+91" + memail.getText().toString().trim();
        // String number = "+917974905659";
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,60, TimeUnit.SECONDS, this, mCallbacks);

    }
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG,Manifest.permission.CALL_PHONE,Manifest.permission.INTERNET}, 101);

    }
    private boolean checkIfAlreadyhavePermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }




    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this.mAuth.addAuthStateListener(this.mAuthListener);
    }

    /* access modifiers changed from: private */
    public void startSignIn() {
        String email = this.memail.getText().toString();
        String password = this.mpassword.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
        } else {
            this.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(Login.this, "Sign In Problem", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}