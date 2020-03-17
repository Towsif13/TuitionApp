package com.example.tuitionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import java.text.DecimalFormat;

public class TeacherRatingActivity extends AppCompatActivity {

    private String userid;

    private SmileRating smileRatingTeaching , smileRatingTimeliness, smileRatingProfessionalism ;
    private Button submitBtn;
    int mTeachingLevel , mTimeLevel , mProfessionalismLevel ;
    private float overall;

    private Toolbar toolbar;
    private TextView teachScr , timeScr , profScr , overallScr , comment;

    private EditText tutorReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_rating);

        userid = getIntent().getExtras().getString("userId");
        Toast.makeText(this, userid, Toast.LENGTH_LONG).show();

        toolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rate");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        teachScr = findViewById(R.id.teaching_score);
        timeScr = findViewById(R.id.timeliness_score);
        profScr = findViewById(R.id.professionalism_score);
        overallScr = findViewById(R.id.overall_score);
        comment = findViewById(R.id.comment);
        tutorReview = findViewById(R.id.tutor_review);

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
            }
        });
    }
}
