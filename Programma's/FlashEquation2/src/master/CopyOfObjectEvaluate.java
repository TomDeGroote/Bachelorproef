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
	public List<Tuple<Equation, Double>> evaluateEquation(Equation eq) {
		int size = eq.getListOfSymbols().size(); // get the length of this equation
		
		// size 1 -> E
		if(size == 1) {
			return evaluateTrivialOne();
		} else if(size == 2) { // size 2 -> +E or -E
			return evaluateTrivalTwo(eq);
		} else { // E*E...
			// calculate the value of every nonsplitable part of the equation
			List<List<Tuple<Equation, Double>>> temp = new ArrayList<List<CopyOfObjectEvaluate.Tuple<Equation,Double>>>();
			for(Equation splitEq : splitEquations(eq)) {
				temp.add(getValueNonSplitableEquation(splitEq));
			}
			// concatenate all possible solutions
			return concatenateAll(temp);
		}
	}
	
	/**
	 * Concatenates a list of a list of tuples and returns all the values
	 * @param temp
	 * 			The list of lists to be concatenated
	 * @return
	 * 			The concatenated lists and their values
	 */
	private List<Tuple<Equation, Double>> concatenateAll(List<List<Tuple<Equation, Double>>> temp) {
		if(temp.size() == 1) {
			for(Tuple<Equation, Double> possibleSolution : temp.get(0)) {
				if(possibleSolution.y.equals(examples.get(0)[examples.get(0).length-1])) {
					bufferSolutions.add(possibleSolution.x);
				}
			}
			return temp.get(0);
		}
		
		List<Tuple<Equation, Double>> result = new ArrayList<CopyOfObjectEvaluate.Tuple<Equation,Double>>();
		
		for(Tuple<Equation, Double> first : temp.get(0)) {	// first tuple list
			for(Tuple<Equation, Double> snd : temp.get(1)) { // second tuple list
				result.add(concatenateTuples(first, snd)); // concatenates tuple1 and tuple2
			}
		}
		
		temp.remove(0);
		temp.remove(0);
		temp.add(0, result);
		return concatenateAll(temp);
	}

	/**
	 * Concatenates two tuples
	 * @param first
	 * 		first tuple
	 * @param snd
	 * 		Second tuple, should contain an operand as first symbol of equation
	 * @return
	 * 		the two concatenated tuples
	 */
	private Tuple<Equation, Double> concatenateTuples(Tuple<Equation, Double> first, Tuple<Equation, Double> snd) {
		List<Symbol> combinationSymbols = new ArrayList<Symbol>();
		combinationSymbols.addAll(first.x.getListOfSymbols());
		combinationSymbols.addAll(snd.x.getListOfSymbols());
		Double combinationValue = Grammar.getValue(first.y, (Operand) snd.x.getListOfSymbols().get(0), snd.y);
		return new Tuple<Equation, Double>(new Equation(combinationSymbols), combinationValue);
	}

	/**
	 * Calculates all possible values of the nonTerminal equation
	 * @param splitEq
	 * 			The nonTerminal equation
	 * @return
	 * 			A list of Terminal equations and values
	 */
	public List<Tuple<Equation, Double>> getValueNonSplitableEquation(Equation eq) {
		// trivial solutions
		if(eq.getListOfSymbols().size() == 1) {
			return evaluateTrivialOne();
		} else if(eq.getListOfSymbols().size() == 2) {
			return evaluateTrivalTwo(eq);
		} 
		
		List<Tuple<Equation, Double>> result = new ArrayList<CopyOfObjectEvaluate.Tuple<Equation,Double>>();
		
		List<Symbol> symbols = eq.getListOfSymbols();
		
		// check if first symbol is operand we will use this later
		int start = 0;
		boolean firstWasOperand = false;
		Operand starter = null;
		if(symbols.get(start).isOperand()) {
			firstWasOperand = true;
			starter = (Operand) symbols.get(0);
			start++;
			symbols.remove(0);
		}
		
		// calculate equation that doesn't start with operand
		// starts with E -> we will replace this by possible K's
		for(; start < symbols.size(); start += 2) {
			symbols.remove(0); // Here comes the K
			Operand op = (Operand) symbols.get(0);
			symbols.remove(0); // This is the operand
			List<Tuple<Equation, Double>> tuples = getValueNonSplitableEquation(new Equation(symbols));
			for(int i = 0; i < examples.get(0).length-1; i++) {
				Symbol K = new Terminal("K" + i, examples.get(0)[i]);
				// we now have E (we get value here) O (the operand) Rest (the value of all the possibilities)
				for(Tuple<Equation, Double> t : tuples) {
					List<Symbol> newSymbols = new ArrayList<Symbol>();
					newSymbols.add(K);
					newSymbols.add(op);
					newSymbols.addAll(t.x.getListOfSymbols());
					Double value = Grammar.getValue(((Terminal) K).getValue(), op, t.y);
					result.add(new Tuple<Equation, Double>(new Equation(newSymbols), value));
				}
			}
		}
		
		// Add the operand to the equation that was at the start
		if(firstWasOperand) {
			List<Tuple<Equation, Double>> realResult = new ArrayList<CopyOfObjectEvaluate.Tuple<Equation,Double>>();
			for(Tuple<Equation, Double> tuple : result) {
				// create how the real equation looked
				List<Symbol> realSymbols = new ArrayList<Symbol>();
				realSymbols.add(starter);
				realSymbols.addAll(tuple.x.getListOfSymbols());
				Equation realEquation = new Equation(realSymbols);
				// get the real value of the equation
				Double realValue = Grammar.evaluateTrivialValue(starter, tuple.y);
				
				realResult.add(new Tuple<Equation, Double>(realEquation, realValue));
			}
			return realResult;
		}

		// if no operand had to be added just return the result
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
//			if(value == goal) {
//				bufferSolutions.add(possibleEq);
//			}
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
//			if(example[i] == goal) {
//				bufferSolutions.add(eq);
//			}
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
	  
	  @Override
	  public String toString() {
		return x.toString() + " = " + y;
	  }
	} 
	
}