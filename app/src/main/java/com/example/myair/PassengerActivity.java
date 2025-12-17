package com.example.myair;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PassengerActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private AddPassengerFragment addPassengerFragment;
    private PassengerListFragment passengerListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        ImageView backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(v -> finish());

        setupViewPager();
    }

    private void setupViewPager() {
        addPassengerFragment = new AddPassengerFragment();
        passengerListFragment = new PassengerListFragment();

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText(R.string.tab_add_passenger);
            } else {
                tab.setText(R.string.tab_view_all);
            }
        }).attach();
    }

    public void editPassenger(Passenger passenger) {
        // Switch to add passenger tab and load data
        viewPager.setCurrentItem(0);
        addPassengerFragment.loadPassengerData(passenger);
    }

    public void refreshPassengerList() {
        // Refresh the list fragment
        passengerListFragment.loadPassengers();
        // Switch to view all tab
        viewPager.setCurrentItem(1);
    }

    private class ViewPagerAdapter extends FragmentStateAdapter {

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return addPassengerFragment;
            } else {
                return passengerListFragment;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
