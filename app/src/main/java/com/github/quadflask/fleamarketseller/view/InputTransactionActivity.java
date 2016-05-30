package com.github.quadflask.fleamarketseller.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Market;
import com.github.quadflask.fleamarketseller.model.Product;
import com.github.quadflask.fleamarketseller.model.Transaction;
import com.github.quadflask.fleamarketseller.model.Vendor;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.actionCreator;
import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;

public class InputTransactionActivity extends BaseActivity {

	@BindView(R.id.ll_root)
	LinearLayout llRoot;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.ll_vendor_container)
	LinearLayout llVendorContainer;
	@BindView(R.id.ll_market_container)
	LinearLayout llMarketContainer;
	@BindView(R.id.rg_buying_or_selling)
	RadioGroup rgBuyingOrSelling;
	@BindView(R.id.sp_market)
	Spinner spMarket;
	@BindView(R.id.sp_vendor)
	Spinner spVendor;
	@BindView(R.id.sp_product)
	Spinner spProduct;
	@BindView(R.id.ed_count)
	EditText edCount;
	@BindView(R.id.ed_price)
	EditText edPrice;
	@BindView(R.id.btn_complete)
	Button button;

	private Transaction.TransactionBuilder transactionBuilder;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		toolbar.setNavigationOnClickListener(v -> finish());

		transactionBuilder = Transaction.builder().isIncome(isSelling(rgBuyingOrSelling.getCheckedRadioButtonId()));
		updateViewState(isSelling(rgBuyingOrSelling.getCheckedRadioButtonId()));
		rgBuyingOrSelling.setOnCheckedChangeListener((group, checkedId) -> {
			boolean isSelling = isSelling(checkedId);
			transactionBuilder.isIncome(isSelling);
			updateViewState(isSelling);
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		reloadData();
	}

	private void reloadData() {
		spMarket.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Stream
				.of(store().loadMarkets())
				.map(Market::getName)
				.collect(Collectors.toList())
		));
		spProduct.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Stream
				.of(store().loadProducts())
				.map(Product::getName)
				.collect(Collectors.toList())
		));
		spVendor.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Stream
				.of(store().loadVendors())
				.map(Vendor::getName)
				.collect(Collectors.toList())
		));
	}

	@OnClick(R.id.btn_complete)
	void addTransaction() {
		if (isSelling(rgBuyingOrSelling.getCheckedRadioButtonId()))
			transactionBuilder.marketName(spMarket.getSelectedItem().toString());
		else transactionBuilder.vendorName(spVendor.getSelectedItem().toString());

		actionCreator().newTransaction(
				transactionBuilder
						.date(new Date())
						.count(Long.parseLong(edCount.getText().toString()))
						.price(Long.parseLong(edPrice.getText().toString()))
						.build()
		);
	}

	@Override
	public void onNext(UiUpdateEvent uiUpdateEvent) {
		if (uiUpdateEvent instanceof UiUpdateEvent.TransactionAdded) {
			Snackbar.make(llRoot, "Product added", Snackbar.LENGTH_SHORT).show();
		}
	}

	@Override
	protected int getContentViewResId() {
		return R.layout.input_transaction;
	}

	private boolean isSelling(int checkedId) {
		return checkedId == R.id.rb_selling;
	}

	private void updateViewState(boolean isChecked) {
		if (isChecked) {
			llVendorContainer.setVisibility(View.GONE);
			llMarketContainer.setVerticalGravity(View.VISIBLE);
		} else {
			llVendorContainer.setVisibility(View.VISIBLE);
			llMarketContainer.setVerticalGravity(View.GONE);
		}
	}
}
