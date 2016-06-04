package com.github.quadflask.fleamarketseller.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Product;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import butterknife.BindView;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmViewHolder;
import lombok.val;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;

public class ProductListActivity extends BaseActivity implements OnClickEditListener<Product> {
	@BindView(R.id.main_content)
	CoordinatorLayout llRoot;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.fab)
	FloatingActionButton fab;
	@BindView(R.id.rv_list)
	RealmRecyclerView rvList;

	private RealmBasedRecyclerViewAdapter<Product, ProductViewHolder> adapter;

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

	private void reloadProducts() {
		val products = store().loadProducts().sort("date");
		if (adapter == null) {
			adapter = new RealmBasedRecyclerViewAdapter<Product, ProductViewHolder>(this, products, true, false) {
				@Override
				public ProductViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
					ProductViewHolder viewHolder = new ProductViewHolder(inflater.inflate(R.layout.li_product, viewGroup, false));
					viewHolder.root.setOnClickListener(v -> ProductListActivity.this.onClickEdit(viewHolder.product));
					return viewHolder;
				}

				@Override
				public void onBindRealmViewHolder(ProductViewHolder viewHolder, int i) {
					Product product = realmResults.get(i);
					viewHolder.product = product;
					viewHolder.name.setText(product.getName());
					val date = new DateTime(product.getDate().getTime());
					viewHolder.date.setText(DateTimeFormat.forPattern("yyyy. M. dd hh:mm").print(date));
					viewHolder.category.setText(product.getCategory().getName());
					viewHolder.icon.setColor(product.getCategory().getColor());
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
	public void onClickEdit(Product product) {
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
		final MaterialIconView icon;
		final LinearLayout root;
		final TextView name, category, date;
		Product product;

		ProductViewHolder(View itemView) {
			super(itemView);
			root = (LinearLayout) itemView.findViewById(R.id.ll_root);
			icon = (MaterialIconView) itemView.findViewById(R.id.icon);
			name = (TextView) itemView.findViewById(R.id.tv_product_name);
			date = (TextView) itemView.findViewById(R.id.tv_date);
			category = (TextView) itemView.findViewById(R.id.tv_product_category);
		}
	}
}