package treebuilder.symbols;


/**
 * Represents the terminal symbols.
 * @author Jeroen & Tom
 *
 */
@SuppressWarnings("serial")
public class ColumnValue extends Terminal {

	private final int number;
	
	public ColumnValue(double value, int number) {
		super(value);
		this.number = number;
		
		super.representation = "K" + this.number;
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
	 * @return the number of this terminal
	 * 		-> in case of this being a weight the number will be equal to the value
	 */
	public int getNumber() {
		return this.number;
	}
	
	
	
	
}
