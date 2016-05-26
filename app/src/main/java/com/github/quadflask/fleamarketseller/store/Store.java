package com.github.quadflask.fleamarketseller.store;

import com.github.quadflask.fleamarketseller.FleamarketApplication;
import com.github.quadflask.fleamarketseller.actions.Action;
import com.github.quadflask.fleamarketseller.dispatcher.Dispatcher;

import java.util.Date;

import io.realm.Realm;
import lombok.val;
import rx.Observer;

public class Store implements Observer {

	private Dispatcher dispatcher;

	public Store(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void onNext(final Object action) {
		if (action instanceof Action.CreateCategory) {
			val _action = (Action.CreateCategory) action;
			val category = _action.category;

			insertWith(realm -> {
				if (category.getDate() == null)
					category.setDate(new Date());
				realm.copyToRealm(category);
			});

			emitStoreChange();
		} else if (action instanceof Action.CreateProduct) {
			val _action = (Action.CreateProduct) action;
			val product = _action.product;

			insertWith(realm -> {
				if (product.getDate() == null)
					product.setDate(new Date());
				realm.copyToRealm(product);
			});

			emitStoreChange();
		}
	}

	private void emitStoreChange() {
		dispatcher.emitChange(new StoreChangeEvent());
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onCompleted() {
	}

	public static class StoreChangeEvent {
	}

	interface RealmInsert {
		void insert(Realm realm);
	}

	void insertWith(RealmInsert realmInsert) {
		Realm realm = FleamarketApplication.realm();
		try {
			realm.beginTransaction();
			realmInsert.insert(realm);
			realm.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			realm.cancelTransaction();
		}
	}
}
