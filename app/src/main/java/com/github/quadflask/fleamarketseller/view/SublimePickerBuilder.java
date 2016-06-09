package com.github.quadflask.fleamarketseller.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;

import java.util.Calendar;

public class SublimePickerBuilder {

	public static SublimePickerBuilder.Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private SublimeOptions options = new SublimeOptions();
		private int displayOptions = 0;
		private DialogInterface.OnCancelListener onCancelListener;
		private SublimeDatePickerDialogFragment.OnDateTimeRecurrenceSetListener onDateTimeRecurrenceSetListener;
		private SublimeDatePickerDialogFragment.OnDateSetListener onDateSetListener;

		public SublimePickerBuilder.Builder displayOption(int displayOption) {
			displayOptions |= displayOption;
			options.setDisplayOptions(displayOptions);
			return this;
		}

		public SublimePickerBuilder.Builder pickerToShow(SublimeOptions.Picker picker) {
			options.setPickerToShow(picker);
			return this;
		}

		public SublimePickerBuilder.Builder canPickDateRange(boolean flag) {
			options.setCanPickDateRange(flag);
			return this;
		}

		public Builder dateParam(Calendar firstDate, Calendar secondDate) {
			options.setDateParams(firstDate, secondDate);
			return this;
		}

		public SublimePickerBuilder.Builder onCancel(Dialog.OnCancelListener onCancelListener) {
			this.onCancelListener = onCancelListener;
			return this;
		}

		public SublimePickerBuilder.Builder onDateTimeRecurrenceSet(SublimeDatePickerDialogFragment.OnDateTimeRecurrenceSetListener onDateTimeRecurrenceSetListener) {
			this.onDateTimeRecurrenceSetListener = onDateTimeRecurrenceSetListener;
			return this;
		}

		public SublimePickerBuilder.Builder onDateSetListener(SublimeDatePickerDialogFragment.OnDateSetListener onDateSetListener) {
			this.onDateSetListener = onDateSetListener;
			return this;
		}

		public SublimeDatePickerDialogFragment build() {
			SublimeDatePickerDialogFragment pickerFrag = new SublimeDatePickerDialogFragment();

			Bundle bundle = new Bundle();
			bundle.putParcelable("SUBLIME_OPTIONS", options);

			pickerFrag.setArguments(bundle);
			pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
			pickerFrag.setOnCancelListener(onCancelListener);
			pickerFrag.setOnDateSetListener(onDateSetListener);
			pickerFrag.setOnDateTimeRecurrenceSetListener(onDateTimeRecurrenceSetListener);

			return pickerFrag;
		}
	}
}