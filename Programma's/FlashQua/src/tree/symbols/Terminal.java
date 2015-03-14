package tree.symbols;


/**
 * Represents the terminal symbols.
 * @author Jeroen & Tom
 *
 */
@SuppressWarnings("serial")
public class Terminal extends Symbol {

	private final Double value;
	
	public Terminal(String terminal, Double value) {
		super.representation = terminal;
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
	public Double getValue() {
		return value;
	}
	
	
	
	
}
