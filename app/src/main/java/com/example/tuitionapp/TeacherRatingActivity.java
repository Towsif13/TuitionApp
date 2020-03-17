package com.example.tuitionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherRatingActivity extends AppCompatActivity {

    private String teacherUserid;

    private SmileRating smileRatingTeaching , smileRatingTimeliness, smileRatingProfessionalism ;
    private Button submitBtn;
    int mTeachingLevel , mTimeLevel , mProfessionalismLevel ;
    private float overall;

    private Toolbar toolbar;
    private TextView teachScr , timeScr , profScr , overallScr , comment;

    private EditText tutorReview;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myref;
    private String uid;

    private CircleImageView teacherProPic;
    private TextView teacherName;

    String mOverallRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_rating);

        teacherUserid = getIntent().getExtras().getString("userId");
        //Toast.makeText(this, teacherUserid, Toast.LENGTH_LONG).show();

        toolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rate");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        teacherProPic = findViewById(R.id.teacher_propic_review);
        teacherName = findViewById(R.id.teacher_name_review);

        teachScr = findViewById(R.id.teaching_score);
        timeScr = findViewById(R.id.timeliness_score);
        profScr = findViewById(R.id.professionalism_score);
        overallScr = findViewById(R.id.overall_score);
        comment = findViewById(R.id.comment);

        tutorReview = findViewById(R.id.tutor_review);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();

        myref.child("Users").child("Teacher").child(teacherUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fname = dataSnapshot.child("FirstName").getValue().toString();
                String lname = dataSnapshot.child("LastName").getValue().toString();
                String name = fname+" "+lname;
                teacherName.setText(name);

                if (dataSnapshot.child("ProfileImage").exists()){
                    String propic = dataSnapshot.child("ProfileImage").getValue().toString();
                    Picasso.get().load(propic).into(teacherProPic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        smileRatingTeaching = findViewById(R.id.smile_rating_teaching);
        smileRatingTeaching.setSelectedSmile(BaseRating.OKAY);
        smileRatingTimeliness = findViewById(R.id.smile_rating_timeliness);
        smileRatingTimeliness.setSelectedSmile(BaseRating.OKAY);
        smileRatingProfessionalism = findViewById(R.id.smile_rating_professionalism);
        smileRatingProfessionalism.setSelectedSmile(BaseRating.OKAY);

        mTeachingLevel = smileRatingTeaching.getRating();
        mTimeLevel = smileRatingTimeliness.getRating();
        mProfessionalismLevel = smileRatingProfessionalism.getRating();// level is from 1 to 5
        // Will return 0 if NONE selected






        smileRatingTeaching.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {
                // level is from 1 to 5 (0 when none selected)
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                mTeachingLevel = level;
                Toast.makeText(TeacherRatingActivity.this, "Level "+ level, Toast.LENGTH_SHORT).show();
            }
        });

        smileRatingTimeliness.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {
                // level is from 1 to 5 (0 when none selected)
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                mTimeLevel = level;
                Toast.makeText(TeacherRatingActivity.this, "Level "+ level, Toast.LENGTH_SHORT).show();
            }
        });

        smileRatingProfessionalism.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {
                // level is from 1 to 5 (0 when none selected)
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                mProfessionalismLevel = level;
                Toast.makeText(TeacherRatingActivity.this, "Level "+ level, Toast.LENGTH_SHORT).show();
            }
        });

        submitBtn = findViewById(R.id.rating_submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                teachScr.setText(String.valueOf(mTeachingLevel));
                timeScr.setText(String.valueOf(mTimeLevel));
                profScr.setText(String.valueOf(mProfessionalismLevel));
                overall = (float) ((mTimeLevel+mTeachingLevel+mProfessionalismLevel)/3.0);
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(1);
                overallScr.setText(String.valueOf(df.format(overall)));
                comment.setText(tutorReview.getText());
                //Toast.makeText(TeacherRatingActivity.this, "Pint "+ mlevel, Toast.LENGTH_SHORT).show();

                //TODO: Database work
                String comment = tutorReview.getText().toString();
                DecimalFormat df2 = new DecimalFormat();
                df2.setMaximumFractionDigits(1);
                Float ov = overall;
                String overall = String.valueOf(df2.format(ov));
                mOverallRating = overall;
//
//                DatabaseReference rating_db = FirebaseDatabase.getInstance().getReference().child("Review").child(teacherUserid);
//                HashMap<String,String> rateMap = new HashMap<>();
//                rateMap.put("Overall",overall);
//                rating_db.setValue(rateMap);

                DatabaseReference review_db = FirebaseDatabase.getInstance().getReference().child("Review").child(teacherUserid).child(uid);
                HashMap<String,String> reviewMap = new HashMap<>();
                reviewMap.put("Review",comment);
                reviewMap.put("Overall",overall);
                reviewMap.put("Teaching",String.valueOf(mTeachingLevel));
                reviewMap.put("Timeliness",String.valueOf(mTimeLevel));
                reviewMap.put("Professionalism",String.valueOf(mProfessionalismLevel));
                review_db.setValue(reviewMap);
                myref.child(teacherUserid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(teacherUserid).exists()){
                            Toast.makeText(TeacherRatingActivity.this, "exists", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(TeacherRatingActivity.this, "exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


//                DatabaseReference rating_db = FirebaseDatabase.getInstance().getReference().child("Rating").child(teacherUserid);
//                HashMap<String,String> rateMap = new HashMap<>();
//                rateMap.put("Rating",overall);
//                rating_db.setValue(rateMap);



            }
        });
    }
}
