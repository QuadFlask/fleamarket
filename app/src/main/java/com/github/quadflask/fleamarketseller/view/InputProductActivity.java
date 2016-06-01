package com.github.quadflask.fleamarketseller.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Product;

import butterknife.BindView;
import butterknife.OnClick;
import lombok.val;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.actionCreator;
import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;

public class InputProductActivity extends BaseActivity {
	@BindView(R.id.ll_root)
	LinearLayout llRoot;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.sp_category)
	Spinner spCategory;
	@BindView(R.id.tv_product_name)
	EditText edName;
	@BindView(R.id.tv_product_price)
	EditText edPrice;
	@BindView(R.id.btn_complete)
	Button btnComplete;

	private String action = "";
	private String productName;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		toolbar.setNavigationOnClickListener(v -> finish());

		Intent intent = getIntent();
		if (intent != null) {
			if (intent.getAction().equals(IntentConstant.ACTION_EDIT)) {
				action = IntentConstant.ACTION_EDIT;
				productName = intent.getStringExtra(IntentConstant.EXTRA_PRODUCT);
				btnComplete.setText("수정하기");
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		reloadCategories();
	}

	private void reloadCategories() {
		val categoryNames = store().loadCategoryNames();
		spCategory.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, categoryNames));
	}

	@Override
	protected int getContentViewResId() {
		return R.layout.input_product;
	}

	@Override
	public void onNext(UiUpdateEvent event) {
		if (event instanceof UiUpdateEvent.CategoryAdded) {
			val _event = (UiUpdateEvent.CategoryAdded) event;
			val parent = _event.addedCategory.getParent();

			reloadCategories();

			edName.setText("");

			Snackbar.make(llRoot, "추가됨", Snackbar.LENGTH_SHORT).show();
		} else if (event instanceof UiUpdateEvent.ProductUpdated) {
			Toast.makeText(this, "수정완료", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	@OnClick(R.id.btn_complete)
	void addOrEditProduct() {
		val categoryName = spCategory.getSelectedItem().toString();
		val productName = edName.getText().toString();
		val price = Long.parseLong(edPrice.getText().toString());

		if (action.equals(IntentConstant.ACTION_EDIT)) {
			String oldName = this.productName;
			actionCreator().editProduct(oldName, Product
					.builder()
					.categoryName(categoryName)
					.name(productName)
					.price(price)
					.build());
		} else {
			actionCreator().newProduct(Product
					.builder()
					.categoryName(categoryName)
					.name(productName)
					.price(price)
					.build());
		}
	}
}