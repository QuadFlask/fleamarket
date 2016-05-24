package com.github.quadflask.fleamarketseller.dispatcher;

import com.github.quadflask.fleamarketseller.rx.RxBus;

public class Dispatcher {
	private final RxBus rxBus;

	public Dispatcher(RxBus rxBus) {
		this.rxBus = rxBus;
	}
}
