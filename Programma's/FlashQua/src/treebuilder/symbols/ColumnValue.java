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
		super.representation = "K" + number;
	}

	/**
	 * @return
	 * 		True if this is a weight
	 * 		False if this is a column value
	 */
	public boolean isWeight() {
		return false;
	}
	
	
	
	
}
