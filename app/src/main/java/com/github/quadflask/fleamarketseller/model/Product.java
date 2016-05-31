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
public class Product extends RealmObject implements Comparable {
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

	@Override
	public int compareTo(Object o) {
		if (this == o) return 0;
		if (o == null || getClass() != o.getClass()) return -1;
		Product product = (Product) o;

		int compareCategory = product.getCategory().compareTo(getCategory());
		if (compareCategory == 0) {
			int compareName = product.getName().compareTo(getName());
			if (compareName == 0)
				return product.getDate().compareTo(getDate());
			else return compareName;
		} else return compareCategory;
	}
}