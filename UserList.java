package com.minhajisartisticgmail.helpyou;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by MINHAJ on 4/8/2018.
 */

public class UserList extends ArrayAdapter<UserDetails> {
    private Activity context;
    private List<UserDetails> userList;
    public UserList(Activity context, List<UserDetails> userList){
        super(context, R.layout.activity_user_list_format, userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.activity_user_list_format,null,true);

        TextView name = (TextView) listViewItem.findViewById(R.id.textViewNameFormat);
        TextView email = (TextView) listViewItem.findViewById(R.id.textViewEmailFormat);
        TextView mobile = (TextView) listViewItem.findViewById(R.id.textViewMobileFormat);
        RatingBar ratingBar = (RatingBar) listViewItem.findViewById(R.id.ratingBarFormat);

        UserDetails userDetails = userList.get(position);

        name.setText(userDetails.getName());
        email.setText(userDetails.getEmail());
        mobile.setText("Mobile No: "+userDetails.getMobileNo());

        float total = userDetails.getRating()/userDetails.getReviewer();

        //SHOW RATING TO THE RATING STAR...
        ratingBar.setRating(total);

        return listViewItem;
    }
}
