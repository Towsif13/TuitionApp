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
import android.widget.ProgressBar;
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

public class TeacherRegistrationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private ProgressBar progressBarTeacherReg;

    TextView login;
    TextView dateOfBirth;
    DatePickerDialog.OnDateSetListener setListener;
    RadioGroup radioGroup;
    RadioButton radioButton;
    String sex = "Male";
    String teacher_department , teacher_year , teacher_region ;

    EditText userfirstname , userlastname , userEmail , userPassword , userConfirmPassword , userAddress, userInstitution , userPhone;

    FloatingActionButton registerbtn;

    private FirebaseAuth mAuth;

    String[] mDepartment = {"CSE" , "EEE" , "BBA" , "Physics" , "Math" , "Chemistry"};

    String[] mYear = {"1st Year" , "2nd Year" , "3rd Year" , "4th Year"};

    String[] mRegion = {"Dhaka","Khulna","Sylhet","Ranjshahi","Chittagong","Barisal","Rongpur"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_registration);

        progressBarTeacherReg = findViewById(R.id.progressBarTeacherReg);
        progressBarTeacherReg.setVisibility(View.GONE);

        login = findViewById(R.id.signintext);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(TeacherRegistrationActivity.this , SigninActivity.class);
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

        Spinner spinner_year = findViewById(R.id.spinner_year);
        Spinner spinner_department = findViewById(R.id.spinner_department);
        Spinner spinner_region = findViewById(R.id.spinner_region);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mDepartment);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_department.setAdapter(arrayAdapter);
        spinner_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String department = adapterView.getItemAtPosition(i).toString();
                teacher_department = department;
                Toast.makeText(TeacherRegistrationActivity.this, department, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mYear);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(arrayAdapter1);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String year = adapterView.getItemAtPosition(i).toString();
                teacher_year = year;
                Toast.makeText(TeacherRegistrationActivity.this, year, Toast.LENGTH_SHORT).show();
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
                teacher_region = region;
                Toast.makeText(TeacherRegistrationActivity.this, region, Toast.LENGTH_SHORT).show();
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
        userInstitution = findViewById(R.id.teacher_institution);
        userPhone = findViewById(R.id.phone);

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
                TeacherRegistrationActivity.this,
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
        String institution = userInstitution.getText().toString();
        String phone = userPhone.getText().toString();

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
        } else if (!email.contains(".edu")){
            userEmail.setError("You must use university email");
            focusView = userEmail;
            cancel = true;
        }//checks for university email
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
        if(TextUtils.isEmpty(institution)){
            userInstitution.setError(getString(R.string.error_field_required));
            focusView = userInstitution;
            cancel = true;
        }
        if(TextUtils.isEmpty(phone)){
            userPhone.setError(getString(R.string.error_field_required));
            focusView = userPhone;
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
        progressBarTeacherReg.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(uemail,upassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher").child(userId);

                                String fname = userfirstname.getText().toString();
                                String lname = userlastname.getText().toString();
                                String address = userAddress.getText().toString();
                                String birthday = dateOfBirth.getText().toString();
                                String gender = sex;
                                String department = teacher_department;
                                String year = teacher_year;
                                String region = teacher_region;
                                String institution = userInstitution.getText().toString();
                                String phone = userPhone.getText().toString();

                                HashMap<String,String> profileMap = new HashMap<>();
                                profileMap.put("id",userId);
                                profileMap.put("FirstName",fname);
                                profileMap.put("LastName",lname);
                                profileMap.put("Address",address);
                                profileMap.put("Birthday",birthday);
                                profileMap.put("Gender",gender);
                                profileMap.put("Department",department);
                                profileMap.put("Year",year);
                                profileMap.put("Region",region);
                                profileMap.put("Institution",institution);
                                profileMap.put("Phone",phone);
                                current_user_db.setValue(profileMap);

                                progressBarTeacherReg.setVisibility(View.GONE);
                                //Toast.makeText(TeacherRegistrationActivity.this, "Registration Complete Verify Email", Toast.LENGTH_SHORT).show();
                                showSuccessDialog("Registration complete verify email");
                            }
                            else {
                                progressBarTeacherReg.setVisibility(View.GONE);
                                Toast.makeText(TeacherRegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                if (!task.isSuccessful()){
                    progressBarTeacherReg.setVisibility(View.GONE);
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
                        Intent intent = new Intent(TeacherRegistrationActivity.this, SigninActivity.class);
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
