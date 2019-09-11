package com.minhajisartisticgmail.helpyou;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchUserActivity extends AppCompatActivity {

    private Spinner country, area, district, city;
    private List<String> countryList, areaList, districtList, cityList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        country = (Spinner) findViewById(R.id.spinnerSelectCountry1);
        area = (Spinner) findViewById(R.id.spinnerSelectArea1);
        district = (Spinner) findViewById(R.id.spinnerSelectDistrict1);
        city = (Spinner) findViewById(R.id.spinnerSelectCity1);

        countryList = new ArrayList<String>();
        areaList = new ArrayList<String>();
        districtList = new ArrayList<String>();
        cityList = new ArrayList<String>();

        databaseReference = FirebaseDatabase.getInstance().getReference("SERVICES");

        showUsers();
    }

    private void showUsers() {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                countryList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String temp = ds.getKey();
                    countryList.add(temp);
                }

                ArrayAdapter<String> Adapter = new ArrayAdapter<String>(SearchUserActivity.this, android.R.layout.simple_spinner_item, countryList);
                Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                country.setAdapter(Adapter);

                final String selectCountry=country.getSelectedItem().toString();
                if(selectCountry.equals("")){
                    Toast.makeText(getApplicationContext(), "Choose country first", Toast.LENGTH_SHORT).show();
                }
                else{

                    databaseReference.child(selectCountry).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            areaList.clear();
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                String temp = ds.getKey();
                                areaList.add(temp);
                            }

                            ArrayAdapter<String> Adapter2 = new ArrayAdapter<String>(SearchUserActivity.this, android.R.layout.simple_spinner_item, areaList);
                            Adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            area.setAdapter(Adapter2);

                            String selectArea = area.getSelectedItem().toString();
                            if(selectArea.equals("")){
                                Toast.makeText(getApplicationContext(), "Choose Area First", Toast.LENGTH_SHORT).show();
                            }else{

                                databaseReference.child(selectCountry).child(selectArea).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        cityList.clear();
                                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                                            String temp = ds.getKey();
                                            cityList.add(temp);
                                        }

                                        ArrayAdapter<String> Adapter3 = new ArrayAdapter<String>(SearchUserActivity.this, android.R.layout.simple_spinner_item, cityList);
                                        Adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        city.setAdapter(Adapter3);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
