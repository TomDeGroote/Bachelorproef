package treebuilder.symbols;


/**
 * Represents the terminal symbols.
 * @author Jeroen & Tom
 *
 */
@SuppressWarnings("serial")
public abstract class Terminal extends Symbol {

	private final Double value;
	
	public Terminal(double value) {
		this.value = value;
	}
	
	@Override
	public boolean isTerminal() {
		return true;
	}

	/**
	 * @return
	 * 		The value of this terminal
	 */
	public double getValue() {
		return this.value;
	}

	/**
	 * @return
	 * 		True if this is a weight
	 * 		False if this is a column value
	 */
	public abstract boolean isWeight();
	
	/**
	 * @return the number of this terminal
	 * 		-> in case of this being a weight the number will be equal  to the value
	 */
	public abstract int getNumber();
	
	
	
	
}
