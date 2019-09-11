package com.minhajisartisticgmail.helpyou;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {

    private RecyclerView mConvList;

    private DatabaseReference databaseReference;
    private DatabaseReference mRef;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;

    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_review, container, false);

        mConvList = (RecyclerView) mMainView.findViewById(R.id.review_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("reviewer").child(mCurrent_user_id);

        //databaseReference.keepSynced(true);
        //mRef.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mConvList.setHasFixedSize(true);
        mConvList.setLayoutManager(linearLayoutManager);


        // Inflate the layout for this fragment*/
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ListItemReview, ReviewViewHolder> firebaseConvAdapter = new FirebaseRecyclerAdapter<ListItemReview, ReviewViewHolder>(
                ListItemReview.class,
                R.layout.review_list_items,
                ReviewViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(final ReviewViewHolder reviewViewHolder, final ListItemReview model, int postion) {

                mRef = FirebaseDatabase.getInstance().getReference("reviewer").child(mCurrent_user_id);
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds : dataSnapshot.getChildren()){

                            String name = ds.child("name").getValue().toString();
                            String comment = ds.child("comment").getValue().toString();
                            float rating = ds.child("rating").getValue(float.class);

                            final String id = ds.child("id").getValue().toString();

                            ReviewViewHolder.setDetails(name, rating, comment);

                            ReviewViewHolder.setImage(id,getContext());

                            //FOR TOUCH ACTION...
                            ReviewViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), ProfileViewActivity.class);
                                    intent.putExtra("uId", id);
                                    intent.putExtra("pExpert", "fuck");
                                    startActivity(intent);
                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        mConvList.setAdapter(firebaseConvAdapter);

    }


    public static class ReviewViewHolder extends RecyclerView.ViewHolder{

        static View mView;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public static void setDetails(String name, float rating, String comment){

            TextView userNameView = (TextView) mView.findViewById(R.id.textViewRevrName);
            RatingBar ratingBar = (RatingBar) mView.findViewById(R.id.ratingBarRevwr);
            TextView userComment = (TextView) mView.findViewById(R.id.textViewRevrCmnt);

            userNameView.setText(name);
            ratingBar.setRating(rating);
            userComment.setText("Comment: "+comment);

        }

        public static void setImage(String id, final Context ctx){

            final CircleImageView imageView = (CircleImageView) mView.findViewById(R.id.revwrImage);

            StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("userPhotos");

            mStorageReference.child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(ctx).load(uri.toString()).fit().centerCrop().into(imageView);
                }
            });

        }
    }

}
