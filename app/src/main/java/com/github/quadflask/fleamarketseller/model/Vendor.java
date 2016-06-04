package com.github.quadflask.fleamarketseller.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Vendor extends RealmObject {
	@Required
	@PrimaryKey
	private Long id;
	private Date date;
	private String name;
	private String location;
	private String phoneNumber;

	public Vendor() {
	}
}