package com.minhajisartisticgmail.helpyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddUser1Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextCountry;
    private Spinner spinnerArea;
    private Button buttonSubmit;
   // public static final String pCountry = "pCountry";
    //public static final String pArea = "pArea";
    public static final String pCountry = "pCountry";
    public static final String pArea = "pArea";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user1);

        editTextCountry = (EditText) findViewById(R.id.editTextCountry);
        spinnerArea = (Spinner) findViewById(R.id.spinnerAreaSelection);
        buttonSubmit = (Button) findViewById(R.id.buttonAddarea1Submit);

        buttonSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonSubmit){
            submitDetails();
        }
    }
    private void submitDetails(){
        String country = editTextCountry.getText().toString().trim();
        String area = spinnerArea.getSelectedItem().toString();

        if(TextUtils.isEmpty(country)){
            Toast.makeText(this, "Please Enter the Country Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(area.equals("Metro Area")){
            Intent intent = new Intent(getApplicationContext(), AddUserMetroActivity.class);
            intent.putExtra(pCountry, country);
            intent.putExtra(pArea, area);

            finish();
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(getApplicationContext(), AddUserDistrictActivity.class);
            intent.putExtra(pCountry, country);
            intent.putExtra(pArea, area);

            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, ProfileActivity.class));
    }
}
