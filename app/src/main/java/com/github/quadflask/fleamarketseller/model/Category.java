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
public class Category extends RealmObject implements Columnable {
	@Required
	private Date date;
	@Required
	private String name;

	private Category parent;

	@Ignore
	private String parentName;

	public Category() {
	}

	@Override
	public String[] getFiledNames() {
		return new String[]{"date", "name", "parent"};
	}

	@Override
	public String[] getData() {
		if (parent != null)
			return new String[]{date.toLocaleString(), name, parent.toString()};
		return new String[]{date.toLocaleString(), name, ""};
	}

	@Override
	public String toString() {
		return name;
	}
}
