package tree;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the grammar that is going to be used to construct the Equation tree.
 * @author Jeroen & Tom
 *
 */
public class Grammar {

	private static List<Operand> possibleOperands = new ArrayList<Operand>();
	private static final String nonTerminalRepresentation = "E";

	/**
	 * 	This method will add all possible operands to operandPossiblities
	 */
	public static void initialize() {
		// define all the possible operands
		possibleOperands.add(new Operand("*", false, true));
		possibleOperands.add(new Operand("/", false, false));
		possibleOperands.add(new Operand("+", true, true));
		possibleOperands.add(new Operand("-", true, false));
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
		if(possibleOperands.isEmpty()) {
			initialize();
		}
		List<Equation> expandedEquations = new ArrayList<Equation>();
		// we replace the first nonTerminal in the equation
		Symbol firstSymbol = equation.getListOfSymbols().get(0); // get the first symbol
		if(firstSymbol.isNonTerminal()) {
			for(Operand operand : possibleOperands) {
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

	/**
	 * @return
	 * 			A list of possible operands
	 */
	public static List<Operand> getPossibleOperands() {
		if(possibleOperands.isEmpty()) {
			initialize();
		}
		return possibleOperands;
	}

	/**
	 * @param s
	 * 		The string to be tested if it is an operand
	 * @return
	 * 		True if s is an operand
	 * 		False if s is not an operand
	 */
	public static boolean isOperand(String s) {
		for(Operand operand : possibleOperands) {
			if(operand.toString().equals(s)) {
				return true;
			}
		} 
		return false;
	}

	/**
	 * @param valueSolution1
	 * 		A double
	 * @param operand
	 * 		the operand
	 * @param valueSolution2
	 * 		A double
	 * @return
	 * 		result of value1 operand value2
	 */
	public static Double getValue(Double value1, Operand operand, Double value2) {
		if(operand.toString().equals("+")) {
			return value1 + value2;
		} else if(operand.toString().equals("-")) {
			return value1 - value2;
		} else if(operand.toString().equals("*")) {
			return value1 * value2;
		} else if(operand.toString().equals("/")) {
			return value1 / value2;
		} else 
			return 0.0;
	}

	/**
	 * @param valueSolution1
	 * 		A double
	 * @param string
	 * 		the operand
	 * @param valueSolution2
	 * 		A double
	 * @return
	 * 		result of value1 operand value2
	 */
	public static Double getValue(Double value1, String operand, Double value2) {
		if(operand.equals("+")) {
			return value1 + value2;
		} else if(operand.equals("-")) {
			return value1 - value2;
		} else if(operand.equals("*")) {
			return value1 * value2;
		} else if(operand.equals("/")) {
			return value1 / value2;
		} else 
			return 0.0;
	}

	/**
	 * @param str
	 * 		The string to be tested if it is a splittable operand
	 * @return
	 * 		True if s is a splittable operand
	 * 		False if s is not a splittable operand
	 */
	public static boolean isSplittableOperand(String str) {

		for(Operand operand : possibleOperands) {
			if(operand.toString().equals(str) && operand.isSplitable()) {
				return true;
			}
		} 
		return false;

	}

}
