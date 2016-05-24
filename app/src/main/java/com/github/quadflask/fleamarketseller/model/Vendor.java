package com.github.quadflask.fleamarketseller.model;

import java.util.Date;

import io.realm.RealmObject;

public class Vendor extends RealmObject {
	private Date date;
	private String name;

	public static final Vendor DEFAULT = Vendor.create("unknown");

	public static Vendor create(String name) {
		Vendor vendor = new Vendor();
		vendor.date = new Date();
		vendor.name = name;
		return vendor;
	}
}