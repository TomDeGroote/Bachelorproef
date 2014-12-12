package master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tree.Equation;
import tree.Operand;
import tree.Symbol;
import tree.Terminal;
import tree.Tree;

class CopyOfEvaluate {
	
	
	private final List<List<Equation>> TREE;

	private int levelCount = 0;
	private int equationCount = 0;
	
	private List<Equation> bufferSolutions = new ArrayList<Equation>();
	private List<List<Terminal>> examples = new ArrayList<List<Terminal>>();
	
	private HashMap<Equation, HashMap<Double, List<Equation>>> alreadySolved = new HashMap<Equation, HashMap<Double,List<Equation>>>();
	
	public CopyOfEvaluate(Tree tree) {
		this.TREE = tree.getTree();
	}
	
	/**
	 * TODO
	 * @param Ks
	 * 		List of K's, last element in the list is the desired solution
	 */
	public void evaluate(List<Terminal> Ks) {
		// empty the buffer containing solutions
		bufferSolutions = new ArrayList<Equation>();
		
		// add current example to examples list
		examples.add(Ks);
		
		// before first ; no variable is needed because levelCount is already initialized
		// for each over every level in TREE
		for(; levelCount < TREE.size(); levelCount++) {
			// the current level
			List<Equation> level =  TREE.get(levelCount);
			// for each over every equation on the current level
			for(; equationCount < level.size(); equationCount++) {
				Equation eq = level.get(equationCount);
				evaluateEquation(eq); // TODO returned?
			}
			// Don't forget to reset equationCount after for loop
			equationCount = 0;
		}
	}

	/**
	 * 
	 * @param eq
	 */
	private HashMap<Double, List<Equation>> evaluateEquation(Equation eq) {
		// split in two parts on splitable operand
		List<Equation> splitEquations = splitSplitableInThreeParts(eq);
		
		if(splitEquations.size() == 3) {
			// evaluate first part, index = 0 in splitEquations
			HashMap<Double, List<Equation>> solutionPart1;
			int indexPart1 = 0;
			if(alreadySolved.containsKey(splitEquations.get(indexPart1))) {
				solutionPart1 = alreadySolved.get(indexPart1);
			} else {
				solutionPart1 = evaluateEquation(splitEquations.get(indexPart1));
			}
			
			// evaluate second part, index = 2 in splitEquations
			HashMap<Double, List<Equation>> solutionPart2;
			int indexPart2 = 2;
			if(alreadySolved.containsKey(splitEquations.get(indexPart2))) {
				solutionPart2 = alreadySolved.get(indexPart2);
			} else {
				solutionPart2 = evaluateEquation(splitEquations.get(indexPart2));
			}
			
			// bring results together
			
			
		} else { // size will be 1, can happen when there was no splitable equation f.e. E*E*E
			// this means the first two symbols (or the first one in case of equation == E)
			// are not calculated but the rest of the equation has been
		}	
		
		return null;
	}
	
	
	
	/**
	 * Splits a given equation into three parts
	 * @param eq
	 * 		The equation to be split
	 * @return
	 * 		A list containing three equations
	 * 			eq1: first part of eq
	 * 			eq2: an operand (splitable)
	 * 			eq3: second part of eq (after operand)
	 * 		Or a list containing the same equation as given (thus one element)
	 * 			-> happens when equation did not contain any splitable parts
	 * 			
	 * 
	 * 		f.e. equation = E+E*E
	 * 		return = {E, +, E*E} (all elements are equations)
	 */
	private List<Equation> splitSplitableInThreeParts(Equation eq) {
		List<Equation> split = new ArrayList<Equation>();
		
		List<Symbol> firstPart = new ArrayList<Symbol>();
		List<Symbol> operandPart = new ArrayList<Symbol>();
		List<Symbol> seconPart = new ArrayList<Symbol>();
		
		int i = 0;
		List<Symbol> eqSymbols = eq.getListOfSymbols();
		for(; i < eqSymbols.size(); i++) {
			Symbol symbol = eqSymbols.get(i);
			if(symbol.isOperand()) {
				if(((Operand) symbol).isSplitable()) {
					// add first part equation to split
					split.add(new Equation(firstPart));
					// add symbol part to split
					operandPart.add(symbol);
					split.add(new Equation(operandPart));		
					// stop working on the first part
					break;
				} else {
					firstPart.add(symbol);
				}
			} else {
				firstPart.add(symbol);
			}
		}
		
		// start copying to second part
		for(; i < eqSymbols.size(); i++) {
			seconPart.add(eqSymbols.get(i));
		}
		
		// add second part to split
		if(seconPart.isEmpty()) {
			// if no parts were added to the second part this means that the first part is not added to split yet
			split.add(new Equation(firstPart));
		} else {
			split.add(new Equation(seconPart));		
		}
		
		return split;
	}	
}