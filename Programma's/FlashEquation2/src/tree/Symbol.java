package tree;
/**
 * A superclass that represents operands, non-terminals and terminals.
 * @author Jeroen & Tom
 *
 */
public abstract class Symbol {
	
	protected String representation;
	
	protected boolean nonTerminal;
	protected boolean terminal;
	protected boolean operand;

	/**
	 * @return
	 * 		True: this symbol is a nonterminal
	 * 		False: this symbol is not a nonterminal
	 */
	public boolean isNonTerminal() {
		return nonTerminal;
	}
	
	/**
	 * @return
	 * 		True: this symbol is a terminal
	 * 	 	False: this symbol is not a terminal
	 */
	public boolean isTerminal() {
		return terminal;
	}
	
	/**
	 * @return
	 * 		True: this symbol is a operand
	 * 		False: this symbol is not a operand
	 */
	public boolean isOperand() {
		return operand;
	}
	
	/**
	 * @return
	 * 		The representation of the symbol
	 */
	public String symbolToString() {
		return representation;
	}
	
}
