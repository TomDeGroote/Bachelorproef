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
			for(Terminal K : KS) { // expand for every possible K
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
		return alreadyFound;
	}
}
