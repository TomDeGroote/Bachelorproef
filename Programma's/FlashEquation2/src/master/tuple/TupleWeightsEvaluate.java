package master.tuple;

import java.util.ArrayList;
import java.util.List;

import master.Evaluate;
import tree.Equation;
import tree.Grammar;
import tree.Operand;
import tree.Symbol;
import tree.Terminal;
import tree.Tree;

public class TupleWeightsEvaluate extends Evaluate {
	
	/**
	 * Prune idee:
	 * 		Enkel vergelijkingen tegen elkaar vergelijken als ze dezelfde uitkomst genereren
	 * 		Weer opsplitsen in termen zoals vorige keer en dan weer overlopen zoals vorige keer
	 */
	private final List<List<Equation>> TREE;

	private int levelCount = 0;
	private int equationCount = 0;
	
	public List<Double[]> examples = new ArrayList<Double[]>();
	public List<Terminal> terminalList = new ArrayList<Terminal>();
	private List<Terminal> weights = new ArrayList<Terminal>();

	private boolean acceptOtherExample = false;
	
	/**
	 * Constructor of Evaluate
	 * @param tree
	 * 		The tree where evaluate will be working on
	 */
	public TupleWeightsEvaluate(Tree tree) {
		this.TREE = tree.getTree();
		this.weights = Grammar.getWeights();
	}
	
	/**
	 * evaluate continues to evaluate the tree with every time more and more examples
	 * @param Ks
	 * 		List of K's, last element in the list is the desired solution
	 * @return
	 * 		The buffer with solutions
	 */
	public List<Equation> evaluate() {
		// empty the buffer containing solutions
		bufferSolutions = new ArrayList<Equation>();
		
		// before first ; no variable is needed because levelCount is already initialized
		// for each over every level in TREE
		for(; levelCount < TREE.size(); levelCount++) {
			// the current level
			List<Equation> level =  TREE.get(levelCount);
			// for each over every equation on the current level
			for(; equationCount < level.size(); equationCount++) {
				// keep running while master decides we need to keep going
				if(!TupleWeightsMaster.timesUp()) {
					Equation eq = level.get(equationCount);
					// add the result of the evaluation of this equation to alreadySolved
					evaluateEquation(eq, 0, true); // check for first example
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
	 * Adds an example to the current example list
	 * @param Ks
	 * 		The example to be added
	 */
	public void addExample(Double[] Ks) {
		if(terminalList.isEmpty()) {
			for(int i = 0; i < Ks.length-1; i++) {
				terminalList.add(new Terminal("K"+i, Ks[i]));
			}
		}
		// add current example to examples list
		examples.add(Ks);
	}

	/**
	 * Evaluates an equation in all possible ways
	 * @param eq
	 * 		The equation to be evaluated
	 * @return
	 * 		all possible solutions of this equation
	 */
	public List<Tuple<Equation, Double>> evaluateEquation(Equation eq, int example, boolean nonTerminal) {
		int size = eq.getListOfSymbols().size(); // get the length of this equation
		
		// size 1 -> E
		if(size == 1) {
			return evaluateTrivialOne(example, nonTerminal, eq);
		} else if(size == 2) { // size 2 -> +E or -E
			if(eq.getListOfSymbols().get(1).isNonTerminal()) {
				return evaluateTrivalTwo(eq, example, nonTerminal);
			} else {
				List<Tuple<Equation, Double>> solution = new ArrayList<TupleWeightsEvaluate.Tuple<Equation,Double>>();
				Double value = Grammar.evaluateTrivial(eq);
				solution.add(new Tuple<Equation, Double>(eq, value));
				return solution;
			}
		} else { // E*E...
			// calculate the value of every nonsplitable part of the equation
			List<List<Tuple<Equation, Double>>> temp = new ArrayList<List<TupleWeightsEvaluate.Tuple<Equation,Double>>>();
			for(Equation splitEq : splitEquations(eq)) {
				temp.add(getValueNonSplitableEquation(splitEq, example, nonTerminal));
			}
			// concatenate all possible solutions
			return concatenateAll(temp, example);
		}
	}
	
	/**
	 * Concatenates a list of a list of tuples and returns all the values
	 * @param temp
	 * 			The list of lists to be concatenated
	 * @return
	 * 			The concatenated lists and their values
	 */
	private List<Tuple<Equation, Double>> concatenateAll(List<List<Tuple<Equation, Double>>> temp, int e) {
		if(temp.size() == 1) {
			for(Tuple<Equation, Double> possibleSolution : temp.get(0)) {
				if(possibleSolution.y.equals(examples.get(e)[examples.get(e).length-1])) {
					if(e == 0) {
						if(checkAgainstOtherExamples(possibleSolution.x)) {
							bufferSolutions.add(possibleSolution.x);
						}
					}
				} else {
					acceptOtherExample = false;
				}
			}
			return temp.get(0);
		}
		
		List<Tuple<Equation, Double>> result = new ArrayList<TupleWeightsEvaluate.Tuple<Equation,Double>>();
		
		for(Tuple<Equation, Double> first : temp.get(0)) {	// first tuple list
			for(Tuple<Equation, Double> snd : temp.get(1)) { // second tuple list
				result.add(concatenateTuples(first, snd)); // concatenates tuple1 and tuple2
			}
		}
		
		temp.remove(0);
		temp.remove(0);
		temp.add(0, result);
		return concatenateAll(temp, e);
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
	 * Calculates all possible values of the (non)Terminal equation
	 * @param splitEq
	 * 			The (non)Terminal equation
	 * @return
	 * 			A list of Terminal equations and values
	 */
	public List<Tuple<Equation, Double>> getValueNonSplitableEquation(Equation eq, int e, boolean nonTerminal) {
		// trivial solutions
		if(eq.getListOfSymbols().size() == 1) {
			return evaluateTrivialOne(e, nonTerminal, eq);
		} else if(eq.getListOfSymbols().size() == 2) {
			return evaluateTrivalTwo(eq, e, nonTerminal);
		} 
				
		List<Tuple<Equation, Double>> result = new ArrayList<TupleWeightsEvaluate.Tuple<Equation,Double>>();
		
		List<Symbol> symbols = eq.getListOfSymbols();
		
		// check if first symbol is operand we will use this later
		int start = 0;
		boolean firstWasOperand = false;
		Operand starter = null;
		if(symbols.get(start).isOperand()) {
			firstWasOperand = true;
			starter = (Operand) symbols.get(0);
			symbols.remove(0);
		}
		
		List<List<Symbol>> newEqs = new ArrayList<List<Symbol>>();

		// nonTrivial solutions
		// generate all possible Terminal equations
		if(nonTerminal) {
			// first equations -> {K0, K1, K2, ...}
			for(Terminal K : terminalList) {
				List<Symbol> newEqStart = new ArrayList<Symbol>();
				newEqStart.add(K);
				newEqs.add(newEqStart);
			}
			for(Terminal W : weights) {
				List<Symbol> newEqStart = new ArrayList<Symbol>();
				newEqStart.add(W);
				newEqs.add(newEqStart);
			}
			for(int i = 1; i < symbols.size(); i++) {
				if(symbols.get(i).isNonTerminal()) { // for every E
					List<List<Symbol>> nextEqs = new ArrayList<List<Symbol>>();
					for(List<Symbol> sym : newEqs) { // for every already made terminal equation (K0+, K1+, ...)
						for(Terminal K : terminalList) { // add every possibility (K0+K0, K0+K1, K1+K0, ...)
							List<Symbol> newEqContinue = new ArrayList<Symbol>(sym);
							newEqContinue.add(K);
							nextEqs.add(newEqContinue);
						}
						for(Terminal W : weights) { // add every possibility (K0+K0, K0+K1, K1+K0, ...)
							List<Symbol> newEqContinue = new ArrayList<Symbol>(sym);
							newEqContinue.add(W);
							nextEqs.add(newEqContinue);
						}
					}
					newEqs = nextEqs;
				} else if(symbols.get(i).isTerminal() || symbols.get(i).isOperand()) { // if already K or O just add it to all possiblities
					for(List<Symbol> sym : newEqs) {
						sym.add(symbols.get(i));
					}
				}
			}
		} else {
			newEqs.add(eq.getListOfSymbols());
		}
		
		// Calculate the value of every terminal equation
		for(List<Symbol> terminalEq : newEqs) {
			result.add(new Tuple<Equation, Double>(new Equation(terminalEq), evaluateTerminalEquation(terminalEq)));			
		}
		
		// Add the operand to the equation that was at the start
		if(firstWasOperand) {
			List<Tuple<Equation, Double>> realResult = new ArrayList<TupleWeightsEvaluate.Tuple<Equation,Double>>();
			for(Tuple<Equation, Double> tuple : result) {
				// create how the real equation looked
				List<Symbol> realSymbols = new ArrayList<Symbol>();
				realSymbols.add(starter);
				realSymbols.addAll(tuple.x.getListOfSymbols());
				Equation realEquation = new Equation(realSymbols);
				// get the real value of the equation
				Double realValue = Grammar.evaluateTrivialValue(starter, tuple.y);
				
				realResult.add(new Tuple<Equation, Double>(Grammar.convertEq(realEquation), realValue));
			}
			return realResult;
		}

		// if no operand had to be added just return the result
		return result;
	}

	/**
	 * Evaluates a equation only containing terminals (for the first example given)
	 * @param terminalEq
	 * 		The terminal equation to be evaluated
	 * @return
	 * 		The value of the terminal equation
	 */
	private Double evaluateTerminalEquation(List<Symbol> terminalEq) {
		Double result = Grammar.getValue(((Terminal) terminalEq.get(0)).getValue(), ((Operand) terminalEq.get(1)), ((Terminal) terminalEq.get(2)).getValue());
		for(int i = 3; i < terminalEq.size(); i=i+2) {
			result = Grammar.getValue(result, (Operand) terminalEq.get(i), ((Terminal) terminalEq.get(i+1)).getValue());
		}
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
	 * Evaluates the trivial (non)Terminal equation OE or OE
	 * But because O are operands some special needs are required
	 * Possible solutions will be added to bufferSolutions
	 * @param eq
	 * 		The trivial equation
	 * @return
	 * 		A list with terminal equations and corresponding values
	 */
	public List<Tuple<Equation, Double>> evaluateTrivalTwo(Equation eq, int e, boolean nonTerminal) {
		if(nonTerminal) {
			List<Tuple<Equation, Double>> result = new ArrayList<TupleWeightsEvaluate.Tuple<Equation,Double>>();
			Double[] example = examples.get(e);
			for(int i = 0; i < example.length-1; i++) { // -1 because last value is goal
				List<Symbol> terminalEq = new ArrayList<Symbol>();
				terminalEq.add(eq.getListOfSymbols().get(0));
				terminalEq.add(new Terminal("K"+i, example[i]));
				Equation possibleEq = new Equation(terminalEq);
				double value = Grammar.evaluateTrivial(possibleEq);
				result.add(new Tuple<Equation, Double>(Grammar.convertTrivialEq(possibleEq), value));
			}
			for(Terminal weight : weights) { // -1 because last value is goal
				List<Symbol> terminalEq = new ArrayList<Symbol>();
				terminalEq.add(eq.getListOfSymbols().get(0));
				terminalEq.add(weight);
				Equation possibleEq = new Equation(terminalEq);
				double value = Grammar.evaluateTrivial(possibleEq);
				result.add(new Tuple<Equation, Double>(Grammar.convertTrivialEq(possibleEq), value));
			}
			return result;
		} else {
			List<Tuple<Equation, Double>> solution = new ArrayList<TupleWeightsEvaluate.Tuple<Equation,Double>>();
			Double value = Grammar.evaluateTrivial(eq);
			solution.add(new Tuple<Equation, Double>(Grammar.convertTrivialEq(eq), value));
			return solution;
		}
	}

	/**
	 * Evaluates the trivial equation (Terminal or nonTerminal)
	 * Possible solutions will be added to bufferSolutions
	 * @return
	 * 		A list with terminal equations and corresponding values
	 */
	public List<Tuple<Equation, Double>> evaluateTrivialOne(int e, boolean nonTerminal, Equation equation) {
		if(nonTerminal) {
			List<Tuple<Equation, Double>> result = new ArrayList<TupleWeightsEvaluate.Tuple<Equation,Double>>();
			Double[] example = examples.get(e);
			for(int i = 0; i < example.length-1; i++) { // -1 because last value is goal
				List<Symbol> eqSymbols = new ArrayList<Symbol>();
				eqSymbols.add(new Terminal("K"+i, example[i]));
				Equation eq = new Equation(eqSymbols);
				result.add(new Tuple<Equation, Double>(eq, example[i]));
			}
			for(Terminal weight : weights) { // -1 because last value is goal
				List<Symbol> eqSymbols = new ArrayList<Symbol>();
				eqSymbols.add(weight);
				Equation eq = new Equation(eqSymbols);
				result.add(new Tuple<Equation, Double>(eq, weight.getValue()));
			}
			return result;
		} else {
			List<Tuple<Equation, Double>> list = new ArrayList<TupleWeightsEvaluate.Tuple<Equation,Double>>();
			list.add(new Tuple<Equation, Double>(equation, ((Terminal) equation.getListOfSymbols().get(0)).getValue()));
			return list;
		}
	}	
	
	
	/**
	 * Checks for all other examples if this equation is a possible solution
	 * @param eq
	 * 		The equation to be checked
	 * @return
	 * 		True if it is a possible solution for all other examples
	 * 		False if not
	 * 
	 */
	public boolean checkAgainstOtherExamples(Equation eq) {
		for(int i = 1; i < examples.size(); i++) {
			List<Symbol> newSymbols = new ArrayList<Symbol>();
			for(Symbol s : eq.getListOfSymbols()) {
				if(s.isOperand()) {
					newSymbols.add(s);
				} else {
					Terminal old = (Terminal) s;
					String firstS = old.toString().substring(0, 1);
					if(Grammar.isOperand(firstS)) {
						if(Grammar.isWeight(old.toString().substring(1))) {
							newSymbols.add(new Terminal(old.toString(), Grammar.evaluateTrivialValue(Grammar.getCorrespondingOperand(firstS), old.getValue())));
						} else {
							int number = (int) Double.parseDouble(old.toString().substring(2));
							newSymbols.add(new Terminal(old.toString(), Grammar.evaluateTrivialValue(Grammar.getCorrespondingOperand(firstS), examples.get(i)[number])));
						}
					} else {
						if(Grammar.isWeight(old.toString().substring(0))) {
							newSymbols.add(old);
						} else {
							int number = (int) Double.parseDouble(old.toString().substring(1)); // TODO only allows terminals of length 1 in name (K, E, ...)
							newSymbols.add(new Terminal(old.toString(), examples.get(i)[number]));
						}
					}
				}
			}
			acceptOtherExample = true;
			evaluateEquation(new Equation(newSymbols), i, false).size();
			if(acceptOtherExample == false) {
				return false;
			}
		}
		return true;
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