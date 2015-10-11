package com.github.tttppp.startChipsCalculator.process;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ChipsCalculatorTest {
	/** The class under test. */
	private ChipsCalculator chipsCalculator = new ChipsCalculator();

	@Test
	public void testMakeDenominationsList() {
		List<List<Integer>> denominations = chipsCalculator.makeDenominationsList(2, 1,
		                                                                          new ArrayList<Integer>());

		@SuppressWarnings("unchecked")
        List<List<Integer>> expected = Arrays.asList(Arrays.asList(10, 100), Arrays.asList(10, 20),
		                                             Arrays.asList(10, 25), Arrays.asList(10, 40),
		                                             Arrays.asList(10, 50), Arrays.asList(2, 20),
		                                             Arrays.asList(2, 4), Arrays.asList(2, 5),
		                                             Arrays.asList(2, 8), Arrays.asList(2, 10),
		                                             Arrays.asList(4, 40), Arrays.asList(4, 8),
		                                             Arrays.asList(4, 10), Arrays.asList(4, 20),
		                                             Arrays.asList(5, 50), Arrays.asList(5, 10),
		                                             Arrays.asList(5, 20), Arrays.asList(5, 25));
		assertEquals("Unexpected list of denominations produced.", expected, denominations);
	}
}
