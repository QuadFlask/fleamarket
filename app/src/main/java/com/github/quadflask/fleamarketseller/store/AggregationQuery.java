package com.github.quadflask.fleamarketseller.store;

import java.util.Calendar;

import lombok.Getter;
import lombok.experimental.Builder;

@Builder
@Getter
public class AggregationQuery {
	public static final String OPTION_TOTAL = "전체";
	public static final String OPTION_BY_DAY = "일별";
	public static final String OPTION_BY_MONTH = "달별";
	public static final String OPTION_BY_QUARTER = "분기별";
	public static final String OPTION_BY_YEAR = "년별";

	private Calendar firstDate, secondDate;
	private String groupByTerm, marketName, productName, categoryName;
}
