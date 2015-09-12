package com.github.tttppp.startChipsCalculator.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.math.Fraction;

public class ChipsCalculator {

	private static final int PRNG_SEED = 1234;
	// Not for changing
	private static final List<Integer> VALID_CHIP_BASES = Arrays.asList(1, 2, 5, 25);
	/**
	 * The increments between each level (starting with 10 to encourage lists
	 * that start with 1).
	 */
	private static final List<Fraction> INCREMENTS = Arrays.asList(Fraction.getFraction(10, 1),
	                                                               Fraction.getFraction(2, 1),
	                                                               Fraction.getFraction(5, 2),
	                                                               Fraction.getFraction(4, 1),
	                                                               Fraction.getFraction(5, 1));
	/** Seeded random number generator. */
	private Random random;
	/** Cache of the multiplier used when presenting a set of denominations. */
	private Map<List<Integer>, Integer> baseMultipleMap = new HashMap<List<Integer>, Integer>();

	public List<List<Integer>> splitChips(int colours, int players, List<Integer> quantitiesPerPlayer) {
		List<List<Integer>> splitPointsList = new ArrayList<List<Integer>>();
		for (int c = 0; c < colours; c++) {
			// Determine how many total of this chip.
			Integer quantity = quantitiesPerPlayer.get(c) * players;

			List<Integer> splitPoints = new ArrayList<Integer>();
			splitPoints.add(0);
			for (int p = 0; p < players; p++) {
				if (quantity == 0) {
					splitPoints.add(0);
				} else if (quantity > 0) {
					splitPoints.add(random.nextInt(quantity));
				} else {
					// Error
					return null;
				}
			}
			splitPoints.add(quantity);
			Collections.sort(splitPoints);
			splitPointsList.add(splitPoints);
		}
		List<List<Integer>> split = new ArrayList<List<Integer>>();
		for (int player = 0; player < players; player++) {
			List<Integer> s = new ArrayList<Integer>();
			for (int c = 0; c < colours; c++) {
				s.add(splitPointsList.get(c).get(player + 1) - splitPointsList.get(c).get(player));
			}
			split.add(s);
		}
		return split;
	}

	private boolean isOk(List<Integer> denominations) {
		for (int d : denominations) {
			while (d % 10 == 0) {
				d = d / 10;
			}
			if (!VALID_CHIP_BASES.contains(d)) {
				return false;
			}
		}
		return true;
	}

	private boolean useableByHumans(List<Integer> denominations) {
		for (int multiplier : VALID_CHIP_BASES) {
			List<Integer> cashDenominations = new ArrayList<Integer>();
			for (int d : denominations) {
				cashDenominations.add(d * multiplier);
			}
			if (isOk(cashDenominations)) {
				baseMultipleMap.put(denominations, multiplier);
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected List<List<Integer>> makeDenominationsList(int colours, int current, List<Integer> partialList) {
		if (partialList.size() == colours) {
			// Check if the completed list is actually usable by humans
			if (useableByHumans(partialList)) {
				return Arrays.asList(partialList);
			} else {
				return new ArrayList<List<Integer>>();
			}
		}
		List<List<Integer>> returnList = new ArrayList<List<Integer>>();
		for (Fraction ratio : INCREMENTS) {
			List<Integer> newPartial = new ArrayList<Integer>(partialList);
			Fraction nextValue = ratio.multiplyBy(Fraction.getFraction(current, 1)).reduce();
			if (nextValue.getDenominator() == 1) {
				newPartial.add(nextValue.getNumerator());
				returnList.addAll(makeDenominationsList(colours, nextValue.getNumerator(), newPartial));
			}
		}
		return returnList;
	}

	public void reseedPRNG() {
		random = new Random(PRNG_SEED);
	}

	public int getBaseMultiple(List<Integer> denominations) {
		return baseMultipleMap.get(denominations);
	}
}
