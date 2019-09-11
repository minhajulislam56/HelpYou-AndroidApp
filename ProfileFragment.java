package com.minhajisartisticgmail.helpyou;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

    private TextView textViewEmail;
    private TextView textViewExpert;
    private TextView textViewName;
    private TextView textViewPhone;
    private TextView textViewAddress;
    private TextView textViewGender;
    private ImageView imageViewProfilePic;
    private RatingBar ratingBar;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewName = (TextView) getView().findViewById(R.id.textViewName);
        textViewExpert = (TextView) getView().findViewById(R.id.textViewExpertIn);
        textViewEmail = (TextView) getView().findViewById(R.id.textViewUserEmail);
        textViewPhone = (TextView) getView().findViewById(R.id.textViewUserPhone);
        textViewAddress = (TextView) getView().findViewById(R.id.textViewUserAddress);
        textViewGender = (TextView) getView().findViewById(R.id.textViewUserGender);
        imageViewProfilePic = (ImageView) getView().findViewById(R.id.imageViewProfilePic);
        ratingBar = (RatingBar) getView().findViewById(R.id.ratingBarProfile);

        userInfo(); //adding details
    }
    private void userInfo(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uId = user.getUid();

        //LOADING THE PROFILE IMAGE

        mStorageReference = FirebaseStorage.getInstance().getReference("userPhotos");
        mStorageReference.child(uId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(getContext()).load(uri.toString()).fit().centerCrop().into(imageViewProfilePic);

            }
        });


        //LOADING THE USERINFO...

        databaseReference = FirebaseDatabase.getInstance().getReference("USER_DETAILS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child(uId).child("name").getValue(String.class);
                String email = dataSnapshot.child(uId).child("email").getValue(String.class);
                String phone = dataSnapshot.child(uId).child("mobileNo").getValue(String.class);
                String address = dataSnapshot.child(uId).child("address").getValue(String.class);
                String gender = dataSnapshot.child(uId).child("gender").getValue(String.class);
                String expert = dataSnapshot.child(uId).child("expert").getValue(String.class);
                float rating = dataSnapshot.child(uId).child("rating").getValue(float.class);


                //float rating = dataSnapshot.child(uId).child("rating").getValue(float.class);
                float person = dataSnapshot.child(uId).child("reviewer").getValue(float.class);

                textViewName.setText(name);
                textViewEmail.setText(email);
                textViewPhone.setText(phone);
                textViewAddress.setText(address.toUpperCase());
                textViewGender.setText(gender);
                textViewExpert.setText("Expert In: "+expert);
                ratingBar.setRating(rating/person);
                //DecimalFormat df = new DecimalFormat("#.##");
                //textViewRating.setText(String.valueOf(df.format(rating/person)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
