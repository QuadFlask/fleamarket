package com.github.quadflask.fleamarketseller.model;

import org.joda.time.DateTime;

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
public class Market extends RealmObject {
	@Required
	@PrimaryKey
	private Long id;
	@Required
	private DateTime date;
	@Required
	private String name;
	private String location;
	private int color;
}