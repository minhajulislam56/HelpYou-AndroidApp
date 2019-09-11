package com.minhajisartisticgmail.helpyou;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName;
    private EditText editTextAddress;
    private EditText editTextDOB;
    private EditText editTextPhone;
    private Button buttonUpdateProfile;
    private Button buttonPhoto;
    private ProgressDialog progressDialog;
    private Button buttonStatus;

    private DatabaseReference databaseReference;
    private FirebaseUser currUser;

    private StorageReference mStorage;
    private static final int GALLERY_PICK = 2;

    private String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        currUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = currUser.getUid();

        editTextName = (EditText) findViewById(R.id.editTextNameUpdate);
        editTextAddress = (EditText) findViewById(R.id.editTextAddressSignup);
        editTextDOB = (EditText) findViewById(R.id.editTextDateUpdate);
        editTextPhone = (EditText) findViewById(R.id.editTextMobilenoUpdate);
        buttonUpdateProfile = (Button) findViewById(R.id.buttonUpdateProfile);
        buttonPhoto = (Button) findViewById(R.id.buttonUploadPhoto);
        buttonStatus = (Button) findViewById(R.id.buttonChangeStatus);

        mStorage = FirebaseStorage.getInstance().getReference();

        buttonUpdateProfile.setOnClickListener(this);
        buttonStatus.setOnClickListener(this);

        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, GALLERY_PICK);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == buttonUpdateProfile){

            updateUserDetails();

        }

        if(v == buttonStatus){ //-------CHANGING CURRENT ACTIVITY STATUS

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String id = mAuth.getCurrentUser().getUid();

            final DatabaseReference df = FirebaseDatabase.getInstance().getReference("USER_DETAILS").child(id);
            df.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    boolean flag = dataSnapshot.child("status").getValue(boolean.class);

                    if(flag){
                        df.child("status").setValue(false);
                        Toast.makeText(getApplicationContext(), "Current activity status deactivated", Toast.LENGTH_SHORT).show();
                    }else{
                        df.child("status").setValue(true);
                        Toast.makeText(getApplicationContext(), "Current activity status activated", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        progressDialog = new ProgressDialog(this);


        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            progressDialog.setMessage("Uploading Photo");
            progressDialog.show();

            final Uri uri = data.getData();

            StorageReference filepath = mStorage.child("userPhotos").child(uId);

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(getApplicationContext(), "Photo Uploaded", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            });

        }
    }

    private void updateUserDetails(){

        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String dob = editTextDOB.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        //IF EVERYTHING IS OKAY.....
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        String uId = currUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("USER_DETAILS").child(uId);

        if(!TextUtils.isEmpty(name)){
            databaseReference.child("name").setValue(name);
        }

        if(!TextUtils.isEmpty(address)){
            databaseReference.child("address").setValue(address);
        }

        if(!TextUtils.isEmpty(dob)){
            databaseReference.child("dob").setValue(dob);
        }

        if(!TextUtils.isEmpty(phone)){
            databaseReference.child("mobileNo").setValue(phone);
        }

        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();

    }
}