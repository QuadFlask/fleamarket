package com.github.quadflask.fleamarketseller.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.quadflask.fleamarketseller.R;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.container)
	ViewPager mViewPager;
	@BindView(R.id.tabs)
	TabLayout tabLayout;
	@BindView(R.id.fab)
	FloatingActionButton fab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSupportActionBar(toolbar);

		mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
		tabLayout.setupWithViewPager(mViewPager);
	}

	@OnClick(R.id.fab)
	void openInputProduct() {
		startActivity(new Intent(this, InputTransactionActivity.class));
	}

	@Override
	public void onNext(UiUpdateEvent event) {
		// TODO update ui...
	}

	@Override
	protected int getContentViewResId() {
		return R.layout.activity_main;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_category_list) {
			startActivity(new Intent(this, CategoryListActivity.class));
			return true;
		} else if (id == R.id.action_product_list) {
			startActivity(new Intent(this, ProductListActivity.class));
			return true;
		} else if (id == R.id.action_vendor_list) {
			startActivity(new Intent(this, VendorListActivity.class));
			return true;
		} else if (id == R.id.action_market_list) {
			startActivity(new Intent(this, MarketListActivity.class));
			return true;
		} else if (id == R.id.action_settings) {

		}

		return super.onOptionsItemSelected(item);
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0:
					return TransactionListFragment.newInstance(true);
				case 1:
					return TransactionListFragment.newInstance(false);
			}
			return PlaceholderFragment.newInstance();
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
				case 0:
					return "매입";
				case 1:
					return "매출";
				case 2:
					return "결산";
			}
			return null;
		}
	}

	public static class PlaceholderFragment extends Fragment {
		public static PlaceholderFragment newInstance() {
			PlaceholderFragment fragment = new PlaceholderFragment();
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			return rootView;
		}
	}
}
