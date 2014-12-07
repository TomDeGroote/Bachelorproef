package tree;
import java.util.*;

/**
 * Represents an equation that can be formed by combining symbols.
 * @author Jeroen en Tom
 *
 */
public class Equation {
	
	List<Symbol> listOfSymbols;
	String representation;
	
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
}
