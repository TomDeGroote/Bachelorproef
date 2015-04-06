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
	
	/**
	 * Parses the comparator to be used, default -> =
	 * @param comparator
	 * 			The comparator to be parsed
	 * @return
	 * 			The Comparator (as a class)
	 */
	public static Comparator parseComparator(String comparator) {
		switch (comparator) {
		case "=":
			return new Equals();
		case "<":
			return new SmallerThan();
		case "<=":
			return new SmallerThanOrEquals();
		case ">":
			return new GreaterThan();
		case ">=":
			return new GreaterThanOrEquals();
		default:
			return new Equals();
		}
	}
}
