package com.example.android.employeesmanagementsoftware;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
 import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.employeesmanagementsoftware.SiteDB.DepFragment;
import com.example.android.employeesmanagementsoftware.TaskCreation.TaskCreation;
import com.example.android.employeesmanagementsoftware.Utilities.SectionsPageAdapter;
import com.example.android.employeesmanagementsoftware.taskDB.TasksFragment;


public class StartingPageActivity extends AppCompatActivity {



    private SectionsPageAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);
        //Add ACTION BAR
        toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Sites");
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        setupViewPager(mViewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SiteCreation.class);
                intent.putExtra("IsEdit", false);
                startActivity(intent);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    toolbar.setTitle("Tasks");
                    setSupportActionBar(toolbar);
                    fab = findViewById(R.id.fab);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), TaskCreation.class);
                            intent.putExtra("task_id",(long)-1);
                            startActivity(intent);
                        }
                    });
                } else {
                    toolbar.setTitle("Sites");
                    setSupportActionBar(toolbar);
                    fab =  findViewById(R.id.fab);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), SiteCreation.class);
                            intent.putExtra("IsEdit", false);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new DepFragment(), "SITES");
        adapter.addFragment(TasksFragment.newInstance(), "TASKS");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            return inflater.inflate(R.layout.fragment_starting_page, container, false);
        }
    }

}
