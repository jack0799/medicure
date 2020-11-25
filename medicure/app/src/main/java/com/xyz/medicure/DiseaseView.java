package com.xyz.medicure;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DiseaseView extends AppCompatActivity {
    private RecyclerView mdiseaseList;
    private EditText msearchdisease;
    private ImageView msearch;

    private DatabaseReference mDatabase;
    public static String x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_view);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Database").child("Diseases");

        mdiseaseList = (RecyclerView) findViewById(R.id.diseaseList);
        mdiseaseList.setHasFixedSize(true);
        mdiseaseList.setLayoutManager(new LinearLayoutManager(this));
        msearchdisease = (EditText)findViewById(R.id.searchdisease);
        msearch = (ImageView)findViewById(R.id.search);
        msearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = msearchdisease.getText().toString();
                if(!TextUtils.isEmpty(name)){
                name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();}
                searchd(name);
            }
        });
    }

    private void searchd(String name) {

        Query firebaseSearchQuery = mDatabase.orderByChild("Disease").startAt(name).endAt(name + "\uf8ff");
        FirebaseRecyclerAdapter<Disease , DiseaseViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Disease, DiseaseViewHolder>(
                Disease.class,
                R.layout.diseas_row,
                DiseaseViewHolder.class,
                firebaseSearchQuery


        ) {

            @Override
            protected void populateViewHolder(DiseaseViewHolder viewHolder, final Disease model, int position) {

                viewHolder.setDisease(model.getDisease());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        x = model.getDisease().toString();

                        Intent intent = new Intent(DiseaseView.this, SymptomView.class);
                        startActivity(intent);

                    }
                });
            }
        };
        mdiseaseList.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();


        Query query = mDatabase.orderByChild("Disease");
        FirebaseRecyclerAdapter<Disease , DiseaseViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Disease, DiseaseViewHolder>(
                Disease.class,
                R.layout.diseas_row,
                DiseaseViewHolder.class,
                query


        ) {

            @Override
            protected void populateViewHolder(DiseaseViewHolder viewHolder, final Disease model, int position) {

                viewHolder.setDisease(model.getDisease());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        x = model.getDisease().toString();

                        Intent intent = new Intent(DiseaseView.this, SymptomView.class);
                        startActivity(intent);

                    }
                });
            }
        };
        mdiseaseList.setAdapter(firebaseRecyclerAdapter);



    }

    public static class DiseaseViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public DiseaseViewHolder(View itemView) {
            super(itemView);

            mView=itemView;
        }
        public  void setDisease(String Disease){

            TextView postDisease = (TextView) mView.findViewById(R.id.diseaseDisplay);
            postDisease.setText(Disease);

        }
    }
}
