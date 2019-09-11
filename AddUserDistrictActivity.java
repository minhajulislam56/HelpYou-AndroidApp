package com.minhajisartisticgmail.helpyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddUserDistrictActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextDistrict;
    private EditText editTextUpazilla;
    private EditText editTextThana;
    private EditText editTextWard;
    private EditText editTextExpert;
    private EditText editTextEmail;

    private Button buttonSubmit;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_district);

        editTextDistrict = (EditText) findViewById(R.id.editTextDistrictAddUser);
        editTextUpazilla = (EditText) findViewById(R.id.editTextUpazillaAddUser);
        editTextThana = (EditText) findViewById(R.id.editTextThanaAddUser);
        editTextWard = (EditText) findViewById(R.id.editTextWardAddUser);
        editTextEmail = (EditText) findViewById(R.id.editTextEmailDistrictAddUser);
        editTextExpert = (EditText) findViewById(R.id.editTextExpertDistrictAddUser);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmitDistrictAddUser);

        databaseReference = FirebaseDatabase.getInstance().getReference("SERVICES");

        buttonSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == buttonSubmit){
            addUserDistrict();
        }
    }

    private void addUserDistrict() {

        Intent intent = getIntent();

        final String country = intent.getStringExtra(AddUser1Activity.pCountry).toLowerCase();
        final String area = intent.getStringExtra(AddUser1Activity.pArea).toLowerCase();

        final String district = editTextDistrict.getText().toString().trim().toLowerCase();
        final String upazilla = editTextUpazilla.getText().toString().trim().toLowerCase();
        final String thana = editTextThana.getText().toString().trim().toLowerCase();
        final String ward = editTextWard.getText().toString().trim().toLowerCase();
        final String email = editTextEmail.getText().toString().trim().toLowerCase();
        final String expert = editTextExpert.getText().toString().trim().toLowerCase();

        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("USER_DETAILS");
        Query query = mRef.orderByChild("email").equalTo(email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    String id = ds.child("userID").getValue(String.class);

                    databaseReference.child(country).child(area).child(district).child(upazilla).child(thana)
                            .child(ward).child(expert).push().setValue(id);

                    //ADDING EXPERT IN OF USER...

                    mRef.child(id).child("expert").setValue(expert);

                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();

                    finish();
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error, Please check Email Address is correct", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, AddUser1Activity.class));
    }
}
