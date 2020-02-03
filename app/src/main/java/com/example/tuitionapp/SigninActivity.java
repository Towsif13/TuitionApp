package com.example.tuitionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;

public class SigninActivity extends AppCompatActivity {

    TextView register , resetPassword;
    EditText userEmail , userPassword;
    FloatingActionButton signbtn;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        signbtn = findViewById(R.id.signinFloatingActionButton);

        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInExistingUser();
            }
        });


        mAuth = FirebaseAuth.getInstance();
        //currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        register = findViewById(R.id.registerTextView);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this , RegisterActivity.class);
                finish();
                startActivity(intent);
            }
        });

//        resetPassword = findViewById(R.id.resetTextView);
//        resetPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this , LandingActivity.class);
//                finish();
//                startActivity(intent);
//            }
//        });
    }

    private void signInExistingUser() {
        boolean cancel = false;
        View focusView = null;

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            userEmail.setError(getString(R.string.error_field_required));
            focusView = userEmail;
            cancel = true;
        }
        if(TextUtils.isEmpty(password)){
            userPassword.setError(getString(R.string.error_field_required));
            focusView = userPassword;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            attemptLogin();
        }
    }

    private void attemptLogin() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if(email.equals("") || password.equals("")) return;
        Toast.makeText(this,"Loging in...",Toast.LENGTH_SHORT).show();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    //Toast.makeText(LoginActivity.this, "There was a problem", Toast.LENGTH_SHORT).show();
                    showErrorDialog("There was a problem");
                }else {
                    if (mAuth.getCurrentUser().isEmailVerified()){
                        //String userId = mAuth.getCurrentUser().getUid();
                        RootRef.child("Users").child("Student").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    Intent intent = new Intent(SigninActivity.this,StudentLandingActivity.class);
                                    finish();
                                    startActivity(intent);
                                }
                                else {
                                    Intent intent = new Intent(SigninActivity.this,TeacherLandingActivity.class);
                                    finish();
                                    startActivity(intent);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }else {
                        Toast.makeText(SigninActivity.this, "Verify email please", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
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
