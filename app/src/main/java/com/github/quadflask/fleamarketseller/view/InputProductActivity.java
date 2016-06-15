package com.github.quadflask.fleamarketseller.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Product;

import butterknife.BindView;
import butterknife.OnClick;
import lombok.val;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.actionCreator;
import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;

public class InputProductActivity extends BaseActivity {
	@BindView(R.id.ll_root)
	CoordinatorLayout llRoot;
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

	private Product product;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setToolbar(toolbar);

		Intent intent = getIntent();
		if (isEditMode()) {
			val productId = intent.getLongExtra(IntentConstant.EXTRA_PRODUCT, -1L);

			product = store().findProductById(productId);
			if (product != null) {
				edName.setText(product.getName());
				edPrice.setText(product.getPrice().toString());
				btnComplete.setText("수정하기");
				getSupportActionBar().setTitle("제품 수정");
			} else {
				Toast.makeText(this, "id='{product}' 제품을 찾지 못했습니다".replace("{product}", "" + productId), Toast.LENGTH_SHORT).show();
				finish();
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

		if (isEditMode()) {
			spCategory.setSelection(categoryNames.indexOf(product.getCategory().getName()));
		}
	}

	@Override
	protected int getContentViewResId() {
		return R.layout.input_product;
	}

	@Override
	public void onNext(UiUpdateEvent event) {
		if (event instanceof UiUpdateEvent.ProductAdded) {
			reloadCategories();

			edName.setText("");
			edPrice.setText("");

			Snackbar.make(llRoot, "추가됨", Snackbar.LENGTH_SHORT).show();
		} else if (event instanceof UiUpdateEvent.ProductUpdated) {
			Toast.makeText(this, "수정완료", Toast.LENGTH_SHORT).show();
			finish();
		} else if (event instanceof UiUpdateEvent.ProductDeleted) {
			new MaterialDialog.Builder(this)
					.title("삭제 완료")
					.content("제품이 삭제되었습니다")
					.positiveText("확인")
					.onPositive((dialog, which) -> finish())
					.show();
		}
	}

	@OnClick(R.id.btn_complete)
	void addOrEditProduct() {
		val categoryName = spCategory.getSelectedItem().toString();
		val productName = edName.getText().toString();
		val price = Long.parseLong(edPrice.getText().toString());

		val newProduct = Product
				.builder()
				.categoryName(categoryName)
				.name(productName)
				.price(price)
				.build();

		store().checkValidAsObservable(newProduct)
				.compose(bindToLifecycle())
				.subscribe(
				v -> {
					if (isEditMode()) {
						newProduct.setId(product.getId());
						actionCreator().editProduct(newProduct);
					} else
						actionCreator().newProduct(newProduct);
				},
				e -> new MaterialDialog.Builder(this)
						.title("실패")
						.content(e.getMessage())
						.positiveText("확인")
						.show());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (isEditMode())
			getMenuInflater().inflate(R.menu.menu_delete, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_delete) {
			actionCreator().deleteProduct(product.getId());
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}