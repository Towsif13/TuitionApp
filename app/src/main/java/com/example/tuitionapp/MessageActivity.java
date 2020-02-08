package com.example.tuitionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView msg_user_name,msg_user_lastName;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ArrayList<UserContacts> list;
    ImageButton imageButton;
    EditText msg_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar mtoolbar = findViewById(R.id.user_msg_toolbar);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        msg_user_name = findViewById(R.id.msg_username);
        msg_user_lastName = findViewById(R.id.msg_user_lastName);
        msg_send= findViewById(R.id.msg_send);
        imageButton = findViewById(R.id.btn_send);
        circleImageView = findViewById(R.id.msg_profile_image);

        Intent intent = getIntent();
        final String userid = intent.getStringExtra("userId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child("Teacher");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list = new ArrayList<UserContacts>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    UserContacts p = dataSnapshot1.getValue(UserContacts.class);
                    list.add(p);
                    msg_user_name.setText(p.getFirstName());
                    msg_user_lastName.setText(p.getLastName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msg_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(firebaseUser.getUid(),userid,msg);
                }else {
                    Toast.makeText(MessageActivity.this,"You cant send message",Toast.LENGTH_LONG);
                }
                msg_send.setText("");
            }
        });

    }

    private void sendMessage(String sender, String receiver, String message){

       DatabaseReference Reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        Reference.child("Chats").push().setValue(hashMap);

    }
}
