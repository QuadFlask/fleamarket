package com.github.quadflask.fleamarketseller.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.github.quadflask.fleamarketseller.R;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class SublimeDatePickerDialogFragment extends DialogFragment {
	private SublimePicker sublimePicker;
	private DateFormat dateFormatter, timeFormatter;
	private DialogInterface.OnCancelListener onCancelListener;
	private OnDateTimeRecurrenceSetListener onDateTimeRecurrenceSetListener;
	private OnDateSetListener onDateSetListener;

	public SublimeDatePickerDialogFragment() {
		dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
		timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
		timeFormatter.setTimeZone(TimeZone.getTimeZone("GMT+0"));
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		sublimePicker = (SublimePicker) getActivity()
				.getLayoutInflater().inflate(R.layout.sublime_picker, container);

		final Bundle arguments = getArguments();
		SublimeOptions options = null;

		if (arguments != null) {
			options = arguments.getParcelable("SUBLIME_OPTIONS");
		}

		sublimePicker.initializePicker(options, new SublimeListenerAdapter() {
			@Override
			public void onCancelled() {
				if (onCancelListener != null) onCancelListener.onCancel(getDialog());
				dismiss();
			}

			@Override
			public void onDateTimeRecurrenceSet(SublimePicker sublimeMaterialPicker,
			                                    SelectedDate selectedDate,
			                                    int hourOfDay, int minute,
			                                    SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
			                                    String recurrenceRule) {
				if (onDateTimeRecurrenceSetListener != null)
					onDateTimeRecurrenceSetListener.onDateTimeRecurrenceSet(selectedDate, hourOfDay, minute, recurrenceOption, recurrenceRule);
				if (onDateSetListener != null)
					onDateSetListener.onDateSet(selectedDate);
				dismiss();
			}
		});
		return sublimePicker;
	}

	public SublimePicker getSublimePicker() {
		return sublimePicker;
	}

	public SublimeDatePickerDialogFragment show(FragmentActivity activity) {
		show(activity.getSupportFragmentManager(), "SUBLIME_PICKER");
		return this;
	}

	public SublimeDatePickerDialogFragment show(FragmentManager fragmentManager) {
		show(fragmentManager, "SUBLIME_PICKER");
		return this;
	}

	public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
		this.onCancelListener = onCancelListener;
	}

	public void setOnDateTimeRecurrenceSetListener(OnDateTimeRecurrenceSetListener onDateTimeRecurrenceSetListener) {
		this.onDateTimeRecurrenceSetListener = onDateTimeRecurrenceSetListener;
	}

	public void setOnDateSetListener(OnDateSetListener onDateSetListener) {
		this.onDateSetListener = onDateSetListener;
	}

	interface OnDateTimeRecurrenceSetListener {
		void onDateTimeRecurrenceSet(SelectedDate selectedDate,
		                             int hourOfDay, int minute,
		                             SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
		                             String recurrenceRule);
	}

	interface OnDateSetListener {
		void onDateSet(SelectedDate selectedDate);
	}
}