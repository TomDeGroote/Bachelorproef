package research;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import master.Input;
import master.Master;
import master.all.ObjectMasterAllSolutions;
import master.normal.ObjectMaster;
import master.tuple.ObjectTupleMaster;
import master.tuple.TupleWeightsMaster;
import tree.Equation;
import tree.Tree;

public class Statistics {

	// Deadline parameters
	private static final int DEADLINE = 100;
	private static final boolean stopAfterOne = false;
	
	
	// Print Parameters
	private static final boolean printSizeAll = true;
	private static final boolean printAll = false;
	private static final boolean printBest = true;
	private static final boolean printTime = true;
	private static final boolean printRandom = true;
	
	// RandomGenerator parameters
	private static final int length = 5; // inclusive solution
	private static final int nrOfExamples = 5;
	private static final int minimum = -100;
	private static final int maximum = 100;

	// inputs
	private static Input inputP = null;
	private static Input inputNP = null;

	public static void main(String[] args) {
		boolean realRandom = false;	
		inputP = new Input(Tree.FILENAME_P);
		inputNP = new Input(Tree.FILENAME_NP);

	}
	
	public static void PvsNP() {
		
	}
	
	public static List<List<Double>> genetereNumbers(boolean useRealRandom) {
		List<List<Double>> numbers = null;
		if(useRealRandom) {
			numbers = RandomGenerator.generateCertainAllK(length, nrOfExamples, minimum, maximum); // Real Random
			System.out.println("***   To be found: " + RandomGenerator.getLastGeneratedEquation());
			System.out.println();
		} else {
			numbers = RandomGenerator.generateCertainAllK(length, nrOfExamples, minimum, maximum); // Real Random
		}
		return numbers;
	}
	
	/**
	 * Runs every Master whose name is in the parameter strings
	 * @param strings
	 * 			List containing the names of the to run Masters
	 */
	public static void runStatistic(Runner runner, Input input, List<List<Double>> numbers, String experimentName, boolean useRealRandom) {
		Master master = null;
		switch (runner) {
		case TUPLE:
			master = new ObjectTupleMaster();
			break;
		case OBJECT:
			master = new ObjectMaster();
			break;
		case OBJECTALL:
			master = new ObjectMasterAllSolutions();
			break;
		case TUPLEWEIGHT:
			master = new TupleWeightsMaster();
			break;
		default:
			System.out.println("Default not running");
			break;
		}	
		// run the master
		runMaster(master, input, DEADLINE, stopAfterOne, numbers, printSizeAll, printAll, printBest, printTime, printRandom, experimentName);
		
		
		String s = "";
		if(useRealRandom) {
			s += "***   To be found: " + RandomGenerator.getLastGeneratedEquation() + "\n";		
		} else {
			System.out.println("***   To be found unknown");
			System.out.println();
		}
		s += "***     Random    \n";
		for(List<Double> ds : numbers) {
			for(double d : ds) {
				s += d + "";
				for(int i = (d + "").length(); i < 7; i++) {
					s += " ";
				}
			}
			s += "\n";
		}
		writeToFile(s, experimentName + "_Random");
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
	private static void runMaster(Master master, Input input, int deadline, boolean stopAfterOne, List<List<Double>> numbers,boolean printSizeAll, boolean printAll, boolean printBest, boolean printTime, boolean printRandom, String experimentName) {
		String toPrint = "";
		toPrint += "***   Executing " + master.getNameOfMaster() + "\n";
		// run master
		long time = System.currentTimeMillis();
		master.run(deadline, stopAfterOne, numbers, input);
		time = System.currentTimeMillis() - time;
		
		if(printTime) {
			toPrint +=  "  -     Time: " + time + "\n";
		}
		if(printSizeAll) {
			toPrint += "  -     Size: " + master.getAllSolutions().size() + "\n"; 
		}
		if(printBest) {
			try {
				toPrint += "  -     Best: " + master.getBestSolution().toString() + "\n";
			} catch (NullPointerException e) {
				toPrint += "  -     Best: Empty" + "\n";
			}
		}
		if(printAll) {
			toPrint += "  -     All:\n";
			for(Equation eq : master.getAllSolutions()) {
				toPrint += "          + " + eq.toString() + "\n";
			}
		}
		writeToFile(toPrint, experimentName + "_" + master.getNameOfMaster());
	}
	
	/**
	 * Writes a given string s to a file named fileName
	 * @param s
	 * 		The string to be written
	 * @param fileName
	 * 		The file name where the string s will be written
	 */
	private static void writeToFile(String s, String fileName) {
		BufferedWriter writer = null;
        try {
            File file = new File(fileName);

            writer = new BufferedWriter(new FileWriter(file));
            writer.write(s);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
	}
	
	public enum Runner {
		OBJECT, TUPLE, TUPLEWEIGHT, OBJECTALL 
	}
}
