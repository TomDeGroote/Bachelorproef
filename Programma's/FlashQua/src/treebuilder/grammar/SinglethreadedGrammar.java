package treebuilder.grammar;

import java.util.HashSet;

import treebuilder.Equation;
import treebuilder.symbols.Terminal;
import treebuilder.symbols.operands.Operand;

public class SinglethreadedGrammar extends Grammar{

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
	public static HashSet<Equation> expand(HashSet<Equation> alreadyFound, Equation equation) throws IllegalArgumentException {
		for (Operand operand : OPERANDS) { // for every possible operand generate the expansion
			expandForOperand(alreadyFound, equation, operand, KS);
			expandForOperand(alreadyFound, equation, operand, WEIGTHS);
		}
		return alreadyFound;
	}

	/**
	 * Expands a equation to the given terminals
	 * @param alreadyFound
	 * 			The equations already found
	 * @param equation
	 * 			The equation to expand
	 * @param operand
	 * 			The operand used to expand
	 * @param terminals
	 * 			The terminals to expand to
	 */
	private static void expandForOperand(HashSet<Equation> alreadyFound, Equation equation, Operand operand, Terminal[] terminals) {
		for(Terminal K : terminals) { // expand for every possible K
			// add the made expansion to the list of expansion equations
			Equation possibleNewEquation = Equation.createEquation(equation, operand, K);
			if(possibleNewEquation != null) {
				if(!alreadyFound.contains(possibleNewEquation)) {
					if(possibleNewEquation.getValueOfEquation() == GOAL) {
						addPossibleSolution(possibleNewEquation);
					}
					alreadyFound.add(possibleNewEquation);
				}
			}
		}
	}
}
