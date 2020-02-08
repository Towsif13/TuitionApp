package com.example.tuitionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class StudentRegistrationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView login;
    TextView dateOfBirth;
    DatePickerDialog.OnDateSetListener setListener;
    RadioGroup radioGroup;
    RadioButton radioButton;
    String sex = "Male";
    String student_class , student_medium , student_region;

    EditText userfirstname , userlastname , userEmail , userPassword , userConfirmPassword , userAddress;

    FloatingActionButton registerbtn;

    private FirebaseAuth mAuth;

    String[] mClass = {"Class 1","Class 2","Class 3","Class 4","Class 5","Class 6","Class 7",
            "Class 8","SSC","HSC","A level","O level"};

    String[] mMedium = {"Bangla Medium" , "English Medium" , "English Version"};

    String[] mRegion = {"Dhaka","Khulna","Sylhet","Rajshahi","Chittagong","Barisal","Rongpur"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        login = findViewById(R.id.signintext);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(StudentRegistrationActivity.this , SigninActivity.class);
                finish();
                startActivity(intent);
            }
        });

        radioGroup = findViewById(R.id.radioGroup);

        dateOfBirth = findViewById(R.id.dateOfBirth);
        //  dateOfBirth.setInputType(InputType.TYPE_NULL); ( for edit text this will remove the keypad )
        // (but its better in textview)

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datepick();
            }
        });

        Spinner spinner_class = findViewById(R.id.spinner_class);
        Spinner spinner_medium = findViewById(R.id.spinner_medium);
        Spinner spinner_region = findViewById(R.id.spinner_region);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mClass);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_class.setAdapter(arrayAdapter);
        spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String classs = adapterView.getItemAtPosition(i).toString();
                student_class = classs;
                Toast.makeText(StudentRegistrationActivity.this, classs, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(StudentRegistrationActivity.this, medium, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(StudentRegistrationActivity.this, region, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        userfirstname = findViewById(R.id.firstName);
        userlastname = findViewById(R.id.lastName);
        userEmail = findViewById(R.id.userEmail);
        userAddress = findViewById(R.id.userAddress);
        userPassword = findViewById(R.id.userPassword);
        userConfirmPassword = findViewById(R.id.confirmPassword);

        mAuth = FirebaseAuth.getInstance();

        registerbtn = findViewById(R.id.registerFloatingActionButton);
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });
    }

    public void datepick() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                StudentRegistrationActivity.this,
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
        //Toast.makeText(this, radioButton.getText(), Toast.LENGTH_SHORT).show();
        sex = radioButton.getText().toString();
    }


    private void attemptRegistration() {
        // Reset errors displayed in the form.
        userEmail.setError(null);
        userPassword.setError(null);

        // Store values at the time of the login attempt
        String fname = userfirstname.getText().toString();
        String lname = userlastname.getText().toString();
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String confirmPassword = userConfirmPassword.getText().toString();
        String address = userAddress.getText().toString();
        String birthday = dateOfBirth.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            userPassword.setError(getString(R.string.error_invalid_password));
            focusView = userPassword;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            userEmail.setError(getString(R.string.error_field_required));
            focusView = userEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            userEmail.setError(getString(R.string.error_invalid_email));
            focusView = userEmail;
            cancel = true;
        }
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
        if(TextUtils.isEmpty(address)){
            userAddress.setError(getString(R.string.error_field_required));
            focusView = userAddress;
            cancel = true;
        }
        if(TextUtils.isEmpty(birthday)){
            dateOfBirth.setError(getString(R.string.error_field_required));
            focusView = dateOfBirth;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // TODO: Call create FirebaseUser() here
            createFirebaseUser();

        }
    }

    private boolean isEmailValid(String email){
        return email.contains("@");
    }

    private boolean isPasswordValid(String password){
        String confirmPassword = userConfirmPassword.getText().toString();
        return confirmPassword.equals(password) && password.length() > 6;
    }

    private void createFirebaseUser(){
        String uemail = userEmail.getText().toString();
        String upassword = userPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(uemail,upassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Student").child(userId);

                                String fname = userfirstname.getText().toString();
                                String lname = userlastname.getText().toString();
                                String address = userAddress.getText().toString();
                                String birthday = dateOfBirth.getText().toString();
                                String gender = sex;
                                String clas = student_class;
                                String medium = student_medium;
                                String region = student_region;

                                HashMap<String,String> profileMap = new HashMap<>();
                                profileMap.put("FirstName",fname);
                                profileMap.put("LastName",lname);
                                profileMap.put("Address",address);
                                profileMap.put("Birthday",birthday);
                                profileMap.put("Gender",gender);
                                profileMap.put("Class",clas);
                                profileMap.put("Medium",medium);
                                profileMap.put("Region",region);
                                current_user_db.setValue(profileMap);

                                Toast.makeText(StudentRegistrationActivity.this, "Registration Complete Verify Email", Toast.LENGTH_SHORT).show();
                                showSuccessDialog("Registration complete verify email");
                            }
                            else {
                                Toast.makeText(StudentRegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                if (!task.isSuccessful()){
                    //Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    showErrorDialog("Registration failed");
                }
            }
        });

    }

    private void showSuccessDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Congratulation")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(StudentRegistrationActivity.this, SigninActivity.class);
                        finish();
                        startActivity(intent);

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
