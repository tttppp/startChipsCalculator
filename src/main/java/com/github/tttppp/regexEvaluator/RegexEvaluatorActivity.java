package com.github.tttppp.regexEvaluator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.tttppp.regexEvaluator.process.OutputUpdater;
import com.github.tttppp.regexEvaluator.ui.DictionaryListener;
import com.github.tttppp.regexEvaluator.ui.OutputTextViewWrapper;
import com.github.tttppp.regexEvaluator.ui.ProgressBarWrapper;
import com.github.tttppp.regexEvaluator.ui.RegexListener;

public class RegexEvaluatorActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TextView output = (TextView) findViewById(R.id.outputTextView);
		OutputTextViewWrapper outputTextViewWrapper = new OutputTextViewWrapper(
		                                                                        output);

		ProgressBar progressBar = (ProgressBar) findViewById(R.id.outputProgressBar);
		ProgressBarWrapper progressBarWrapper = new ProgressBarWrapper(
		                                                               progressBar);

		OutputUpdater outputUpdater = new OutputUpdater(outputTextViewWrapper,
		                                                progressBarWrapper);

		EditText input = (EditText) findViewById(R.id.inputEditText);
		input.addTextChangedListener(new RegexListener(input, outputUpdater));

		Spinner dictionarySpinner = (Spinner) findViewById(R.id.dictionarySpinner);
		OnItemSelectedListener dictionaryListner = new DictionaryListener(
		                                                                  outputUpdater);
		dictionarySpinner.setOnItemSelectedListener(dictionaryListner);
		String[] dictionaryArray = new String[10];
		dictionaryArray[0] = "english-words.10.txt";
		dictionaryArray[1] = "english-words.20.txt";
		dictionaryArray[2] = "english-words.35.txt";
		dictionaryArray[3] = "english-words.40.txt";
		dictionaryArray[4] = "english-words.50.txt";
		dictionaryArray[5] = "english-words.55.txt";
		dictionaryArray[6] = "english-words.60.txt";
		dictionaryArray[7] = "english-words.70.txt";
		dictionaryArray[8] = "english-words.80.txt";
		dictionaryArray[9] = "english-words.95.txt";
		ArrayAdapter<String> dictionaryAdapter = new ArrayAdapter<String>(
		                                                                  this,
		                                                                  android.R.layout.simple_spinner_item,
		                                                                  dictionaryArray);
		dictionarySpinner.setAdapter(dictionaryAdapter);
	}
}