package com.github.tttppp.startChipsCalculator.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.tttppp.startChipsCalculator.ui.OutputTextViewWrapper;
import com.github.tttppp.startChipsCalculator.ui.ProgressBarWrapper;

public class OutputUpdater {
	/** A pattern to validate the input. */
	private static final Pattern VALIDATION_PATTERN = Pattern.compile("^[0-9]+ [0-9]+( [0-9]+)+$");
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
	public void inputChanged(String inputString) {
		if (inputString == "") {
			// Do nothing;
		}
		Pattern pattern = Pattern.compile("^([0-9]+) ([0-9]+) ([0-9]+( [0-9]+)*)$");
		Matcher matcher = pattern.matcher(inputString);
		if (matcher.find()) {
			List<String> quantitiesStrings = Arrays.asList(matcher.group(3).split(" "));
			List<Integer> quantities = new ArrayList<Integer>();
			for (String quantitiesString : quantitiesStrings) {
				quantities.add(Integer.valueOf(quantitiesString));
			}
			int colours = Integer.valueOf(matcher.group(2));
			for (int i = quantities.size(); i < colours; i++) {
				quantities.add(quantities.get(quantities.size() - 1));
			}
			inputParameters = new InputParameters(Integer.valueOf(matcher.group(1)), colours, quantities);
			update();
		} else {
			// The user is probably editing their input
			output.setText("Please enter a string of the form \"players colours quantityOfColour...\"");
		}
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
