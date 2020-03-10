package com.example.tuitionapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabAccessAdapter_Teacher extends FragmentStatePagerAdapter {

    int counttab;


    public TabAccessAdapter_Teacher(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.counttab= behavior;
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 0:
                com.example.tuitionapp.HomeFragment_Teacher homeFragment_teacher = new com.example.tuitionapp.HomeFragment_Teacher();
                return homeFragment_teacher;
            case 1:
                com.example.tuitionapp.PostFragment postFragment = new com.example.tuitionapp.PostFragment();
                return postFragment;
            case 2:
                com.example.tuitionapp.ChatsFragment_Teacher chatsFragment_teacher = new com.example.tuitionapp.ChatsFragment_Teacher();
                return chatsFragment_teacher;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return counttab;
    }


}


