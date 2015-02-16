package master;

import java.util.List;

import tree.Equation;

public abstract class Master {

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

	
}
