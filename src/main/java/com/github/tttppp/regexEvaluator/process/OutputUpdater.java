package com.github.tttppp.regexEvaluator.process;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.github.tttppp.regexEvaluator.ui.OutputTextViewWrapper;
import com.github.tttppp.regexEvaluator.ui.ProgressBarWrapper;

public class OutputUpdater {
	/** A default pattern used to avoid matching anything. */
	private static final String NO_WORDS = "#";
	/** The wrapper for the output UI field. */
	private OutputTextViewWrapper output;
	/** A pattern, initially set to match nothing. */
	private Pattern pattern = Pattern.compile(NO_WORDS);
	/** The list of dictionaries to check. */
	private List<String> dictionaries = Arrays.asList("english-words.10.txt");
	/** Wrapper object for progress bar. */
	private ProgressBarWrapper progressBarWrapper;
	/** A class to wrap the async task. */
	private OutputUpdaterTaskRunner outputUpdaterTaskRunner = new OutputUpdaterTaskRunner();

	public OutputUpdater(OutputTextViewWrapper output,
	                     ProgressBarWrapper progressBarWrapper) {
		this.output = output;
		this.progressBarWrapper = progressBarWrapper;
	}

	/**
	 * If the supplied string is empty then set the pattern to match nothing,
	 * otherwise set it to find matches anywhere in the word.
	 * 
	 * @param regexString The supplied regular expression.
	 */
	public void regexChanged(String regexString) {
		if (regexString == "") {
			regexString = NO_WORDS;
		}
		try {
			pattern = Pattern.compile(regexString);
			update();
		} catch (PatternSyntaxException e) {
			// The user is probably editing their regex
			output.setText(e.getMessage());
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
		outputUpdaterTaskRunner.createAndExecute(progressBarWrapper, output,
		                                         dictionaries, pattern);
	}

	public void setOutputUpdaterTaskFactory(OutputUpdaterTaskRunner outputUpdaterTaskFactory) {
		this.outputUpdaterTaskRunner = outputUpdaterTaskFactory;
	}
}
