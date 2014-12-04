package tree;
/**
 * Represents the operand symbols.
 * @author Jeroen
 *
 */
public class Operand extends Symbol {

	public Operand(String value) {
		super.representation = value;
	}
	
	@Override
	public boolean isOperand() {
		return true;
	}

	@Override
	public boolean isNonTerminal() {
		return false;
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
