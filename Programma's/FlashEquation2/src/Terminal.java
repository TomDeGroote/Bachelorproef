/**
 * Represents the terminal symbols.
 * @author Jeroen
 *
 */
public class Terminal extends Symbol {

	public Terminal(String terminal){
		super.representation = terminal;
	}
	
	@Override
	public boolean isOperand() {
		return false;
	}

	@Override
	public boolean isNonTerminal() {
		return false;
	}

	@Override
	public boolean isTerminal() {
		return true;
	}

	@Override
	public String symbolToString() {
		return super.representation;
	}

}
