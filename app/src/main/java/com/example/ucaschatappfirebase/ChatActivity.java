package com.example.ucaschatappfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.ucaschatappfirebase.Constant.COLLECTION_MESSAGES;
import static com.example.ucaschatappfirebase.Constant.DATE;
import static com.example.ucaschatappfirebase.Constant.MESSAGE_BODY;
import static com.example.ucaschatappfirebase.Constant.USER_EMAIL;

public class ChatActivity extends AppCompatActivity {
    FirebaseFirestore dp;
    EditText messageEditText;
    private static final String TAG = "ChatActivity";
    MessageAdapter messageAdapter;
    ListView messagesListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        TextView signOutTextView = findViewById(R.id.signOutTextView);
        ImageView sendImageView = findViewById(R.id.sendImageView);
        messageEditText = findViewById(R.id.messageEditText);
          messagesListView = findViewById(R.id.messagesListView);

        dp = FirebaseFirestore.getInstance();


        messageAdapter = new MessageAdapter(this, new ArrayList<>());
        messagesListView.setAdapter(messageAdapter);


        signOutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


        sendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        getMessages();

    }

    private void getMessages() {
        dp.collection(COLLECTION_MESSAGES).orderBy(DATE).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.w(TAG, "getMessages: " + error);
                    return;
                }

                List<Message> messageList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    if ((doc.get(USER_EMAIL) != null) && doc.get(MESSAGE_BODY) != null) {
                        messageList.add(new Message(doc.getString(USER_EMAIL), doc.getString(MESSAGE_BODY), doc.getLong(DATE)));
                    }

                }

                Log.d(TAG, "messageList: " + messageList);

                messageAdapter.clear();
                messageAdapter.addAll(messageList);
                //scroll
                messagesListView.smoothScrollToPosition(messageList.size()-1);


            }
        });
    }

    private void sendMessage() {
        if (!isValid()) return;

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


//            Message message = new Message(user.getEmail(),messageEditText.getText().toString(),TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

            Map<String, Object> message = new HashMap<>();
            message.put(MESSAGE_BODY, messageEditText.getText().toString());
            message.put(USER_EMAIL, user.getEmail());
            message.put(DATE, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));


            dp.collection(COLLECTION_MESSAGES).add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG, "onSuccess done !!");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "OnFailureListener done !!");
                }
            })


            ;
        }

    }

    private boolean isValid() {
        if (messageEditText.getText().toString().isEmpty()) {
            return false;
        }

        return true;
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}