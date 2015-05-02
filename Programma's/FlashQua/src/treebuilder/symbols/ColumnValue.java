package treebuilder.symbols;


/**
 * Represents the terminal symbols.
 * @author Jeroen & Tom
 *
 */
@SuppressWarnings("serial")
public class ColumnValue extends Terminal {
	
	public ColumnValue(double value, int number) {
		super(value, number);		
		super.representation = getCharForNumber(number+1);//"K" + number;
	}

	/**
	 * @return
	 * 		True if this is a weight
	 * 		False if this is a column value
	 */
	public boolean isWeight() {
		return false;
	}
	
	/**
	 * Converts a number to a letter
	 * 
	 * @param i
	 * 		The number
	 * @return
	 * 		The letter
	 */
	private String getCharForNumber(int i) {
	    return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
	}
	
	
	
}
