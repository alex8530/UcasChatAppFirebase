package com.example.ucaschatappfirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter (Context context, List<Message> messages){
        super(context,0,messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView =convertView;

        if (listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.message_list_item,parent,false);
        }

        Message message = getItem(position);

        TextView messageBodyTextView= listItemView.findViewById(R.id.messageBodyTextView);
        ImageView userSenderImageImageView= listItemView.findViewById(R.id.userSenderImageImageView);
        ImageView userReciverImageImageView= listItemView.findViewById(R.id.userReciverImageImageView);
        LinearLayout root= listItemView.findViewById(R.id.root);

        messageBodyTextView.setText(message.getMessageBody());

        //if i am sender >>
        if (message.getUserEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            userSenderImageImageView.setVisibility(View.VISIBLE);
            userReciverImageImageView.setVisibility(View.GONE);
            root.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.senderColor));
        }else {
            userSenderImageImageView.setVisibility(View.GONE);
            userReciverImageImageView.setVisibility(View.VISIBLE);
            root.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.reciverColor));

        }



        return listItemView;
    }
}
