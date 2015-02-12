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

public class ObjectEvaluate {
	
	/**
	 * Prune idee:
	 * 		Enkel vergelijkingen tegen elkaar vergelijken als ze dezelfde uitkomst genereren
	 * 		Weer opsplitsen in termen zoals vorige keer en dan weer overlopen zoals vorige keer
	 */
	private final List<List<Equation>> TREE;

	private int levelCount = 0;
	private int equationCount = 0;
	
	public  List<Equation> bufferSolutions = new ArrayList<Equation>();
	public List<HashMap<String, Double>> examples = new ArrayList<HashMap<String, Double>>();
	private HashMap<Equation, HashMap<Double, List<Equation>>> alreadySolved = new HashMap<Equation, HashMap<Double,List<Equation>>>();
	
	/**
	 * Constructor of Evaluate
	 * @param tree
	 * 		The tree where evaluate will be working on
	 */
	public ObjectEvaluate(Tree tree) {
		this.TREE = tree.getTree();
	}
	
	/**
	 * evaluate continues to evaluate the tree with every time more and more examples
	 * @param Ks
	 * 		List of K's, last element in the list is the desired solution
	 * @return
	 * 		The buffer with solutions
	 */
	public List<Equation> evaluate(HashMap<String, Double> Ks) {
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
					alreadySolved.put(eq, evaluateEquation(eq));
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
	public HashMap<Double, List<Equation>> evaluateEquation(Equation eq) {
		// Here the size will be 1, Thus the equation will just be E
		if(eq.getListOfSymbols().size() == 1) {
			HashMap<Double, List<Equation>> result = new HashMap<Double, List<Equation>>();
			for(String K : examples.get(0).keySet()) {
				if(K.equals(ObjectMaster.getNameOfGoalK())) {
					// do not make an possible equation for this
				} else {
					// create the basic structure for later
					// Thus calculate an equation T for the K value
					// Put that in a list with equations
					// and put that list with the value of the equation in the result
					List<Equation> kEquation = new ArrayList<Equation>();
					List<Symbol> terminalK = new ArrayList<Symbol>();
					terminalK.add(new Terminal(K, examples.get(0).get(K)));
					kEquation.add(new Equation(terminalK));
					result.put(examples.get(0).get(K), kEquation);
				}
			}
			return result;
		}
		
		// split in two parts on splitable operand
		List<Equation> splitEquations = splitSplitableInThreeParts(eq);
		HashMap<Double, List<Equation>> solutionPart1;
		HashMap<Double, List<Equation>> solutionPart2;

		if(splitEquations.size() == 1) {
			// size will be 1, can happen when there was no splitable equation f.e. E*E*E
			splitEquations = splitNonSplitableInThreeParts(eq);
		} 
		
		// evaluate first part, index = 0 in splitEquations
		int indexPart1 = 0;
		Equation part1 = splitEquations.get(indexPart1);
		if(alreadySolved.containsKey(part1)) {
			solutionPart1 = alreadySolved.get(part1);
		} else {
			solutionPart1 = evaluateEquation(part1);
		}
		
		// evaluate second part, index = 2 in splitEquations
		int indexPart2 = 2;
		Equation part2 = splitEquations.get(indexPart2);
		if(alreadySolved.containsKey(part2)) {
			solutionPart2 = alreadySolved.get(part2);
		} else {
			solutionPart2 = evaluateEquation(part2);
		}
		
		// bring results together and return
		Operand operand = (Operand) splitEquations.get(1).getListOfSymbols().get(0); 
		return concatenateResults(solutionPart1, operand, solutionPart2);
	}	
	
	/**
	 * Concatenates all possible combinations of the solution for a first equation and a second
	 * with in between the operand
	 * @return
	 * 		The resulting combination
	 */
	public HashMap<Double, List<Equation>> concatenateResults(HashMap<Double, List<Equation>> solutionPart1, Operand operand, HashMap<Double, List<Equation>> solutionPart2) {
		HashMap<Double, List<Equation>> result = new HashMap<Double, List<Equation>>();		
		// loop over every element in hashMap solutionPart1
		for(Double valueSolution1 : solutionPart1.keySet()) {
			List<Equation> equationsValue1 = solutionPart1.get(valueSolution1);
			
			// loop over every element in hashMap solutionPart2
			for(Double valueSolution2 : solutionPart2.keySet()) {
				List<Equation> equationsValue2 = solutionPart2.get(valueSolution2);
				
				// All the possible equations when combining part1 and part2
				List<Equation> equationsValue1_2 = concatenateEquationLists(equationsValue1, operand, equationsValue2);
				
				// the resulting value
				Double value = Grammar.getValue(valueSolution1, operand, valueSolution2);
				
				// if the value is the value we are looking for, add it to possible solutions
				addPossibelSolutions(value, equationsValue1_2);
				
				// put it in result
				result.put(value, equationsValue1_2);
			}
		}	
		return result;
	}

	/**
	 * Checks if the value is a possible solution for the first example
	 * an if so if the equations are a solution for every example already given
	 * @param value
	 * 			The value that was calculated for the first example
	 * @param equationsValue1_2
	 * 			The possible equations that create this value for the first example
	 */
	public void addPossibelSolutions(Double value, List<Equation> equationsValue1_2) {
		// extract the value of the first equation
		if(examples.get(0).get(ObjectMaster.getNameOfGoalK()).equals(value)) {
			List<Equation> equationsToCheck = new ArrayList<Equation>(equationsValue1_2);
			// for every example test if there is a possible equation
			for(HashMap<String, Double> Ks : examples) {
				List<Equation> newEquationsToCheck = new ArrayList<Equation>();
				for(Equation equation : equationsToCheck) {
					List<Symbol> toEvaluateList = new ArrayList<Symbol>();
					for(Symbol symbol : equation.getListOfSymbols()) {
						if(symbol.isOperand()) {
							toEvaluateList.add(symbol);
						} else if (symbol.isTerminal()) {
							// no if test containsKey needed
							// adds the terminal of this example to the equation (indirectly)
							toEvaluateList.add(new Terminal(symbol.toString(), Ks.get(symbol.toString())));
						} else {
							// should never happen
						}
					}
					// generate the resulting equation for the next example
					Equation equationToEvaluate = new Equation(toEvaluateList);
					if(evaluateTerminalEquation(equationToEvaluate, Ks.get(ObjectMaster.getNameOfGoalK()))) {
						// is possible solution
						newEquationsToCheck.add(equationToEvaluate);
					} else {
						// is not a possible solution for this equation, do nothing
					}
				}
				// check only equations who are still valid
				equationsToCheck = newEquationsToCheck;
			}
			// all equations who are still in equationsToCheck are possible solutions given the current examples
			bufferSolutions.addAll(equationsToCheck);
		}
		
	}

	/**
	 * Evaluates if a equation containing only terminals equals the goal
	 * @param equationToEvaluate
	 * 			The equation to evaluate
	 * @param goal
	 * 			The goal that should be met
	 * @return
	 * 			True if the goal is met
	 * 			False if the goal is not met
	 */
	public static boolean evaluateTerminalEquation(Equation equationToEvaluate, double goal) {
		// first split on every equation
		List<List<Symbol>> terms = splitOnEverySplitable(equationToEvaluate);
		
		// second evaluate every term
		// the values of the terms and operands need to be read in the same order
		List<Double> valueTerms = new ArrayList<Double>();
		List<Operand> operands = new ArrayList<Operand>();
		for(List<Symbol> term : terms) {
			if(term.size() == 1) { // or operand or T
				if(term.get(0).isOperand()) {
					operands.add((Operand) term.get(0)); // in case of operand, save operand
				} else {
					valueTerms.add(((Terminal) term.get(0)).getValue()); // in case of T, save value of terminal
				}
			} else {
				valueTerms.add(calculateTerm(term));
			}
		}
		
		// calculate the concatenation of the terms
		double result = valueTerms.get(0);
		for(int i = 0; i < operands.size(); i++) {
			result = Grammar.getValue(result, operands.get(i), valueTerms.get(i+1));
		}
		return result == goal;
	}
	
	/**
	 * Calculates the value of a term (thus containing only nonSplitable parts
	 * @param term
	 * 		The term to be evaluated
	 * 		Contains only non splitable operands
	 * 		Starts with an Terminal then a Operand then a Terminal and so on...
	 * @return
	 * 		The value of the term
	 */
	public static Double calculateTerm(List<Symbol> term) {
		Double result = ((Terminal) term.get(0)).getValue();
		for(int i = 1; i < term.size(); i = i+2) {
			result = Grammar.getValue(result, (Operand) term.get(i), ((Terminal) term.get(i+1)).getValue());
		}
		return result;
	}

	/**
	 * E+E*E
	 * -> {E, +, E*E}
	 * @param equation
	 * 			Equation containing only terminals to be split
	 * @return
	 */
	public static List<List<Symbol>> splitOnEverySplitable(Equation equation) {
		List<List<Symbol>> result = new ArrayList<List<Symbol>>();
		List<Symbol> term = new ArrayList<Symbol>();
		for(Symbol symbol : equation.getListOfSymbols()) {
			if(symbol.isTerminal()) {
				term.add(symbol);
			} else {
				if(((Operand) symbol).isSplitable()) {
					result.add(new ArrayList<Symbol>(term));
					term = new ArrayList<Symbol>();
					term.add(symbol);
					result.add(new ArrayList<Symbol>(term));
					term = new ArrayList<Symbol>();
				} else {
					term.add(symbol);
				}
			}
		}
		result.add(term);
		return result;
	}

	/**
	 * Concatenates two lists of equations to all possible combinations
	 * @param equationsValue1
	 * 			first list containing equations
	 * @param operand
	 * 			the operand between the two equations
	 * @param equationsValue2
	 * 			second list containing equations
	 * @return
	 * 			list with all possible combinations of the two list with in between the operand
	 * 
	 */
	public List<Equation> concatenateEquationLists(List<Equation> equationsValue1, Operand operand, List<Equation> equationsValue2) {
		List<Equation> result = new ArrayList<Equation>();
		for(Equation eq1 : equationsValue1) {
			for(Equation eq2 : equationsValue2) {
				result.add(concatenateEquations(eq1, operand, eq2));
			}
		}
		return result;
	}

	/**
	 * Makes an equation which list of symbols will look like:
	 * 		{eq1.getListOfSymbols, operand, eq2.getListOfSymbols}
	 * @return
	 * 		{eq1.getListOfSymbols, operand, eq2.getListOfSymbols}
	 */
	public Equation concatenateEquations(Equation eq1, Operand operand, Equation eq2) {
		List<Symbol> symbolsResult = new ArrayList<Symbol>();
		symbolsResult.addAll(eq1.getListOfSymbols());
		symbolsResult.add(operand);
		symbolsResult.addAll(eq2.getListOfSymbols());
		return new Equation(symbolsResult);
	}

	/**
	 * Splits a given equation into three parts
	 * @param eq
	 * 		The equation to be split
	 * @return
	 * 		A list containing three equations
	 * 			eq1: first part of equation (length will be 1, only E)
	 * 			eq2: an operand (non splitable)
	 * 			eq3: second part of equation (this should be in alreadySolved)
	 * 		Or a list containing the same equation as given (thus one element)
	 * 			-> happens when equation did not contain any splitable parts
	 * 			
	 * 
	 * 		f.e. equation = E+E*E
	 * 		return = {E, +, E*E} (all elements are equations)
	 */
	public List<Equation> splitNonSplitableInThreeParts(Equation eq) {
		List<Equation> split = new ArrayList<Equation>();
		
		List<Symbol> firstPart = new ArrayList<Symbol>();
		List<Symbol> operandPart = new ArrayList<Symbol>();
		List<Symbol> seconPart = new ArrayList<Symbol>();
		
		List<Symbol> eqSymbols = eq.getListOfSymbols();
		// first part, will be E
		firstPart.add(eqSymbols.get(0));
		// operandPart will be an Operand (non splitable)
		operandPart.add(eqSymbols.get(1));
		// second part will be the rest of the symbols of the equation
		for(int i = 2; i < eqSymbols.size(); i++) {
			seconPart.add(eqSymbols.get(i));
		}
		
		split.add(new Equation(firstPart));
		split.add(new Equation(operandPart));
		split.add(new Equation(seconPart));
		
		return split;
	}

	
	
	
	/**
	 * Splits a given equation into three parts
	 * @param eq
	 * 		The equation to be split
	 * @return
	 * 		A list containing three equations
	 * 			eq1: first part of equation
	 * 			eq2: an operand (splitable)
	 * 			eq3: second part of equation (after operand)
	 * 		Or a list containing the same equation as given (thus one element)
	 * 			-> happens when equation did not contain any splitable parts
	 * 			
	 * 
	 * 		f.e. equation = E+E*E
	 * 		return = {E, +, E*E} (all elements are equations)
	 */
	public List<Equation> splitSplitableInThreeParts(Equation eq) {
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
		for(i++; i < eqSymbols.size(); i++) {
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
	
	public List<Equation> getBufferSolutions() {
		return bufferSolutions;
	}
}