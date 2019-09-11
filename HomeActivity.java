package com.minhajisartisticgmail.helpyou;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private Button loginButton;
    private EditText editTextEmail;
    private EditText editTextPass;
    private TextView textViewRegister;
    private FirebaseAuth firebaseAuth;
    ProgressDialog progDia;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()){ //*** LOGGED IN & VERIFICATION CHECKING...

            finish();
            startActivity(new Intent(this, ProfileActivity.class));

        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.firstLayoutHome);
        mNavigationView = (NavigationView) findViewById(R.id.navViewHome);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPass = (EditText) findViewById(R.id.editTextPassword);
        loginButton = (Button) findViewById(R.id.buttonLogin);
        textViewRegister = (TextView) findViewById(R.id.textViewRegister);

        loginButton.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == loginButton){
            //finish(); lagbe na...
            //startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

            //CHECKING USER AUTHENTICATION...
            userLogin();
        }
        if(v == textViewRegister){
            finish();
            startActivity(new Intent(this, SignupActivity.class));
        }
    }
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String pass = editTextPass.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter your email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Please enter your password",Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.length()<6){
            editTextPass.setError("Minimum 6 Characters Required!");
            return;
        }

        //If everything is okay...
        progDia = new ProgressDialog(this);
        progDia.setMessage("Logging In");
        progDia.show();

        firebaseAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {;
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progDia.dismiss();
                        if(task.isSuccessful()){

                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            /*if(firebaseAuth.getCurrentUser().isEmailVerified()){

                                finish();
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

                            }else{

                                finish();
                                startActivity(new Intent(getApplicationContext(), VerificationActivity.class));

                            }*/

                        }else{
                            Toast.makeText(getApplicationContext(), "Wrong User ID or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.aboutMe){

            finish();
            startActivity(new Intent(this, AboutMeActivity.class));

        }else if(id == R.id.terms){

            finish();
            startActivity(new Intent(this, TermsActivity.class));

        }else if(id == R.id.instructions){

            finish();
            startActivity(new Intent(this, InstructionsActivity.class));

        }


        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }
}
