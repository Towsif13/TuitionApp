package com.example.tuitionapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabAccessAdapter_Student extends FragmentStatePagerAdapter{

    int counttab;

    //Constructor
    public TabAccessAdapter_Student(@NonNull FragmentManager fm,int counttab) {
        super(fm);
        this.counttab = counttab;
    }

    @Override
        public Fragment getItem(int position) {

        switch(position){
            case 0:
                HomeFragment_Student homeFragment_student = new HomeFragment_Student();
                return homeFragment_student;
            case 1:
                PostFragment_Student postFragment_student = new PostFragment_Student();
                return postFragment_student;
            case 2:
                ChatsFragment_Student chatsFragment = new ChatsFragment_Student();
                return chatsFragment;
            default:
                return null;
        }
    }

        @Override
        public int getCount() {
        return counttab;
    }

}
