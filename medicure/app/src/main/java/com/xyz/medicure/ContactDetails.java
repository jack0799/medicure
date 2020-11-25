package com.xyz.medicure;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactDetails extends AppCompatActivity {
    private DatabaseReference mDatabase1;
    private DatabaseReference mDatabase2;
    private DatabaseReference mDatabase3;
    private TextView mNameText;
    private TextView mNumberText;
    private TextView mAddressText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Contact").child("Name");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Contact").child("Number");
        mDatabase3 = FirebaseDatabase.getInstance().getReference().child("Contact").child("Address");
        mNameText = (TextView)findViewById(R.id.NameText);
        mNumberText = (TextView)findViewById(R.id.NumberText);
        mAddressText = (TextView)findViewById(R.id.AddressText);
        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String n = dataSnapshot.getValue(String.class);
                mNameText.setText("Name:- "+ n);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String no = dataSnapshot.getValue(String.class);
                mNumberText.setText("Number:- " + no);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String add = dataSnapshot.getValue(String.class);
                mAddressText.setText("Address:- " +add);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
