package com.example.tuitionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;


public class StudentLandingActivity extends AppCompatActivity {

    //Elements used to make top navigation
    public Toolbar toolbar;
    public ViewPager viewPager;
    public TabLayout tabLayout;

    public TabAccessAdapter_Student tabsAccessorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_landing);


        //Toolbar is added
        toolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Porao");

        viewPager = findViewById(R.id.main_tabs_pager);
        tabLayout = findViewById(R.id.main_tabs);
        //Adapter for fragments
        tabsAccessorAdapter = new TabAccessAdapter_Student(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(tabsAccessorAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //When fragments are clicked
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.main_menu_logout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(StudentLandingActivity.this, SigninActivity.class);
            startActivity(intent);
            //Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}






