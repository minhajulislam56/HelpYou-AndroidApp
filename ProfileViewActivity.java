package com.minhajisartisticgmail.helpyou;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileViewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewName;
    private TextView textViewEmail;
    private TextView textViewAddress;
    private TextView textViewGender;
    private TextView textViewExpert;
    private TextView textViewRating;
    private TextView textViewType;
    private String id;
    private String expert;
    private String phoneNo;
    private String emailR;
    private String rName;
    private String currUserId;

    private ImageView imageViewProfile;
    private ImageView imageViewStatus;

    private Button buttonCall;
    private Button buttonMail;
    private Button buttonMessage;

    private StorageReference mStorage;
    private FirebaseAuth mAuth;

    public static final String chatId="chatId";
    public static final String chatName="chatName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        textViewName = (TextView) findViewById(R.id.textViewProfileViewName);
        textViewEmail = (TextView) findViewById(R.id.textViewProfileViewUserEmail);
        textViewAddress = (TextView) findViewById(R.id.textViewProfileViewUserAddress);
        textViewGender = (TextView) findViewById(R.id.textViewProfileViewUserGender);
        textViewExpert = (TextView) findViewById(R.id.textViewProfileViewExpert);
        textViewRating = (TextView) findViewById(R.id.textViewProfileViewRating);
        imageViewProfile = (ImageView) findViewById(R.id.imageViewProfileViewPic);
        textViewType = (TextView) findViewById(R.id.textViewAccntTypeProfileView);
        imageViewStatus = (ImageView) findViewById(R.id.user_single_online_icon_profileView);

        buttonCall = (Button) findViewById(R.id.buttonCall);
        buttonMail = (Button) findViewById(R.id.buttonEmail);
        buttonMessage = (Button) findViewById(R.id.buttonMessage);
        mStorage = FirebaseStorage.getInstance().getReference("userPhotos");

        buttonCall.setOnClickListener(this);
        buttonMail.setOnClickListener(this);
        buttonMessage.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        id = intent.getStringExtra("uId");
        expert = intent.getStringExtra("pExpert");
        //Toast.makeText(getApplicationContext(), id+" "+expert, Toast.LENGTH_SHORT).show();

        //CHECKING THE CURRENT USER TO DISABLE THE BUTTON...
        mAuth = FirebaseAuth.getInstance();
        currUserId = mAuth.getCurrentUser().getUid();

        loadImage();
        userInfo();
    }

    private void loadImage() {

        mStorage.child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(ProfileViewActivity.this).load(uri.toString()).fit().centerCrop().into(imageViewProfile);
            }
        }).addOnFailureListener(
                new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(getApplicationContext(), "Picture Load Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void userInfo() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("USER_DETAILS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child(id).child("name").getValue(String.class);
                String email = dataSnapshot.child(id).child("email").getValue(String.class);
                String gender = dataSnapshot.child(id).child("gender").getValue(String.class);
                String address = dataSnapshot.child(id).child("address").getValue(String.class);
                String expertIn = dataSnapshot.child(id).child("expert").getValue(String.class);
                String mobile = dataSnapshot.child(id).child("mobileNo").getValue(String.class); //button er jonno use kora hobe
                float rating = dataSnapshot.child(id).child("rating").getValue(float.class);  //button er jonno user kora hobe
                float reviewer = dataSnapshot.child(id).child("reviewer").getValue(float.class);

                boolean type = dataSnapshot.child(id).child("type").getValue(boolean.class);
                boolean status = dataSnapshot.child(id).child("status").getValue(boolean.class);

                phoneNo=mobile;
                emailR=email;
                rName=name;

                DecimalFormat df = new DecimalFormat("#.##"); //FOR FLOATING POINT...

                textViewName.setText(name);
                textViewEmail.setText(email);
                textViewGender.setText(gender);
                textViewAddress.setText(address.toUpperCase());
                textViewRating.setText("Rating: " + String.valueOf(df.format(rating/reviewer)) + " By " + String.valueOf((int) reviewer) + " Users");
                textViewExpert.setText("Expert In: " + expertIn.toUpperCase());

                if(type) textViewType.setText("Account Type: Admin");
                else textViewType.setText("Account Type: User");

                if(status){
                    imageViewStatus.setVisibility(View.VISIBLE);
                }else{
                    imageViewStatus.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == buttonCall) {

            if(currUserId.equals(id)){
                Toast.makeText(getApplicationContext(), "Sorry, You cannot call yourself", Toast.LENGTH_SHORT).show();
            }else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNo));
                startActivity(callIntent);
                //Toast.makeText(this, phoneNo, Toast.LENGTH_SHORT).show();
            }

        }
        if(v == buttonMessage){

            if(currUserId.equals(id)){
                Toast.makeText(getApplicationContext(), "Sorry, You cannot message yourself", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra(chatId, id);
                startActivity(intent);
            }

        }
        if(v == buttonMail){
            if(currUserId.equals(id)){
                Toast.makeText(getApplicationContext(), "Sorry, You cannot Mail yourself", Toast.LENGTH_SHORT).show();
            }else {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String id = user.getUid();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("USER_DETAILS");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String sName = dataSnapshot.child(id).child("name").getValue(String.class);
                        String sEmail = dataSnapshot.child(id).child("email").getValue(String.class);
                        String sAddress = dataSnapshot.child(id).child("address").getValue(String.class);

                        Intent mailIntent = new Intent(Intent.ACTION_SEND);
                        mailIntent.setType("plain/text");
                        mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailR});
                        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Help You Task Request");
                        mailIntent.putExtra(Intent.EXTRA_TEXT, "Hello " + rName + ",\n" + sName + ", from " + sAddress + " needs some help. As we know you are expert in " + expert +
                                ", So we think you are the right person for the task. Please Contact " + sName + ", If you are willing to do this task.\n\n" +
                                "Thank You!\nHelp You Team");
                        startActivity(mailIntent.createChooser(mailIntent, "Send email"));
                        ;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //For Error Handling
                    }
                });
            }

        }
    }
}
