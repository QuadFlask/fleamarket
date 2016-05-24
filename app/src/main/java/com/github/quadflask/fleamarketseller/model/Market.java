package com.github.quadflask.fleamarketseller.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Market extends RealmObject {
	@Required
	private Date date;
	@Required
	private String name;

	public static Market create(String name) {
		Market market = new Market();
		market.date = new Date();
		market.name = name;
		return market;
	}
}