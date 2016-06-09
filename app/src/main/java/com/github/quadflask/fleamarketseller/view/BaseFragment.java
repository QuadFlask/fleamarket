package com.github.quadflask.fleamarketseller.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.quadflask.fleamarketseller.FleamarketApplication;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observer;

public abstract class BaseFragment extends Fragment implements Observer<UiUpdateEvent> {
	private Unbinder bind;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(getContentViewResId(), container, false);
		bind = ButterKnife.bind(this, rootView);
		onBindView();
		return rootView;
	}

	protected void onBindView() {
	}

	@Override
	public void onResume() {
		super.onResume();
		FleamarketApplication.dispatcher().registerView(this);
	}

	@Override
	public void onPause() {
		FleamarketApplication.dispatcher().unregister(this);
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		bind.unbind();
		super.onDestroyView();
	}

	protected abstract int getContentViewResId();

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	public void onFabClick(FloatingActionButton fab) {
	}
}
