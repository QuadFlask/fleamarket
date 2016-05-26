package com.github.quadflask.fleamarketseller.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.quadflask.fleamarketseller.FleamarketApplication;
import com.github.quadflask.fleamarketseller.store.Store;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observer;

public abstract class BaseActivity extends AppCompatActivity implements Observer<Store.StoreChangeEvent> {
	private Unbinder bind;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentViewResId());
		bind = ButterKnife.bind(this);
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
}
