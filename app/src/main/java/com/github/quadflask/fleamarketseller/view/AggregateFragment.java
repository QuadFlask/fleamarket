package com.github.quadflask.fleamarketseller.view;

import android.support.design.widget.FloatingActionButton;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Market;
import com.github.quadflask.fleamarketseller.model.Product;
import com.github.quadflask.fleamarketseller.model.Vendor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import lombok.experimental.Builder;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;

public class AggregateFragment extends BaseFragment {

	@BindView(R.id.btn_open_date_range_selector)
	Button btnDateRangeSelector;
	@BindView(R.id.sp_term_type)
	Spinner spTermType;
	@BindView(R.id.sp_vendor)
	Spinner spVendor;
	@BindView(R.id.sp_product)
	Spinner spProduct;
	@BindView(R.id.sp_category)
	Spinner spCategory;
	@BindView(R.id.sp_market)
	Spinner spMarket;

	private Calendar firstDate;
	private Calendar secondDate;

	@Override
	protected void onBindView() {
		firstDate = Calendar.getInstance();
		firstDate.add(Calendar.MONTH, -1);
		secondDate = Calendar.getInstance();

		spTermType.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, new String[]{"전체", "일별", "달별", "분기별", "년별"}));

		final List<String> vendorNames = Stream.of(store().loadVendors()).map(Vendor::getName).collect(Collectors.toList());
		vendorNames.add(0, "전체");
		spVendor.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, vendorNames));

		final List<String> productNames = Stream.of(store().loadProducts()).map(Product::getName).collect(Collectors.toList());
		productNames.add(0, "전체");
		spProduct.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, productNames));

		final List<String> categoryNames = store().loadCategoryNames();
		categoryNames.add(0, "전체");
		spCategory.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, categoryNames));

		final List<String> marketNames = Stream.of(store().loadMarkets()).map(Market::getName).collect(Collectors.toList());
		marketNames.add(0, "전체");
		spProduct.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, marketNames));
	}

	@OnClick(R.id.btn_open_date_range_selector)
	void openDateRangeSelector() {
		SublimePickerBuilder.builder()
				.displayOption(SublimeOptions.ACTIVATE_DATE_PICKER)
				.pickerToShow(SublimeOptions.Picker.DATE_PICKER)
				.canPickDateRange(true)
				.dateParam(firstDate, secondDate)
				.onDateSetListener(selectedDate -> {
					firstDate = selectedDate.getFirstDate();
					secondDate = selectedDate.getSecondDate();

					btnDateRangeSelector.setText(new SimpleDateFormat("yyyy/MM/dd").format(firstDate.getTime())
							+ " ~ "
							+ new SimpleDateFormat("yyyy/MM/dd").format(secondDate.getTime()));
				})
				.build()
				.show(getActivity());
	}

	private AggregationQuery createQuery() {
		return AggregationQuery.builder()
				.firstDate(firstDate)
				.secondDate(secondDate)
				.groupByTerm(spTermType.getSelectedItem().toString())
				.marketName(spMarket.getSelectedItem().toString())
				.categoryName(spCategory.getSelectedItem().toString())
				.productName(spProduct.getSelectedItem().toString())
				.vendorName(spVendor.getSelectedItem().toString())
				.build();
	}

	private void runQuery(AggregationQuery query) {

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

	public void onFabClick(FloatingActionButton fab) {

	}

	@Builder
	static class AggregationQuery {
		Calendar firstDate, secondDate;
		String groupByTerm, vendorName, marketName, productName, categoryName;
	}
}
