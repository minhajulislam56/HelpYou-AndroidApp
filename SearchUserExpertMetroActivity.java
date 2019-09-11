package com.minhajisartisticgmail.helpyou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchUserExpertMetroActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinnerExpert;
    private Button buttonSubmit;
    private List<String> expertList;
    public static final String pArea="pArea";
    public static final String pCountry="pCountry";
    public static final String pCity = "pCity";
    public static final String pLocalArea="pLocalArea";
    public static final String pExpert = "pExpert";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_expert_metro);

        spinnerExpert = (Spinner) findViewById(R.id.spinnerSelectExpertMetro);
        expertList = new ArrayList<String>();
        buttonSubmit = (Button) findViewById(R.id.buttonSubmitExpertMetro);

        buttonSubmit.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showUser();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonSubmit){
            Intent intent = getIntent();
            String country = intent.getStringExtra(SearchUserLocalAreaActivity.pCountry);
            String area = intent.getStringExtra(SearchUserLocalAreaActivity.pArea);
            String city = intent.getStringExtra(SearchUserLocalAreaActivity.pCity);
            String localArea = intent.getStringExtra(SearchUserLocalAreaActivity.pLocalArea);
            String expert = spinnerExpert.getSelectedItem().toString();

            Intent intent1 = new Intent(this, ShowUsersActivity.class);
            intent1.putExtra(pCountry, country);
            intent1.putExtra(pArea, area);
            intent1.putExtra(pCity, city);
            intent1.putExtra(pLocalArea, localArea);
            intent1.putExtra(pExpert, expert);

            startActivity(intent1);
        }
    }
    private void showUser(){
        Intent intent = getIntent();
        String country = intent.getStringExtra(SearchUserLocalAreaActivity.pCountry);
        String area = intent.getStringExtra(SearchUserLocalAreaActivity.pArea);
        String city = intent.getStringExtra(SearchUserLocalAreaActivity.pCity);
        String localArea = intent.getStringExtra(SearchUserLocalAreaActivity.pLocalArea);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SERVICES").child(country).child(area).child(city).child(localArea);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                expertList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = ds.getKey();
                  expertList.add(name);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchUserExpertMetroActivity.this, android.R.layout.simple_spinner_item, expertList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerExpert.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
