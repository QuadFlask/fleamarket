package com.github.quadflask.fleamarketseller.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Market;
import com.github.quadflask.fleamarketseller.model.Vendor;

import butterknife.BindView;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmViewHolder;

public class VendorListActivity extends BaseActivity {
	@BindView(R.id.main_content)
	CoordinatorLayout llRoot;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.fab)
	FloatingActionButton fab;
	@BindView(R.id.rv_list)
	RealmRecyclerView rvList;

	private RealmBasedRecyclerViewAdapter<Market, VendorViewHolder> adapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setToolbar(toolbar);
	}

	@Override
	protected void onResume() {
		super.onResume();
		reloadVendors();
	}

	private void reloadVendors() {
//		val markets = store().loadMarkets();
//		if (adapter == null) {
//			adapter = new RealmBasedRecyclerViewAdapter<Market, MarketListActivity.MarketViewHolder>(this, markets, true, false) {
//				@Override
//				public MarketListActivity.MarketViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
//					MarketListActivity.MarketViewHolder viewHolder = new MarketListActivity.MarketViewHolder(inflater.inflate(MarketListActivity.MarketViewHolder.RES_ID, viewGroup, false));
//					viewHolder.root.setOnClickListener(v -> MarketListActivity.this.onClickEdit(viewHolder.market));
//					return viewHolder;
//				}
//
//				@Override
//				public void onBindRealmViewHolder(MarketListActivity.MarketViewHolder viewHolder, int i) {
//					val market = realmResults.get(i);
//					viewHolder.market = market;
//					viewHolder.name.setText(market.getName());
//					viewHolder.location.setText(market.getLocation());
//				}
//			};
//			rvList.setAdapter(adapter);
//		} else adapter.updateRealmResults(markets);
	}

	@OnClick(R.id.fab)
	void addMarket() {
//		new MaterialDialog.Builder(this)
//				.title("마켓 추가")
//				.customView(R.layout.dialog_input_market, true)
//				.positiveText("추가")
//				.onPositive((dialog, which) -> {
//					View view = dialog.getCustomView();
//					EditText edName = (EditText) view.findViewById(R.id.ed_name);
//					EditText edLocation = (EditText) view.findViewById(R.id.ed_location);
//
//					if (!Strings.isNullOrEmpty(edName.getText().toString())) {
//						actionCreator().newMarket(Market.builder()
//								.name(edName.getText().toString())
//								.location(edLocation.getText().toString())
//								.build());
//					} else Toast.makeText(this, "이름이 비어있습니다", Toast.LENGTH_SHORT).show();
//				})
//				.show();
	}

	@Override
	protected int getContentViewResId() {
		return R.layout.activity_vendor_list;
	}

	@Override
	public void onNext(UiUpdateEvent uiUpdateEvent) {
	}

	private static class VendorViewHolder extends RealmViewHolder {
		@LayoutRes
		static final int RES_ID = 0;

//		final LinearLayout root;

		Vendor vendor;

		public VendorViewHolder(View itemView) {
			super(itemView);
		}
	}
}
