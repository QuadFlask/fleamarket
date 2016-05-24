package com.github.quadflask.fleamarketseller.actions;

import com.github.quadflask.fleamarketseller.dispatcher.Dispatcher;
import com.github.quadflask.fleamarketseller.dispatcher.DispatcherModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
		includes = DispatcherModule.class
)
public class ActionCreatorModule {
	@Provides
	@Singleton
	static ActionCreator provideActionCreator(Dispatcher dispatcher) {
		return new ActionCreator(dispatcher);
	}
}