package com.xyz.medicure;


import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetails extends AppCompatActivity {
    private DatabaseReference mDatabase1;
    private DatabaseReference mDatabase2;
    private DatabaseReference mDatabase3;
    private FirebaseAuth mAuth;
    private TextView mnamed;
    private TextView mnumberd;
    private TextView maddressd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();



       mnamed=(TextView)findViewById(R.id.named);
       mnumberd=(TextView)findViewById(R.id.numberd);
       maddressd=(TextView)findViewById(R.id.addressd);
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("name");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("number");
        mDatabase3 = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("address");
        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String n = dataSnapshot.getValue(String.class);
                mnamed.setText("Name:- "+ n);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nu = dataSnapshot.getValue(String.class);
                mnumberd.setText("Number:- "+ nu);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String a = dataSnapshot.getValue(String.class);
                maddressd.setText("Address:- "+ a);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
