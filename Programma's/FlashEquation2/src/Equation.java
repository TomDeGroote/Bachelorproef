import java.util.List;

/**
 * Represents an equation that can be formed by combining symbols.
 * @author Jeroen
 *
 */
public class Equation{
	
	List<Symbol> listOfSymbols;
	String representation;
	
	public Equation(List<Symbol> inputlist){
		listOfSymbols = inputlist;
		String currentString = "";
		for(Symbol currentSymbol: this.getListOfSymbols() ){
			currentString.concat(currentSymbol.symbolToString());
		}
		this.representation = currentString;
	}
	
	public List<Symbol> getListOfSymbols(){
		return this.listOfSymbols;
	}
	
	@Override
	public String toString(){
		return representation;
	}
}
