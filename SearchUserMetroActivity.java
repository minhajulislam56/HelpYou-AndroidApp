package com.minhajisartisticgmail.helpyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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

public class SearchUserMetroActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner countryMetro, cityMetro, localAreaMetro, expertMetro;
    private List<String> countryListMetro, cityListMetro, localAreaListMetro, expertListMetro;
    private DatabaseReference databaseReference;
    private Button viewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_metro);

        countryMetro = (Spinner) findViewById(R.id.spinnerCountryMetro);
        cityMetro = (Spinner) findViewById(R.id.spinnerCityMetro);
        localAreaMetro = (Spinner) findViewById(R.id.spinnerLocalAreaMetro);
        expertMetro = (Spinner) findViewById(R.id.spinnerExpertMetro);

        viewButton = (Button) findViewById(R.id.viewButtonMetro);

        countryListMetro = new ArrayList<String>();
        cityListMetro = new ArrayList<String>();
        localAreaListMetro = new ArrayList<String>();
        expertListMetro = new ArrayList<String>();

        databaseReference = FirebaseDatabase.getInstance().getReference("SERVICES");

        /****** AVAILABLE COUNTRIES ******/
        loadCountries();

        /****** CITY LIST ******/
        countryMetro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentCountry, View view, final int positionCountry, long id) {

                databaseReference.child(parentCountry.getItemAtPosition(positionCountry).toString().toLowerCase()).child("metro area").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        cityListMetro.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            String temp = ds.getKey();
                            cityListMetro.add(temp.toUpperCase());
                        }

                        ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(SearchUserMetroActivity.this, android.R.layout.simple_spinner_item, cityListMetro);
                        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        cityMetro.setAdapter(adapterCity);

                        /****** LOCAL AREA LIST ******/

                        cityMetro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentCity, View view, int positionCity, long id) {

                                databaseReference.child(countryMetro.getSelectedItem().toString().toLowerCase()).child("metro area")
                                        .child(parentCity.getItemAtPosition(positionCity).toString().toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        localAreaListMetro.clear();
                                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                                            String temp = ds.getKey();
                                            localAreaListMetro.add(temp.toUpperCase());
                                        }

                                        ArrayAdapter<String> adapterLocalArea = new ArrayAdapter<String>(SearchUserMetroActivity.this, android.R.layout.simple_spinner_item, localAreaListMetro);
                                        adapterLocalArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        localAreaMetro.setAdapter(adapterLocalArea);

                                        /****** EXPERT IN LIST ******/

                                        localAreaMetro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parentLocal, View view, int positionLocal, long id) {

                                                databaseReference.child(countryMetro.getSelectedItem().toString().toLowerCase()).child("metro area")
                                                        .child(cityMetro.getSelectedItem().toString().toLowerCase()).child(parentLocal.getItemAtPosition(positionLocal).toString().toLowerCase())
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                                expertListMetro.clear();
                                                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                                    String temp = ds.getKey();
                                                                    expertListMetro.add(temp.toUpperCase());
                                                                }

                                                                ArrayAdapter<String> adapterExpert = new ArrayAdapter<String>(SearchUserMetroActivity.this, android.R.layout.simple_spinner_item, expertListMetro);
                                                                adapterExpert.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                expertMetro.setAdapter(adapterExpert);

                                                                //****** END OF PHASE ******

                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {
                                                                //forLocalAreaListMethood
                                                            }
                                                        });

                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {
                                                //forLocalAreaListMethood
                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        //forCityListMethood
                                    }
                                });

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                //forCityListMethood
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //forCountryListMethood handling...
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //forCountryListMethood
            }
        });

        viewButton.setOnClickListener(this);
    }

    private void loadCountries() {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                countryListMetro.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String temp = ds.getKey();
                    countryListMetro.add(temp.toUpperCase());
                }

                ArrayAdapter<String> adapterCountry = new ArrayAdapter<String>(SearchUserMetroActivity.this, android.R.layout.simple_spinner_item, countryListMetro);
                adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                countryMetro.setAdapter(adapterCountry);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {

        if(v == viewButton){

            Intent intent = new Intent(this, ShowUsersActivity.class);

            intent.putExtra("pCountry", countryMetro.getSelectedItem().toString().toLowerCase());
            intent.putExtra("pArea", "metro area");
            intent.putExtra("pCity", cityMetro.getSelectedItem().toString().toLowerCase());
            intent.putExtra("pLocalArea", localAreaMetro.getSelectedItem().toString().toLowerCase());
            intent.putExtra("pExpert", expertMetro.getSelectedItem().toString().toLowerCase());

            startActivity(intent); /****** SHOW AVAILABLE USERS ******/
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, ProfileActivity.class));
    }
}
