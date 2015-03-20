package treebuilder.symbols;


/**
 * Represents the non-terminal symbols.
 * @author Jeroen & Tom
 *
 */
@SuppressWarnings("serial")
public class NonTerminal extends Symbol {

	public NonTerminal(String nonTerminal) {
		super.representation = nonTerminal;
	}
	
	@Override
	public boolean isNonTerminal() {
		return true;
	}
}
