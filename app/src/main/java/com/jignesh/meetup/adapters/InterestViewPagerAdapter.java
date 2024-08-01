package com.jignesh.meetup.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class InterestViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> alFragments;

    public InterestViewPagerAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> alFragments) {
        super(fm);
        this.alFragments = alFragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return alFragments.get(position);
    }

    @Override
    public int getCount() {
        return alFragments.size();
    }
}
