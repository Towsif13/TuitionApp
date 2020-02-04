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
                HomeFragment_Teacher homeFragment_teacher = new HomeFragment_Teacher();
                return homeFragment_teacher;
            case 1:
                ChatsFragment_Teacher chatsFragment_teacher = new ChatsFragment_Teacher();
                return chatsFragment_teacher;
            case 2:
                PostFragment postFragment = new PostFragment();
                return postFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return counttab;
    }


}


