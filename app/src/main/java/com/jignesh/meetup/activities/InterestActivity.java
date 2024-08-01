package com.jignesh.meetup.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.jignesh.meetup.R;
import com.jignesh.meetup.adapters.InterestViewPagerAdapter;
import com.jignesh.meetup.fragments.LocationLanguageFragment;
import com.jignesh.meetup.fragments.ProfessionHobbyFragment;
import com.jignesh.meetup.fragments.SetUpProfileFragment;
import com.jignesh.meetup.fragments.SportFragment;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;

public class InterestActivity extends AppCompatActivity {

    public static ViewPager interestViewPager;
    WormDotsIndicator wormDotsIndicator;
    InterestViewPagerAdapter interestViewPagerAdapter;
    ArrayList<Fragment> alFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        interestViewPager = findViewById(R.id.interest_view_pager);
        wormDotsIndicator = findViewById(R.id.worm_dots_indicator);

        alFragments.add(new SetUpProfileFragment());
        alFragments.add(new LocationLanguageFragment());
        alFragments.add(new SportFragment());
        alFragments.add(new ProfessionHobbyFragment());

        interestViewPagerAdapter = new InterestViewPagerAdapter(getSupportFragmentManager(), alFragments);
        interestViewPager.setAdapter(interestViewPagerAdapter);
        wormDotsIndicator.setViewPager(interestViewPager);
    }
}