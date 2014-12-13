package master;

import java.util.*;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import tree.*;


public class Master {

	
	private static int inputCount;
	private static int currentTreeLevel;
	private static List<List<Double>> inputlist = new ArrayList<List<Double>>();
	private static ArrayList<String> solutionSpace;
	private static Tree tree;
	private static Input input;
	
	private static Evaluate evaluation; // contains the evaluation class
	private static Terminal terminal;
	
	private static Timer timer = new Timer(100);
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		
		input = new Input();
		inputlist = input.getList();
		currentTreeLevel = 0;
		List<Double> currentExample = inputlist.get(0);
		ArrayList <Double> columnValues = new ArrayList<Double>(currentExample);
		double mainGoal = columnValues.remove(columnValues.size()-1);

		terminal = new Terminal("Master");
		terminal.setColumnValues(columnValues);
		evaluation = new Evaluate(mainGoal,terminal); 
		
		TimerThread timer = new TimerThread(5);
        timer.start();
        while(timer.isAlive()){
        	
        	evaluate();
        	currentTreeLevel++;
        	for(int i = 1; i < 3; i++){
        		if(solutionSpace.size() != 0)
        			nextInputCheckSolutionSpace();
        		inputCount++;
        	}
        	
        	
        	try {
                timer.sleep(1);
             } catch (InterruptedException e) {
                System.out.println("Interrupted.");
             }
        	
        }
        
        for(String s : solutionSpace) 
        	System.out.println(s);
        System.out.println("Job's done");
		

	}
	
	
	/**
	 *  Checks the next example in function of the current solution space.
	 */
	private static void nextInputCheckSolutionSpace() {
		List<Double> currentExample = inputlist.get(getInputCount());
		List <Double> columnValues = currentExample;
		HashMap<String,Double> valuesByName = new HashMap<String,Double>();
		DoubleEvaluator evaluator = new DoubleEvaluator();
		double currentGoal = columnValues.remove(columnValues.size()-1);
		for(int i = 0; i < columnValues.size(); i++) {
			valuesByName.put("K"+i,columnValues.get(i));
		}
		
		ArrayList<String> newSolutionSpace = new ArrayList<String>();
		for(String s : solutionSpace) {
			String[] seperateParts = s.split("(?<=[()+*/-])|(?=[()+*/-])");
			String evalString = new String();
			for(String sp : seperateParts) {
				if(valuesByName.containsKey(sp)){
					evalString.concat(valuesByName.get(sp).toString());
				} else {
					evalString.concat(sp);
				}
			}
			
			if(currentGoal == evaluator.evaluate(evalString)){
				newSolutionSpace.add(s);
			}
		}
		solutionSpace = newSolutionSpace;
		
	}
	
	private static void evaluate() {
		HashMap<Equation, ArrayList<String>> sol = evaluation.evaluate((ArrayList<Equation>) tree.getTree().get(getCurrentTreeLevel()));
		for(ArrayList<String> s : sol.values()) {
			for(int i = 0; i < s.size(); i++) { 
				solutionSpace.addAll(s);
			}
		}
	}
	
	private static int getCurrentTreeLevel() {
		return currentTreeLevel;
	}
	
	private static int getInputCount(){
		return inputCount;
	}


	public static String getNameOfGoalK() {
		return "Goal";
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