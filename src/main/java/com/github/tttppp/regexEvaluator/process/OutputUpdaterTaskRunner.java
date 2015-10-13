package com.github.tttppp.regexEvaluator.process;

import java.util.List;
import java.util.regex.Pattern;

import com.github.tttppp.regexEvaluator.ui.OutputTextViewWrapper;
import com.github.tttppp.regexEvaluator.ui.ProgressBarWrapper;

public class OutputUpdaterTaskRunner {
	private OutputUpdaterTask outputUpdaterTask;

	public void createAndExecute(ProgressBarWrapper progressBarWrapper,
	                             OutputTextViewWrapper output,
	                             List<String> dictionaries, Pattern pattern) {
		if (outputUpdaterTask != null) {
			outputUpdaterTask.cancel(true);
		}
		outputUpdaterTask = new OutputUpdaterTask(progressBarWrapper, output,
		                                          dictionaries);
		outputUpdaterTask.execute(pattern);
	}
}
