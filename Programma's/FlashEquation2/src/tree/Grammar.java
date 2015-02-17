package tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the grammar that is going to be used to construct the Equation
 * tree.
 * 
 * @author Jeroen & Tom
 *
 */
public class Grammar {
	private static List<Operand> possibleOperands = new ArrayList<Operand>();
	private static final String nonTerminalRepresentation = "E";
	private static final boolean USE_MAX_MIN = false;

	/**
	 * This method will add all possible operands to operandPossiblities
	 */
	private static void initialize() {
		// define all the possible operands
		possibleOperands.add(new Operand("*", false, true, 1));
		possibleOperands.add(new Operand("/", false, false, 1));
		possibleOperands.add(new Operand("+", true, true, 0));
		possibleOperands.add(new Operand("-", true, false, 0));
		if(USE_MAX_MIN) {
			possibleOperands.add(new Operand("Max", false, true, Integer.MAX_VALUE));
			possibleOperands.add(new Operand("Min", false, true, Integer.MIN_VALUE));
		}
	}

	/**
	 * @return The weights to be used (currently 1 tem 9)
	 */
	public static List<Terminal> getWeights() {
		List<Terminal> weights = new ArrayList<Terminal>();
		for (int i = 0; i < 9; i++) {
			weights.add(new Terminal("W" + (i + 1), (double) i + 1));
		}
		weights.add(new Terminal("N1", -1.0));
		return weights;
	}

	/**
	 *
	 * @param s
	 *            The string to be checked
	 * @return If the string is a weight or not
	 */
	public static boolean isWeight(String s) {
		for (Terminal w : getWeights()) {
			if (w.toString().equals(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method expands a given equation
	 * 
	 * @param equation
	 *            An equation starting with a nonTerminal
	 * @return A list of equations which represent the expansions of the given
	 *         equation null if the equation does not start with a nonTerminal
	 */
	public static List<Equation> expand(Equation equation) {
		if (possibleOperands.isEmpty()) {
			initialize();
		}
		List<Equation> expandedEquations = new ArrayList<Equation>();
		// we replace the first nonTerminal in the equation
		Symbol firstSymbol = equation.getListOfSymbols().get(0); // get the
		// first
		// symbol
		if (firstSymbol.isNonTerminal()) {
			for (Operand operand : possibleOperands) {
				// for every possible operand generate the expansion
				List<Symbol> newSymbols = new ArrayList<Symbol>();
				newSymbols.add(new NonTerminal(nonTerminalRepresentation));
				newSymbols.add(operand);
				for (Symbol symbol : equation.listOfSymbols) {
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
	 * @return A list of possible operands
	 */
	public static List<Operand> getPossibleOperands() {
		if (possibleOperands.isEmpty()) {
			initialize();
		}
		return possibleOperands;
	}

	/**
	 * @param s
	 *            The string to be tested if it is an operand
	 * @return True if s is an operand False if s is not an operand
	 */
	public static boolean isOperand(String s) {
		if (possibleOperands.isEmpty()) {
			initialize();
		}
		for (Operand operand : possibleOperands) {
			if (operand.toString().equals(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param valueSolution1
	 *            A double
	 * @param operand
	 *            the operand
	 * @param valueSolution2
	 *            A double
	 * @return result of value1 operand value2
	 */
	public static Double getValue(Double value1, Operand operand, Double value2) {
		if (operand.toString().equals("+")) {
			return value1 + value2;
		} else if (operand.toString().equals("-")) {
			return value1 - value2;
		} else if (operand.toString().equals("*")) {
			return value1 * value2;
		} else if (operand.toString().equals("/")) {
			return value1 / value2;
		} else if (operand.toString().equals("Max")) {
			return Math.max(value1, value2);
		} else if (operand.toString().equals("Min")) {
			return Math.min(value1, value2);
		} else
			return 0.0;
	}

	/**
	 * @param valueSolution1
	 *            A double
	 * @param valueSolution2
	 *            A double
	 * @return result of value1 operand value2
	 */
	public static Double getValueConcatenate(Double value1, Double value2) {
		return value1 + value2;
	}

	/**
	 * @param valueSolution1
	 *            A double
	 * @param string
	 *            the operand
	 * @param valueSolution2
	 *            A double
	 * @return result of value1 operand value2
	 */
	public static Double getValue(Double value1, String operand, Double value2) {
		if (operand.equals("+")) {
			return value1 + value2;
		} else if (operand.equals("-")) {
			return value1 - value2;
		} else if (operand.equals("*")) {
			return value1 * value2;
		} else if (operand.equals("/")) {
			return value1 / value2;
		}  else if (operand.equals("Max")) {
			return Math.max(value1, value2);
		} else if (operand.equals("Min")) {
			return Math.min(value1, value2);
		} else
			return 0.0;
	}

	/**
	 * @param str
	 *            The string to be tested if it is a splittable operand
	 * @return True if s is a splittable operand False if s is not a splittable
	 *         operand
	 */
	public static boolean isSplittableOperand(String str) {
		if (possibleOperands.isEmpty()) {
			initialize();
		}
		for (Operand operand : possibleOperands) {
			if (operand.toString().equals(str) && operand.isSplitable()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Evaluates +E or -E
	 * 
	 * @param terminalEq
	 *            The trivial terminal equation
	 * @return The value of the trivial terminal Equation
	 */
	public static Double evaluateTrivial(Equation terminalEq) {
		Operand op = (Operand) terminalEq.getListOfSymbols().get(0);
		Double value = ((Terminal) terminalEq.listOfSymbols.get(1)).getValue();
		return getValue((double) op.getNeutralElement(), op, value);
	}

	/**
	 *
	 * @param op
	 *            The operand
	 * @param y
	 *            The value
	 * @return The real value
	 */
	public static Double evaluateTrivialValue(Operand op, Double y) {
		return getValue((double) op.getNeutralElement(), op, y);
	}

	/**
	 * TODO comment + oppassen CFG
	 * 
	 * @param eq
	 * @return
	 */
	public static Equation convertTrivialEq(Equation eq) {
		List<Symbol> newSymbols = new ArrayList<Symbol>();
		if (eq.getListOfSymbols().get(0).toString().equals("-")) {
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
	 * 
	 * @param eq
	 * @return
	 */
	public static Equation convertEq(Equation eq) {
		List<Symbol> newSymbols = new ArrayList<Symbol>();
		if (eq.getListOfSymbols().get(0).toString().equals("-")) {
			newSymbols.add(0, new Operand("+", true, true, 0));
			Terminal old = (Terminal) eq.getListOfSymbols().get(1);
			newSymbols.add(new Terminal("-" + old.toString(), old.getValue()));
			eq.getListOfSymbols().remove(0);
			eq.getListOfSymbols().remove(0);
			if (!eq.getListOfSymbols().isEmpty()) {
				newSymbols.addAll(eq.getListOfSymbols());
			}
			return new Equation(newSymbols);
		} else {
			return eq;
		}
	}

	/**
	 * TODO comments
	 * 
	 * @param s
	 * @return
	 */
	public static Operand getCorrespondingOperand(String s) {
		if (possibleOperands.isEmpty()) {
			initialize();
		}
		for (Operand operand : possibleOperands) {
			if (operand.toString().equals(s) && operand.isSplitable()) {
				return operand;
			}
		}
		return null;
	}


	public static Equation convertEquation(Equation eq) {
		List<Symbol> newSymList = new ArrayList<Symbol>(eq.getListOfSymbols().size());
		for(int i = 0; i < eq.getListOfSymbols().size(); i++){
			Symbol sym = eq.getListOfSymbols().get(i);
			Terminal notNeg;
			if(sym.isTerminal()) {
				if(((Terminal) sym).toString().startsWith("-")) {
					notNeg = new Terminal(sym.toString().substring(1, sym.toString().length()), -((Terminal) sym).getValue());
					if(i > 1)
						newSymList.set(i-1, new Operand("-", true, false, 0));
				} else {
					notNeg = (Terminal) sym;
				}	
				newSymList.add(notNeg);

			} else {
				newSymList.add(sym);
			}


		}
		return new Equation(newSymList);
	}
}