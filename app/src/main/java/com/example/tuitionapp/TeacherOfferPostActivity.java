package com.example.tuitionapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class TeacherOfferPostActivity extends AppCompatActivity {

    private EditText subjects , salary , notes;
    private Spinner spinner_preference_medium , spinner_days;

    private String prefered_medium , days_per_week;

    private String[] mPreferenceMedium = {"Any" , "Bangla Medium" , "English Medium" , "English Version"};
    private String[] mDays = {"1 day" , "2 days", "3 days", "4 days", "5 days", "6 days", "7 days"};

    private Button post;

    private FirebaseAuth mAuth;

    private String fname , lname , department , year , region, propic;

    private String date;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_offer_post);

        DateFormat df = new SimpleDateFormat("d MMM yyyy, h:mm a");
        date = df.format(Calendar.getInstance().getTime());
        Toast.makeText(this, date, Toast.LENGTH_LONG).show();

        spinner_days = findViewById(R.id.tutor_days);
        spinner_preference_medium = findViewById(R.id.tutor_preference_medium);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mPreferenceMedium);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_preference_medium.setAdapter(arrayAdapter);
        spinner_preference_medium.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String pref_medium = adapterView.getItemAtPosition(i).toString();
                prefered_medium = pref_medium;
                //Toast.makeText(TuitionPostActivity.this, prefered_gender, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mDays);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_days.setAdapter(arrayAdapter1);
        spinner_days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String dpw = adapterView.getItemAtPosition(i).toString();
                days_per_week = dpw;
                //Toast.makeText(TuitionPostActivity.this, days_per_week, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        salary = findViewById(R.id.tutor_salary);
        subjects = findViewById(R.id.tutor_subjects);
        notes = findViewById(R.id.tutor_notes);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference();

        getDataFromProfile();

        post = findViewById(R.id.button_tutor_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptPost();
            }
        });

    }

    private void getDataFromProfile() {
        String userId = mAuth.getCurrentUser().getUid();

        myref.child("Users").child("Teacher").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fname = dataSnapshot.child("FirstName").getValue().toString();
                lname = dataSnapshot.child("LastName").getValue().toString();
                department = dataSnapshot.child("Department").getValue().toString();
                year = dataSnapshot.child("Year").getValue().toString();
                region = dataSnapshot.child("Region").getValue().toString();
                propic = dataSnapshot.child("ProfileImage").getValue().toString();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void attemptPost() {

        String sub = subjects.getText().toString();
        String sal = salary.getText().toString();

        int note_len = notes.getText().toString().length();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(sub)){
            subjects.setError(getString(R.string.error_field_required));
            focusView = subjects;
            cancel =true;
        }
        if(TextUtils.isEmpty(sal)){
            salary.setError(getString(R.string.error_field_required));
            focusView = salary;
            cancel = true;
        }
        if(note_len > 100){
            notes.setError(getString(R.string.error_note_too_long));
            focusView = salary;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // TODO: Call create FirebaseUser() here
            createPostInFirebase();
            Toast.makeText(this, String.valueOf(note_len), Toast.LENGTH_SHORT).show();

        }
    }

    private void createPostInFirebase() {

        DateFormat df = new SimpleDateFormat("d-MM-yyyy,HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());

        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("TuitionPosts").child("TeacherPosts").child(userId+" "+date);


        String desired_subjects = subjects.getText().toString();
        String desired_salary = salary.getText().toString();
        String desired_days = days_per_week;
        String desired_medium = prefered_medium;
        String desired_note = notes.getText().toString();


        DateFormat df1 = new SimpleDateFormat("d-MM-yyyy");
        String today_date = df1.format(Calendar.getInstance().getTime());

        DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
        String today_time = df2.format(Calendar.getInstance().getTime());

        HashMap<String,String> offerMap = new HashMap<>();
        offerMap.put("id",userId);
        offerMap.put("Subjects",desired_subjects);
        offerMap.put("Days",desired_days);
        offerMap.put("PreferredMedium",desired_medium);
        offerMap.put("Salary",desired_salary);
        offerMap.put("Notes",desired_note);

        offerMap.put("FirstName",fname);
        offerMap.put("LastName",lname);
        offerMap.put("Department",department);
        offerMap.put("Year",year);
        offerMap.put("Region",region);

        offerMap.put("Date",today_date);
        offerMap.put("Time",today_time);
        offerMap.put("ProfileImage",propic);



        current_user_db.setValue(offerMap);

        showSuccessDialog("Offer Posted");

    }

    private void showSuccessDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Congratulation")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(TeacherOfferPostActivity.this, TeacherLandingActivity.class);
                        finish();
                        startActivity(intent);

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
