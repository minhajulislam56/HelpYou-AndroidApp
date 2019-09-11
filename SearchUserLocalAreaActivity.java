package com.minhajisartisticgmail.helpyou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchUserLocalAreaActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinnerLocalArea;
    private Button buttonSubmit;
    private List<String> localAreaList;
    public static final String pArea="pArea";
    public static final String pCountry="pCountry";
    public static final String pCity = "pCity";
    public static final String pLocalArea="pLocalArea";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_local_area);

        spinnerLocalArea = (Spinner) findViewById(R.id.spinnerSelectLocalArea);
        localAreaList = new ArrayList<String>();
        buttonSubmit = (Button) findViewById(R.id.buttonSubmitLocalArea);

        buttonSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonSubmit){
            Intent intent = getIntent();
            String country = intent.getStringExtra(SearchUserCityActivity.pCountry);
            String area = intent.getStringExtra(SearchUserCityActivity.pArea);
            String city = intent.getStringExtra(SearchUserCityActivity.pCity);
            String localArea = spinnerLocalArea.getSelectedItem().toString();

            Intent intent1 = new Intent(this, SearchUserExpertMetroActivity.class);
            intent1.putExtra(pCountry,country);
            intent1.putExtra(pArea, area);
            intent1.putExtra(pCity, city);
            intent1.putExtra(pLocalArea, localArea);

            startActivity(intent1);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showUser();
    }
    private void showUser(){
        Intent intent = getIntent();
        String country = intent.getStringExtra(SearchUserCityActivity.pCountry);
        String area = intent.getStringExtra(SearchUserCityActivity.pArea);
        String city = intent.getStringExtra(SearchUserCityActivity.pCity);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SERVICES").child(country).child(area).child(city);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                localAreaList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = ds.getKey();
                    localAreaList.add(name);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchUserLocalAreaActivity.this, android.R.layout.simple_spinner_item, localAreaList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerLocalArea.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
