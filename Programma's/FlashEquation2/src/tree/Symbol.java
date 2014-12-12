package tree;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A superclass that represents operands, non-terminals and terminals.
 * @author Jeroen & Tom
 *
 */
@SuppressWarnings("serial")
public abstract class Symbol implements Serializable {
	
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
	@Override
	public String toString() {
		return representation;
	}
	
	/**
	 * Overrides the equal method
	 * this method sees if a given symbol and this symbol are equal
	 */
	@Override
    public boolean equals(Object obj) {
		if (!(obj instanceof Symbol))
			return false;
		if (obj == this)
			return true;

		Symbol otherSymbol = (Symbol) obj;
		return new EqualsBuilder().
				// if deriving: appendSuper(super.equals(obj)).
				append(representation, otherSymbol.representation).
				append(operand, otherSymbol.operand).
				append(terminal, otherSymbol.terminal).
				append(nonTerminal, otherSymbol.nonTerminal).
				isEquals();
    }
	
	/**
	 * Overrides the method to create a hash code for this object class
	 */
	@Override
	public int hashCode() {
       return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
           // if deriving: appendSuper(super.hashCode()).
           append(representation).
           append(operand).
           append(terminal).
           append(nonTerminal).
           toHashCode();
	}
	
}
