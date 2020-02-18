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

public class StudentEditProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    CircleImageView studentProfileImage;
    private static final int gallarypic = 1;
    private StorageReference userProfileImagesRef;

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

    String propiclink;

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

        studentProfileImage = findViewById(R.id.editStudentProfileImage);
        studentProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(StudentEditProfileActivity.this);
//                Intent galleryIntent = new Intent();
//                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("image/*");
//                startActivityForResult(galleryIntent,gallarypic);
//                Toast.makeText(StudentEditProfileActivity.this, "Pro pic", Toast.LENGTH_SHORT).show();
                //TODO: profile pic
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

        userProfileImagesRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");

        myref.child("Users").child("Student").child(usersid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userfirstname.setText(dataSnapshot.child("FirstName").getValue().toString());
                userlastname.setText(dataSnapshot.child("LastName").getValue().toString());
                userPhone.setText(dataSnapshot.child("Phone").getValue().toString());
                dateOfBirth.setText(dataSnapshot.child("Birthday").getValue().toString());
                userAddress.setText(dataSnapshot.child("Address").getValue().toString());

                if (dataSnapshot.child("ProfileImage").exists()){
                    String propic = dataSnapshot.child("ProfileImage").getValue().toString();
                    propiclink=propic;
                    Picasso.get().load(propic).into(studentProfileImage);
                    //Toast.makeText(StudentProfileActivity.this, propic, Toast.LENGTH_LONG).show();

                }

                //String propic = dataSnapshot.child("ProfileImage").getValue().toString();
                //Picasso.get().load(propic).into(studentProfileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode==gallarypic && resultCode==RESULT_OK && data != null)
//        {
//            Uri ImageUri = data.getData();
//
//            CropImage.activity()
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1,1)
//                    .start(this);
//        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK){
                ///
                final Uri resultUri = result.getUri();
                final StorageReference filePath = userProfileImagesRef.child(usersid+".jpg");
                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();
                                myref.child("Users").child("Student").child(usersid).child("ProfileImage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(StudentEditProfileActivity.this, "Image saved in database", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(StudentEditProfileActivity.this,StudentEditProfileActivity.class);
                                            finish();
                                            startActivity(intent);
                                        }
                                        else {
                                            Toast.makeText(StudentEditProfileActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            }
                        });
                    }
                });

                ///
//                Uri resultUri = result.getUri();
//
//                StorageReference filepath = userProfileImagesRef.child(usersid+".jpg");
//                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(StudentEditProfileActivity.this, "DP uploaded", Toast.LENGTH_SHORT).show();
//                            final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
//                            myref.child("Users").child("Student").child(usersid).child("ProfileImage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()){
//                                        Toast.makeText(StudentEditProfileActivity.this, "Image saved in database", Toast.LENGTH_SHORT).show();
//                                    }
//                                    else{
//                                        Toast.makeText(StudentEditProfileActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        }
//                        else{
//                            Toast.makeText(StudentEditProfileActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
            }
        }
        //TODO
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
        profileMap.put("ProfileImage",propiclink);
        current_user_db.setValue(profileMap);

        Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(StudentEditProfileActivity.this,StudentProfileActivity.class);
        finish();
        startActivity(intent);
    }
}
