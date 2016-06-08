package com.github.quadflask.fleamarketseller.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Market;
import com.google.common.base.Strings;

import butterknife.BindView;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmViewHolder;
import lombok.val;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.actionCreator;
import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;

public class MarketListActivity extends BaseActivity implements OnClickEditListener<Market> {
	@BindView(R.id.main_content)
	CoordinatorLayout llRoot;
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
					MarketViewHolder viewHolder = new MarketViewHolder(inflater.inflate(MarketViewHolder.RES_ID, viewGroup, false));
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
		new MaterialDialog.Builder(this)
				.title("마켓 추가")
				.customView(R.layout.dialog_input_market, true)
				.positiveText("추가")
				.onPositive((dialog, which) -> {
					View view = dialog.getCustomView();
					EditText edName = (EditText) view.findViewById(R.id.ed_name);
					EditText edLocation = (EditText) view.findViewById(R.id.ed_location);

					if (!Strings.isNullOrEmpty(edName.getText().toString())) {
						Market newMarket = Market.builder()
								.name(edName.getText().toString())
								.location(edLocation.getText().toString())
								.build();

						store()
								.checkValidAsObservable(newMarket)
								.subscribe(
										v -> actionCreator().newMarket(newMarket),
										e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
					} else Toast.makeText(this, "이름이 비어있습니다", Toast.LENGTH_SHORT).show();
				})
				.show();
	}

	@Override
	public void onClickEdit(final Market targetMarket) {
		MaterialDialog dialog = new MaterialDialog.Builder(this)
				.title("수정")
				.customView(R.layout.dialog_input_market, true)
				.positiveText("완료")
				.onPositive((_dialog, which) -> {
					View view = _dialog.getCustomView();
					EditText edName = (EditText) view.findViewById(R.id.ed_name);
					EditText edLocation = (EditText) view.findViewById(R.id.ed_location);

					val editedMarket = Market.builder()
							.id(targetMarket.getId())
							.name(edName.getText().toString())
							.location(edLocation.getText().toString())
							.build();

					store().checkValidAsObservable(editedMarket).subscribe(
							v -> actionCreator().editMarket(editedMarket),
							e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
					);
				})
				.show();

		View customView = dialog.getCustomView();
		EditText edName = (EditText) customView.findViewById(R.id.ed_name);
		EditText edLocation = (EditText) customView.findViewById(R.id.ed_location);

		edName.setText(targetMarket.getName());
		edLocation.setText(targetMarket.getLocation());
	}

	@Override
	public void onNext(UiUpdateEvent uiUpdateEvent) {
		if (uiUpdateEvent instanceof UiUpdateEvent.MarketAdded) {
			reloadMarkets();
			Snackbar.make(llRoot, "마켓이 추가되었습니다", Snackbar.LENGTH_SHORT).show();
		} else if (uiUpdateEvent instanceof UiUpdateEvent.MarketUpdated) {
			reloadMarkets();
			Snackbar.make(llRoot, "마켓이 수정되었습니다", Snackbar.LENGTH_SHORT).show();
		}
	}

	@Override
	protected int getContentViewResId() {
		return R.layout.activity_market_list;
	}

	private static class MarketViewHolder extends RealmViewHolder {
		@LayoutRes
		static final int RES_ID = R.layout.li_market;

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
