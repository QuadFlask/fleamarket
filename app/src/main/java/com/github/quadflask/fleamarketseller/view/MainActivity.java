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
import android.view.Menu;
import android.view.MenuItem;

import com.github.quadflask.fleamarketseller.R;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.container)
	ViewPager viewPager;
	@BindView(R.id.tabs)
	TabLayout tabLayout;
	@BindView(R.id.fab)
	FloatingActionButton fab;

	SectionsPagerAdapter sectionsPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSupportActionBar(toolbar);

		sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(sectionsPagerAdapter);
		tabLayout.setupWithViewPager(viewPager);
		viewPager.setOffscreenPageLimit(3);
	}

	@OnClick(R.id.fab)
	void openInputProduct() {
		if (viewPager.getCurrentItem() == 0) {
			final Intent intent = new Intent(this, InputTransactionActivity.class);
			intent.putExtra(IntentConstant.EXTRA_ISINCOME, true);
			startActivity(intent);
		} else if (viewPager.getCurrentItem() == 1) {
			final Intent intent = new Intent(this, InputTransactionActivity.class);
			intent.putExtra(IntentConstant.EXTRA_ISINCOME, false);
			startActivity(intent);
		} else if (viewPager.getCurrentItem() == 2) {
			final AggregateFragment item = (AggregateFragment) sectionsPagerAdapter.getItem(2);
			item.onFabClick(fab);
		}
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
				case 2:
					return AggregateFragment.newInstance();
			}
			throw new IndexOutOfBoundsException();
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
				case 0:
					return "매출";
				case 1:
					return "매입";
				case 2:
					return "결산";
			}
			return null;
		}
	}
}