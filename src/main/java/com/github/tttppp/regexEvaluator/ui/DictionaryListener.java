package com.github.tttppp.regexEvaluator.ui;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.github.tttppp.regexEvaluator.process.OutputUpdater;

public class DictionaryListener implements OnItemSelectedListener {
	private OutputUpdater outputUpdater;

	public DictionaryListener(OutputUpdater outputUpdater) {
		this.outputUpdater = outputUpdater;
	}

	public void onItemSelected(AdapterView<?> parentView,
	                           View selectedItemView, int position, long id) {
		List<String> dictionaries = new ArrayList<String>();
		for (int i = 0; i <= position; i++) {
			String dictionary = parentView.getItemAtPosition(i).toString();
			dictionaries.add(dictionary);
		}
		outputUpdater.dictionaryChanged(dictionaries);

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
