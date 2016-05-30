package com.github.quadflask.fleamarketseller.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Transaction {
	private Date date;
	private Product product;
	private Boolean isIncome;
	private Long price;
	private Market market;
	private String text;
}
