package master.all;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import master.Input;
import master.Master;
import master.Timer;
import master.normal.ObjectMaster;
import tree.Equation;
import tree.Symbol;
import tree.Terminal;
import tree.Tree;



public class ObjectMasterAllSolutions extends Master {

	public HashMap<String, Double> example;

	/**
	 * @param deadline
	 * 			The time in milliseconds the program can maximumly run before it has to return something
	 * 			Please give a number <= 0 if you don't want a time deadline
	 * @param stopAfterOne
	 * 			If true the program will run until one solution has been found
	 * 			If false the program will run as long as possible to get as much solutions as possible
	 * @return
	 * 			"Empty" if no formula was found
	 * 			The formula in string form if a formula was found
	 * 
	 * 			TODO aangepast voor jar
	 */
	@Override
	public String run(int deadline, boolean stopAfterOne, List<List<Double>> numbers, Input input) {	

		solutionSpace = new ArrayList<Equation>();
		// set a possible deadline
		if(deadline > 0) {
			this.hasDeadLine = true;
			timer = new Timer(deadline);
		}
		
		// read the tree generated earlier
		Tree tree = input.getTree();

		// generate the evaluate class
		evaluate = new ObjectEvaluateAllSolutions(tree);

		this.stopAfterOne = stopAfterOne; // initialize if the program should stop after one solution
		if(numbers ==  null) {
			return run(input.getList());
		} else {
			return run(input.convertArrayList(numbers));
		}
	}

	/**
	 * @return
	 * 		Returns the current best known solution in String format
	 * 		Or "Empty" if there is no solution yet
	 * 
	 * 		TODO aangepast voor jar
	 */
	private String run(List<HashMap<String, Double>> numbers) {
		//		int i = 1; // counter to say how many examples have passed
		for(HashMap<String, Double> Ks : numbers) {
			// remember the example
			example = Ks;

			// start timer
			timer.start();

			// Check solution space for possible solution
			checkSolutionSpace(Ks);
			
			// If stopAfterOne then run until one possible solution has found (or timer has ended)
			// else only stop when the timer is done
			if(stopAfterOne) {
				// If the solution space is empty start searching for a new equation
				if(solutionSpace.isEmpty()) {
					// start to evaluate
					evaluate.addExample(Input.covertToHashMap(Ks));
					solutionSpace.addAll(evaluate.evaluate(this));
				}
			} else {				
				// start to evaluate
				evaluate.addExample(Input.covertToHashMap(Ks));
				List<Equation> toBeAdded = (List<Equation>) evaluate.evaluate(this);
				solutionSpace.addAll(toBeAdded);

				//				i++;
			}
			
		}

		// return
		if(solutionSpace.isEmpty()) {
			return "Empty";
		} else {
			return solutionSpace.get(0).toString();
		}
	}

	/**
	 * Checks the current solutionSpace to see if there are any equations
	 * that are also correct for this input
	 * @param Ks
	 * 		The input 
	 * 		Should be of form (but as hashmap): {(K0, 3), (K1, 4), ..., (KN-1, 2), (Goal, 5)}
	 */
	public void checkSolutionSpace(HashMap<String, Double> Ks) {
		List<Equation> newSolutionSpace = new ArrayList<Equation>();
		for(Equation eq : solutionSpace) {
			if(!timer.timesUp()){
				List<Symbol> symbols = new ArrayList<Symbol>();
				for(Symbol s : eq.getListOfSymbols()) {
					if(s.isTerminal()) {
						String terminalName = ((Terminal) s).toString();
						symbols.add(new Terminal(terminalName, Ks.get(terminalName))); 
					} else {
						symbols.add(s);
					}
				}
				if(ObjectEvaluateAllSolutions.evaluateTerminalEquation(new Equation(symbols))==Ks.get(ObjectMaster.getNameOfGoalK())) {
					newSolutionSpace.add(eq);
				}
			}
			else	
				break;
		}	
		if(newSolutionSpace.isEmpty())
			solutionSpace = new ArrayList<Equation>();
		else
			solutionSpace = newSolutionSpace;
	}




	/**
	 * Prints the solution containing all Ks and is the smallest or
	 * prints the solution containing the most nr of Ks
	 * 
	 * @return
	 * 		The best solution
	 */
	@Override
	public Equation getBestSolution() {
		Equation bestSolution = null; // variable to remember best solution
		int nrOfKeys = 0; // variable to remember nr of Ks in best solution

		// check every solution in solutionSpace
		for(Equation eq : solutionSpace) {
			boolean containsAll = true;
			int temp = 0;

			// check how many Ks this solution contains
			for(String K : example.keySet()) {
				if(!K.equals(ObjectMaster.getNameOfGoalK())) {
					if(!eq.toString().contains(K)) {
						temp++;
						containsAll = false;
					}
				}
			}

			// if this solution contains more Ks than the previous one, remember this one
			if(temp > nrOfKeys) {
				nrOfKeys = temp;
				bestSolution = eq;
			}

			// if this solution contains all Ks, this will be the best solution
			if(containsAll) {
				bestSolution = eq;
				break;
			}
		}
		return bestSolution;
	}

	/**
	 * 
	 * @return
	 * 		All current solutions
	 */
	@Override
	public List<Equation> getAllSolutions() {
		List<Equation> solutions = new ArrayList<Equation>();
		for(Equation eq : solutionSpace) {
			solutions.add(eq);
		}
		return solutions;
	}




	@Override
	public String getNameOfMaster() {
		return "ObjectAll";
	}


}