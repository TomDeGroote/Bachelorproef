package master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import tree.Equation;
import tree.Grammar;
import tree.Operand;
import tree.Symbol;
import tree.Terminal;
import tree.Tree;

public class ObjectEvaluateAllSolutions {


	private final List<List<Equation>> TREE;
	private int levelCount = 0;
	private int equationCount = 0;

	public  List<Equation> bufferSolutions = new LinkedList<Equation>();

	// Initiele grootte is gekend dus gebruiken om tijd te besparen.
	public List<HashMap<String, Double>> examples = new ArrayList<HashMap<String, Double>>();
	private HashMap<Equation, HashMap<Double, List<Equation>>> alreadySolved = new HashMap<Equation, HashMap<Double,List<Equation>>>();

	/**
	 * Constructor of ObjectEvaluateAllSolutions
	 * @param tree
	 * 		The tree on which the algorithm will be working.
	 */
	public ObjectEvaluateAllSolutions(Tree tree) {
		this.TREE = tree.getTree();
	}

	/**
	 * evaluate continues at the point where it left off.
	 * @param Ks
	 * 		List of K's, but the last element in the list is the desired solution.
	 * @return
	 * 		The buffer with solutions
	 */
	public List<Equation> evaluate(HashMap<String, Double> Ks) {
		// empty the buffer containing solutions
		bufferSolutions = new LinkedList<Equation>();

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
	public HashMap<Double, List<Equation>> evaluateEquation(Equation eq) {

		// split in two parts on splitable operand
		List<List<Symbol>> splitEquations = splitOnEverySplitable(eq);
		HashMap<Double,List<Equation>> temp = solvingEquation(splitEquations);
		for(Entry<Double,List<Equation>> entry : temp.entrySet()){
			if(entry.getKey().equals(examples.get(0).get(ObjectMaster.getNameOfGoalK())))
				bufferSolutions.addAll(entry.getValue());
		}
		
		return temp;
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

				// put it in result
				if(result.containsKey(value)) {
					result.get(value).add(equationsValue1_2.get(0));
				} else {
					result.put(value, equationsValue1_2);
				}
				//result.put(value, equationsValue1_2);
			}
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
			if(!symbol.isOperand()) {
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
	 * Returns the buffered solutions
	 * @return
	 * 		bufferSolutions
	 */
	public List<Equation> getBufferSolutions() {
		return bufferSolutions;
	}

	/**
	 * Recursive function to solve an equation.
	 * @param splittedEquation
	 * 			The equation to be solved.
	 * @return
	 * 			HashMap containing all possible values for this certain equation.
	 */
	public HashMap<Double,List<Equation>> solvingEquation(List<List<Symbol>> splittedEquation) {

		List<Symbol> temp = splittedEquation.get(0);
		HashMap<Double,List<Equation>> current = new HashMap<Double,List<Equation>>();
		if(temp.size() > 1) {
			List<List<Symbol>> tempList = new ArrayList<List<Symbol>>();
			List<Symbol> test = new ArrayList<Symbol>();
			for(Symbol sym: temp){
				test = new ArrayList<Symbol>();
				test.add(sym);
				tempList.add(test);
			}
			current = solvingEquation(tempList);

		} else {
			for(String K : examples.get(0).keySet()) {
				if(K.equals(ObjectMaster.getNameOfGoalK())) {
					// do not make an possible equation for this
				} else {
					List<Equation> kEquation = new LinkedList<Equation>();
					List<Symbol> terminalK = new LinkedList<Symbol>();
					terminalK.add(new Terminal(K, examples.get(0).get(K)));
					kEquation.add(new Equation(terminalK));
					if(current.containsKey(examples.get(0).get(K))) {
						current.get(examples.get(0).get(K)).add(new Equation(terminalK));
					} else {
						current.put(examples.get(0).get(K), kEquation);
					}

				}
			}
		}
		if(splittedEquation.size() == 1)
			return current;

		HashMap<Double,List<Equation>> remainder = solvingEquation(splittedEquation.subList(2, splittedEquation.size()));
		HashMap<Double,List<Equation>> currentSol = new HashMap<Double,List<Equation>>();
		if(splittedEquation.get(1).get(0).isOperand())
			currentSol = concatenateResults(current, (Operand) splittedEquation.get(1).get(0), remainder);
		else
			System.out.println("Something went wrong");
		
		return currentSol;
	}
}