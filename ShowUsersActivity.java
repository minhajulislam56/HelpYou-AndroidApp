package com.minhajisartisticgmail.helpyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowUsersActivity extends AppCompatActivity {

    ListView listViewUser;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    List<UserDetails> userList;

    public static final String uId="uId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);

        listViewUser = (ListView) findViewById(R.id.listViewUserList);
        userList = new ArrayList<>();

        listViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails userDetails = userList.get(position);

                //checking the current user...
                mAuth = FirebaseAuth.getInstance();
                String currUser = mAuth.getCurrentUser().getUid();

                if(userDetails.getUserID().equals(currUser)){

                    Toast.makeText(getApplicationContext(), "User Cannot Visit Own Profile", Toast.LENGTH_SHORT).show();

                }else {

                    Intent intent1 = new Intent(getApplicationContext(), ProfileViewActivity.class);
                    intent1.putExtra(uId, userDetails.getUserID());
                    startActivity(intent1);

                }
            }
        });

        listViewUser.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                UserDetails userDetails = userList.get(position);
                userList.clear();

                String uId = userDetails.getUserID();

                userRatingDialog(uId, userDetails.getName());
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent checkIntent = getIntent();
        String area = checkIntent.getStringExtra("pArea");

        if(area.equals("metro area")){
            showUsers();
        }else{
            showDistrictUsers();
        }


    }

    private void showDistrictUsers() {

        Intent intent = getIntent();

        String country = intent.getStringExtra("pCountry");
        String district = intent.getStringExtra("pDistrict");
        String upazilla = intent.getStringExtra("pUpazilla");
        String thana = intent.getStringExtra("pThana");
        String ward = intent.getStringExtra("pWard");
        String expert = intent.getStringExtra("pExpert");

        databaseReference = FirebaseDatabase.getInstance().getReference("SERVICES").child(country).child("district").child(district).child(upazilla)
                .child(thana).child(ward).child(expert);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    final String id=ds.getValue(String.class);

                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("USER_DETAILS");
                    Query query = mRef.orderByChild("userID").equalTo(id);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dss : dataSnapshot.getChildren()){
                                UserDetails userDetails = dss.getValue(UserDetails.class);
                                userList.add(userDetails);
                                //Toast.makeText(getApplicationContext(), dss.getKey(), Toast.LENGTH_SHORT).show();
                            }
                            UserList adapter = new UserList(ShowUsersActivity.this, userList);
                            listViewUser.setAdapter(adapter);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }

    private void showUsers(){

        Intent intent = getIntent();

        String country = intent.getStringExtra("pCountry");
        String area = intent.getStringExtra("pArea");
        String city = intent.getStringExtra("pCity");
        String localArea = intent.getStringExtra("pLocalArea");
        String expert = intent.getStringExtra("pExpert");


        databaseReference = FirebaseDatabase.getInstance().getReference("SERVICES").child(country).child(area).child(city).child(localArea).child(expert);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    final String id=ds.getValue(String.class);

                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("USER_DETAILS");
                    Query query = mRef.orderByChild("userID").equalTo(id);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dss : dataSnapshot.getChildren()){
                                UserDetails userDetails = dss.getValue(UserDetails.class);
                                userList.add(userDetails);
                                //Toast.makeText(getApplicationContext(), dss.getKey(), Toast.LENGTH_SHORT).show();
                            }
                            UserList adapter = new UserList(ShowUsersActivity.this, userList);
                            listViewUser.setAdapter(adapter);

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

    private void userRatingDialog(final String uId, String name){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_user_review, null);

        dialogBuilder.setView(dialogView);

        final RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.ratingBarDialog);
        final TextView textViewName = (TextView) dialogView.findViewById(R.id.textViewDialogName);
        final EditText editTextComment = (EditText) dialogView.findViewById(R.id.editTextComment);
        final Button button = (Button) dialogView.findViewById(R.id.buttonDialog);
        mAuth = FirebaseAuth.getInstance();
        final String currUser = mAuth.getCurrentUser().getUid();

        textViewName.setText("Reviewing of "+name);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("reviewer");

                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String isReviewer = dataSnapshot.child("isReviewer").getValue(String.class);

                        if(dataSnapshot.child(uId).hasChild(currUser)){

                            Toast.makeText(getApplicationContext(), "Sorry, You have already reviewed this user!", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            showUsers(); //REFRESH LIST VIEW...

                        }else if(currUser.equals(uId)){

                            Toast.makeText(getApplicationContext(), "Sorry, You cannot rate yourself", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            showUsers(); //REFRESH LIST VIEW...
                        }
                        else {

                            //IF USER IS NOT REVIEWED THEN SAVE THE USER DETAILS WITH REVIEW VALUE...

                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("USER_DETAILS").child(uId);
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    float value = dataSnapshot.child("rating").getValue(float.class);
                                    float person = dataSnapshot.child("reviewer").getValue(float.class);

                                    float total = value+ratingBar.getRating();
                                    String comment = editTextComment.getText().toString().trim();

                                    databaseReference.child("rating").setValue(total);
                                    databaseReference.child("reviewer").setValue(person+1.0);
                                    Toast.makeText(getApplicationContext(), "User Reviewed Successfully", Toast.LENGTH_SHORT).show();

                                    //SAVING THE DETAILS OF REVIEWER...
                                    savingDetails(currUser, uId, comment, ratingBar.getRating());

                                    alertDialog.dismiss();
                                    showUsers();

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
        });
    }

    private void savingDetails(final String currUser, final String listUser, final String comment, final float value){

        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("reviewer").child(listUser).child(currUser);

        DatabaseReference df = FirebaseDatabase.getInstance().getReference("USER_DETAILS").child(currUser);

        df.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();

                Map reviewMap = new HashMap();
                reviewMap.put("isReviewer", "true");
                reviewMap.put("comment", comment);
                reviewMap.put("rating", value);
                reviewMap.put("name", name);
                reviewMap.put("id", currUser);
                mRef.setValue(reviewMap);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
