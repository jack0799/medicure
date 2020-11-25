package com.xyz.medicure;

import android.content.Intent;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.xyz.medicure.DiseaseView.x;

public class SymptomView extends AppCompatActivity {
    private RecyclerView msymptomList;

    private DatabaseReference mDatabase;
    public static String y;
    public static String a;
    private ImageView mcustomsubmit;
    private EditText mcustomsymptom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_view);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Database").child("Symptoms").child(x);

        msymptomList = (RecyclerView) findViewById(R.id.SymptomList);
        msymptomList.setHasFixedSize(true);
        msymptomList.setLayoutManager(new LinearLayoutManager(this));
        mcustomsubmit= (ImageView) findViewById(R.id.customsubmit);
        mcustomsymptom= (EditText) findViewById((R.id.customsymptom));
        mcustomsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                a = mcustomsymptom.getText().toString();
                if(TextUtils.isEmpty(a)){
                    Toast.makeText(getApplicationContext(),"Please Enter Or Select",Toast.LENGTH_SHORT).show();
                }
                else {
                    y = null;

                    Intent intent = new Intent(SymptomView.this, Final.class);
                    startActivity(intent);
                    mcustomsymptom.setText("");

                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Symptom , SymptomViewHolder>firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Symptom, SymptomViewHolder>(
                Symptom.class,
                R.layout.symptom_row,
                SymptomViewHolder.class,
                mDatabase


        ) {
            @Override
            protected void populateViewHolder(SymptomViewHolder viewHolder,final Symptom model, int position) {

                viewHolder.setSymptom(model.getSymptom());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        a=null;
                        y = model.getSymptom().toString();

                        Intent intent = new Intent(SymptomView.this, Final.class);
                        startActivity(intent);
                    }
                });

            }
        };

        msymptomList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class SymptomViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public SymptomViewHolder(View itemView) {
            super(itemView);

            mView=itemView;
        }
        public  void setSymptom(String Symptom){

            TextView postSymptom = (TextView) mView.findViewById(R.id.symptomDisplay);
            postSymptom.setText(Symptom);

        }
    }
}
