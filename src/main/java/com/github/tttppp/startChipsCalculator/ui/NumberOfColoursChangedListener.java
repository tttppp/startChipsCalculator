package com.github.tttppp.startChipsCalculator.ui;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.tttppp.startChipsCalculator.util.IdGenerator;

public class NumberOfColoursChangedListener implements TextWatcher {

	private EditText input;
	private InputListener inputListener;
	private LinearLayout chipCountContainer;

	public NumberOfColoursChangedListener(EditText input, InputListener inputListener,
	                                      LinearLayout chipCountContainer) {
		this.input = input;
		this.inputListener = inputListener;
		this.chipCountContainer = chipCountContainer;
	}

	public void afterTextChanged(Editable unused) {
		int numberOfColours = 0;
		try {
			numberOfColours = Integer.valueOf(input.getText().toString().trim());
		} catch (NumberFormatException e) {
			// TODO Error handling.
			return;
		}
		if (numberOfColours != chipCountContainer.getChildCount()) {
			chipCountContainer.removeAllViews();
			for (int colour = 0; colour < numberOfColours; colour++) {
				EditText colourQuantity = new EditText(chipCountContainer.getContext());
				colourQuantity.setId(IdGenerator.generateViewId());
				colourQuantity.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
				colourQuantity.addTextChangedListener(inputListener);
				chipCountContainer.addView(colourQuantity);
			}
		}
		inputListener.afterTextChanged(null);
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

}
