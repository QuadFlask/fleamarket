package com.github.quadflask.fleamarketseller.dispatcher;

import com.github.quadflask.fleamarketseller.rx.RxBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DispatcherModule {
	@Provides
	@Singleton
	static Dispatcher provideDispatcher() {
		return new Dispatcher(new RxBus());
	}
}
