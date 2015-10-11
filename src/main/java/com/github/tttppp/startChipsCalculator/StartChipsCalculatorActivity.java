package com.github.tttppp.startChipsCalculator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.tttppp.startChipsCalculator.process.OutputUpdater;
import com.github.tttppp.startChipsCalculator.ui.InputListener;
import com.github.tttppp.startChipsCalculator.ui.NumberOfColoursChangedListener;
import com.github.tttppp.startChipsCalculator.ui.OutputTextViewWrapper;
import com.github.tttppp.startChipsCalculator.ui.ProgressBarWrapper;

public class StartChipsCalculatorActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TextView output = (TextView) findViewById(R.id.outputTextView);
		OutputTextViewWrapper outputTextViewWrapper = new OutputTextViewWrapper(output);

		ProgressBar progressBar = (ProgressBar) findViewById(R.id.outputProgressBar);
		ProgressBarWrapper progressBarWrapper = new ProgressBarWrapper(progressBar);

		OutputUpdater outputUpdater = new OutputUpdater(outputTextViewWrapper, progressBarWrapper);

		EditText input = (EditText) findViewById(R.id.numberOfPlayers);
		InputListener inputListener = new InputListener(this, outputUpdater);
		input.addTextChangedListener(inputListener);
		input = (EditText) findViewById(R.id.numberOfColours);
		LinearLayout chipCountContainer = (LinearLayout) findViewById(R.id.chipCountContainer);
		input.addTextChangedListener(new NumberOfColoursChangedListener(input, inputListener, chipCountContainer));
	}
}