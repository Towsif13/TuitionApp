package com.example.tuitionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
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

import java.util.Calendar;

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
}
