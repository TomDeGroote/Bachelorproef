package master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tree.Equation;
import tree.Grammar;
import tree.Operand;
import tree.Symbol;
import tree.Terminal;
import tree.Tree;

public class CopyOfObjectEvaluate {
	
	/**
	 * Prune idee:
	 * 		Enkel vergelijkingen tegen elkaar vergelijken als ze dezelfde uitkomst genereren
	 * 		Weer opsplitsen in termen zoals vorige keer en dan weer overlopen zoals vorige keer
	 */
	private final List<List<Equation>> TREE;

	private int levelCount = 0;
	private int equationCount = 0;
	
	public List<Equation> bufferSolutions = new ArrayList<Equation>();
	public List<Double[]> examples = new ArrayList<Double[]>();
	
	/**
	 * Constructor of Evaluate
	 * @param tree
	 * 		The tree where evaluate will be working on
	 */
	public CopyOfObjectEvaluate(Tree tree) {
		this.TREE = tree.getTree();
	}
	
	/**
	 * evaluate continues to evaluate the tree with every time more and more examples
	 * @param Ks
	 * 		List of K's, last element in the list is the desired solution
	 * @return
	 * 		The buffer with solutions
	 */
	public List<Equation> evaluate(Double[] Ks) {
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
				// keep running while master decides we need to keep going
				if(!ObjectMaster.timesUp()) {
					Equation eq = level.get(equationCount);
					// add the result of the evaluation of this equation to alreadySolved
					evaluateEquation(eq);
				} else {
					// return the buffer when terminated
					return bufferSolutions;
				}
			}
			// Don't forget to reset equationCount after for loop
			equationCount = 0;
		}
		return bufferSolutions; // return buffered solutions when we are at the end of the tree
	}

	/**
	 * Evaluates an equation in all possible ways
	 * @param eq
	 * 		The equation to be evaluated
	 * @return
	 * 		all possible solutions of this equation
	 */
	public void evaluateEquation(Equation eq) {
		int size = eq.getListOfSymbols().size(); // get the length of this equation
		
		// size 1 -> E
		if(size == 1) {
			evaluateTrivialOne();
		} else if(size == 2) { // size 2 -> +E or -E
			evaluateTrivalTwo(eq);
		} else { // E*E...
			// calculate the value of every nonsplitable part of the equation
			List<List<Tuple<Equation, Double>>> temp = new ArrayList<List<CopyOfObjectEvaluate.Tuple<Equation,Double>>>();
			for(Equation splitEq : splitEquations(eq)) {
				if(splitEq.getListOfSymbols().size() == 1) {
					temp.add(evaluateTrivialOne());
				} else if(splitEq.getListOfSymbols().size() == 2) {
					temp.add(evaluateTrivalTwo(splitEq));
				} else {
					temp.add(getValueNonSplitableEquation(splitEq));
				}
			}
			// concatenate all possible solutions
		}
	}
	
	/**
	 * Calculates all possible values of the nonTerminal equation
	 * @param splitEq
	 * 			The nonTerminal equation
	 * @return
	 * 			A list of Terminal equations and values
	 */
	public List<Tuple<Equation, Double>> getValueNonSplitableEquation(Equation eq) {
		List<Tuple<Equation, Double>> result = new ArrayList<CopyOfObjectEvaluate.Tuple<Equation,Double>>();
		
		List<Symbol> symbols = eq.getListOfSymbols();
		
		// check if first symbol is operand we will use this later
		int start = 0;
		if(symbols.get(start).isOperand()) {
			start++;
		}
		
		// calculate equation that doesn't start with operand
		// starts with E -> we will replace this by possible K's
		for(; start < symbols.size(); start += 2) {
			for(int i = 0; i < examples.get(0).length-1; i++) {
				Symbol K = new Terminal("K" + i, examples.get(0)[i]);
				symbols.remove(0); // Here comes the K
				Operand op = (Operand) symbols.get(1);
				symbols.remove(1); // This is the operand
				for(Tuple<Equation, Double> tuple : getValueNonSplitableEquation(new Equation(symbols))) { // get value of the rest
					// we now have E (we get value here) O (the operand) Rest (the value of all the possibilities)
				}
			}
		}
		
		// use operand on calculated equation
		
		
		
		
		
		
		return result;
	}

	/**
	 * Splits an equation in the smallest possible splitable parts
	 * e.g. E*E+E-E/E => {E*E, +E, -E/E}
	 * @param eq
	 * 		The equation to be split
	 * @return
	 * 		A list of nonTerminal equations that are the smallest possible
	 */
	public List<Equation> splitEquations(Equation eq) {
		List<Equation> splitEquations = new ArrayList<Equation>();
		List<Symbol> symbols = new ArrayList<Symbol>();
		
		int i = 0;
		// check if first symbol is an operand (should only be splitable operands
		if(eq.getListOfSymbols().get(0).isOperand()) { // +E*E....
			symbols.add(eq.getListOfSymbols().get(i));
			i = 1;
		}
		// scan over equation until we can split it, then split it and scan further
		for(; i < eq.getListOfSymbols().size(); i++) {
			if(eq.getListOfSymbols().get(i).isOperand()) {
				if(((Operand) eq.getListOfSymbols().get(i)).isSplitable()) {
					splitEquations.add(new Equation(symbols));
					symbols = new ArrayList<Symbol>();
				}
			}
			symbols.add(eq.getListOfSymbols().get(i));
		}	
		splitEquations.add(new Equation(symbols));
		return splitEquations;
	}

	/**
	 * Evaluates the trivial nonTerminal equation OE or OE
	 * But because O are operands some special needs are required
	 * Possible solutions will be added to bufferSolutions
	 * @param eq
	 * 		The trivial equation
	 * @return
	 * 		A list with terminal equations and corresponding values
	 */
	public List<Tuple<Equation, Double>> evaluateTrivalTwo(Equation eq) {
		List<Tuple<Equation, Double>> result = new ArrayList<CopyOfObjectEvaluate.Tuple<Equation,Double>>();
		Double[] example = examples.get(0);
		double goal = example[example.length-1];
		for(int i = 0; i < example.length-1; i++) { // -1 because last value is goal
			List<Symbol> terminalEq = new ArrayList<Symbol>();
			terminalEq.add(eq.getListOfSymbols().get(0));
			terminalEq.add(new Terminal("K"+i, example[i]));
			Equation possibleEq = new Equation(terminalEq);
			double value = Grammar.evaluateTrivial(possibleEq);
			if(value == goal) {
				bufferSolutions.add(possibleEq);
			}
			result.add(new Tuple<Equation, Double>(possibleEq, value));
		}
		return result;
	}

	/**
	 * Evaluates the trivial nonTerminal equation E
	 * Possible solutions will be added to bufferSolutions
	 * @return
	 * 		A list with terminal equations and corresponding values
	 */
	public List<Tuple<Equation, Double>> evaluateTrivialOne() {
		List<Tuple<Equation, Double>> result = new ArrayList<CopyOfObjectEvaluate.Tuple<Equation,Double>>();
		Double[] example = examples.get(0);
		double goal = example[example.length-1];
		for(int i = 0; i < example.length-1; i++) { // -1 because last value is goal
			List<Symbol> eqSymbols = new ArrayList<Symbol>();
			eqSymbols.add(new Terminal("K"+i, example[i]));
			Equation eq = new Equation(eqSymbols);
			if(example[i] == goal) {
				bufferSolutions.add(eq);
			}
			result.add(new Tuple<Equation, Double>(eq, example[i]));
		}
		return result;
	}	
	
	/**
	 * To Support multiple type returns
	 * @param <X>
	 * @param <Y>
	 */
	public class Tuple<X, Y> { 
	  public final X x; 
	  public final Y y; 
	  
	  public Tuple(X x, Y y) { 
	    this.x = x; 
	    this.y = y; 
	  } 
	} 
	
}