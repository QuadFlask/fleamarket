package com.github.quadflask.fleamarketseller;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class FleamarketApplication extends Application {
	private static Realm realm;

	@Override
	public void onCreate() {
		super.onCreate();
		initRealm();
	}

	public static Realm getRealm() {
		return realm;
	}

	private void initRealm() {
		RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext()).build();
		realm = Realm.getInstance(realmConfig);
	}
}
