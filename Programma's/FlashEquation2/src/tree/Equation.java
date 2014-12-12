package tree;
import java.io.Serializable;
import java.util.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an equation that can be formed by combining symbols.
 * @author Jeroen en Tom
 *
 */
@SuppressWarnings("serial")
public class Equation implements Serializable {
	
	List<Symbol> listOfSymbols;
	String representation;
	private boolean prooned = false;
	
	/**
	 * Constructor when no parameters are given
	 * This constructor will generate an equation = E
	 * 
	 */
	public Equation(){
		Symbol startSymbol = new NonTerminal("E");
		this.listOfSymbols = new ArrayList<Symbol>();
		this.listOfSymbols.add(startSymbol);
		this.setRepresentation();
	}
	

	/**
	 * This constructor will generate a equation containing the symbols
	 * of inputlist using those symbols in the same order.
	 * @param inputlist
	 * 			List of symbols in order for this equation
	 */
	public Equation(List<Symbol> inputlist){
		this.listOfSymbols = inputlist;
		this.setRepresentation();
	}
	
	/**
	 * Creates a representation of the current equation represented
	 * by listOfSymbols. this.representation is then set to the representation created.
	 */
	private void setRepresentation() {
		String currentString = "";
		for(Symbol currentSymbol: listOfSymbols){
			currentString += currentSymbol.toString();
		}
		this.representation = currentString;
	}

	/**
	 * @return
	 * 		Returns the list of symbols of this equation, the symbols are in order.
	 */
	public List<Symbol> getListOfSymbols() {
		return this.listOfSymbols;
	}
	
	/**
	 * Returns a string version of this equation
	 */
	@Override
	public String toString() {
		return this.representation;
	}
	
	/**
	 * Overrides the equal method
	 * this method sees if a given equation and this equation are equal
	 */
	@Override
    public boolean equals(Object obj) {
		if (!(obj instanceof Equation))
			return false;
		if (obj == this)
			return true;

		Equation otherSymbol = (Equation) obj;
		return new EqualsBuilder().
				// if deriving: appendSuper(super.equals(obj)).
				append(representation, otherSymbol.representation).
				append(listOfSymbols, otherSymbol.listOfSymbols).
				isEquals();
    }
	
	/**
	 * Overrides the method to create a hash code for this object class
	 */
	@Override
	public int hashCode() {
       return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
           // if deriving: appendSuper(super.hashCode()).
           append(listOfSymbols).
           append(prooned).
           append(representation).
           toHashCode();
	}


	public boolean isProoned() {
		return prooned;
	}


	public void setProoned(boolean prooned) {
		this.prooned = prooned;
	}

	
	
	
}
