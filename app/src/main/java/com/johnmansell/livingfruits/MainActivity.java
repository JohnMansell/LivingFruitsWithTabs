package com.johnmansell.livingfruits;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.livingfruits.johnmansell.R;

public class MainActivity extends AppCompatActivity {

    //ToDo: Add google maps intent to contact screen
    //ToDo: Add phone call intent to contact screen

    //-------------------------------------------
    //  Google Code
    //===========================================

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    //-------------------------------------------
    //  On Create
    //===========================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //-----------------------------------
        //  Set Current page
        //-----------------------------------
        int defaultValue = 0;
        int page = getIntent().getIntExtra("Page", defaultValue);
        mViewPager.setCurrentItem(page);

    }

    //-------------------------------------------
    //  On Create Options Menu
    //===========================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //-------------------------------------------
    //  On Options Item Selected
    //===========================================
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //-------------------------------------------
    //  Place Holder Fragment
    //===========================================
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        //-------------------------------------------
        //  On Create View
        //===========================================
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Open Tab Specific Fragments
            int myFragment;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    myFragment = R.layout.fragment_order;
                    break;
                case 2:
                    myFragment = R.layout.fragment_about_us;
                    break;
                case 3:
                    myFragment = R.layout.fragment_contact;
                    break;
                default:
                    myFragment = R.layout.fragment_menu;
            }



            View rootView = inflater.inflate(myFragment, container, false);

            // Set Cover photo text
            String spanCoverText = "About\nLiving Fruits\nBlueberries";
            SpannableString mySpanable = new SpannableString(spanCoverText);
            mySpanable.setSpan(new RelativeSizeSpan(1.5f), 6, 31, 0);
            TextView aboutCoverText;
            if (rootView.findViewById(R.id.aboutUsCover) != null) {
                aboutCoverText = rootView.findViewById(R.id.aboutUsCover);
                aboutCoverText.setText(mySpanable);
            }


            return rootView;
        }
    }

    //-------------------------------------------
    //  Get Tab Specific Fragments
    //===========================================
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return OrderScreenFragment.newInstance();

                case 2:
                    return contact_fragment.newInstance();

                default:
                    return PlaceholderFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        //-------------------------------------------
        //  Get Page Title
        //===========================================
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Order Now";
                case 1:
                    return "About Us";
                case 2:
                    return "Contact";
            }
            return null;
        }
    }
}
