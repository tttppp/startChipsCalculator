package com.github.tttppp.startChipsCalculator.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.tttppp.startChipsCalculator.R;
import com.github.tttppp.startChipsCalculator.process.OutputUpdater;

public class InputListener implements TextWatcher {

	private Activity activity;
	private OutputUpdater outputUpdater;

	public InputListener(Activity activity, OutputUpdater outputUpdater) {
		this.activity = activity;
		this.outputUpdater = outputUpdater;
	}

	public void afterTextChanged(Editable unused) {
		EditText numberOfPlayers = (EditText) activity.findViewById(R.id.numberOfPlayers);
		EditText numberOfColours = (EditText) activity.findViewById(R.id.numberOfColours);
		LinearLayout chipCountContainer = (LinearLayout) activity.findViewById(R.id.chipCountContainer);
		List<String> chipCounts = new ArrayList<String>();
		for (int i = 0; i < chipCountContainer.getChildCount(); i++) {
			EditText chipCountView = (EditText) chipCountContainer.getChildAt(i);
			chipCounts.add(chipCountView.getText().toString());
		}
		outputUpdater.inputChanged(numberOfPlayers.getText().toString(), numberOfColours.getText().toString(), chipCounts);
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

}
