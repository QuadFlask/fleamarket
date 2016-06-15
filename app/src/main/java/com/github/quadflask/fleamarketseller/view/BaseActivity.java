package com.github.quadflask.fleamarketseller.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.github.quadflask.fleamarketseller.FleamarketApplication;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observer;

public abstract class BaseActivity extends RxAppCompatActivity implements Observer<UiUpdateEvent> {
	private Unbinder bind;
	private Boolean isEditMode = null;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentViewResId());
		bind = ButterKnife.bind(this);
	}

	protected void setToolbar(Toolbar toolbar) {
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		toolbar.setNavigationOnClickListener(v -> finish());
	}

	@Override
	protected void onResume() {
		super.onResume();
		FleamarketApplication.dispatcher().registerView(this);
	}

	@Override
	protected void onPause() {
		FleamarketApplication.dispatcher().unregister(this);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (bind != null)
			bind.unbind();
		super.onDestroy();
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@LayoutRes
	protected abstract int getContentViewResId();

	protected boolean isEditMode() {
		if (isEditMode == null) {
			isEditMode = false;
			Intent intent = getIntent();
			if (intent != null && intent.getAction() != null)
				isEditMode = (IntentConstant.ACTION_EDIT.equals(intent.getAction()));
		}
		return isEditMode;
	}
}
