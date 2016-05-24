package com.github.quadflask.fleamarketseller.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Product extends RealmObject {
	@Required
	private Date date;
	@Required
	private String name;

	private Category category;
	private Vendor vendor;
	private Market market;
	private Long price = 0L;
	private Integer count = 1;
	private String text = "";
}