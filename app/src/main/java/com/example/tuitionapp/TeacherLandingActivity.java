package com.example.tuitionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class TeacherLandingActivity extends AppCompatActivity {

    //Elements used to make top navigation
    public Toolbar toolbar;
    public ViewPager viewPager;
    public TabLayout tabLayout;

    public TabAccessAdapter_Teacher tabsAccessorAdapter_teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_landing);

        //Toolbar is added
        toolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Porao");

        viewPager = findViewById(R.id.main_tabs_pager);
        tabLayout = findViewById(R.id.main_tabs);
        //Adapter for fragments
        tabsAccessorAdapter_teacher = new TabAccessAdapter_Teacher(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(tabsAccessorAdapter_teacher);
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

}
