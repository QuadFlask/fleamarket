package com.github.quadflask.fleamarketseller.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Transaction extends RealmObject {
	private Date date;
	private Boolean isIncome;
	private Product product;
	private Market market;
	private Vendor vendor;
	private Long count;
	private Long price;
	private String text;

	@Ignore
	private String marketName;
	@Ignore
	private String vendorName;
}
