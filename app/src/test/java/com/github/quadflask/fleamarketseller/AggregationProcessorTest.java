package com.github.quadflask.fleamarketseller;

import com.github.quadflask.fleamarketseller.model.Transaction;
import com.github.quadflask.fleamarketseller.store.AggregationProcessor;
import com.github.quadflask.fleamarketseller.store.AggregationQuery;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AggregationProcessorTest {
	List<Transaction> data;

	@Before
	public void setup() {
		data = Lists.newArrayList(
				Transaction.builder().date(new Date(2016, 1, 1, 12, 0)).price(1L).build(),
				Transaction.builder().date(new Date(2016, 1, 1, 13, 0)).price(1L).build(),
				Transaction.builder().date(new Date(2016, 1, 2)).price(1L).build(),
				Transaction.builder().date(new Date(2016, 1, 3)).price(1L).build(),
				Transaction.builder().date(new Date(2016, 1, 4)).price(1L).build(),
				Transaction.builder().date(new Date(2016, 1, 5)).price(1L).build(),
				Transaction.builder().date(new Date(2016, 2, 1)).price(1L).build(),
				Transaction.builder().date(new Date(2016, 2, 2)).price(1L).build(),
				Transaction.builder().date(new Date(2016, 2, 3)).price(1L).build(),
				Transaction.builder().date(new Date(2016, 2, 4)).price(1L).build(),
				Transaction.builder().date(new Date(2016, 3, 4)).price(1L).build(),
				Transaction.builder().date(new Date(2016, 3, 5, 1, 0)).price(1L).build(),
				Transaction.builder().date(new Date(2016, 3, 5, 1, 2)).price(1L).build(),
				Transaction.builder().date(new Date(2016, 4, 1)).price(1L).build(),
				Transaction.builder().date(new Date(2016, 4, 2)).price(1L).build(),
				Transaction.builder().date(new Date(2016, 5, 3)).price(1L).build(),
				Transaction.builder().date(new Date(2016, 6, 1)).price(1L).build(),

				Transaction.builder().date(new Date(2017, 1, 3)).price(1L).build(),
				Transaction.builder().date(new Date(2017, 2, 3)).price(1L).build(),
				Transaction.builder().date(new Date(2017, 7, 3)).price(1L).build()
		);
	}

	@Test
	public void testGroupByYear() {
		final List<Transaction.TransactionSummary> aggregatedTransactions = AggregationProcessor.aggregate(data, AggregationQuery.OPTION_BY_YEAR);
		final Iterator<Transaction.TransactionSummary> iterator = aggregatedTransactions.iterator();

		assertEquals(2, aggregatedTransactions.size());
		assertEquals("3", iterator.next().getPrice().toString());
		assertEquals("17", iterator.next().getPrice().toString());
	}

	@Test
	public void testGroupByQuarter() {
		final List<Transaction.TransactionSummary> aggregatedTransactions = AggregationProcessor.aggregate(data, AggregationQuery.OPTION_BY_QUARTER);
		final Iterator<Transaction.TransactionSummary> iterator = aggregatedTransactions.iterator();

		assertEquals(4, aggregatedTransactions.size());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("2", iterator.next().getPrice().toString());
		assertEquals("4", iterator.next().getPrice().toString());
		assertEquals("13", iterator.next().getPrice().toString());
	}

	@Test
	public void testGroupByMonth() {
		final List<Transaction.TransactionSummary> aggregatedTransactions = AggregationProcessor.aggregate(data, AggregationQuery.OPTION_BY_MONTH);
		final Iterator<Transaction.TransactionSummary> iterator = aggregatedTransactions.iterator();

		assertEquals(9, aggregatedTransactions.size());

		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("2", iterator.next().getPrice().toString());
		assertEquals("3", iterator.next().getPrice().toString());
		assertEquals("4", iterator.next().getPrice().toString());
		assertEquals("6", iterator.next().getPrice().toString());
	}

	@Test
	public void testGroupByDay() {
		final List<Transaction.TransactionSummary> aggregatedTransactions = AggregationProcessor.aggregate(data, AggregationQuery.OPTION_BY_DAY);
		final Iterator<Transaction.TransactionSummary> iterator = aggregatedTransactions.iterator();

		assertEquals(18, aggregatedTransactions.size());

		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("2", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("1", iterator.next().getPrice().toString());
		assertEquals("2", iterator.next().getPrice().toString());
	}


}