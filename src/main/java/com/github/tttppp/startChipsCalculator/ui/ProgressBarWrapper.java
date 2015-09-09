package com.github.tttppp.startChipsCalculator.ui;

import android.widget.ProgressBar;

public class ProgressBarWrapper {
	private ProgressBar progressBar;

	public ProgressBarWrapper(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public void setProgress(int progress) {
		progressBar.setProgress(progress);
	}

	public int getMax() {
		return progressBar.getMax();
	}

	public void setVisibility(int visible) {
		progressBar.setVisibility(visible);
	}
}
