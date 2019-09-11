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

public class SearchUserCityActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinnerCity;
    private Button buttonSubmit;
    private List<String> cityList;
    public static final String pArea="pArea";
    public static final String pCountry="pCountry";
    public static final String pCity = "pCity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_city);

        spinnerCity = (Spinner) findViewById(R.id.spinnerSelectCity);
        cityList = new ArrayList<String>();
        buttonSubmit = (Button) findViewById(R.id.buttonSubmitCity);

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
            String country = intent.getStringExtra(SearchUserAreaActivity.pCountry);
            String area = intent.getStringExtra(SearchUserAreaActivity.pArea);
            String city = spinnerCity.getSelectedItem().toString();

            Intent intent1 = new Intent(this, SearchUserLocalAreaActivity.class);
            intent1.putExtra(pCountry, country);
            intent1.putExtra(pArea, area);
            intent1.putExtra(pCity, city);

            startActivity(intent1);
        }
    }
    private void showUser(){
        Intent intent = getIntent();
        String country = intent.getStringExtra(SearchUserAreaActivity.pCountry);
        String area = intent.getStringExtra(SearchUserAreaActivity.pArea);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SERVICES").child(country).child(area);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cityList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = ds.getKey();
                    cityList.add(name);
                }
                ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(SearchUserCityActivity.this, android.R.layout.simple_spinner_item, cityList);
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCity.setAdapter(cityAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
