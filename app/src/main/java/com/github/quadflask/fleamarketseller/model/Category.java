package com.github.quadflask.fleamarketseller.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Category extends RealmObject {
	@Required
	private Date date;
	@Required
	private String name;

	private Category parent;

	public static final Category create(String name, Category parent) {
		Category category = new Category();
		category.date = new Date();
		category.name = name;
		category.parent = parent;
		return category;
	}
}
