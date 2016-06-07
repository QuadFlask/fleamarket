package com.github.quadflask.fleamarketseller.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Category;
import com.github.quadflask.fleamarketseller.store.ModelValidationException;
import com.google.common.base.Strings;

import butterknife.BindView;
import butterknife.OnClick;
import lombok.val;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.actionCreator;
import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;

public class InputCategoryActivity extends BaseActivity {

	@BindView(R.id.ll_root)
	CoordinatorLayout llRoot;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.actv_category)
	AutoCompleteTextView acParentCategory;
	@BindView(R.id.sp_parent_category)
	Spinner spParentCategory;
	@BindView(R.id.tv_category_name)
	EditText edCategoryName;
	@BindView(R.id.btn_complete)
	Button btnComplete;
	@BindView(R.id.ll_parent_category_container)
	LinearLayout llParentCategoryContainer;

	private Category category;
	private boolean isEditingParent = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setToolbar(toolbar);

		Intent intent = getIntent();
		if (isEditMode()) {
			val categoryId = intent.getLongExtra(IntentConstant.EXTRA_CATEGORY, -1L);

			category = store().findCategoryById(categoryId);
			if (category != null) {
				edCategoryName.setText(category.getName());
				final Category parent = category.getParent();
				if (parent != null)
					acParentCategory.setText(parent.getName());
				else {
					isEditingParent = true;
					llParentCategoryContainer.setVisibility(View.GONE);
				}
				btnComplete.setText("수정하기");
				getSupportActionBar().setTitle("카테고리 수정");
			} else {
				Toast.makeText(this, "'{category}' 카테고리를 찾지 못했습니다".replace("{category}", category.getName()), Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		reloadParentCategories();
	}

	@Override
	protected int getContentViewResId() {
		return R.layout.input_category;
	}

	@Override
	public void onNext(UiUpdateEvent event) {
		if (event instanceof UiUpdateEvent.CategoryAdded) {
			reloadParentCategories();

			edCategoryName.setText("");

			// TODO should remain parent category which selected before.
//					acParentCategory.setText(category.getParent().getName());
//					spParentCategory.setSelection(categoryNames.indexOf(category.getParent().getName()));

			Snackbar.make(llRoot, "키테고리가 추가되었습니다", Snackbar.LENGTH_SHORT).show();
		} else if (event instanceof UiUpdateEvent.CategoryUpdated) {
			reloadParentCategories();

			Snackbar.make(llRoot, "카테고리가 수정되었습니다", Snackbar.LENGTH_SHORT).show();
		} else if (event instanceof UiUpdateEvent.CategoryDeleted) {
			new MaterialDialog.Builder(this)
					.title("삭제 완료")
					.content("카테고리가 삭제되었습니다")
					.positiveText("확인")
					.onPositive((dialog, which) -> finish())
					.show();
		}
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
			actionCreator().deleteCategory(category.getId());
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void reloadParentCategories() {
		if (isEditingParent) return;

		val categoryNames = store().loadParentCategoryNames();
		final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, categoryNames);
		final ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, categoryNames);
		acParentCategory.setAdapter(adapter);
		spParentCategory.setAdapter(adapter2);
		spParentCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				acParentCategory.setText(adapter2.getItem(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		if (isEditMode()) {
			if (category.getParent() != null) {
				acParentCategory.setText(category.getParent().getName());
				spParentCategory.setSelection(categoryNames.indexOf(category.getParent().getName()));
			}
		}
	}

	@OnClick(R.id.btn_complete)
	void onClickCompleteBtn() {
		String parentCategoryName = isEditingParent ? null : acParentCategory.getText().toString();
		val inputCategoryName = edCategoryName.getText().toString();

		if (!Strings.isNullOrEmpty(inputCategoryName)) {
			Category newCategory = Category
					.builder()
					.name(inputCategoryName)
					.parentName(parentCategoryName)
					.build();

			try {
				store().checkValid(newCategory);

				if (isEditMode()) {
					newCategory.setId(category.getId());
					actionCreator().editCategory(newCategory);
				} else
					actionCreator().newCategory(newCategory);
			} catch (ModelValidationException e) {
				new MaterialDialog.Builder(this)
						.title("실패")
						.content(e.getMessage())
						.positiveText("확인")
						.show();
			}
		}
	}
}
