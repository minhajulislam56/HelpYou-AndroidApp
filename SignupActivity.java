package com.minhajisartisticgmail.helpyou;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPass;
    private EditText editTextConfirmPass;
    private EditText editTextAddresss;
    private Spinner spinnerGender;
    private EditText editTextDOB;
    private EditText editTextMobileNo;
    private Button buttonRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /***** CHECK USER IS LOGGED IN *****/
        firebaseAuth = FirebaseAuth.getInstance();
        /*if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }*/

        databaseReference = FirebaseDatabase.getInstance().getReference("USER_DETAILS");

        String gender[]=new String[]{"MALE", "FEMALE", "COMMON"};

        editTextName = (EditText) findViewById(R.id.editTextNameSignup);
        editTextEmail = (EditText) findViewById(R.id.editTextEmailSignup);
        editTextPass = (EditText) findViewById(R.id.editTextPasswordSignup);
        editTextConfirmPass = (EditText) findViewById(R.id.editTextConfirmPasswordSignup);

        editTextAddresss = (EditText) findViewById(R.id.editTextAddressSignup);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,gender);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerGender = (Spinner) findViewById(R.id.spinnerGenderSignup);
        spinnerGender.setAdapter(spinnerArrayAdapter);

        editTextDOB = (EditText) findViewById(R.id.editTextDateSignup);
        editTextMobileNo = (EditText) findViewById(R.id.editTextMobilenoSignup);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        progressDialog = new ProgressDialog(this);

        buttonRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){

            registerUser();
        }
    }
    private void registerUser(){

        String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String pass = editTextPass.getText().toString().trim();
        String confirmPass = editTextConfirmPass.getText().toString().trim();
        String address = editTextAddresss.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String dob = editTextDOB.getText().toString().trim();
        String mobile = editTextMobileNo.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter Your Email Address", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(confirmPass)){
            Toast.makeText(this, "Please Enter Your Confirm Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(address)){
            Toast.makeText(this, "Please Enter Your Address", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(dob)){
            Toast.makeText(this, "Please Enter Your Date of Birth", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(mobile)){
            Toast.makeText(this, "Please Enter Your Mobile No.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.equals(confirmPass) == false){
            Toast.makeText(this, "Password Do Not Match", Toast.LENGTH_SHORT).show();
            return;
        }

        //IF EVERYTHING IS OKAY...
        progressDialog.setMessage("Registering User");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //AUTHENTICATION IS SUCCESSFULL...
                            addUserDetails(); //ADDING USER DETAILS...
                        }else{
                            Toast.makeText(getApplicationContext(),"Registration is Not Successfull! Please Try Again.",Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
    private void addUserDetails(){

        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String address = editTextAddresss.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String dob = editTextDOB.getText().toString().trim();
        String mobile = editTextMobileNo.getText().toString().trim();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        //-------SET DISPLAY NAME
        UserProfileChangeRequest displayName = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();
        currentUser.updateProfile(displayName);

        UserDetails userDetails = new UserDetails(name, address, "nothing", gender, dob, mobile, 0, 0, email, userId, false, true);
        databaseReference.child(userId).setValue(userDetails);

        progressDialog.dismiss();

        currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "PLEASE VERIFY YOUR EMAIL TO COMPLETE YOUR REGISTRATION", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });


    }
    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this,HomeActivity.class));
    }
}
