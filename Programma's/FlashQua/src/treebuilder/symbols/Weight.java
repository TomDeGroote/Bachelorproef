package treebuilder.symbols;


/**
 * Represents the terminal symbols.
 * @author Jeroen & Tom
 *
 */
@SuppressWarnings("serial")
public class Weight extends Terminal {
	
	public Weight(Double value) {
		super(value);
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
	
	/**
	 * @return the number of this terminal
	 * 		-> in case of this being a weight the number will be equal  to the value
	 */
	public int getNumber() {
		return (int) super.getValue();
	}
	
	
	
	
}
