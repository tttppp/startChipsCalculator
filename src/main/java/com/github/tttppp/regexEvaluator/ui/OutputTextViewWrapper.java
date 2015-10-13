package com.github.tttppp.regexEvaluator.ui;

import android.widget.TextView;

public class OutputTextViewWrapper {
	private TextView output;

	public OutputTextViewWrapper(TextView output) {
		this.output = output;
	}

	public void setText(String outputText) {
		output.setText(outputText);
	}
}
