package com.xyz.medicure;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {

    private EditText mName;
    private EditText mNumber;
    private EditText mAddress;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mvcode;
    private ImageView mbar;
    private Button mSignUp;
    private Button mverify;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    private static final String TAG = "MainActivity";
    String codesent;
    private DatabaseReference mDatabase3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mProgress = new ProgressDialog(this);
        mName = (EditText) findViewById(R.id.fName);
        mNumber = (EditText) findViewById(R.id.mob);
        mAddress = (EditText) findViewById(R.id.address);
        mEmail = (EditText) findViewById(R.id.Email);
        //mPassword = (EditText) findViewById(R.id.pwd);
        mSignUp = (Button) findViewById(R.id.SignUp);
        mbar=(ImageView)findViewById(R.id.bar);
        mvcode=(EditText) findViewById(R.id.vcode);
        mverify=(Button) findViewById(R.id.verify);
        mbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent maineIntent = new Intent(Register.this,Login.class);
                maineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(maineIntent);
            }
        });




            mverify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(TextUtils.isEmpty(mName.getText().toString()) || TextUtils.isEmpty(mNumber.getText().toString())||TextUtils.isEmpty(mAddress.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Fields are empty",Toast.LENGTH_LONG).show();

                    }
                    else{
                            String number = "+91" + mNumber.getText().toString().trim();
                            mDatabase3= mDatabase.child(number).child("flag");
                            mDatabase3.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String n = dataSnapshot.getValue(String.class);

                                    if(n!=null){
                                        Toast.makeText(getApplicationContext(),"Account already exists",Toast.LENGTH_LONG).show();


                                    }
                                    else{
                                        sendveri();

                                        mSignUp.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                onCodesent();

                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                    }
                }
            });


    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted:");
            //Toast.makeText(Register.this, "Verification Complete", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Register.this, "Verification Failed", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codesent =s;
            Toast.makeText(Register.this, "OTP Sent", Toast.LENGTH_SHORT).show();
            mverify.setVisibility(View.GONE);
            Toast.makeText(Register.this, "You can resend OTP after 60 seconds", Toast.LENGTH_SHORT).show();
            new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {
                    // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    // mTextField.setText("done!");
                    mverify.setVisibility(View.VISIBLE);
                }
            }.start();

        }
    };

    private void onCodesent(){
        final String name = mName.getText().toString().trim();
        final String number = "+91" + mNumber.getText().toString().trim();
        final String address = mAddress.getText().toString().trim();
        final String codesent1 = mvcode.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent, codesent1);
        signInWithPhoneAuthCredential(credential,name,number,address);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final String name,final String number,final String address) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                           // user.sendEmailVerification();
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = mDatabase.child(user_id);
                            DatabaseReference current_user_db1 = mDatabase.child(number);
                            current_user_db1.child("flag").setValue("1");
                            current_user_db.child("name").setValue(name);
                            current_user_db.child("number").setValue(number);
                            current_user_db.child("address").setValue(address);

                            mProgress.dismiss();
                            Intent maineIntent = new Intent(Register.this,Login.class);
                            maineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(maineIntent);
                            Toast.makeText(getApplicationContext(),"Verify your Email ID",Toast.LENGTH_LONG).show();

                            //FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(getApplicationContext(),"Wrong OTP",Toast.LENGTH_LONG).show();
                            mProgress.dismiss();
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

        final String number = "+91" + mNumber.getText().toString().trim();
       // String number = "+917974905659";
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,60, TimeUnit.SECONDS, this, mCallbacks);

    }

    private void StartRegister() {
        final String name = mName.getText().toString().trim();
        final String number = "+91" + mNumber.getText().toString().trim();
        final String address = mAddress.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)){
            mProgress.setMessage("Signing UP....");
            mProgress.show();



//            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful()){
//                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                        user.sendEmailVerification();
//                        String user_id = mAuth.getCurrentUser().getUid();
//                        DatabaseReference current_user_db = mDatabase.child(user_id);
//                        current_user_db.child("name").setValue(name);
//                        current_user_db.child("number").setValue(number);
//                        current_user_db.child("address").setValue(address);
//                        mProgress.dismiss();
//                        Intent maineIntent = new Intent(Register.this,Login.class);
//                        maineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(maineIntent);
//                        Toast.makeText(getApplicationContext(),"Verify your Email ID",Toast.LENGTH_LONG).show();
//                    }
//                    else{
//                        Toast.makeText(getApplicationContext(),"Check Email and Password should contain at least one letter",Toast.LENGTH_LONG).show();
//                        mProgress.dismiss();
//                        return;
//                    }
//                }
//            });
        }
        else{
            Toast.makeText(getApplicationContext(),"Fields are empty",Toast.LENGTH_SHORT).show();
            return;

        }
    }
}
