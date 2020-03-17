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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
    MessageAdapter messageAdapter;
    ArrayList<Chat>chats;
    RecyclerView recyclerView;
    ValueEventListener valueEventListener;
    String userid;

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
        recyclerView = findViewById(R.id.msg_recyclerView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);




        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userid = getIntent().getExtras().getString("userId");

        databaseReference.child("Users").child("Student").child(currentuser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                 databaseReference.child("Users").child("Teacher").child(userid)
                            .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //list = new ArrayList<UserContacts>();
                            UserContacts p = dataSnapshot.getValue(UserContacts.class);
                            //list.add(p);
                            msg_user_name.setText(p.getFirstName());
                            msg_user_lastName.setText(p.getLastName());
                            if(dataSnapshot.child("ProfileImage").getValue() != null) {
                                Picasso.get().load(p.getProfileImage()).into(circleImageView);
                            }

                            readMessage(firebaseUser.getUid(),userid);
                            seenMessage(userid);

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
                else {

                    databaseReference.child("Users").child("Student").child(userid).addValueEventListener(new ValueEventListener()  {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //list = new ArrayList<UserContacts>();
                            UserContacts p = dataSnapshot.getValue(UserContacts.class);
                            //list.add(p);
                            msg_user_name.setText(p.getFirstName());
                            msg_user_lastName.setText(p.getLastName());

                            if(dataSnapshot.child("ProfileImage").getValue() != null) {
                                Picasso.get().load(p.getProfileImage()).into(circleImageView);
                            }

                            readMessage(firebaseUser.getUid(),userid);
                            Toast.makeText(MessageActivity.this, userid, Toast.LENGTH_SHORT).show();
                            seenMessage(userid);
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


                    // Toast.makeText(UserActivity.this, "not working", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void seenMessage (final String userid){

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Chat chat = dataSnapshot1.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen",true);
                        dataSnapshot1.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(valueEventListener);
    }

    private void sendMessage(String sender, String receiver, String message){

       DatabaseReference Reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isSeen",false);

        Reference.child("Chats").push().setValue(hashMap);

    }

    private void readMessage(final String myid, final String userid){

        chats = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                chats.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Chat chat = dataSnapshot1.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid)
                        || chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        chats.add(chat);
                        messageAdapter= new MessageAdapter(MessageActivity.this,chats);
                        recyclerView.setAdapter(messageAdapter);
                        messageAdapter.notifyDataSetChanged();

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
