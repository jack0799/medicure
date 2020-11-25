package com.xyz.medicure;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.xyz.medicure.DiseaseView.x;
import static com.xyz.medicure.SymptomView.a;
import static com.xyz.medicure.SymptomView.y;

public class Final extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseGet1;
    private DatabaseReference mDatabaseGet2;
    private DatabaseReference mDatabaseGet3;
    private DatabaseReference mDatabaseSet;
    private DatabaseReference mDatabaseSet4;
    private Button mhomeback;
    private Button mcontactus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        mAuth = FirebaseAuth.getInstance();
        mhomeback = (Button)findViewById(R.id.homeback);
        mhomeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Final.this, Home.class);
                startActivity(intent);
            }
        });

        mcontactus = (Button) findViewById(R.id.contactus);
        mcontactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Final.this, ContactDetails.class);
                startActivity(intent);

            }
        });

        String user_id = mAuth.getCurrentUser().getUid();

        mDatabaseGet1 =  FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("name");
        mDatabaseGet2 =  FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("number");
        mDatabaseGet3 =  FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("address");
        mDatabaseSet = FirebaseDatabase.getInstance().getReference().child("Problems").child(user_id);
        mDatabaseSet4 = FirebaseDatabase.getInstance().getReference().child("ByDisease").child("ByDiseasedetail").child(x).child(user_id);
        mDatabaseGet1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                mDatabaseSet.child("name").setValue(name);
                mDatabaseSet4.child("name").setValue(name);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabaseGet2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String number = dataSnapshot.getValue(String.class);
                mDatabaseSet.child("number").setValue(number);
                mDatabaseSet4.child("number").setValue(number);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabaseGet3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String address = dataSnapshot.getValue(String.class);
                mDatabaseSet.child("address").setValue(address);
                mDatabaseSet4.child("address").setValue(address);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabaseSet.child("Disease").setValue(x);
        if(y==null){
            mDatabaseSet4.child("symptom").setValue(a);
            mDatabaseSet.child("symptom").setValue(a);


        }
        else {
            mDatabaseSet.child("symptom").setValue(y);

            mDatabaseSet4.child("symptom").setValue(y);
        }
        mDatabaseSet4.child("Disease").setValue(x);


    }

}
