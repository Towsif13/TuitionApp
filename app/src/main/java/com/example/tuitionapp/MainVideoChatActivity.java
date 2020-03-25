package com.example.tuitionapp;

import android.Manifest;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainVideoChatActivity extends AppCompatActivity implements  Session.SessionListener,PublisherKit.PublisherListener {


    private static  String API_key="46608752";
    private static  String SESSION_ID="2_MX40NjYwODc1Mn5-MTU4NTEzNjk3NzE4NX52cWlvYTY5a05VUWNNQzVaZDFsSEFoR3p-fg";
    private static  String TOKEN ="T1==cGFydG5lcl9pZD00NjYwODc1MiZzaWc9ODcxMTNjNzY5M2RlMmUyNmNlOWQwZTQwNWU3YzgyMzBhNDVjMDVlNDpzZXNzaW9uX2lkPTJfTVg0ME5qWXdPRGMxTW41LU1UVTROVEV6TmprM056RTROWDUyY1dsdllUWTVhMDVWVVdOTlF6VmFaREZzU0VGb1IzcC1mZyZjcmVhdGVfdGltZT0xNTg1MTM3MDQwJm5vbmNlPTAuMjcwNTgxMzQxMzUzNDE1NyZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTg3NzI5MDM5JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static final String LOG_TAG = MainVideoChatActivity.class.getSimpleName();
    private static  final int RC_VIDEO_APP_PERM =124;

    private Session mSession;// video stream session // ther

    private Publisher mPublisher; // sender of the call

    private Subscriber mSubscriber;

    private ImageView closeVidChatBtn;

    private DatabaseReference userRef;
    private  String userID ="";

    private FrameLayout mPublisherViewController;
    private FrameLayout mSubscriberViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_video_chat);


        closeVidChatBtn=findViewById(R.id.CloseVidChat);




        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        requestPermissions();


        closeVidChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("Teacher").child(userID).hasChild("Ringing"))
                        {



                            userRef.child("Teacher").child(userID).child("Ringing").removeValue();

                            if(mPublisher!= null){
                                mPublisher.destroy();
                            }
                            if(mSubscriber!= null){
                                mSubscriber.destroy();
                            }

                            Intent intent = new Intent(MainVideoChatActivity.this,MainActivity.class);
                            startActivity(intent );
                            finish();
                        }

                        if(dataSnapshot.child("Student").child(userID).hasChild("Calling"))
                        {
                            userRef.child("Student").child(userID).child("Calling").removeValue();

                            if(mPublisher!= null){
                                mPublisher.destroy();
                            }
                            if(mSubscriber!= null){
                                mSubscriber.destroy();
                            }

                            Intent intent = new Intent(MainVideoChatActivity.this,MainActivity.class);
                            startActivity(intent );
                            finish();
                        }
                        else {

                            if(mPublisher!= null){
                                mPublisher.destroy();
                            }
                            if(mSubscriber!= null){
                                mSubscriber.destroy();
                            }
                            Intent intent = new Intent(MainVideoChatActivity.this,MainActivity.class);
                            startActivity(intent );
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });





    }

    // adding permission


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,MainVideoChatActivity.this);


    }


    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private  void requestPermissions(){


        String [] perms = {Manifest.permission.INTERNET,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};

        Log.d("chekcing "," True");
        if (EasyPermissions.hasPermissions(this,perms))
        {

            mPublisherViewController =findViewById(R.id.publisher_container);
            mSubscriberViewController = findViewById(R.id.subscriber_container);

            Log.d("ifadhukse "," True");

            // 1st  initialize and connect to the session
            mSession = new Session.Builder(this,API_key,SESSION_ID).build();//
            // session listener
            mSession.setSessionListener(MainVideoChatActivity.this);

            mSession.connect(TOKEN);

        }else {
            EasyPermissions.requestPermissions(this,"Hey THis App Needs Mic And Cam ,Please Allow",RC_VIDEO_APP_PERM,perms);
        }

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
 // publiushing stream to the session
    @Override
    public void onConnected(Session session) {


        Log.i(LOG_TAG,"Session Connected");
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(MainVideoChatActivity.this);

        mPublisherViewController.addView(mPublisher.getView());
        if(mPublisher.getView() instanceof GLSurfaceView){
            ((GLSurfaceView)mPublisher.getView()).setZOrderOnTop(true);
        }

        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {

        Log.i(LOG_TAG,"Stream Disconnected");

    }

    //3rd step subscribing to the stream
    @Override
    public void onStreamReceived(Session session, Stream stream) {

        Log.i(LOG_TAG,"Stream Recieved");
        if(mSubscriber == null)
        {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberViewController.addView(mSubscriber.getView());
        }

    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        Log.i(LOG_TAG,"Stream Dropped");

        if(mSubscriber!=null){
            mSubscriber=null;
            mSubscriberViewController.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
