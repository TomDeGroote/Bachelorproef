package master;

import java.util.List;

import tree.Equation;

public abstract class Master {

	
	protected Timer timer = new Timer(Long.MAX_VALUE);	
	
	protected static final String NAME_GOAL = "Goal";
	
	protected Evaluate evaluate;
	
	public List<Equation> solutionSpace = null;
	
	protected boolean hasDeadLine = false;		
	protected boolean stopAfterOne = false;	

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
	public boolean timesUp(Evaluate evaluate) {
		if(stopAfterOne) {
			if(hasDeadLine) {
				if(timer.timesUp()) {
					return true;
				} else {
					return !evaluate.bufferSolutions.isEmpty();
				}
			} else {
				return !evaluate.bufferSolutions.isEmpty();
			}
		} else {
			if(hasDeadLine) {
				return timer.timesUp();
			} else {
				return false;
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
