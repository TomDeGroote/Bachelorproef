package master;

import java.util.*;

import tree.*;

//TODO Oppassen met de Ks

public class Master {

	private static final String NAME_GOAL = "Goal";

	private static Timer timer = new Timer(100);	
	private static Evaluate evaluate;

	public static List<String> solutionSpace = new ArrayList<String>();

	public static void main(String[] args) {
		// read the tree generated earlier
		Input input = new Input();
		Tree tree = input.getTree();

		// generate the evaluate class
		
		int h = 1; // counter to say how many examples have passed
		
		for(int j = 0; j < input.getList().size(); j++) {
			ArrayList<Double> KsList = new ArrayList<Double>();

			for(int i = 0; i < input.getList().get(j).size()-1; i++) {
				KsList.add(input.getList().get(j).get("K"+(i)));
			}
			KsList.add(input.getList().get(j).get(Master.getNameOfGoalK()));
			//TODO Solve this shit
			if(j==0)
				evaluate = new Evaluate(tree,KsList);

			

			// start timer
			timer.start();

			// Check solution space for possible solution
			checkSolutionSpace(input.getList().get(j));

			System.out.println("Solutions before " + h + " number of examples");
			for(String solution : solutionSpace) {
				System.out.println(solution);
			}
			// start to evaluate
			solutionSpace.addAll(evaluate.evaluate(KsList));

			// Print possible solutions
			System.out.println("Solutions after " + h + " number of examples");
			for(String solution : solutionSpace) {
				System.out.println(solution);
			}
			h++;

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
		List<String> newSolutionSpace = new ArrayList<String>();
		for(String eq : solutionSpace) {
			System.out.println("Is here?");
			if(evaluate.evalString(Ks, eq)) {
				System.out.println("Added to new solspace");
				newSolutionSpace.add(eq);
				
			}
		}	
		solutionSpace = newSolutionSpace;
	}



	/**
	 * TODO comment
	 * @return
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