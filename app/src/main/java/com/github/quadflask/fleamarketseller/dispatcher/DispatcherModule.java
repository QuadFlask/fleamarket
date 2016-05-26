package com.github.quadflask.fleamarketseller.dispatcher;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DispatcherModule {
	@Provides
	@Singleton
	Dispatcher provideDispatcher() {
		return new Dispatcher();
	}
}
