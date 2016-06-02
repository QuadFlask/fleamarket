package com.github.quadflask.fleamarketseller.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Transaction;

import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmViewHolder;
import lombok.val;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;

public class TransactionListFragment extends BaseFragment implements OnClickEditListener<Transaction> {
	@BindView(R.id.rv_list)
	RealmRecyclerView rvList;

	private RealmBasedRecyclerViewAdapter<Transaction, TransactionViewHolder> adapter;

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
		val transactions = store().loadTransactions();

		if (adapter == null && getActivity() != null) {
			adapter = new RealmBasedRecyclerViewAdapter<Transaction, TransactionViewHolder>(getActivity(), transactions, true, false) {
				@Override
				public TransactionViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
					TransactionViewHolder viewHolder = new TransactionViewHolder(inflater.inflate(R.layout.li_transaction, viewGroup, false));
					viewHolder.root.setOnClickListener(v -> TransactionListFragment.this.onClickEdit(viewHolder.transaction));
					return viewHolder;
				}

				@Override
				public void onBindRealmViewHolder(TransactionViewHolder viewHolder, int i) {
					Transaction transaction = realmResults.get(i);
					viewHolder.transaction = transaction;
					viewHolder.tvDate.setText(transaction.getDate().toLocaleString());
					viewHolder.tv_product_name.setText(transaction.getProduct().getName());
					viewHolder.tv_category.setText(transaction.getProduct().getCategory().getName());

					if (transaction.getIsIncome())
						viewHolder.tv_vendor_or_market.setText(transaction.getMarket().getName());
					else viewHolder.tv_vendor_or_market.setText(transaction.getVendor().getName());

					if (transaction.getPrice() > 0) viewHolder.tv_price.setTextColor(0x3333ff);
					else viewHolder.tv_price.setTextColor(0xff3333);
					viewHolder.tv_price.setText(transaction.getPrice().toString());
				}
			};
			rvList.setAdapter(adapter);
		} else adapter.updateRealmResults(transactions);
	}

	@Override
	public void onNext(UiUpdateEvent uiUpdateEvent) {

	}

	@Override
	public void onClickEdit(Transaction transaction) {
	}

	private static class TransactionViewHolder extends RealmViewHolder {
		final RelativeLayout root;
		final TextView tvDate, tv_category, tv_product_name, tv_vendor_or_market, tv_price;
		Transaction transaction;

		TransactionViewHolder(View itemView) {
			super(itemView);
			root = (RelativeLayout) itemView.findViewById(R.id.rl_root);
			tvDate = (TextView) itemView.findViewById(R.id.tv_date);
			tv_category = (TextView) itemView.findViewById(R.id.tv_category);
			tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
			tv_vendor_or_market = (TextView) itemView.findViewById(R.id.tv_vendor_or_market);
			tv_price = (TextView) itemView.findViewById(R.id.tv_price);
		}
	}
}