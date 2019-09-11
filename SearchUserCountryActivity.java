package com.minhajisartisticgmail.helpyou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class SearchUserCountryActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner selectCountry;
    private Button submitButton;
    private List<String> countryList;
    public static final String pCountry = "pCountry";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_country);

        selectCountry = (Spinner) findViewById(R.id.spinnerSelectCountry);
        submitButton = (Button) findViewById(R.id.buttonSubmitCountry);
        countryList = new ArrayList<String>();

        submitButton.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        showUser();
    }

    @Override
    public void onClick(View v) {
        if(v == submitButton){
            String selectedCountry = selectCountry.getSelectedItem().toString();

            Intent intent = new Intent(this, SearchUserAreaActivity.class);
            intent.putExtra(pCountry, selectedCountry);

            startActivity(intent);
        }
    }
    private void showUser(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SERVICES");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countryList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = ds.getKey();
                    countryList.add(name);
                }
                ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(SearchUserCountryActivity.this, android.R.layout.simple_spinner_item, countryList);
                countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selectCountry.setAdapter(countryAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
