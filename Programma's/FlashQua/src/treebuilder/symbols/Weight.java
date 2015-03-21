package treebuilder.symbols;


/**
 * Represents the terminal symbols.
 * @author Jeroen & Tom
 *
 */
@SuppressWarnings("serial")
public class Weight extends Terminal {
	
	public Weight(double value, int number) {
		super(value, number);
		super.representation = "W" + value;
	}

	/**
	 * @return
	 * 		True if this is a weight
	 * 		False if this is a column value
	 */
	public boolean isWeight() {
		return true;
	}
	
	
	
	
}
