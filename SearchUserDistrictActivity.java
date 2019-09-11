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

public class SearchUserDistrictActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinnerCountry, spinnerDistrict, spinnerUpazilla, spinnerThana, spinnerWard, spinnerExpert;
    private List<String> countryList, districtList, upazillaList, thanaList, wardList, expertList;
    private Button viewButton;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_district);

        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountryDistrict);
        spinnerDistrict = (Spinner) findViewById(R.id.spinnerDistrict);
        spinnerUpazilla = (Spinner) findViewById(R.id.spinnerUpazillaDistrict);
        spinnerThana = (Spinner) findViewById(R.id.spinnerThanaDistrict);
        spinnerWard = (Spinner) findViewById(R.id.spinnerWardDistrict);
        spinnerExpert = (Spinner) findViewById(R.id.spinnerExpertDistrict);
        viewButton = (Button) findViewById(R.id.viewButtonDistrict);

        countryList = new ArrayList<String>();
        districtList = new ArrayList<String>();
        upazillaList = new ArrayList<String>();
        thanaList = new ArrayList<String>();
        wardList = new ArrayList<String>();
        expertList = new ArrayList<String>();

        databaseReference = FirebaseDatabase.getInstance().getReference("SERVICES");

        viewButton.setOnClickListener(this);

        /****** AVAILABLE COUNTRIES ******/
        loadCountries();

        /****** DISTRICT LIST ******/
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                databaseReference.child(parent.getItemAtPosition(position).toString().toLowerCase()).child("district")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                districtList.clear();
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    String temp = ds.getKey();
                                    districtList.add(temp.toUpperCase());
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchUserDistrictActivity.this, android.R.layout.simple_spinner_item, districtList);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerDistrict.setAdapter(adapter);

                                //****** UPAZILLA LIST ******
                                spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        databaseReference.child(spinnerCountry.getSelectedItem().toString().toLowerCase()).child("district")
                                                .child(parent.getItemAtPosition(position).toString().toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                upazillaList.clear();
                                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                    String temp = ds.getKey();
                                                    upazillaList.add(temp.toUpperCase());
                                                }

                                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchUserDistrictActivity.this, android.R.layout.simple_spinner_item, upazillaList);
                                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spinnerUpazilla.setAdapter(adapter);

                                                //****** THANA LIST ******
                                                spinnerUpazilla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                        databaseReference.child(spinnerCountry.getSelectedItem().toString().toLowerCase()).child("district")
                                                                .child(spinnerDistrict.getSelectedItem().toString().toLowerCase())
                                                                .child(parent.getItemAtPosition(position).toString().toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                                thanaList.clear();
                                                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                                    String temp = ds.getKey();
                                                                    thanaList.add(temp.toUpperCase());
                                                                }

                                                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchUserDistrictActivity.this, android.R.layout.simple_spinner_item, thanaList);
                                                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                spinnerThana.setAdapter(adapter);

                                                                //****** WARD LIST ******
                                                                spinnerThana.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                    @Override
                                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                                        databaseReference.child(spinnerCountry.getSelectedItem().toString().toLowerCase()).child("district")
                                                                                .child(spinnerDistrict.getSelectedItem().toString().toLowerCase())
                                                                                .child(spinnerUpazilla.getSelectedItem().toString().toLowerCase())
                                                                                .child(parent.getItemAtPosition(position).toString().toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                                                wardList.clear();
                                                                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                                                    String temp = ds.getKey();
                                                                                    wardList.add(temp.toUpperCase());
                                                                                }

                                                                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchUserDistrictActivity.this, android.R.layout.simple_spinner_item, wardList);
                                                                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                                spinnerWard.setAdapter(adapter);

                                                                                //****** EXPERT IN LIST ******
                                                                                spinnerWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                                    @Override
                                                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                                                        databaseReference.child(spinnerCountry.getSelectedItem().toString().toLowerCase()).child("district")
                                                                                                .child(spinnerDistrict.getSelectedItem().toString().toLowerCase())
                                                                                                .child(spinnerUpazilla.getSelectedItem().toString().toLowerCase())
                                                                                                .child(spinnerThana.getSelectedItem().toString().toLowerCase())
                                                                                                .child(parent.getItemAtPosition(position).toString().toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                                                                expertList.clear();
                                                                                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                                                                    String temp = ds.getKey();
                                                                                                    expertList.add(temp.toUpperCase());
                                                                                                }

                                                                                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchUserDistrictActivity.this, android.R.layout.simple_spinner_item, expertList);
                                                                                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                                                spinnerExpert.setAdapter(adapter);

                                                                                                //****** END OF PHASE ******

                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                                            }
                                                                                        });

                                                                                    }

                                                                                    @Override
                                                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                                                    }
                                                                                });
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                            }
                                                                        });

                                                                    }

                                                                    @Override
                                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                                    }
                                                                });

                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> parent) {
                                                        //UPAZILLA SPINNER
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        //DISTRICT SPINNER....
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //COUNTRY SPINNER...
            }
        });

    }

    private void loadCountries() {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                countryList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String temp = ds.getKey();
                    countryList.add(temp.toUpperCase());
                }

                ArrayAdapter<String> adapterCountry = new ArrayAdapter<String>(SearchUserDistrictActivity.this, android.R.layout.simple_spinner_item, countryList);
                adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCountry.setAdapter(adapterCountry);
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

            intent.putExtra("pCountry", spinnerCountry.getSelectedItem().toString().toLowerCase());
            intent.putExtra("pArea", "district");
            intent.putExtra("pDistrict", spinnerDistrict.getSelectedItem().toString().toLowerCase());
            intent.putExtra("pUpazilla", spinnerUpazilla.getSelectedItem().toString().toLowerCase());
            intent.putExtra("pThana", spinnerThana.getSelectedItem().toString().toLowerCase());
            intent.putExtra("pWard", spinnerWard.getSelectedItem().toString().toLowerCase());
            intent.putExtra("pExpert", spinnerExpert.getSelectedItem().toString().toLowerCase());

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
