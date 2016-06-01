package com.github.quadflask.fleamarketseller.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vendor extends RealmObject {
	@Required
	@PrimaryKey
	private Long id;
	private Date date;
	private String name;
	private String location;
	private String phoneNumber;
}