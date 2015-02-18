package master;

import java.util.ArrayList;
import java.util.List;

import master.all.ObjectEvaluateAllSolutions;
import master.tuple.ObjectTupleEvaluate;
import tree.Equation;

public abstract class Master {

	
	protected static Timer timer = new Timer(Long.MAX_VALUE);	
	
	protected static final String NAME_GOAL = "Goal";
	
	protected static Evaluate evaluate;
	
	public static List<Equation> solutionSpace = null;
	
	protected static boolean hasDeadLine = false;		
	protected static boolean stopAfterOne = false;	

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
	 */
	public abstract String run(int deadline, boolean stopAfterOne, List<List<Double>> numbers, Input input);
	
	/**
	 * Prints the solution containing all Ks and is the smallest or
	 * prints the solution containing the most nr of Ks
	 * 
	 * @return
	 * 		The best solution
	 */
	public abstract Equation getBestSolution();
	
	/**
	 * 
	 * @return
	 * 		All current solutions
	 */
	public abstract List<Equation> getAllSolutions();
	
	/**
	 * @return
	 * 		The name of this master
	 */
	public abstract String getNameOfMaster();

	/**
	 * Method used to check if there is still time on the clock
	 * 
	 * @return
	 * 		True if the there is no more time left
	 * 		False if there is time left
	 * 
	 * 		TODO aangepast voor jar support
	 */
	public static boolean timesUp() {
		if(hasDeadLine) {
			return timer.timesUp();			
		} else {
			if(evaluate.bufferSolutions.isEmpty()) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * @return
	 * 		The name of the goal column value
	 */
	public static String getNameOfGoalK() {
		return NAME_GOAL;
	}
	
}
