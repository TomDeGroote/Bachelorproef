package treebuilder.comparators;

public abstract class Comparator {

	/**
	 * Compares two values
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 * 		True if value1 C value2
	 * 		False if value1 !C value2
	 */
	public abstract boolean compareOK(double value1, double value2);
	
	public abstract String getRepresentation();
}
