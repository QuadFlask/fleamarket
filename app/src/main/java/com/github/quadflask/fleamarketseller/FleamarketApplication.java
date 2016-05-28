package com.github.quadflask.fleamarketseller;

import android.app.Application;

import com.github.quadflask.fleamarketseller.actions.ActionCreator;
import com.github.quadflask.fleamarketseller.actions.ActionCreatorModule;
import com.github.quadflask.fleamarketseller.dispatcher.Dispatcher;
import com.github.quadflask.fleamarketseller.dispatcher.DispatcherModule;
import com.github.quadflask.fleamarketseller.store.Store;
import com.github.quadflask.fleamarketseller.store.StoreModule;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class FleamarketApplication extends Application {
	private static Realm realm;

	@Inject
	static Dispatcher dispatcher;
	@Inject
	static Store store;
	@Inject
	static ActionCreator actionCreator;

	@Override
	public void onCreate() {
		super.onCreate();
		initDependencies();

		dispatcher.register(store);
	}

	private void initDependencies() {
		RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext()).build();
		Realm.setDefaultConfiguration(realmConfig);
		// delete all data
		realm().beginTransaction();
		realm().deleteAll();
		realm().commitTransaction();
		//

		DaggerFleamarketApplicationComponent
				.builder()
				.dispatcherModule(new DispatcherModule())
				.actionCreatorModule(new ActionCreatorModule())
				.storeModule(new StoreModule())
				.build().inject(this);
	}

	@Override
	public void onTerminate() {
		dispatcher.unregisterAll();
		realm.close();
		super.onTerminate();
	}

	public static Realm realm() {
		if (realm == null) realm = Realm.getDefaultInstance();
		return realm;
	}

	public static Dispatcher dispatcher() {
		return dispatcher;
	}

	public static Store store() {
		return store;
	}

	public static ActionCreator actionCreator() {
		return actionCreator;
	}
}
