package com.github.quadflask.fleamarketseller.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Transaction;

import butterknife.BindView;
import butterknife.OnClick;

public class InputTransactionActivity extends BaseActivity {

	@BindView(R.id.ll_root)
	LinearLayout llRoot;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.cb_is_income)
	CheckBox cbIsIncome;
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

		transactionBuilder = Transaction.builder().isIncome(cbIsIncome.isChecked());
		cbIsIncome.setOnCheckedChangeListener((buttonView, isChecked) -> transactionBuilder.isIncome(isChecked));
	}

	@Override
	protected int getContentViewResId() {
		return R.layout.input_transaction;
	}

	@OnClick(R.id.btn_complete)
	void addTransaction() {

	}

	@Override
	public void onNext(UiUpdateEvent uiUpdateEvent) {

	}
}
