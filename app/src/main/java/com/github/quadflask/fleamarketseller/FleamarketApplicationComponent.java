package com.github.quadflask.fleamarketseller;

import com.github.quadflask.fleamarketseller.actions.ActionCreatorModule;
import com.github.quadflask.fleamarketseller.dispatcher.DispatcherModule;
import com.github.quadflask.fleamarketseller.store.StoreModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
		modules = {
				DispatcherModule.class,
				ActionCreatorModule.class,
				StoreModule.class
		}
)
public interface FleamarketApplicationComponent {
	void inject(FleamarketApplication application);
}
