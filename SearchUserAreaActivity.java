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

public class SearchUserAreaActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner selectArea;
    private Button submitArea;
    private List<String> areaList;
    public static final String pArea="pArea";
    public static final String pCountry="pCountry";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_area);

        selectArea = (Spinner) findViewById(R.id.spinnerSelectArea);
        submitArea = (Button) findViewById(R.id.buttonSubmitArea);
        areaList = new ArrayList<String>();

        submitArea.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showUser();
    }

    @Override
    public void onClick(View v) {
        if(v == submitArea){

            Intent intent = getIntent();
            String country = intent.getStringExtra(SearchUserCountryActivity.pCountry);
            String area = selectArea.getSelectedItem().toString();

            if(area.equals("metro area")){
                Intent intent1 = new Intent(this, SearchUserCityActivity.class);
                intent1.putExtra(pArea, area);
                intent1.putExtra(pCountry, country);

                startActivity(intent1);
            }
            else{
                Toast.makeText(this, "Division Searching is Under Development", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void showUser(){
        Intent intent = getIntent();
        String country = intent.getStringExtra(SearchUserCountryActivity.pCountry);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SERVICES").child(country);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                areaList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = ds.getKey();
                    areaList.add(name);
                }
                ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(SearchUserAreaActivity.this, android.R.layout.simple_spinner_item, areaList);
                areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selectArea.setAdapter(areaAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
