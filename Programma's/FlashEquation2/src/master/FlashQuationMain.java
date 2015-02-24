package master;

import java.util.ArrayList;
import java.util.List;

import master.all.ObjectMasterAllSolutions;
import master.normal.ObjectMaster;
import master.tuple.ObjectTupleMaster;
import master.tuple.TupleWeightsMaster;
import research.RandomGenerator;
import tree.Equation;
import tree.Tree;

public class FlashQuationMain {

	// Deadline parameters
	private static final int DEADLINE = -1;
	private static final boolean stopAfterOne = false;
	
	
	// Print Parameters
	private static final boolean printSizeAll = true;
	private static final boolean printAll = true;
	private static final boolean printBest = true;
	private static final boolean printTime = true;
	private static final boolean printRandom = true;
	
	// RandomGenerator parameters
	private static final boolean useRandom = true;
	private static final int length = 5; // inclusive solution
	private static final int nrOfExamples = 3;
	private static final int minimum = -100;
	private static final int maximum = 100;



	public static void main(String[] args) {
		List<String> toRun = new ArrayList<String>();
		toRun.add("all");
//		toRun.add("string");
//		toRun.add("normal");
		toRun.add("tuple");
//		toRun.add("tupleWeight");
		runListOfStrings(toRun);
	}
	
	/**
	 * Runs every Master whose name is in the parameter strings
	 * @param strings
	 * 			List containing the names of the to run Masters
	 */
	public static void runListOfStrings(List<String> strings) {
		List<List<Double>> numbers = null;
		if(useRandom) {
			numbers = RandomGenerator.generateCertainAllK(length, nrOfExamples, minimum, maximum); // Real Random
			System.out.println("***   To be found: " + RandomGenerator.getLastGeneratedEquation());
			System.out.println();
		} else {
			System.out.println("***   To be found unknown");
			System.out.println();
		}
		for(String s : strings) {
			Master master = null;
			switch (s) {
			case "tuple":
				master = new ObjectTupleMaster();
				break;
			case "normal":
				master = new ObjectMaster();
				break;
			case "all":
				master = new ObjectMasterAllSolutions();
				break;
			case "tupleWeight":
				master = new TupleWeightsMaster();
				break;
			default:
				System.out.println("Default not running");
				break;
			}	
			// run the master
			runMaster(master, DEADLINE, stopAfterOne, numbers, printSizeAll, printAll, printBest, printTime, printRandom);
			System.out.println("");
		}
		if(useRandom) {
			if(printRandom) {
				System.out.println("***     Random    ");
				for(List<Double> ds : numbers) {
					for(double d : ds) {
						System.out.print(d + "");
						for(int i = (d + "").length(); i < 7; i++) {
							System.out.print(" ");
						}
					}
					System.out.println();
				}
			}
		}
	}
	

	/**
	 * Logical parameters, will run logically
	 * @param master
	 * 			The Master which should be run (Object)
	 * @param deadline
	 * @param stopAfterOne
	 * @param numbers
	 * @param printSizeAll
	 * @param printAll
	 * @param printBest
	 * @param printTime
	 */
	public static void runMaster(Master master, int deadline, boolean stopAfterOne, List<List<Double>> numbers,boolean printSizeAll, boolean printAll, boolean printBest, boolean printTime, boolean printRandom) {
		Input input = new Input(Tree.FILENAME_P);
		System.out.println(   "***   Executing " + master.getNameOfMaster());
		// run master
		long time = System.currentTimeMillis();
		master.run(deadline, stopAfterOne, numbers, input);
		time = System.currentTimeMillis() - time;
		
		if(printTime) {
			System.out.println("  -     Time: " + time);
		}
		if(printSizeAll) {
			System.out.println("  -     Size: " + master.getAllSolutions().size());
		}
		if(printBest) {
			try {
				System.out.println("  -     Best: " + master.getBestSolution().toString());
			} catch (NullPointerException e) {
				System.out.println("  -     Best: Empty");
			}
		}
		if(printAll) {
			System.out.println("  -     All:");
			for(Equation eq : master.getAllSolutions()) {
				System.out.println("          + " + eq.toString());
			}
		}
	}

	/**
	 * Calculates the formula
	 * 
	 * WARNING: This method currently only calculates possible equations that are shorter than length of 6 values
	 * 			e.g. 5+5+1*8 will be found but
	 * 				 6+2/4-9+3-9+5 will never be found
	 * @param inputList
	 * 			A two dimensional list containing values
	 * @param deadline
	 * 			<= 0  then no deadline
	 * 			Else the maximum time it can run in milliseconds
	 * @return
	 * 			The formula in String format
	 * 				K0 means the first element in the InputList
	 * 				K1 means the second element in the InputList
	 * 				...
	 * 				KN means the N'th element in the InputList
	 * 				Possible operators: +, -, *, /
	 * 				A number is a given constant (not supported yet) TODO
	 * 			Or "Empty" if no formula was found
	 * 
	 * Currently using a 9 level tree, takes +- 800ms extra above deadline
	 */
	public static String getFormula(List<List<Double>> inputList, int deadline) {
		Input input = new Input(Tree.FILENAME_P);
		ObjectMaster master = new ObjectMaster();
		return master.run(deadline, true, inputList, input);
	}
}
