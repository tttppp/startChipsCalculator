package com.github.tttppp.regexEvaluator.process;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.junit.Before;
import org.junit.Test;

import com.github.tttppp.regexEvaluator.ui.OutputTextViewWrapper;
import com.github.tttppp.regexEvaluator.ui.ProgressBarWrapper;

public class OutputUpdaterTest {
	/** The class under test. */
	OutputUpdater outputUpdater;

	OutputTextViewWrapper mockOutputTextViewWrapper = mock(OutputTextViewWrapper.class);
	ProgressBarWrapper mockProgressBarWrapper = mock(ProgressBarWrapper.class);
	OutputUpdaterTaskRunner mockFactory = mock(OutputUpdaterTaskRunner.class);

	@Before
	public void setup() {
		reset(mockOutputTextViewWrapper, mockProgressBarWrapper, mockFactory);

		outputUpdater = new OutputUpdater(mockOutputTextViewWrapper,
		                                  mockProgressBarWrapper);
		outputUpdater.setOutputUpdaterTaskFactory(mockFactory);
	}

	@Test
	public void testRegexChanged_valid() {
		outputUpdater.regexChanged("apple");

		verify(mockFactory).createAndExecute(eq(mockProgressBarWrapper),
		                                     eq(mockOutputTextViewWrapper),
		                                     any(List.class),
		                                     any(Pattern.class));
		// verify(mockOutputTextViewWrapper).setText("apple\napple's");
	}

	@Test
	public void testRegexChanged_invalid() {
		outputUpdater.regexChanged("appl[");

		String expected = "";
		try {
			Pattern.compile("appl[");
		} catch (PatternSyntaxException e) {
			expected = e.getMessage();
		}

		verify(mockOutputTextViewWrapper).setText(expected);
	}

	@Test
	public void testDictionaryChanged_oneDictionary() {
		// Initialise with previous test
		// testRegexChanged_valid();

		// Method under test.
		outputUpdater.dictionaryChanged(Arrays.asList("english-words.35.txt"));

		verify(mockFactory).createAndExecute(eq(mockProgressBarWrapper),
		                                     eq(mockOutputTextViewWrapper),
		                                     any(List.class),
		                                     any(Pattern.class));
		// verify(mockOutputTextViewWrapper)
		// .setText("apples\ngrapple\ngrappled\ngrapples\npineapple\npineapple's\npineapples");
	}

	@Test
	public void testDictionaryChanged_manyDictionaries() {
		// Initialise with previous test
		// testRegexChanged_valid();

		// Method under test.
		outputUpdater.dictionaryChanged(Arrays.asList("english-words.10.txt",
		                                              "english-words.20.txt",
		                                              "english-words.35.txt"));

		verify(mockFactory).createAndExecute(eq(mockProgressBarWrapper),
		                                     eq(mockOutputTextViewWrapper),
		                                     any(List.class),
		                                     any(Pattern.class));
		// verify(mockOutputTextViewWrapper)
		// .setText("apple\napple's\napples\ngrapple\ngrappled\ngrapples\npineapple\npineapple's\npineapples");
	}
}
