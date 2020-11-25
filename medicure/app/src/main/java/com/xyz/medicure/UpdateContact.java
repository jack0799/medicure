package com.xyz.medicure;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateContact extends AppCompatActivity {

    private EditText mName;
    private EditText mNumber;
    private EditText mAddress;
    private Button mSignUp;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);
        mName = (EditText) findViewById(R.id.fName1);
       // mNumber = (EditText) findViewById(R.id.mob1);
        mAddress = (EditText) findViewById(R.id.address1);
        mSignUp = (Button) findViewById(R.id.SignUp1);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                StartRegister();

            }
        });
    }

    private void StartRegister() {


        final String name = mName.getText().toString().trim();
        //final String number = mNumber.getText().toString().trim();
        final String address = mAddress.getText().toString().trim();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(address))
        {
            String user_id = mAuth.getCurrentUser().getUid();
            DatabaseReference current_user_db = mDatabase.child(user_id);
            current_user_db.child("name").setValue(name);
            //current_user_db.child("number").setValue(number);
            current_user_db.child("address").setValue(address);
            Toast.makeText(getApplicationContext(),"Details Updated",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(UpdateContact.this, Home.class);
            startActivity(intent);





        }

        else{
            Toast.makeText(getApplicationContext(),"Check the Fields",Toast.LENGTH_LONG).show();

            return;
        }







    }
}
