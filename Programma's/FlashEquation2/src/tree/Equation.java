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
	
	public Equation(){
		Symbol startSymbol = new NonTerminal("E");
		List<Symbol> newList = new ArrayList<Symbol>();
		newList.add(startSymbol);
		listOfSymbols = newList;
		String currentString = "";
		for(Symbol currentSymbol: this.getListOfSymbols() ){
			currentString.concat(currentSymbol.symbolToString());
		}
		this.representation = currentString;
	}
	
	public Equation(List<Symbol> inputlist){
		listOfSymbols = inputlist;
		String currentString = "";
		for(Symbol currentSymbol: this.getListOfSymbols() ) {
			currentString.concat(currentSymbol.symbolToString());
		}
		this.representation = currentString;
	}
	
	public List<Symbol> getListOfSymbols() {
		return this.listOfSymbols;
	}
	
	@Override
	public String toString() {
		return representation;
	}
}
