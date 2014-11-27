/**
 * A superclass that represents operands, non-terminals and terminals.
 * @author Jeroen
 *
 */
public abstract class Symbol{
	
	protected String representation;

	public abstract boolean isOperand();
	
	public abstract boolean isNonTerminal();

	public abstract boolean isTerminal();
	
	public abstract String symbolToString();
	
}
