package com.github.tttppp.startChipsCalculator.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import android.os.AsyncTask;
import android.view.View;

import com.github.tttppp.startChipsCalculator.ui.OutputTextViewWrapper;
import com.github.tttppp.startChipsCalculator.ui.ProgressBarWrapper;

public class OutputUpdaterTask extends AsyncTask<InputParameters, Integer, String> {
	private static final int NUMBER_LOOPS = 2000;
	private static final int PRNG_SEED = 1234;
	// Probably not for changing
	private static final int START_CHIPS_RATIO = 3;
	private static final int REBUY_CHIPS_RATIO = 1;
	private static final List<Integer> PREFERRED_INITIAL_PLAYER_CHIP_COUNT = Arrays.asList(100, 80, 40, 20,
	                                                                                       16, 12, 8, 4);

	// Not for changing
	private static final List<Integer> VALID_CHIP_BASES = Arrays.asList(1, 2, 5, 25);
	private static final List<Integer> INCREMENTS = Arrays.asList(2, 4, 5, 10);

	private static final Map<InputParameters, Config> CACHED_BEST_CONFIGS = new HashMap<InputParameters, Config>();

	/** Seeded random number generator. */
	private Random random;
	/** Why is this a field? */
	private Map<List<Integer>, Integer> baseMultipleMap = new HashMap<List<Integer>, Integer>();

	/** The progress bar to update periodically. */
	private ProgressBarWrapper progressBarWrapper;
	/** The wrapper for the output UI field. */
	private OutputTextViewWrapper output;

	/**
	 * Constructor.
	 * 
	 * @param progressBarWrapper The progress bar to update periodically.
	 * @param output The wrapper for the output UI field.
	 */
	public OutputUpdaterTask(ProgressBarWrapper progressBarWrapper, OutputTextViewWrapper output) {
		this.progressBarWrapper = progressBarWrapper;
		this.output = output;
	}

	/** Show the progress bar. */
	@Override
	protected void onPreExecute() {
		progressBarWrapper.setVisibility(View.VISIBLE);
	}

	class Config {
		public int players;
		public List<Integer> denominations;
		public int baseMultiple;
		public int totalCash;
		public int startCash;
		public int rebuyCash;
		public List<Integer> startChipsPerPlayer;
		public List<Integer> rebuyChipsPerPlayer;
		public List<Integer> cashDenominations;

		public Config(int players, List<Integer> denominations, int baseMultiple, List<Integer> quantities) {
			this.players = players;
			this.denominations = denominations;
			this.baseMultiple = baseMultiple;

			determineCashAndQuantities(quantities);
		}

		private void determineCashAndQuantities(List<Integer> quantities) {
			// Determine how much cash each player gets for each phase.
			int colours = denominations.size();
			List<Integer> cashPerPlayerOfGivenColour = new ArrayList<Integer>();
			int maxCashPerPlayer = 0;
			for (int i = 0; i < colours; i++) {
				cashPerPlayerOfGivenColour.add(denominations.get(i)
				    * chipsPerPlayerOfGivenColour(quantities, players, i) * baseMultiple);
				maxCashPerPlayer += cashPerPlayerOfGivenColour.get(i);
			}
			startCash = 0;
			rebuyCash = 0;
			for (int preferredChipCount : PREFERRED_INITIAL_PLAYER_CHIP_COUNT) {
				totalCash = preferredChipCount * baseMultiple;
				if (totalCash < maxCashPerPlayer) {
					startCash = (START_CHIPS_RATIO * totalCash) / (START_CHIPS_RATIO + REBUY_CHIPS_RATIO);
					rebuyCash = (REBUY_CHIPS_RATIO * totalCash) / (START_CHIPS_RATIO + REBUY_CHIPS_RATIO);
					break;
				}
			}

			// Work out which chips to use to make all the cash from (start +
			// rebuy).
			List<Integer> totalCashPerPlayer = computeCashPerPlayer(colours, baseMultiple, denominations,
			                                                        cashPerPlayerOfGivenColour, startCash
			                                                            + rebuyCash);
			List<Integer> totalChipsPerPlayer = findNumberOfChipsFromCash(colours, baseMultiple,
			                                                              denominations, totalCashPerPlayer);

			// Split out the start chips
			List<Integer> startCashPerPlayer = computeCashPerPlayer(colours, baseMultiple, denominations,
			                                                        totalCashPerPlayer, startCash);
			startChipsPerPlayer = findNumberOfChipsFromCash(colours, baseMultiple, denominations,
			                                                startCashPerPlayer);

			// Find rebuy chips
			rebuyChipsPerPlayer = new ArrayList<Integer>();
			for (int colour = 0; colour < colours; colour++) {
				rebuyChipsPerPlayer.add(totalChipsPerPlayer.get(colour) - startChipsPerPlayer.get(colour));
			}

			cashDenominations = new ArrayList<Integer>();
			for (int d : denominations) {
				cashDenominations.add(d * baseMultiple);
			}
		}

		@Override
		public String toString() {
			List<String> denominationsStrs = new ArrayList<String>();
			for (int d : denominations) {
				denominationsStrs.add(String.valueOf(baseMultiple * d));
			}
			return String.valueOf(players) + ", " + denominationsStrs;
		}
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
	private List<List<Integer>> makeDenominationsList(int colours, int current, List<Integer> partialList) {
		if (partialList.size() == colours) {
			// Check if the completed list is actually usable by humans
			if (useableByHumans(partialList)) {
				return Arrays.asList(partialList);
			} else {
				return new ArrayList<List<Integer>>();
			}
		}
		List<List<Integer>> returnList = new ArrayList<List<Integer>>();
		for (int i : INCREMENTS) {
			List<Integer> newPartial = new ArrayList<Integer>(partialList);
			newPartial.add(i * current);
			returnList.addAll(makeDenominationsList(colours, i * current, newPartial));
		}
		return returnList;
	}

	private List<List<Integer>> splitChips(int colours, int players, List<Integer> quantitiesPerPlayer) {
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

	private int findMinUnmakable(int colours, List<Integer> playersChips, List<Integer> denominations) {
		int carry = 0;
		for (int i = 0; i < colours - 1; i++) {
			int quantity = playersChips.get(i) + carry;
			int denomination = denominations.get(i);
			int ratio = denominations.get(i + 1) / denomination;
			if (quantity < ratio - 1) {
				return (quantity + 1) * denomination;
			}
			carry = (quantity / ratio);
		}
		// Handle case for top colour (this gives a lower amount than is
		// actually possible)
		return (playersChips.get(colours - 1) + carry) * denominations.get(colours - 1);
	}

	private int chipsPerPlayerOfGivenColour(List<Integer> quantities, int players, int i) {
		return quantities.get(i) / players;
	}

	private List<Integer> computeCashPerPlayer(int colours, int baseMultiple, List<Integer> denominations,
	                                           List<Integer> cashPerPlayerOfGivenColour, int cashTarget) {
		List<Integer> outputCashPerPlayer = new ArrayList<Integer>();
		// Assume we can do it entirely with the smallest chips
		outputCashPerPlayer.add(cashTarget);
		for (int colour = 1; colour < colours; colour++) {
			int cashInThisColour = 0;
			if (outputCashPerPlayer.get(colour - 1) > cashPerPlayerOfGivenColour.get(colour - 1)) {
				int amountTooMuch = outputCashPerPlayer.get(colour - 1)
				    - cashPerPlayerOfGivenColour.get(colour - 1);
				// Ensure we round up to next multiple if there's a
				// fraction.
				cashInThisColour = ((amountTooMuch - 1) / (denominations.get(colour) * baseMultiple) + 1)
				    * denominations.get(colour) * baseMultiple;
				outputCashPerPlayer.set(colour - 1, outputCashPerPlayer.get(colour - 1) - cashInThisColour);
			}
			outputCashPerPlayer.add(cashInThisColour);
		}
		return outputCashPerPlayer;
	}

	private List<Integer> findNumberOfChipsFromCash(int colours, int baseMultiple,
	                                                List<Integer> denominations, List<Integer> cashPerPlayer) {
		List<Integer> chipsPerPlayer = new ArrayList<Integer>();
		for (int colour = 0; colour < colours; colour++) {
			chipsPerPlayer.add(cashPerPlayer.get(colour) / (denominations.get(colour) * baseMultiple));
		}
		return chipsPerPlayer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String doInBackground(InputParameters... args) {
		InputParameters inputParameters = args[0];
		if (inputParameters == null) {
			return "Please enter some parameters";
		}

		int players = inputParameters.getPlayers();
		int colours = inputParameters.getColours();
		List<Integer> quantities = inputParameters.getQuantities();

		if (players < 2) {
			return "The number of players must be at least 2";
		}
		if (players < 1) {
			return "There must be at least one colour of chip";
		}
		if (quantities.size() != colours) {
			return "Please enter the right number of quantities";
		}
		for (int quantity : quantities) {
			if (quantity <= 0) {
				return "Quantities must be at least 0: " + quantities;
			}
		}

		Config bestConfig = null;
		if (!CACHED_BEST_CONFIGS.containsKey(inputParameters)) {
			// Ensure the same seed is used each time.
			random = new Random(PRNG_SEED);

			// Create configs
			List<List<Integer>> denominationList = makeDenominationsList(colours, 1, Arrays.asList(1));

			List<Config> configs = new ArrayList<Config>();
			for (List<Integer> denominations : denominationList) {
				Config config = new Config(players, denominations, baseMultipleMap.get(denominations),
				                           quantities);
				// If there are no initial small blinds then this isn't a valid
				// combination.
				if (config.startChipsPerPlayer != null && config.startChipsPerPlayer.size() > 0
				    && config.startChipsPerPlayer.get(0) > 0) {
					configs.add(config);
				}
			}

			Map<Config, List<Integer>> scoring = new HashMap<Config, List<Integer>>();

			for (int loop = 0; loop < NUMBER_LOOPS; loop++) {
				// Loop through configs doing division of coins into n players
				for (Config config : configs) {
					// Split start chips between players randomly (we need
					// tournament to work before rebuy chips appear).
					List<List<Integer>> chips = splitChips(colours, config.players,
					                                       config.startChipsPerPlayer);
					if (chips == null) {
						// Invalid chip calculation.
						continue;
					}

					// For each player find min un-makable amount
					int minMinUnmakable = 100000;
					for (List<Integer> playersChips : chips) {
						int minUnmakable = findMinUnmakable(colours, playersChips, config.denominations);
						if (minUnmakable < 0) {
							return "Error: minUnmakable should not be less than 0: " + playersChips + "\n"
							    + config.denominations;
						}
						if (minUnmakable < minMinUnmakable) {
							minMinUnmakable = minUnmakable;
						}
					}
					if (!scoring.containsKey(config)) {
						scoring.put(config, new ArrayList<Integer>());
					}
					scoring.get(config).add(minMinUnmakable);
				}
				updateProgress(loop);
				if (isCancelled()) {
					return "";
				}
			}

			// Pick best config
			double bestScore = 0;
			for (Config config : configs) {
				if (config.players == players) {
					int total = 0;
					if (scoring.get(config) != null) {
						// Ignore the top and bottom tenth percentile.
						int start = scoring.get(config).size() / 10;
						int end = scoring.get(config).size() - start;
						for (int score : scoring.get(config).subList(start, end)) {
							total += score;
						}
						double average = ((double) total) / scoring.size();
						if (average > bestScore) {
							bestScore = average;
							bestConfig = config;
						}
					}
				}
			}
			CACHED_BEST_CONFIGS.put(inputParameters, bestConfig);
		} else {
			bestConfig = CACHED_BEST_CONFIGS.get(inputParameters);
		}
		if (bestConfig == null) {
			return "Error: No config was best.";
		}

		// OUTPUT

		List<String> outputLines = new ArrayList<String>();

		outputLines.add("Tournament ratio: " + (bestConfig.totalCash / bestConfig.baseMultiple));
		outputLines.add("Denominations: " + bestConfig.cashDenominations);
		outputLines.add("Player Cash: " + (bestConfig.totalCash) + " [Start: " + bestConfig.startCash
		    + ", Rebuy: " + bestConfig.rebuyCash + "]");
		outputLines.add("Start chips: " + bestConfig.startChipsPerPlayer);
		outputLines.add("Rebuy chips: " + bestConfig.rebuyChipsPerPlayer);

		return StringUtils.join(outputLines, "\n");
	}

	/**
	 * Helper method to update the amount of progress.
	 */
	private void updateProgress(int loopsDone) {
		onProgressUpdate(loopsDone);
	}

	/**
	 * {@inheritDoc} Update the progress bar to show roughly how much of the
	 * task is done.
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		int loopsDone = values[0];
		int max = progressBarWrapper.getMax();
		int progress = max * loopsDone / NUMBER_LOOPS;
		progressBarWrapper.setProgress(progress);
	}

	/** Display the results and hide the progress bar again. */
	@Override
	protected void onPostExecute(String result) {
		progressBarWrapper.setVisibility(View.GONE);
		output.setText(result);
	}
}
