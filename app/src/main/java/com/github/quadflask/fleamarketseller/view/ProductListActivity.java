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
import com.github.quadflask.fleamarketseller.model.Product;

import butterknife.BindView;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmViewHolder;
import lombok.val;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;

public class ProductListActivity extends BaseActivity implements OnClickEditProduct {
	@BindView(R.id.ll_root)
	LinearLayout llRoot;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.fab)
	FloatingActionButton fab;
	@BindView(R.id.rv_list)
	RealmRecyclerView rvList;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setToolbar(toolbar);
	}

	@Override
	protected void onResume() {
		super.onResume();
		reloadProducts();
	}

	private RealmBasedRecyclerViewAdapter<Product, ProductViewHolder> adapter;

	private void reloadProducts() {
		val products = store().loadProducts().sort("category");
		if (adapter == null) {
			adapter = new RealmBasedRecyclerViewAdapter<Product, ProductViewHolder>(this, products, true, false) {
				@Override
				public ProductViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
					ProductViewHolder productViewHolder = new ProductViewHolder(inflater.inflate(R.layout.li_product, viewGroup, false));
					productViewHolder.root.setOnClickListener(v -> ProductListActivity.this.onClickEditProduct((Product) productViewHolder.root.getTag()));
					return productViewHolder;
				}

				@Override
				public void onBindRealmViewHolder(ProductViewHolder productViewHolder, int i) {
					Product product = realmResults.get(i);
					productViewHolder.root.setTag(product);
					productViewHolder.name.setText(product.getName());
					productViewHolder.date.setText(product.getDate().toLocaleString());
					productViewHolder.category.setText(product.getCategory().getName());
				}
			};
			rvList.setAdapter(adapter);
		} else adapter.updateRealmResults(products);
	}

	@OnClick(R.id.fab)
	void addProduct() {
		startActivity(new Intent(this, InputProductActivity.class));
	}

	@Override
	public void onNext(UiUpdateEvent uiUpdateEvent) {
		// ui will be update in onResume
		if (uiUpdateEvent instanceof UiUpdateEvent.ProductAdded) {
			// TODO...
		} else if (uiUpdateEvent instanceof UiUpdateEvent.ProductUpdated) {
			// TODO...
		}
	}

	@Override
	public void onClickEditProduct(Product product) {
		Intent intent = new Intent(this, InputProductActivity.class);
		intent.setAction(IntentConstant.ACTION_EDIT);
		intent.putExtra(IntentConstant.EXTRA_PRODUCT, product.getName());
		startActivity(intent);
	}

	@Override
	protected int getContentViewResId() {
		return R.layout.activity_product_list;
	}

	private static class ProductViewHolder extends RealmViewHolder {
		LinearLayout root;
		TextView name, category, date;

		ProductViewHolder(View itemView) {
			super(itemView);
			root = (LinearLayout) itemView.findViewById(R.id.ll_root);
			name = (TextView) itemView.findViewById(R.id.tv_product_name);
			date = (TextView) itemView.findViewById(R.id.tv_date);
			category = (TextView) itemView.findViewById(R.id.tv_product_category);
		}
	}
}

interface OnClickEditProduct {
	void onClickEditProduct(Product product);
}
