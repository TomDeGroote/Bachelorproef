package research;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import master.Input;
import master.Master;
import master.all.ObjectMasterAllSolutions;
import master.normal.ObjectMaster;
import master.tuple.ObjectTupleMaster;
import master.tuple.TupleWeightsMaster;

import org.jopendocument.dom.OOUtils;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import tree.Equation;
import tree.Tree;

public class Statistics {

	// Deadline parameters
	private static final int DEADLINE = 200;
	private static final boolean stopAfterOne = false;
	
	
	// Print Parameters
	private static final boolean printSizeAll = true;
	private static final boolean printAll = false;
	private static final boolean printBest = true;
	private static final boolean printTime = true;
	private static final boolean printRandom = true;
	
	// Random Parameters
	private static final int length = 5; // AFBLIJVEN!!!
	private static final int nrOfExamples = 1;
	private static final int minRange = 0;
	private static final int maxRange = 1000;
	
	// Research
	private static final int nrOfIterations = 100;


	// inputs
	private static Input inputP = null;
	private static Input inputNP = null;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		inputP = new Input(Tree.FILENAME_P);
		inputNP = new Input(Tree.FILENAME_NP);
		PvsNP(nrOfIterations);
		System.out.println("Done!");
	}
	
	public static void PvsNP(int numberOfIterations) throws FileNotFoundException, IOException {
		String randomUsed = "";		
		
		// needed for excel document
		final Object[][] statisticsAll_P = new Object[numberOfIterations][3];
		final Object[][] statisticsAll_NP = new Object[numberOfIterations][3];
		final Object[][] statisticsTuple_P = new Object[numberOfIterations][3];
		final Object[][] statisticsTuple_NP = new Object[numberOfIterations][3];
		final Object[][] random = new Object[numberOfIterations][length];

		
		// Run statistics for ObjectAll, Tuple for P and NP
		for(int i = 0; i < numberOfIterations; i++) {	
			System.out.println("Experiment: " + (i+1));
			List<List<Double>> numbers = genetereNumbers(true, length, nrOfExamples, minRange, maxRange);
			runStatistic(Runner.OBJECTALL, inputP, numbers, true, true, statisticsAll_P, i);
			runStatistic(Runner.OBJECTALL, inputNP, numbers, true, true, statisticsAll_NP, i);
			runStatistic(Runner.TUPLE, inputP, numbers, true, true, statisticsTuple_P, i);
			runStatistic(Runner.TUPLE, inputNP, numbers, true, true, statisticsTuple_NP, i);
			getRandomString(true, numbers, false, random, i);
		}
		
		// combine all of the object to one big table
		final Object[][] allCombined = new Object[numberOfIterations][12+length];
		for(int i = 0; i < numberOfIterations; i++) {
			for(int j = 0; j < 3; j++) {
				allCombined[i][j] = statisticsAll_NP[i][j];
			}
			for(int j = 3; j < 6; j++) {
				allCombined[i][j] = statisticsAll_P[i][j-3];
			}
			for(int j = 6; j < 9; j++) {
				allCombined[i][j] = statisticsTuple_NP[i][j-6];
			}
			for(int j = 9; j < 12; j++) {
				allCombined[i][j] = statisticsTuple_P[i][j-9];
			}
			for(int j = 12; j < 12+length; j++) {
				allCombined[i][j] = random[i][j-12];
			}
		}
		
		// define column names for excel file
		String[] columns = new String[] {"Time_All_NP", "Number", "Best Solution", "Time_All_P", "Number", "Best Solution", "Time_Tuple_NP", "Number", "Best Solution", "Time_Tuple_P", "Number", "Best Solution", "Random", "Random","Random","Random","Random"};

		TableModel allComb = new DefaultTableModel(allCombined, columns);
		// Save the data to an ODS file and open it.
		final File file = new File("PvsNP");
		SpreadSheet.createEmpty(allComb).saveAs(file);
		
		// write random to file
		writeToFile(randomUsed, "PvsNP_Random");
	}
	
	/**
	 * Generates random numbers
	 * @param useRealRandom
	 * @return
	 */
	public static List<List<Double>> genetereNumbers(boolean useRealRandom, int length, int nrOfExamples, int minimum, int maximum) {
		List<List<Double>> numbers = null;
		if(useRealRandom) {
			numbers = RandomGenerator.generateRealRandom(length, nrOfExamples, minimum, maximum); // Real Random
			System.out.println("***   To be found: " + RandomGenerator.getLastGeneratedEquation());
			System.out.println();
		} else {
			numbers = RandomGenerator.generateCertainAllK(length, nrOfExamples, minimum, maximum); // Real Random
		}
		return numbers;
	}
	
	/**
	 * Runs Master whose name is in the parameter Runner
	 * @param statisticsAll_P 
	 */
	public static void runStatistic(Runner runner, Input input, List<List<Double>> numbers, boolean useRealRandom, boolean raw, Object[][] statisticsAll_P, int indexOfExel) {
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
		runMaster(master, input, DEADLINE, stopAfterOne, numbers, printSizeAll, printAll, printBest, printTime, printRandom, raw, statisticsAll_P, indexOfExel);
	}
	
	
	/**
	 * Writes the random used numbers to a file named experimentName_Random
	 * @param useRealRandom
	 * 		Was realRandom used or not
	 * @param numbers
	 * 		The numbers to be written away
	 * @param random 
	 * @param iterator 
	 */
	private static void getRandomString(boolean useRealRandom, List<List<Double>> numbers, boolean print, Object[][] random, int iterator) {
		for(List<Double> ds : numbers) {
			for(int i = 0 ; i < ds.size(); i++) {
				random[iterator][i] = ds.get(i);
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
	 * @param raw
	 * 		If raw returns a string : "time  size  best"
	 * @param statisticsAll_P 
	 */
	private static void runMaster(Master master, Input input, int deadline, boolean stopAfterOne, List<List<Double>> numbers,boolean printSizeAll, boolean printAll, boolean printBest, boolean printTime, boolean printRandom, boolean raw, Object[][] statisticsAll_P, int i) {
		String toPrint = "";
		if(!raw) {
			toPrint += "***   Executing " + master.getNameOfMaster() + "\n";
		}
		// run master
		long time = System.currentTimeMillis();
		master.run(deadline, stopAfterOne, numbers, input);
		time = System.currentTimeMillis() - time;
		
		if(!raw) {
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
		} else {
			statisticsAll_P[i] = new Object[] {time, master.getAllSolutions().size(), master.getBestSolution()};
		}
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
