package com.github.quadflask.fleamarketseller.store;

import com.github.quadflask.fleamarketseller.dispatcher.Dispatcher;
import com.github.quadflask.fleamarketseller.dispatcher.DispatcherModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
		includes = DispatcherModule.class
)
public class StoreModule {
	@Provides
	@Singleton
	Store provideStore(Dispatcher dispatcher) {
		return new Store(dispatcher);
	}
}
