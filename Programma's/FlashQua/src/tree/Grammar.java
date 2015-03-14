package tree;

import java.util.ArrayList;
import java.util.List;

import exceptions.UselessEquationException;
import tree.symbols.Terminal;
import tree.symbols.operands.Division;
import tree.symbols.operands.Multiplication;
import tree.symbols.operands.Operand;
import tree.symbols.operands.Substraction;
import tree.symbols.operands.Sum;

/**
 * Represents the grammar that is going to be used to construct the Equation
 * tree.
 * 
 * @author Jeroen & Tom
 *
 */
public class Grammar {
	public static final String NONTERMINALREP = "E";
	public static final String TERMINALREP = "E";
	public static final Terminal[] KS = new Terminal[]{new Terminal("K0", 5.0), new Terminal("K1", 6.0)};
	public static final Operand[] OPERANDS = new Operand[]{new Multiplication(), new Substraction(), new Division(), new Sum()};
	public static final Double GOAL = 31.0;
	
	/**
	 * @return
	 * 			The first equation of the Grammar
	 */
	public static List<Equation> getStartEquation() {
		List<Equation> eqs = new ArrayList<Equation>();
		for(Terminal K : KS) {
			eqs.add(new Equation(K));
		}
		return eqs;
	}

	/**
	 * This method expands a given equation
	 * 
	 * @param equation
	 *            An equation ending with a nonTerminal
	 * @return A list of equations which represent the expansions of the given
	 *         equation
	 * @throws IllegalArgumentException
	 * 			If the last element is not a nonTerminal
	 */
	public static List<Equation> expand(List<Equation> alreadyFound, Equation equation) throws IllegalArgumentException {
		List<Equation> expandedEquations = new ArrayList<Equation>();
		for (Operand operand : OPERANDS) { // for every possible operand generate the expansion
			for(Terminal K : KS) { // expand for every possible K
				// add the made expansion to the list of expansion equations
				try {
					Equation possibleNewEquation = new Equation(equation, operand, K);
					if(!alreadyFound.contains(possibleNewEquation)) {
						expandedEquations.add(possibleNewEquation);
						break;
					}
				} catch(UselessEquationException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return expandedEquations;
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
		return operand.calculateValue(value1, value2);
	}
}