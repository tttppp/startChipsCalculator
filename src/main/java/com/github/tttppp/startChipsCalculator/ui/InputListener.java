package com.github.tttppp.startChipsCalculator.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.github.tttppp.startChipsCalculator.process.OutputUpdater;

public class InputListener implements TextWatcher {

	private EditText input;
	private OutputUpdater outputUpdater;

	public InputListener(EditText input, OutputUpdater outputUpdater) {
		this.input = input;
		this.outputUpdater = outputUpdater;
	}

	public void afterTextChanged(Editable unused) {
		outputUpdater.inputChanged(input.getText().toString());
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

}
