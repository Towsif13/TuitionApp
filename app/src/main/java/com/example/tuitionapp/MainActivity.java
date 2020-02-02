package com.example.tuitionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button teacher , student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this , SigninActivity.class);
        finish();
        startActivity(intent);
        //efaz

//        teacher = findViewById(R.id.button_teacher);
//        student = findViewById(R.id.button_student);
//
//        teacher.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this , SigninActivity.class);
//                finish();
//                startActivity(intent);
//            }
//        });
//
//        student.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this , SigninActivity.class);
//                finish();
//                startActivity(intent);
//            }
//        });
    }
}
