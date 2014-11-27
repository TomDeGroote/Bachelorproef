/**
 * Represents the non-terminal symbols.
 * @author Jeroen
 *
 */
public class NonTerminal extends Symbol {

	public NonTerminal(String nonTerminal){
		super.representation = nonTerminal;
	}
	
	@Override
	public boolean isOperand() {
		return false;
	}

	@Override
	public boolean isNonTerminal() {
		return true;
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public String symbolToString() {
		return super.representation;
	}

}
