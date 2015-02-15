package master.string;

import java.util.*;

import master.Input;
import master.Master;
import master.Timer;
import tree.*;

//TODO Oppassen met de Ks

public class StringMaster extends Master {

	private static final String NAME_GOAL = "Goal";

	private static Timer timer = new Timer(Integer.MAX_VALUE);	
	private static StringEvaluate evaluate;
	private static int nrOfK;


	public static List<String> solutionSpace = new ArrayList<String>();

	public static void run() {
		// read the tree generated earlier
		Input input = new Input();
		Tree tree = input.getTree();

		// generate the evaluate class

		for(int j = 0; j < input.getList().size(); j++) {
			ArrayList<Double> KsList = new ArrayList<Double>();

			for(int i = 0; i < input.getList().get(j).size()-1; i++) {
				KsList.add(input.getList().get(j).get("K"+(i)));
			}
			nrOfK = KsList.size();
			KsList.add(input.getList().get(j).get(StringMaster.getNameOfGoalK()));
			//TODO Solve this shit
			if(j==0)
				evaluate = new StringEvaluate(tree,KsList);



			// start timer
			timer.start();

			// Check solution space for possible solution
			HashMap<String,Double> temp = new HashMap<String,Double>();
			for(int i = 0; i < KsList.size(); i++) {
				if(i == KsList.size()-1)
					temp.put("Goal", KsList.get(i));
				else 
					temp.put("K"+(i+1),KsList.get(i));
			}
			checkSolutionSpace(temp);

			// start to evaluate
			solutionSpace.addAll(evaluate.evaluate(KsList));

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
			if(evaluate.evalString(Ks, eq)) {
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


	/**
	 * Returns the best solution form a collection from the solution space.
	 * @param solutions
	 * @return
	 */
	public String getBestSolution(){
		String bestSolution = "";
		int nrOfKeys = 0;

		for(String eq: getAllSolutions()) {
			int temp = 0;

			List<String> split = evaluate.splitStringAll(eq);
			for(int i = 0; i< nrOfK; i++){
				if(split.contains("K"+(i+1))){
					temp++;
				}

			}
			if(temp == nrOfK){
				return eq;
			}
			if(temp > nrOfKeys){
				bestSolution = eq;
			}

		}
		return bestSolution;
	}
	
	/**
	 * Returns the solution space
	 * @return
	 */
	public List<String> getAllSolutions(){
		return solutionSpace;
	}
	
	/**
	 * @return
	 * 		The name of this master
	 */
	public String getNameOfMaster() {
		return "StringMaster";
	}


	@Override
	public String run(int deadline, boolean stopAfterOne,
			List<List<Double>> numbers) {
		// TODO Auto-generated method stub
		return null;
	}


}