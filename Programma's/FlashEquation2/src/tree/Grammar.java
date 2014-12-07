package tree;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the grammar that is going to be used to construct the Equation tree.
 * @author Jeroen & Tom
 *
 */
public class Grammar {

	private List<Operand> operandPossibilities = new ArrayList<Operand>();
	
	/**
	 * Constructor
	 */
	public Grammar() {
		
	}
	//TODO Gebruik operand en Non-terminal om dynamisch te werken.
	/**
	 * 
	 * @param equation
	 * @return
	 * 			null if the equation does not start with a nonTerminal
	 */
	public static List<Equation> expand(Equation equation) {
		// we replace the first nonTerminal in the equation
		Symbol firstSymbol = equation.getListOfSymbols().get(0);
		if(firstSymbol.isNonTerminal()) {
			
		} else {
			return null;
		}
		
		
		return null;
	}
	
}
