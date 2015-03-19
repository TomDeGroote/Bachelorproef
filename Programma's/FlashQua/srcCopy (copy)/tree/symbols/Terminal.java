package tree.symbols;


/**
 * Represents the terminal symbols.
 * @author Jeroen & Tom
 *
 */
@SuppressWarnings("serial")
public class Terminal extends Symbol {

	private final Double value;
	private final boolean isWeight;
	
	public Terminal(String terminal, Double value, boolean isWeight) {
		super.representation = terminal;
		this.value = value;
		this.isWeight = isWeight;
	}
	
	@Override
	public boolean isTerminal() {
		return true;
	}

	/**
	 * @return
	 * 		The value of this terminal
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * @return the isWeight
	 */
	public boolean isWeight() {
		return isWeight;
	}
	
	
	
	
}
