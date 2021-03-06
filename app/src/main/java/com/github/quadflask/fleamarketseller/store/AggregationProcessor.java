package com.github.quadflask.fleamarketseller.store;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.github.quadflask.fleamarketseller.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AggregationProcessor {
	private static final Comparator<String> STRING_REVERSE_COMPARATOR = (lhs, rhs) -> -1 * lhs.compareTo(rhs);

	private AggregationProcessor() {
	}

	public static List<Transaction.TransactionSummary> aggregate(List<Transaction> transactions, String condition) {
		return aggregateAndReduceGroups(Stream
				.of(transactions)
				.collect(Collectors
						.groupingBy(createDateTermGrouper(condition))));
	}

	private static List<Transaction.TransactionSummary> aggregateAndReduceGroups(Map<String, List<Transaction>> groupedMap) {
		return Stream
				.of(groupedMap.keySet())
				.sorted(STRING_REVERSE_COMPARATOR)
				.map(key -> Stream
						.of(groupedMap.get(key))
						.reduce(Transaction.TransactionSummary
										.builder()
										.text(key)
										.price(0L)
										.count(0L)
										.details(groupedMap.get(key))
										.build(),
								(value1, value2) -> {
									value1.setPrice(value1.getPrice() + value2.getPrice());
									return value1;
								}))
				.collect(Collectors.toList());
	}

	private static Function<Transaction, String> createDateTermGrouper(String groupByTerm) {
		return t -> {
			final Date date = t.getDate();
			final String isIncome = t.getIsIncome() ? "" : "-";

			switch (groupByTerm) {
				case AggregationQuery.OPTION_TOTAL:
					return "total" + isIncome;
				case AggregationQuery.OPTION_BY_DAY:
					return new SimpleDateFormat("YYMMDD").format(date) + isIncome;
				case AggregationQuery.OPTION_BY_MONTH:
					return new SimpleDateFormat("YYMM").format(date) + isIncome;
				case AggregationQuery.OPTION_BY_QUARTER:
					return new SimpleDateFormat("YY").format(date) + ((date.getMonth() - 1) / 3) + isIncome;
				case AggregationQuery.OPTION_BY_YEAR:
					return new SimpleDateFormat("YY").format(date) + isIncome;
			}

			return "unknown";
		};
	}
}
