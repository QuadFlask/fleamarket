package com.github.quadflask.fleamarketseller.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Category;

import net.steamcrafted.materialiconlib.MaterialIconView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmViewHolder;
import io.realm.Sort;
import lombok.val;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;
import static net.steamcrafted.materialiconlib.MaterialDrawableBuilder.IconValue;

public class CategoryListActivity extends BaseActivity implements OnClickEditListener<Category> {
	@BindView(R.id.main_content)
	CoordinatorLayout llRoot;
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
		val categories = store().loadAllCategories().sort("parent", Sort.ASCENDING, "date", Sort.DESCENDING);
		if (adapter == null) {
			adapter = new RealmBasedRecyclerViewAdapter<Category, CategoryViewHolder>(this, categories, true, false) {
				@Override
				public CategoryViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
					CategoryViewHolder viewHolder = new CategoryViewHolder(inflater.inflate(CategoryViewHolder.RES_ID, viewGroup, false));
					viewHolder.root.setOnClickListener(v -> CategoryListActivity.this.onClickEdit(viewHolder.category));
					return viewHolder;
				}

				@Override
				public void onBindRealmViewHolder(CategoryViewHolder viewHolder, int i) {
					val category = realmResults.get(i);
					viewHolder.category = category;
					if (category.getParent() != null) {
						viewHolder.parentName.setText(category.getParent().getName());
						viewHolder.parentName.setTextColor(category.getParent().getColor());
					} else viewHolder.parentName.setText("");
					viewHolder.name.setText(category.getName());
					viewHolder.icon.setColor(category.getColor());
					viewHolder.icon.setIcon(category.getParent() == null ? IconValue.LABEL_OUTLINE : IconValue.ACCOUNT_MINUS);
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
		intent.putExtra(IntentConstant.EXTRA_CATEGORY, category.getId());
		startActivity(intent);
	}

	@Override
	protected int getContentViewResId() {
		return R.layout.activity_category_list;
	}

	private static class CategoryViewHolder extends RealmViewHolder {
		@LayoutRes
		static final int RES_ID = R.layout.li_category;

		@BindView(R.id.ll_root)
		MaterialIconView icon;
		@BindView(R.id.icon)
		LinearLayout root;
		@BindView(R.id.tv_category_parent_name)
		TextView name;
		@BindView(R.id.tv_category_name)
		TextView parentName;

		Category category;

		CategoryViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}