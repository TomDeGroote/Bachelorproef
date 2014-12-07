package tree;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the grammar that is going to be used to construct the Equation tree.
 * @author Jeroen & Tom
 *
 */
public class Grammar {

	private static List<Operand> operandPossibilities = new ArrayList<Operand>();
	private static final String nonTerminalRepresentation = "E";
	
	/**
	 * Constructor
	 * 		This constructor will add all possible operands to operandPossiblities
	 */
	public Grammar() {
		// define all the possible operands
		operandPossibilities.add(new Operand("*", false));
		operandPossibilities.add(new Operand("/", false));
		operandPossibilities.add(new Operand("+", true));
		operandPossibilities.add(new Operand("-", true));
	}
	
	/**
	 * This method expands a given equation
	 * @param equation
	 * 			An equation starting with a nonTerminal
	 * @return
	 * 			A list of equations which represent the expansions of the given equation
	 * 			null if the equation does not start with a nonTerminal
	 */
	public static List<Equation> expand(Equation equation) {
		List<Equation> expandedEquations = new ArrayList<Equation>();
		// we replace the first nonTerminal in the equation
		Symbol firstSymbol = equation.getListOfSymbols().get(0); // get the first symbol
		if(firstSymbol.isNonTerminal()) {
			for(Operand operand : operandPossibilities) {
				// for every possible operand generate the expansion
				List<Symbol> newSymbols = new ArrayList<Symbol>();
				newSymbols.add(new NonTerminal(nonTerminalRepresentation));
				newSymbols.add(operand);
				for(Symbol symbol : equation.listOfSymbols) {
					newSymbols.add(symbol);
				}
				// add the made expansion to the list of expansion equations
				expandedEquations.add(new Equation(newSymbols));
			}
		} else {
			return null;
		}	
		return expandedEquations;
	}
	
}
