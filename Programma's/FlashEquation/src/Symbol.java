/**
 * Super class who unites all Terminals, NonTerminals and Operands
 * @author tom
 *
 */
public class Symbol {
	protected boolean nonTerminal;
	protected boolean terminal;
	protected boolean operand;
	
	protected String representation;
	
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
	 * 		String representation of the current symbol
	 */
	public String getRepresentation() {
		return representation;
	}
}
