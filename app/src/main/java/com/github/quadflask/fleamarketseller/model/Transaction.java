package com.github.quadflask.fleamarketseller.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
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
public class Transaction extends RealmObject {
	@Required
	@PrimaryKey
	private Long id;
	private Date date;
	private Boolean isIncome;
	private Product product;
	private Market market;
	private Vendor vendor;
	private Long count;
	private Long price;
	private String text;

	public Transaction() {
	}

	@Ignore
	private String productName;
	@Ignore
	private String marketName;
	@Ignore
	private String vendorName;
	@Ignore
	private String formattedDate;

	public String getFormattedDate() {
		if (date != null && formattedDate == null) {
			formattedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date);
			return formattedDate;
		} else return formattedDate;
	}
}
