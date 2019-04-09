package uk.ac.kent.pceh3.miniproject.ui;

import android.app.SearchManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import uk.ac.kent.pceh3.miniproject.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private RadioButton news, research,internationalimpact,researchcommunications,researchices, researchii, researchcommunicationsantennas,
            researchcommunicationsmobcomms, researchcommunicationsphotonics, researchcommunicationssmartrfandmicrowavesystems, researchicescontrol,
            researchicesembedded, researchicesinstrumentation, researchiibiometrics, researchiidigmedia, researchiirobotics;
    public ViewPagerAdapter adapter;
    Button closePopupBtn;
    PopupWindow popupWindow;
    CoordinatorLayout coordinatorLayout1;


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

        isNetworkConnected();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        // Get the SearchView and set the searchable configuration
        final MenuItem showPopupBtn = menu.findItem(R.id.app_bar_search);

        showPopupBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                coordinatorLayout1 = (CoordinatorLayout) findViewById(R.id.activity_main);
                viewPager.setCurrentItem(0, true);

                int pos = viewPager.getCurrentItem();
                final Fragment activeFragment = adapter.getItem(pos);

                if(pos == 0){
                    //instantiate the popup.xml layout file
                    LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View customView = layoutInflater.inflate(R.layout.search_popup,null);


                    closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);

                    //instantiate popup window
                    popupWindow = new PopupWindow(customView, Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);

                    //display the popup window
                    popupWindow.showAtLocation(coordinatorLayout1, Gravity.CENTER, 0, 0);

                    popupWindow.setFocusable(true);
                    popupWindow.update();

                    radioGroup = (RadioGroup) customView.findViewById(R.id.categoryGroup);
                    final String[] category = {""};


                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            // find which radio button is selected
                            if(checkedId == R.id.news) {
                                category[0] = "news";
                            } else if(checkedId == R.id.research) {
                                category[0] = "research";
                            } else if(checkedId == R.id.internationalimpact) {
                                category[0] = "internationalimpact";
                            } else if(checkedId == R.id.researchcommunications) {
                                category[0] = "research/communications";
                            } else if(checkedId == R.id.researchices) {
                                category[0] = "research/ices";
                            } else if(checkedId == R.id.researchii) {
                                category[0] = "research/ii";
                            } else if(checkedId == R.id.researchcommunicationsantennas) {
                                category[0] = "research/communications/antennas";
                            } else if(checkedId == R.id.researchcommunicationsmobcomms) {
                                category[0] = "research/communications/mobcomms";
                            } else if(checkedId == R.id.researchcommunicationsphotonics) {
                                category[0] = "research/communications/photonics";
                            } else if(checkedId == R.id.researchcommunicationssmartrfandmicrowavesystems) {
                                category[0] = "research/communications/smartrfandmicrowavesystems";
                            } else if(checkedId == R.id.researchicescontrol) {
                                category[0] = "research/ices/control";
                            } else if(checkedId == R.id.researchicesembedded) {
                                category[0] = "research/ices/embedded";
                            } else if(checkedId == R.id.researchicesinstrumentation) {
                                category[0] = "research/ices/instrumentation";
                            } else if(checkedId == R.id.researchiibiometrics) {
                                category[0] = "research/ii/biometrics";
                            } else if(checkedId == R.id.researchiidigmedia) {
                                category[0] = "research/ii/digmedia";
                            } else if(checkedId == R.id.researchiirobotics) {
                                category[0] = "research/ii/robotics";
                            } else {
                                category[0] = "";
                            }
                        }
                    });

                    //close the popup window on button click
                    closePopupBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText searchText = (EditText) customView.findViewById(R.id.searchText);
                            String search = "";
                            search = searchText.getText().toString();

                            ((FeedFragment)activeFragment).loadFeed(search, category[0]);
                            popupWindow.dismiss();
                        }
                    });
                }
                return true;
            }
        });

        return true;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null){
            System.out.println("CONNECTED");
        }
        else {
            System.out.println("NOT CONNECTED");
            viewPager.setCurrentItem(1, true); //change page to saved articles
        }

        return cm.getActiveNetworkInfo() != null;
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