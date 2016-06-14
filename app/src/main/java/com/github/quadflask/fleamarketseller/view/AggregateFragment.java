package com.github.quadflask.fleamarketseller.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Market;
import com.github.quadflask.fleamarketseller.model.Product;
import com.github.quadflask.fleamarketseller.model.Transaction;
import com.github.quadflask.fleamarketseller.store.AggregationQuery;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import io.realm.RealmViewHolder;
import rx.android.schedulers.AndroidSchedulers;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;

public class AggregateFragment extends BaseFragment implements OnClickEditListener<Transaction.TransactionSummary> {
	@BindView(R.id.rv_list)
	RecyclerView rvList;

	private Calendar firstDate;
	private Calendar secondDate;

	private Activity activity;
	private FragmentManager fragmentManager;
	private RecyclerViewAdapterForTransaction adapter;

	@Override
	protected void onBindView() {
		firstDate = Calendar.getInstance();
		firstDate.add(Calendar.MONTH, -1);
		secondDate = Calendar.getInstance();
	}

	@Override
	protected int getContentViewResId() {
		return R.layout.fragment_aggregate;
	}

	@Override
	public void onNext(UiUpdateEvent event) {
		if (event instanceof UiUpdateEvent.OnFabClick) {
			final MaterialDialog dialog = new MaterialDialog.Builder(activity)
					.title("필터")
					.customView(R.layout.dialog_aggregation_filter, true)
					.positiveText("적용")
					.onPositive((dialog1, which) -> {
						final View customView = dialog1.getCustomView();

						Spinner spTermType = (Spinner) customView.findViewById(R.id.sp_term_type);
						Spinner spMarket = (Spinner) customView.findViewById(R.id.sp_market);
						Spinner spCategory = (Spinner) customView.findViewById(R.id.sp_category);
						Spinner spProduct = (Spinner) customView.findViewById(R.id.sp_product);

						store().runQuery(
								AggregationQuery.builder()
										.firstDate(firstDate)
										.secondDate(secondDate)
										.groupByTerm(spTermType.getSelectedItem().toString())
										.marketName(spMarket.getSelectedItem().toString())
										.categoryName(spCategory.getSelectedItem().toString())
										.productName(spProduct.getSelectedItem().toString())
										.build())
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(
										this::updateTransactions, e -> {
										});
					})
					.show();

			final View customView = dialog.getCustomView();
			Button btnDateSelectorFirst = (Button) customView.findViewById(R.id.btn_open_date_selector_first);
			Button btnDateSelectorSecond = (Button) customView.findViewById(R.id.btn_open_date_selector_second);
			Spinner spTermType = (Spinner) customView.findViewById(R.id.sp_term_type);
			Spinner spMarket = (Spinner) customView.findViewById(R.id.sp_market);
			Spinner spCategory = (Spinner) customView.findViewById(R.id.sp_category);
			Spinner spProduct = (Spinner) customView.findViewById(R.id.sp_product);

			btnDateSelectorFirst.setOnClickListener(v -> SublimePickerBuilder.builder()
					.displayOption(SublimeOptions.ACTIVATE_DATE_PICKER)
					.pickerToShow(SublimeOptions.Picker.DATE_PICKER)
					.canPickDateRange(false)
					.dateParam(firstDate, null)
					.onDateSetListener(selectedDate -> {
						firstDate = selectedDate.getFirstDate();
						btnDateSelectorFirst.setText(new SimpleDateFormat("yyyy/MM/dd").format(firstDate.getTime()));
					})
					.build()
					.show(fragmentManager));

			btnDateSelectorSecond.setOnClickListener(v -> SublimePickerBuilder.builder()
					.displayOption(SublimeOptions.ACTIVATE_DATE_PICKER)
					.pickerToShow(SublimeOptions.Picker.DATE_PICKER)
					.canPickDateRange(false)
					.dateParam(secondDate, null)
					.onDateSetListener(selectedDate -> {
						secondDate = selectedDate.getFirstDate();
						btnDateSelectorSecond.setText(new SimpleDateFormat("yyyy/MM/dd").format(secondDate.getTime()));
					})
					.build()
					.show(fragmentManager));

			spTermType.setAdapter(new ArrayAdapter<>(activity, R.layout.support_simple_spinner_dropdown_item, new String[]{
					AggregationQuery.OPTION_TOTAL,
					AggregationQuery.OPTION_BY_DAY,
					AggregationQuery.OPTION_BY_MONTH,
					AggregationQuery.OPTION_BY_QUARTER,
					AggregationQuery.OPTION_BY_YEAR}));

			final List<String> marketNames = Stream.of(store().loadMarkets()).map(Market::getName).collect(Collectors.toList());

			marketNames.add(0, AggregationQuery.OPTION_TOTAL);
			spMarket.setAdapter(new ArrayAdapter<>(activity, R.layout.support_simple_spinner_dropdown_item, marketNames));

			final List<String> categoryNames = store().loadCategoryNames();
			categoryNames.add(0, AggregationQuery.OPTION_TOTAL);
			spCategory.setAdapter(new ArrayAdapter<>(activity, R.layout.support_simple_spinner_dropdown_item, categoryNames));

			final List<String> productNames = Stream.of(store().loadProducts()).map(Product::getName).collect(Collectors.toList());
			productNames.add(0, AggregationQuery.OPTION_TOTAL);
			spProduct.setAdapter(new ArrayAdapter<>(activity, R.layout.support_simple_spinner_dropdown_item, productNames));
		}
	}

	public static AggregateFragment newInstance(Activity activity, FragmentManager fragmentManager) {
		AggregateFragment aggregateFragment = new AggregateFragment();
		aggregateFragment.activity = activity;
		aggregateFragment.fragmentManager = fragmentManager;
		return aggregateFragment;
	}

	private void updateTransactions(List<Transaction.TransactionSummary> transactions) {
		if (adapter == null && activity != null) {
			adapter = new RecyclerViewAdapterForTransaction(activity);
			adapter.updateData(transactions);
			rvList.setAdapter(adapter);
		} else {
			adapter.updateData(transactions);
			adapter.notifyDataSetChanged();
		}
	}

	private String toThousandComma(Long n) {
		return NumberFormat.getInstance().format(n == null ? 0 : n);
	}

	@Override
	public void onClickEdit(Transaction.TransactionSummary transaction) {
		// TODO show transaction list dialog...
	}

	private class RecyclerViewAdapterForTransaction extends RecyclerView.Adapter<TransactionSummaryViewHolder> {
		private List<Transaction.TransactionSummary> transactions;
		private final LayoutInflater inflater;

		public RecyclerViewAdapterForTransaction(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public TransactionSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			TransactionSummaryViewHolder viewHolder = new TransactionSummaryViewHolder(inflater.inflate(TransactionSummaryViewHolder.RES_ID, parent, false));
			viewHolder.root.setOnClickListener(v -> AggregateFragment.this.onClickEdit(viewHolder.transaction));
			return viewHolder;
		}

		@Override
		public void onBindViewHolder(TransactionSummaryViewHolder viewHolder, int position) {
			Transaction.TransactionSummary transaction = transactions.get(position);
			viewHolder.transaction = transaction;
			viewHolder.tv_product_name.setText(transaction.getProduct().getName());

			if (transaction.getIsIncome())
				viewHolder.tv_vendor_or_market.setText(transaction.getMarket().getName());
			else
				viewHolder.tv_vendor_or_market.setText(transaction.getVendor().getName());

			viewHolder.tv_price.setTextColor(transaction.getIsIncome() ? 0xff2244ff : 0xffff4422);
			viewHolder.tv_price.setText(toThousandComma(transaction.getProduct().getPrice()));
			viewHolder.tvText.setText(transaction.getText());
		}

		@Override
		public int getItemCount() {
			if (transactions != null)
				return transactions.size();
			return 0;
		}

		public void updateData(List<Transaction.TransactionSummary> transactions) {
			this.transactions = transactions;
		}
	}

	static class TransactionSummaryViewHolder extends RealmViewHolder {
		static final int RES_ID = R.layout.li_transaction2;

		final RelativeLayout root;
		final TextView tvDate, tv_product_name, tv_vendor_or_market, tv_price, tvText;
		Transaction.TransactionSummary transaction;

		TransactionSummaryViewHolder(View itemView) {
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
