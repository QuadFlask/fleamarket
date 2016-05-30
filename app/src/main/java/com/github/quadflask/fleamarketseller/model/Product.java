package com.github.quadflask.fleamarketseller.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Required;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Product extends RealmObject {
	@Required
	private Date date;
	@Required
	private String name;

	private Category category;
	private Long price;
	private String text = "";

	@Ignore
	private String categoryName;

	public Product() {
	}
}