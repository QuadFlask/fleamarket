package com.github.quadflask.fleamarketseller.view;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Market;
import com.github.quadflask.fleamarketseller.model.Product;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import lombok.experimental.Builder;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;

public class AggregateFragment extends BaseFragment {

	private Calendar firstDate;
	private Calendar secondDate;

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
	}

	public static AggregateFragment newInstance() {
		return new AggregateFragment();
	}

	@Override
	public void onFabClick(FloatingActionButton fab) {
		final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
				.title("매출 상세")
				.customView(R.layout.dialog_aggregation_filter, true)
				.positiveText("확인")
				.onPositive((dialog1, which) -> {
					final View customView = dialog1.getCustomView();

					Spinner spTermType = (Spinner) customView.findViewById(R.id.sp_term_type);
					Spinner spMarket = (Spinner) customView.findViewById(R.id.sp_market);
					Spinner spCategory = (Spinner) customView.findViewById(R.id.sp_category);
					Spinner spProduct = (Spinner) customView.findViewById(R.id.sp_product);

					final AggregationQuery query = AggregationQuery.builder()
							.firstDate(firstDate)
							.secondDate(secondDate)
							.groupByTerm(spTermType.getSelectedItem().toString())
							.marketName(spMarket.getSelectedItem().toString())
							.categoryName(spCategory.getSelectedItem().toString())
							.productName(spProduct.getSelectedItem().toString())
							.build();
				})
				.show();

		final View customView = dialog.getCustomView();
		Button btnDateRangeSelector = (Button) customView.findViewById(R.id.btn_open_date_range_selector);
		Spinner spTermType = (Spinner) customView.findViewById(R.id.sp_term_type);
		Spinner spMarket = (Spinner) customView.findViewById(R.id.sp_market);
		Spinner spCategory = (Spinner) customView.findViewById(R.id.sp_category);
		Spinner spProduct = (Spinner) customView.findViewById(R.id.sp_product);

		btnDateRangeSelector.setOnClickListener(v -> SublimePickerBuilder.builder()
				.displayOption(SublimeOptions.ACTIVATE_DATE_PICKER)
				.pickerToShow(SublimeOptions.Picker.DATE_PICKER)
				.canPickDateRange(true)
				.dateParam(firstDate, secondDate)
				.onDateSetListener(selectedDate -> {
					firstDate = selectedDate.getFirstDate();
					secondDate = selectedDate.getSecondDate();

					btnDateRangeSelector.setText(
							new SimpleDateFormat("yyyy/MM/dd").format(firstDate.getTime())
									+ " ~ " + new SimpleDateFormat("yyyy/MM/dd").format(secondDate.getTime()));
				})
				.build()
				.show(getActivity()));

		spTermType.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, new String[]{"전체", "일별", "달별", "분기별", "년별"}));

		final List<String> marketNames = Stream.of(store().loadMarkets()).map(Market::getName).collect(Collectors.toList());
		marketNames.add(0, "전체");
		spMarket.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, marketNames));

		final List<String> categoryNames = store().loadCategoryNames();
		categoryNames.add(0, "전체");
		spCategory.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, categoryNames));

		final List<String> productNames = Stream.of(store().loadProducts()).map(Product::getName).collect(Collectors.toList());
		productNames.add(0, "전체");
		spProduct.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, productNames));
	}

	@Builder
	static class AggregationQuery {
		Calendar firstDate, secondDate;
		String groupByTerm, vendorName, marketName, productName, categoryName;
	}
}
