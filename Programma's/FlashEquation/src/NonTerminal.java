
/**
 * Represents the nonterminals
 */
public class NonTerminal extends Symbol{

	private final String value; // The value of the nonterminal (i.e. E)
	
	/**
	 * @param value
	 *  	value of the nonterminal
	 */
	public NonTerminal(String value) {
		super.representation = value;
		super.nonTerminal = true;
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	
}
