package com.github.quadflask.fleamarketseller.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Category;
import com.google.common.base.Strings;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import lombok.val;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.actionCreator;
import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;

public class InputCategoryActivity extends BaseActivity {

	@BindView(R.id.ll_root)
	LinearLayout llRoot;
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

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setToolbar(toolbar);
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
			val _event = (UiUpdateEvent.CategoryAdded) event;
			val parent = _event.addedCategory.getParent();

			reloadParentCategories();

			edCategoryName.setText("");

			Snackbar.make(llRoot, "Category added", Snackbar.LENGTH_SHORT).show();
		}
	}

	private void reloadParentCategories() {
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
	}

	@OnClick(R.id.btn_complete)
	void onClickCompleteBtn() {
		val parentCategoryName = acParentCategory.getText().toString();
		val categoryName = edCategoryName.getText().toString();

		if (!Strings.isNullOrEmpty(categoryName)) {
			actionCreator().newCategory(Category
					.builder()
					.date(new Date())
					.name(categoryName)
					.parentName(parentCategoryName)
					.build()
			);
		}
	}
}
