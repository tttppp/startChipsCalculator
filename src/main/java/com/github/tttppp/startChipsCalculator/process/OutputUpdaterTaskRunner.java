package com.github.tttppp.startChipsCalculator.process;

import java.util.List;

import com.github.tttppp.startChipsCalculator.ui.OutputTextViewWrapper;
import com.github.tttppp.startChipsCalculator.ui.ProgressBarWrapper;

public class OutputUpdaterTaskRunner {
	private OutputUpdaterTask outputUpdaterTask;

	public void createAndExecute(ProgressBarWrapper progressBarWrapper, OutputTextViewWrapper output,
	                             List<String> dictionaries, InputParameters inputParameters) {
		if (outputUpdaterTask != null) {
			outputUpdaterTask.cancel(true);
		}
		outputUpdaterTask = new OutputUpdaterTask(progressBarWrapper, output);
		outputUpdaterTask.execute(inputParameters);
	}
}
