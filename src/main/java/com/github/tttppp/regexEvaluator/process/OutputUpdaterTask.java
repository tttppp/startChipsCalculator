package com.github.tttppp.regexEvaluator.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import android.os.AsyncTask;
import android.view.View;

import com.github.tttppp.regexEvaluator.ui.OutputTextViewWrapper;
import com.github.tttppp.regexEvaluator.ui.ProgressBarWrapper;

public class OutputUpdaterTask extends AsyncTask<Pattern, Integer, String> {
	/** The maximum number of words to put in the output. */
	private static final int MAX_WORDS = 100;

	/** The progress bar to update periodically. */
	private ProgressBarWrapper progressBarWrapper;
	/** The wrapper for the output UI field. */
	private OutputTextViewWrapper output;

	/** The list of dictionaries to check. */
	private List<String> dictionaries = Arrays.asList("english-words.10.txt");

	/**
	 * Constructor.
	 * 
	 * @param progressBarWrapper The progress bar to update periodically.
	 * @param output The wrapper for the output UI field.
	 */
	public OutputUpdaterTask(ProgressBarWrapper progressBarWrapper,
	                         OutputTextViewWrapper output,
	                         List<String> dictionaries) {
		this.progressBarWrapper = progressBarWrapper;
		this.output = output;
		this.dictionaries = dictionaries;
	}

	/** Show the progress bar. */
	@Override
	protected void onPreExecute() {
		progressBarWrapper.setVisibility(View.VISIBLE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String doInBackground(Pattern... patterns) {
		Pattern pattern = patterns[0];

		List<String> outputWords = new ArrayList<String>();
		for (String dictionary : dictionaries) {
			InputStream wordsStream = Thread.currentThread()
			    .getContextClassLoader().getResourceAsStream(dictionary);
			BufferedReader wordsBuffer = new BufferedReader(
			                                                new InputStreamReader(
			                                                                      wordsStream));

			String word = wordFromBuffer(wordsBuffer);
			while (word != null && !outputWords.contains(word)
			    && outputWords.size() < MAX_WORDS) {
				String matchedWord = matchWord(word, pattern);
				if (matchedWord != null) {
					outputWords.add(matchedWord);
					updateProgress(outputWords, dictionary);
				}
				if (isCancelled()) {
					outputWords.add("...");
					return StringUtils.join(outputWords, "\n");
				}
				word = wordFromBuffer(wordsBuffer);
			}
			updateProgress(outputWords, dictionary);
		}
		Collections.sort(outputWords);
		if (outputWords.size() == MAX_WORDS) {
			outputWords.add("...");
		}
		return StringUtils.join(outputWords, "\n");
	}

	/**
	 * Return a word that should be stored as a single line in the buffer.
	 * 
	 * @param wordsBuffer
	 * @return
	 */
	private String wordFromBuffer(BufferedReader wordsBuffer) {
		String word = null;
		try {
			word = wordsBuffer.readLine();
		} catch (IOException e) {
			System.out.println(e);
		}
		return word;
	}

	/**
	 * If a word matches the pattern then return it.
	 * 
	 * @param word The word to test.
	 * @param pattern The compiled regex.
	 * @return Either the word, or null if it doesn't match.
	 */
	private String matchWord(String word, Pattern pattern) {
		Matcher matcher = pattern.matcher(word);
		if (matcher.find()) {
			return word;
		}
		return null;
	}

	/**
	 * Helper method to update the amount of progress.
	 * 
	 * @param outputWords The list of words found.
	 * @param dictionary The dictionary currently being checked.
	 */
	private void updateProgress(List<String> outputWords, String dictionary) {
		onProgressUpdate(outputWords.size(), dictionaries.indexOf(dictionary),
		                 dictionaries.size());
	}

	/**
	 * {@inheritDoc} Update the progress bar to show roughly how much of the
	 * task is done.
	 * 
	 * @param values Three int values must be supplied - the number of words
	 *        found so far, the number of files checked so far, and the total
	 *        number of files to check.
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		int words = values[0];
		int files = values[1];
		int totalFiles = values[2];

		int max = progressBarWrapper.getMax();

		int progress = Math.max(max * words / MAX_WORDS, max * files
		    / totalFiles);
		progressBarWrapper.setProgress(progress);
	}

	/** Display the results and hide the progress bar again. */
	@Override
	protected void onPostExecute(String result) {
		progressBarWrapper.setVisibility(View.GONE);
		output.setText(result);
	}
}
