package com.github.quadflask.fleamarketseller.view;

import android.widget.Button;

import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.github.quadflask.fleamarketseller.R;

import butterknife.BindView;
import butterknife.OnClick;

public class AggregateFragment extends BaseFragment {

	@BindView(R.id.btn_open_date_range_selector)
	Button btnDAteRangeSelector;

	@OnClick(R.id.btn_open_date_range_selector)
	void openDateRangeSelector() {
		SublimePickerBuilder.builder()
				.displayOption(SublimeOptions.ACTIVATE_DATE_PICKER)
				.pickerToShow(SublimeOptions.Picker.DATE_PICKER)
				.canPickDateRange(true)
				.onDateSetListener(selectedDate -> {

				})
				.build()
				.show(getActivity());
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
}
