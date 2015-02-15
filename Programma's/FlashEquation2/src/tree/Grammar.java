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
	private static void initialize() {
		// define all the possible operands
		possibleOperands.add(new Operand("*", false, true, 1));
		possibleOperands.add(new Operand("/", false, false, 1));
		possibleOperands.add(new Operand("+", true, true, 0));
		possibleOperands.add(new Operand("-", true, false, 0));
	}
	
	/**
	 * @return
	 * 		The weights to be used (currently 1 tem 9)
	 */
	public static Double[] getWeights() {
		Double[] weights = new Double[9];
		for(int i = 0; i < weights.length; i++) {
			weights[i] = (double) (i+1);
		}
		return weights;
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
		if(possibleOperands.isEmpty()) {
			initialize();
		}
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
	 * @param valueSolution2
	 * 		A double
	 * @return
	 * 		result of value1 operand value2
	 */
	public static Double getValueConcatenate(Double value1, Double value2) {
		return value1 + value2;
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
		if(possibleOperands.isEmpty()) {
			initialize();
		}
		for(Operand operand : possibleOperands) {
			if(operand.toString().equals(str) && operand.isSplitable()) {
				return true;
			}
		} 
		return false;

	}

	/**
	 * Evaluates +E or -E
	 * @param terminalEq
	 * 		The trivial terminal equation
	 * @return
	 * 		The value of the trivial terminal Equation
	 */
	public static Double evaluateTrivial(Equation terminalEq) {
		Operand op = (Operand) terminalEq.getListOfSymbols().get(0);
		Double value = ((Terminal) terminalEq.listOfSymbols.get(1)).getValue();
		return getValue((double) op.getNeutralElement(), op, value);
	}

	/**
	 * 
	 * @param op
	 * 			The operand
	 * @param y
	 * 			The value
	 * @return
	 * 			The real value
	 */
	public static Double evaluateTrivialValue(Operand op, Double y) {
		return getValue((double) op.getNeutralElement(), op, y);
	}

	/**
	 * TODO comment + oppassen CFG
	 * @param eq
	 * @return
	 */
	public static Equation convertTrivialEq(Equation eq) {
		List<Symbol> newSymbols = new ArrayList<Symbol>();
		if(eq.getListOfSymbols().get(0).toString().equals("-")) {
			newSymbols.add(0, new Operand("+", true, true, 0));
			Terminal old = (Terminal) eq.getListOfSymbols().get(1);
			newSymbols.add(new Terminal("-" + old.toString(), old.getValue()));
			return new Equation(newSymbols);
		} else {
			return eq;
		}
	}
	
	/**
	 * TODO comment + oppassen CFG
	 * @param eq
	 * @return
	 */
	public static Equation convertEq(Equation eq) {
		List<Symbol> newSymbols = new ArrayList<Symbol>();
		if(eq.getListOfSymbols().get(0).toString().equals("-")) {
			newSymbols.add(0, new Operand("+", true, true, 0));
			Terminal old = (Terminal) eq.getListOfSymbols().get(1);
			newSymbols.add(new Terminal("-" + old.toString(), old.getValue()));
			eq.getListOfSymbols().remove(0);
			eq.getListOfSymbols().remove(0);
			if(!eq.getListOfSymbols().isEmpty()) {
				newSymbols.addAll(eq.getListOfSymbols());
			}
			return new Equation(newSymbols);
		} else {
			return eq;
		}
	}
	
	/**
	 * TODO comments
	 * @param s
	 * @return
	 */
	public static Operand getCorrespondingOperand(String s) {
		if(possibleOperands.isEmpty()) {
			initialize();
		}
		for(Operand operand : possibleOperands) {
			if(operand.toString().equals(s) && operand.isSplitable()) {
				return operand;
			}
		} 
		return null;
	}

}
