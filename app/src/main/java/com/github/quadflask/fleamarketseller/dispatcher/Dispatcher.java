package com.github.quadflask.fleamarketseller.dispatcher;

import com.github.quadflask.fleamarketseller.actions.Action;
import com.github.quadflask.fleamarketseller.rx.RxBus;
import com.github.quadflask.fleamarketseller.store.Store;
import com.github.quadflask.fleamarketseller.view.UiUpdateEvent;
import com.google.common.collect.Maps;

import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class Dispatcher {
	private static final RxBus rxBus = new RxBus();
	private static final Map<Observer, Subscription> registerMap = Maps.newConcurrentMap();

	public void dispatch(Object action) {
		rxBus.send(action);
	}

	public void dispatch(String type, Object data) {
		rxBus.send(Action.builder().type(type).data(data));
	}

	public void register(final Observer observer) {
		if (observer != null) {
			unregister(observer);
			registerMap.put(observer, rxBus
					.toObserverable()
					.subscribe(observer));
		}
	}

	public void registerView(final Observer<UiUpdateEvent> observer) {
		if (observer != null) {
			unregister(observer);
			registerMap.put(observer, rxBus
					.toObserverable()
					.filter(a -> a instanceof UiUpdateEvent)
					.map(e -> (UiUpdateEvent) e)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(observer));
		}
	}

	public void unregister(final Observer observer) {
		if (observer != null) {
			final Subscription subscription = registerMap.get(observer);
			if (subscription != null) {
				if (!subscription.isUnsubscribed())
					subscription.unsubscribe();
				registerMap.remove(observer);
			}
		}
	}

	public void emitChange(Store.StoreChangeEvent storeChangeEvent) {
		dispatch(storeChangeEvent == null ? new Store.StoreChangeEvent() : storeChangeEvent);
	}

	public void emitUiUpdate(UiUpdateEvent event) {
		dispatch(event);
	}

	public void unregisterAll() {
		for (final Map.Entry<Observer, Subscription> entry : registerMap.entrySet())
			unregister(entry.getKey());
	}
}
