package com.minhajisartisticgmail.helpyou;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSend;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        buttonSend = (Button) findViewById(R.id.buttonVerify);
        firebaseAuth = FirebaseAuth.getInstance();

        buttonSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonSend){

            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(getApplicationContext(), "Verification Email Sent", Toast.LENGTH_SHORT).show();

                    finish();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        startActivity(new Intent(this, HomeActivity.class));
    }
}
