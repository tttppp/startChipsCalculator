package com.github.tttppp.startChipsCalculator.process;

import java.util.List;

public class InputParameters {
	private int players;
	private int colours;
	private List<Integer> quantities;

	public InputParameters(int players, int colours, List<Integer> quantities) {
		this.players = players;
		this.colours = colours;
		this.quantities = quantities;
	}

	public int getPlayers() {
		return players;
	}

	public void setPlayers(int players) {
		this.players = players;
	}

	public int getColours() {
		return colours;
	}

	public void setColours(int colours) {
		this.colours = colours;
	}

	public List<Integer> getQuantities() {
		return quantities;
	}

	public void setQuantities(List<Integer> quantities) {
		this.quantities = quantities;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof InputParameters)) {
			return false;
		}
		InputParameters otherIP = (InputParameters) other;
		return (otherIP.colours == colours && otherIP.players == players && otherIP.quantities
		    .equals(quantities));
	}

	@Override
	public int hashCode() {
		return colours + players * 31 + quantities.hashCode() * 31 * 31;
	}
}
