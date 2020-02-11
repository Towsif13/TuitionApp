package com.example.tuitionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class StudentEditProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    TextView dateOfBirth;
    DatePickerDialog.OnDateSetListener setListener;
    RadioGroup radioGroup;
    RadioButton radioButton;
    String sex = "Male";
    String student_class , student_medium , student_region;

    EditText userfirstname , userlastname , userAddress , userPhone;

    FloatingActionButton editbtn;

    private FirebaseAuth mAuth;

    private FirebaseDatabase mFirebaseDatabase;

    private FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference myref;
    private String usersid;

    String[] mClass = {"Class 1","Class 2","Class 3","Class 4","Class 5","Class 6","Class 7",
            "Class 8","SSC","HSC","A level","O level"};

    String[] mMedium = {"Bangla Medium" , "English Medium" , "English Version"};

    String[] mRegion = {"Dhaka","Khulna","Sylhet","Rajshahi","Chittagong","Barisal","Rongpur"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit_profile);

        radioGroup = findViewById(R.id.editradioGroup);

        dateOfBirth = findViewById(R.id.editdateOfBirth);
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datepick();
            }
        });


        Spinner spinner_class = findViewById(R.id.editspinner_class);
        Spinner spinner_medium = findViewById(R.id.editspinner_medium);
        Spinner spinner_region = findViewById(R.id.editspinner_region);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mClass);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_class.setAdapter(arrayAdapter);
        spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String classs = adapterView.getItemAtPosition(i).toString();
                student_class = classs;
                Toast.makeText(StudentEditProfileActivity.this, classs, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mMedium);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_medium.setAdapter(arrayAdapter1);
        spinner_medium.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String medium = adapterView.getItemAtPosition(i).toString();
                student_medium = medium;
                Toast.makeText(StudentEditProfileActivity.this, medium, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mRegion);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_region.setAdapter(arrayAdapter2);
        spinner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String region = adapterView.getItemAtPosition(i).toString();
                student_region = region;
                Toast.makeText(StudentEditProfileActivity.this, region, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        userfirstname = findViewById(R.id.editfirstName);
        userlastname = findViewById(R.id.editlastName);
        userAddress = findViewById(R.id.edituserAddress);
        userPhone = findViewById(R.id.editphone);



        editbtn = findViewById(R.id.editFloatingActionButton);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editConditions();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        usersid = user.getUid();

        myref.child("Users").child("Student").child(usersid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userfirstname.setText(dataSnapshot.child("FirstName").getValue().toString());
                userlastname.setText(dataSnapshot.child("LastName").getValue().toString());
                userPhone.setText(dataSnapshot.child("Phone").getValue().toString());
                dateOfBirth.setText(dataSnapshot.child("Birthday").getValue().toString());
                userAddress.setText(dataSnapshot.child("Address").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void datepick() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                StudentEditProfileActivity.this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month = month+1;
        String date = day+"/"+month+"/"+year;
        dateOfBirth.setText(date);
    }

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this, radioButton.getText(), Toast.LENGTH_SHORT).show();
        sex = radioButton.getText().toString();
    }

    public void editConditions(){
        String fname = userfirstname.getText().toString();
        String lname = userlastname.getText().toString();
        String phone = userPhone.getText().toString();
        String address = userAddress.getText().toString();
        String dob = dateOfBirth.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(fname)){
            userfirstname.setError(getString(R.string.error_field_required));
            focusView = userfirstname;
            cancel =true;
        }
        if(TextUtils.isEmpty(lname)){
            userlastname.setError(getString(R.string.error_field_required));
            focusView = userlastname;
            cancel = true;
        }
        if(TextUtils.isEmpty(phone)){
            userPhone.setError(getString(R.string.error_field_required));
            focusView = userPhone;
            cancel =true;
        }
        if(TextUtils.isEmpty(address)){
            userAddress.setError(getString(R.string.error_field_required));
            focusView = userAddress;
            cancel =true;
        }
        if(TextUtils.isEmpty(dob)){
            dateOfBirth.setError(getString(R.string.error_field_required));
            focusView = dateOfBirth;
            cancel =true;
        }
        if (cancel) {
            // There was an error;  focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            editProfile();
        }
    }

    public void editProfile(){
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Student").child(userId);

        String fname = userfirstname.getText().toString();
        String lname = userlastname.getText().toString();
        String phone = userPhone.getText().toString();
        String address = userAddress.getText().toString();
        String dob = dateOfBirth.getText().toString();
        String region = student_region;
        String medium = student_medium;
        String classs = student_class;
        String gender = sex;

        HashMap<String,String> profileMap = new HashMap<>();
        profileMap.put("Id",userId);
        profileMap.put("FirstName",fname);
        profileMap.put("LastName",lname);
        profileMap.put("Address",address);
        profileMap.put("Birthday",dob);
        profileMap.put("Gender",gender);
        profileMap.put("Class",classs);
        profileMap.put("Medium",medium);
        profileMap.put("Region",region);
        profileMap.put("Phone",phone);
        current_user_db.setValue(profileMap);

        Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(StudentEditProfileActivity.this,StudentProfileActivity.class);
        finish();
        startActivity(intent);
    }
}
