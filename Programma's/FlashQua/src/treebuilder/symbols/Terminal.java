package treebuilder.symbols;


/**
 * Represents the terminal symbols.
 * @author Jeroen & Tom
 *
 */
@SuppressWarnings("serial")
public abstract class Terminal extends Symbol {

	private final double value;
	private final int number;
	
	public Terminal(double value, int number) {
		this.value = value;
		this.number = number;
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
	 */
	public int getNumber() {
		return this.number;
	}
	
	
}
