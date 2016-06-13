package com.github.quadflask.fleamarketseller.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Transaction;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmViewHolder;
import rx.android.schedulers.AndroidSchedulers;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;

public class TransactionListFragment extends BaseFragment implements OnClickEditListener<Transaction>, RealmRecyclerView.OnRefreshListener {
	@BindView(R.id.rv_list)
	RealmRecyclerView rvList;

	private RealmBasedRecyclerViewAdapter<Transaction, TransactionViewHolder> adapter;

	private boolean isIncome = true;

	public TransactionListFragment() {
	}

	public static TransactionListFragment newInstance(Activity activity, FragmentManager fragmentManager, boolean isIncome) {
		TransactionListFragment transactionListFragment = new TransactionListFragment();
		transactionListFragment.isIncome = isIncome;
		return transactionListFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (getArguments() != null)
			isIncome = getArguments().getBoolean(IntentConstant.EXTRA_ISINCOME);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void onBindView() {
		rvList.setOnRefreshListener(this);
	}

	@Override
	protected int getContentViewResId() {
		return R.layout.fragment_transaction_list;
	}

	@Override
	public void onResume() {
		super.onResume();
		reloadTransactionList();
	}

	private void reloadTransactionList() {
		store().loadTransactionsByIncome(isIncome)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(transactions -> {
					if (adapter == null && getActivity() != null) {
						adapter = new RealmBasedRecyclerViewAdapter<Transaction, TransactionViewHolder>(getActivity(), transactions, true, false) {
							@Override
							public TransactionViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
								TransactionViewHolder viewHolder = new TransactionViewHolder(inflater.inflate(TransactionViewHolder.RES_ID, viewGroup, false));
								viewHolder.root.setOnClickListener(v -> TransactionListFragment.this.onClickEdit(viewHolder.transaction));
								return viewHolder;
							}

							@Override
							public void onBindRealmViewHolder(TransactionViewHolder viewHolder, int i) {
								Transaction transaction = realmResults.get(i);
								viewHolder.transaction = transaction;
								viewHolder.tvDate.setText(transaction.getFormattedDate());
								viewHolder.tv_product_name.setText(transaction.getProduct().getName());

								if (transaction.getIsIncome())
									viewHolder.tv_vendor_or_market.setText(transaction.getMarket().getName());
								else
									viewHolder.tv_vendor_or_market.setText(transaction.getVendor().getName());

								viewHolder.tv_price.setTextColor(transaction.getIsIncome() ? 0xff2244ff : 0xffff4422);
								viewHolder.tv_price.setText(toThousandComma(transaction.getPrice()));
								viewHolder.tvText.setText(transaction.getText());
							}
						};
						rvList.setAdapter(adapter);
					} else adapter.updateRealmResults(transactions);

					rvList.setRefreshing(false);
				});
	}

	@Override
	public void onNext(UiUpdateEvent uiUpdateEvent) {
	}

	@Override
	public void onClickEdit(Transaction transaction) {
		final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
				.title("매출 상세")
				.customView(R.layout.dialog_transaction_details, true)
				.positiveText("확인")
				.show();

		final View customView = dialog.getCustomView();

		TextView tvDate = (TextView) customView.findViewById(R.id.tv_date);
		TextView tvMarketName = (TextView) customView.findViewById(R.id.tv_market_name);
		TextView tvProductName = (TextView) customView.findViewById(R.id.tv_product_name);
		TextView tvPrice = (TextView) customView.findViewById(R.id.tv_price);
		TextView tvText = (TextView) customView.findViewById(R.id.tv_text);

		tvDate.setText(transaction.getFormattedDate());
		tvMarketName.setText(transaction.getMarket().getName());
		tvProductName.setText(transaction.getProduct().getName());
		tvPrice.setText(toThousandComma(transaction.getPrice()));
		tvText.setText(transaction.getText());
	}

	@Override
	public void onRefresh() {
		reloadTransactionList();
	}

	private String toThousandComma(Long n) {
		return NumberFormat.getInstance(Locale.US).format(n == null ? 0 : n);
	}

	static class TransactionViewHolder extends RealmViewHolder {
		static final int RES_ID = R.layout.li_transaction2;

		final RelativeLayout root;
		final TextView tvDate, tv_product_name, tv_vendor_or_market, tv_price, tvText;
		Transaction transaction;

		TransactionViewHolder(View itemView) {
			super(itemView);
			root = (RelativeLayout) itemView.findViewById(R.id.rl_root);
			tvDate = (TextView) itemView.findViewById(R.id.tv_date);
			tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
			tv_vendor_or_market = (TextView) itemView.findViewById(R.id.tv_vendor_or_market);
			tv_price = (TextView) itemView.findViewById(R.id.tv_price);
			tvText = (TextView) itemView.findViewById(R.id.tv_text);
		}
	}
}
