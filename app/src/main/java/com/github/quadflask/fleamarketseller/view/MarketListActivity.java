package com.github.quadflask.fleamarketseller.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Market;

import butterknife.BindView;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmViewHolder;
import lombok.val;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;

public class MarketListActivity extends BaseActivity implements OnClickEditListener<Market> {
	@BindView(R.id.ll_root)
	LinearLayout llRoot;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.fab)
	FloatingActionButton fab;
	@BindView(R.id.rv_list)
	RealmRecyclerView rvList;

	private RealmBasedRecyclerViewAdapter<Market, MarketViewHolder> adapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setToolbar(toolbar);
	}

	@Override
	protected void onResume() {
		super.onResume();
		reloadMarkets();
	}

	private void reloadMarkets() {
		val markets = store().loadMarkets();
		if (adapter == null) {
			adapter = new RealmBasedRecyclerViewAdapter<Market, MarketViewHolder>(this, markets, true, false) {
				@Override
				public MarketViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
					MarketViewHolder viewHolder = new MarketViewHolder(inflater.inflate(R.layout.li_market, viewGroup, false));
					viewHolder.root.setOnClickListener(v -> MarketListActivity.this.onClickEdit(viewHolder.market));
					return viewHolder;
				}

				@Override
				public void onBindRealmViewHolder(MarketViewHolder viewHolder, int i) {
					val market = realmResults.get(i);
					viewHolder.market = market;
					viewHolder.name.setText(market.getName());
					viewHolder.location.setText(market.getLocation());
				}
			};
			rvList.setAdapter(adapter);
		} else adapter.updateRealmResults(markets);
	}

	@OnClick(R.id.fab)
	void addMarket() {
		// TODO.. open dialog

		MaterialDialog dialog = new MaterialDialog.Builder(this)
				.title("마켓 추가")
				.customView(R.layout.dialog_input_market, true)
				.positiveText("추가")
				.show();
		View view = dialog.getContentView();
	}

	@Override
	public void onNext(UiUpdateEvent uiUpdateEvent) {

	}

	@Override
	public void onClickEdit(Market market) {
		// TODO.. open dialog
	}

	@Override
	protected int getContentViewResId() {
		return R.layout.activity_market_list;
	}

	private static class MarketViewHolder extends RealmViewHolder {
		final LinearLayout root;
		final TextView name, location;
		Market market;

		public MarketViewHolder(View itemView) {
			super(itemView);
			root = (LinearLayout) itemView.findViewById(R.id.ll_root);
			name = (TextView) itemView.findViewById(R.id.tv_name);
			location = (TextView) itemView.findViewById(R.id.tv_location);
		}
	}
}
