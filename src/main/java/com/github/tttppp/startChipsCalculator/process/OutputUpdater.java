package com.github.tttppp.startChipsCalculator.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.tttppp.startChipsCalculator.ui.OutputTextViewWrapper;
import com.github.tttppp.startChipsCalculator.ui.ProgressBarWrapper;

public class OutputUpdater {
	/** The wrapper for the output UI field. */
	private OutputTextViewWrapper output;
	/** The object containing the input parameters. */
	private InputParameters inputParameters;
	/** The list of dictionaries to check. */
	private List<String> dictionaries = Arrays.asList("english-words.10.txt");
	/** Wrapper object for progress bar. */
	private ProgressBarWrapper progressBarWrapper;
	/** A class to wrap the async task. */
	private OutputUpdaterTaskRunner outputUpdaterTaskRunner = new OutputUpdaterTaskRunner();

	public OutputUpdater(OutputTextViewWrapper output, ProgressBarWrapper progressBarWrapper) {
		this.output = output;
		this.progressBarWrapper = progressBarWrapper;
	}

	/**
	 * Create the InputParameters to pass to the processor.
	 * 
	 * @param a string of the form "players colours quantityOfColour...". If
	 *        there are more colours than quantities then the remainder will be
	 *        filled assumed to have the last quantity.
	 */
	public void inputChanged(String numberOfPlayersString, String numberOfColoursString, List<String> quantitiesOfChipsStrings) {
		int numberOfPlayers = 0;
		try {
			numberOfPlayers = Integer.valueOf(numberOfPlayersString.toString().trim());
		} catch (NumberFormatException e) {
			// TODO Error handling.
			return;
		}
		
		int numberOfColours = 0;
		try {
			numberOfColours = Integer.valueOf(numberOfColoursString.toString().trim());
		} catch (NumberFormatException e) {
			// TODO Error handling.
			return;
		}
		
		List<Integer> quantitiesOfChips = new ArrayList<Integer>();
		for (String quantityString : quantitiesOfChipsStrings) {
			try {
				quantitiesOfChips.add(Integer.valueOf(quantityString.toString().trim()));
			} catch (NumberFormatException e) {
				// TODO Error handling.
				return;
			}
		}
		while (quantitiesOfChips.size() < numberOfColours) {
			quantitiesOfChips.add(quantitiesOfChips.get(quantitiesOfChips.size() - 1));
		}

		inputParameters = new InputParameters(numberOfPlayers, numberOfColours, quantitiesOfChips);
		update();

	}

	public void dictionaryChanged(List<String> dictionaries) {
		this.dictionaries = dictionaries;
		update();
	}

	/**
	 * Create a long running task and execute it.
	 */
	private void update() {
		outputUpdaterTaskRunner.createAndExecute(progressBarWrapper, output, dictionaries, inputParameters);
	}

	public void setOutputUpdaterTaskFactory(OutputUpdaterTaskRunner outputUpdaterTaskRunner) {
		this.outputUpdaterTaskRunner = outputUpdaterTaskRunner;
	}
}
