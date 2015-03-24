package treebuilder.comparators;

import treebuilder.comparators.Comparator;

public class Equals extends Comparator {

	@Override
	public boolean compareOK(double value1, double value2) {
		return value1 == value2;
	}

	@Override
	public String getRepresentation() {
		return "=";
	}
	
}
