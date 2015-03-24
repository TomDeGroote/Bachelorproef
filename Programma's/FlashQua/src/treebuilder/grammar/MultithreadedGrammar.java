package treebuilder.grammar;

import java.util.ArrayList;
import java.util.List;

import treebuilder.Equation;
import treebuilder.symbols.Terminal;
import treebuilder.symbols.operands.Operand;
import treebuilder.tree.Tree;

public class MultithreadedGrammar extends Grammar implements Runnable {

	private Equation toExpand;
	public List<Equation> found = new ArrayList<Equation>();
	
	public MultithreadedGrammar(Equation equation) {
		this.toExpand = equation;
	}
	
	@Override
	public void run() {
		for (Operand operand : OPERANDS) { // for every possible operand generate the expansion
			expand(operand, KS);
			expand(operand, WEIGTHS);
		}		
	}
	
	/**
	 * Expands an equation
	 * 
	 * @param operand
	 * @param terminals
	 */
	private void expand(Operand operand, Terminal[] terminals) {
		for(Terminal K : terminals) { // expand for every possible K
			// add the made expansion to the list of expansion equations
			Equation possibleNewEquation = Equation.createEquation(this.toExpand, operand, K);
			if(possibleNewEquation != null) {
				if(!Tree.getAlreadyFound().contains(possibleNewEquation)) {
					if(comparator.compareOK(possibleNewEquation.getValueOfEquation(), GOAL)) {
						addPossibleSolution(possibleNewEquation);
					}
					found.add(possibleNewEquation);
//					Tree.alreadyFound.add(possibleNewEquation);
				}
			}
		}
	}
}
