package uk.ac.kent.pceh3.miniproject.ui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.ac.kent.pceh3.miniproject.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public FeedFragment feedFragment;
    public ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        // Get the SearchView and set the searchable configuration
        final MenuItem myActionMenuItem = menu.findItem( R.id.app_bar_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String search) {
                viewPager.setCurrentItem(0, true);

                int pos = viewPager.getCurrentItem();
                Fragment activeFragment = adapter.getItem(pos);

                if(pos == 0){
                    ((FeedFragment)activeFragment).loadFeed(search);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchView.setOnCloseListener (new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                int pos = viewPager.getCurrentItem();
                Fragment activeFragment = adapter.getItem(pos);

                if(pos == 0){
                    String search = "";
                    ((FeedFragment)activeFragment).loadFeed(search);
                }

                return false;
            }
        });

        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FeedFragment(), "FEED");
        adapter.addFragment(new SavedFragment(), "SAVED");
        adapter.addFragment(new AboutFragment(), "ABOUT");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}