package com.minhajisartisticgmail.helpyou;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private ImageView imageViewProfilePicNav;
    //private BlurImageView blurImageViewNav;
    private TextView textViewNavName;
    private TextView textViewAccntType;
    private TextView textViewStatus;

    private DatabaseReference databaseReference, mUserRef;
    private FirebaseAuth firebaseAuth;

    private StorageReference mStorage;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //------- FOR TRANSITION
        //overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){

            /*if(!firebaseAuth.getCurrentUser().isEmailVerified()){ //**** VERIFICATION CHECKING...

                finish();
                startActivity(new Intent(this, VerificationActivity.class));

            }*/

            mUserRef = FirebaseDatabase.getInstance().getReference().child("USER_DETAILS").child(firebaseAuth.getCurrentUser().getUid());

        }

        //ON DISCONNECT ACTIVITY FOR LAST SEEN OPTION...
        if(firebaseAuth.getCurrentUser() != null){

            final DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("USER_DETAILS").child(firebaseAuth.getCurrentUser().getUid());
            dr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot != null){

                        dr.child("online").onDisconnect().setValue(ServerValue.TIMESTAMP);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.firstLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navView);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);

        mStorage = FirebaseStorage.getInstance().getReference("userPhotos");

        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        textViewNavName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.textViewNavName);
        imageViewProfilePicNav = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.imageViewProfilePicNav);
        textViewAccntType = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.textViewAccntType);
        textViewStatus = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.textViewStatus);
        //blurImageViewNav = (BlurImageView) mNavigationView.getHeaderView(0).findViewById(R.id.imageViewNavBlur);
        //blurImageViewNav.setBlur(5);

    }
    @Override
    public void onBackPressed(){

        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currUser = firebaseAuth.getCurrentUser();

        if(currUser != null){

            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currUser = firebaseAuth.getCurrentUser();

        if(currUser == null){

            finish();
            startActivity(new Intent(this,HomeActivity.class));

        }else{

            mUserRef.child("online").setValue("true");

        }

        userInfo();
        loadImage();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.updateProfile){

            finish();
            startActivity(new Intent(getApplicationContext(), UpdateProfileActivity.class));
            //Toast.makeText(getApplicationContext(), "Kaj", Toast.LENGTH_SHORT).show();

        }else if(id == R.id.searchUsers){

            selectAreaRadioButton();

        }else if(id == R.id.addUser){

            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("USER_DETAILS").child(firebaseAuth.getCurrentUser().getUid());
            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot){

                    boolean type = dataSnapshot.child("type").getValue(boolean.class);
                    if(type){

                        finish();
                        startActivity(new Intent(getApplicationContext(), AddUser1Activity.class));


                    }else{

                        Toast.makeText(getApplicationContext(), "Sorry, you don't have this permission.\n   Contact with your local Admin", Toast.LENGTH_LONG ).show();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if(id == R.id.logOut){

            //LAST SEEN OF THE ACTIVITY
            FirebaseUser currUser = firebaseAuth.getCurrentUser();

            if(currUser != null){

                mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

            }

            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }

        return false;
    }

    private void selectAreaRadioButton() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.area_dialog, null);

        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        final RadioButton radioButtonMetro = (RadioButton) dialogView.findViewById(R.id.radioButtonMetro);
        final RadioButton radioButtonDistrict = (RadioButton) dialogView.findViewById(R.id.radioButtonDistrict);
        Button nextButton = (Button) dialogView.findViewById(R.id.areaButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(radioButtonMetro.isChecked()){

                    /****** SEARCH THE METRO USERS ******/
                    finish();
                    startActivity(new Intent(getApplicationContext(), SearchUserMetroActivity.class));

                }else {

                    /****** SEARCH THE DISTRICT USERS ******/
                    finish();
                    startActivity(new Intent(getApplicationContext(), SearchUserDistrictActivity.class));
                }

            }
        });



    }

    private void loadImage(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uId = user.getUid();

        mStorage.child(uId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(ProfileActivity.this).load(uri.toString()).fit().centerCrop().into(imageViewProfilePicNav);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(getApplicationContext(), "Picture Load Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void userInfo(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uId = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("USER_DETAILS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child(uId).child("name").getValue(String.class);
                String email = dataSnapshot.child(uId).child("email").getValue(String.class);
                String phone = dataSnapshot.child(uId).child("mobileNo").getValue(String.class);
                String address = dataSnapshot.child(uId).child("address").getValue(String.class);
                String gender = dataSnapshot.child(uId).child("gender").getValue(String.class);
                boolean type = dataSnapshot.child(uId).child("type").getValue(boolean.class);
                boolean status = dataSnapshot.child(uId).child("status").getValue(boolean.class);

                float rating = dataSnapshot.child(uId).child("rating").getValue(float.class);
                float person = dataSnapshot.child(uId).child("reviewer").getValue(float.class);

                /****  HAVE TO ADD FRAGMENT ACTIVITY.... *******/
                //DecimalFormat df = new DecimalFormat("#.##");
                //textViewRating.setText(String.valueOf(df.format(rating/person)));

                textViewNavName.setText(name);
                if(type){
                    textViewAccntType.setText("Account Type: Admin");
                }else{
                    textViewAccntType.setText("Account Type: User");
                }

                if(status){
                    textViewStatus.setText("Currently: Active");
                }else{
                    textViewStatus.setText("Currently: Not Active");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
