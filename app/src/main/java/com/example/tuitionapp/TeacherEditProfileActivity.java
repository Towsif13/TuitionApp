package com.example.tuitionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherEditProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private ProgressBar progressBar;
    CircleImageView teacherProPic;
    private Uri teacherPicUri=null;
    private StorageReference storageReference;
    private String teacherpropiclink;

    TextView dateOfBirth;
    DatePickerDialog.OnDateSetListener setListener;
    RadioGroup radioGroup;
    RadioButton radioButton;
    String sex = "Male";
    String teacher_department , teacher_year , teacher_region ;

    EditText userfirstname , userlastname , userAddress, userInstitution , userPhone;

    FloatingActionButton editbtn;

    private FirebaseAuth mAuth;

    private FirebaseDatabase mFirebaseDatabase;

    private FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference myref;
    private String usersid;

    String[] mDepartment = {"CSE" , "EEE" , "BBA" , "Physics" , "Math" , "Chemistry"};

    String[] mYear = {"1st Year" , "2nd Year" , "3rd Year" , "4th Year"};

    String[] mRegion = {"Dhaka","Khulna","Sylhet","Ranjshahi","Chittagong","Barisal","Rongpur"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_edit_profile);

        radioGroup = findViewById(R.id.editradioGroup);
        dateOfBirth = findViewById(R.id.editdateOfBirth);
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datepick();
            }
        });

        Spinner spinner_year = findViewById(R.id.editspinner_year);
        Spinner spinner_department = findViewById(R.id.editspinner_department);
        Spinner spinner_region = findViewById(R.id.editspinner_region);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mDepartment);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_department.setAdapter(arrayAdapter);
        spinner_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String department = adapterView.getItemAtPosition(i).toString();
                teacher_department = department;
                Toast.makeText(TeacherEditProfileActivity.this, department, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TeacherEditProfileActivity.this, year, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TeacherEditProfileActivity.this, region, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        usersid = user.getUid();

        storageReference = FirebaseStorage.getInstance().getReference();

        userfirstname = findViewById(R.id.editfirstName);
        userlastname = findViewById(R.id.editlastName);
        userAddress = findViewById(R.id.edituserAddress);
        userPhone = findViewById(R.id.editphone);
        userInstitution = findViewById(R.id.editteacher_institution);

        teacherProPic = findViewById(R.id.editTeacherProfileImage);
        progressBar = findViewById(R.id.progressBar);

        myref.child("Users").child("Teacher").child(usersid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userfirstname.setText(dataSnapshot.child("FirstName").getValue().toString());
                userlastname.setText(dataSnapshot.child("LastName").getValue().toString());
                userPhone.setText(dataSnapshot.child("Phone").getValue().toString());
                dateOfBirth.setText(dataSnapshot.child("Birthday").getValue().toString());
                userAddress.setText(dataSnapshot.child("Address").getValue().toString());
                userInstitution.setText(dataSnapshot.child("Institution").getValue().toString());

                if (dataSnapshot.child("ProfileImage").exists()){
                    String propic = dataSnapshot.child("ProfileImage").getValue().toString();
                    teacherpropiclink=propic;
                    Picasso.get().load(propic).into(teacherProPic);
                    teacherPicUri = Uri.parse(propic);
                    //Toast.makeText(StudentProfileActivity.this, propic, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        editbtn = findViewById(R.id.editFloatingActionButton);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editConditions();
            }
        });

        teacherProPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(TeacherEditProfileActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                teacherPicUri = result.getUri();
                teacherProPic.setImageURI(teacherPicUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void datepick() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                TeacherEditProfileActivity.this,
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
        String institution = userInstitution.getText().toString();

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
        if(TextUtils.isEmpty(institution)){
            userInstitution.setError(getString(R.string.error_field_required));
            focusView = userInstitution;
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
        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher").child(userId);

        progressBar.setVisibility(View.VISIBLE);
        final StorageReference image_path = storageReference.child("ProfileImages").child(userId+".jpg");
        image_path.putFile(teacherPicUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String downloadUrl = uri.toString();
                        myref.child("Users").child("Teacher").child(usersid).child("ProfileImage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(TeacherEditProfileActivity.this, "Image saved in database", Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    Toast.makeText(TeacherEditProfileActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                });
            }
        });

        String fname = userfirstname.getText().toString();
        String lname = userlastname.getText().toString();
        String phone = userPhone.getText().toString();
        String address = userAddress.getText().toString();
        String dob = dateOfBirth.getText().toString();
        String region = teacher_region;
        String year = teacher_year;
        String department = teacher_department;
        String gender = sex;
        String institution = userInstitution.getText().toString();

        HashMap<String,String> profileMap = new HashMap<>();
        profileMap.put("Id",userId);
        profileMap.put("FirstName",fname);
        profileMap.put("LastName",lname);
        profileMap.put("Address",address);
        profileMap.put("Birthday",dob);
        profileMap.put("Gender",gender);
        profileMap.put("Year",year);
        profileMap.put("Department",department);
        profileMap.put("Region",region);
        profileMap.put("Phone",phone);
        profileMap.put("Institution",institution);
        profileMap.put("ProfileImage",teacherPicUri.toString());
        current_user_db.setValue(profileMap);

        Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(TeacherEditProfileActivity.this,TeacherProfileActivity.class);
        finish();
        startActivity(intent);
    }
}
