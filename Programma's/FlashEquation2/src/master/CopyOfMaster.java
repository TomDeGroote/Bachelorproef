package master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tree.Equation;
import tree.Symbol;
import tree.Terminal;
import tree.Tree;



public class CopyOfMaster {
	
	private static final String NAME_GOAL = "Goal";
	
	private static Timer timer = new Timer(1000);	
	private static CopyOfEvaluate evaluate;
	
	public static List<Equation> solutionSpace = new ArrayList<Equation>();
	
	public static HashMap<String, Double> example;
	
	public static void main(String[] args) {
		// read the tree generated earlier
		Input input = new Input();
		Tree tree = input.getTree();
		
		// generate the evaluate class
		evaluate = new CopyOfEvaluate(tree);
		
		int i = 1; // counter to say how many examples have passed
		for(HashMap<String, Double> Ks : input.getList()) {
			// remember the example
			example = Ks;
			
			// start timer
			timer.start();
			
			// Check solution space for possible solution
			checkSolutionSpace(Ks);

			// start to evaluate
			solutionSpace.addAll(evaluate.evaluate(Ks));

			// Print possible solutions
			System.out.println("Best Solution after " + i + " number of examples");
//			for(Equation solution : solutionSpace) {
//				System.out.println(solution.toString());
//			}
			returnBestSolution();
			i++;
		}
	}

	
	/**
	 * Checks the current solutionSpace to see if there are any equations
	 * that are also correct for this input
	 * @param Ks
	 * 		The input 
	 * 		Should be of form (but as hashmap): {(K0, 3), (K1, 4), ..., (KN-1, 2), (Goal, 5)}
	 */
	public static void checkSolutionSpace(HashMap<String, Double> Ks) {
		List<Equation> newSolutionSpace = new ArrayList<Equation>();
		for(Equation eq : solutionSpace) {
			List<Symbol> symbols = new ArrayList<Symbol>();
			for(Symbol s : eq.getListOfSymbols()) {
				if(s.isTerminal()) {
					String terminalName = ((Terminal) s).toString();
					symbols.add(new Terminal(terminalName, Ks.get(terminalName))); 
				} else {
					symbols.add(s);
				}
			}
			if(CopyOfEvaluate.evaluateTerminalEquation(new Equation(symbols), Ks.get(Master.getNameOfGoalK()))) {
				newSolutionSpace.add(eq);
			}
		}	
		solutionSpace = newSolutionSpace;
	}



	/**
	 * Prints the best solution
	 * Or the one who contains all smallest K's or if no one contains all K's the smallest one
	 */
	private static void returnBestSolution() {
		boolean printed = false;
		for(Equation eq : solutionSpace) {
			boolean containsAll = true;
			for(String K : example.keySet()) {
				if(!K.equals(Master.getNameOfGoalK())) {
					if(!eq.toString().contains(K)) {
						containsAll = false;
					}
				}
			}
			if(containsAll) {
				System.out.println(eq.toString());
				printed = true;
				break;
			}
		}
		if(!printed && solutionSpace.size() > 0)
			System.out.println(solutionSpace.get(0));
	}
	/**
	 * @return
	 * 		The name of the goal column value
	 */
	public static String getNameOfGoalK() {
		return NAME_GOAL;
	}
	
	/**
	 * Method used to check if there is still time on the clock
	 * 
	 * @return
	 * 		True if the there is no more time left
	 * 		False if there is time left
	 */
	public static boolean timesUp() {
		return timer.timesUp();
	}
	
	
}