package com.github.quadflask.fleamarketseller.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Category;

import butterknife.BindView;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmViewHolder;
import lombok.val;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;

public class CategoryListActivity extends BaseActivity implements OnClickEditListener<Category> {
	@BindView(R.id.ll_root)
	LinearLayout llRoot;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.fab)
	FloatingActionButton fab;
	@BindView(R.id.rv_list)
	RealmRecyclerView rvList;

	private RealmBasedRecyclerViewAdapter<Category, CategoryViewHolder> adapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setToolbar(toolbar);
	}

	@Override
	protected void onResume() {
		super.onResume();
		reloadCategories();
	}

	private void reloadCategories() {
		val categories = store().loadAllCategories();
		if (adapter == null) {
			adapter = new RealmBasedRecyclerViewAdapter<Category, CategoryViewHolder>(this, categories, true, false) {
				@Override
				public CategoryViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
					CategoryViewHolder categoryViewHolder = new CategoryViewHolder(inflater.inflate(R.layout.li_category, viewGroup, false));
					categoryViewHolder.root.setOnClickListener(v -> CategoryListActivity.this.onClickEdit(categoryViewHolder.category));
					return categoryViewHolder;
				}

				@Override
				public void onBindRealmViewHolder(CategoryViewHolder categoryViewHolder, int i) {
					val category = realmResults.get(i);
					categoryViewHolder.category = category;
					if (category.getParent() != null)
						categoryViewHolder.parentName.setText(category.getParent().getName());
					else categoryViewHolder.parentName.setText("");
					categoryViewHolder.name.setText(category.getName());
				}
			};
			rvList.setAdapter(adapter);
		} else adapter.updateRealmResults(categories);
	}

	@Override
	public void onNext(UiUpdateEvent uiUpdateEvent) {

	}

	@OnClick(R.id.fab)
	void addCategory() {
		startActivity(new Intent(this, InputCategoryActivity.class));
	}

	@Override
	public void onClickEdit(Category category) {
		Intent intent = new Intent(this, InputCategoryActivity.class);
		intent.setAction(IntentConstant.ACTION_EDIT);
		intent.putExtra(IntentConstant.EXTRA_CATEGORY, category.getName());
		startActivity(intent);
	}

	@Override
	protected int getContentViewResId() {
		return R.layout.activity_category_list;
	}

	private static class CategoryViewHolder extends RealmViewHolder {
		final LinearLayout root;
		final TextView name, parentName;
		Category category;

		CategoryViewHolder(View itemView) {
			super(itemView);
			root = (LinearLayout) itemView.findViewById(R.id.ll_root);
			parentName = (TextView) itemView.findViewById(R.id.tv_category_parent_name);
			name = (TextView) itemView.findViewById(R.id.tv_category_name);
		}
	}
}
