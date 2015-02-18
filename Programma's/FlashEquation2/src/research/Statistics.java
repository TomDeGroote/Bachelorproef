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

import org.jopendocument.dom.spreadsheet.SpreadSheet;

import tree.Tree;

public class Statistics {

	// Deadline parameters
	private static final int DEADLINE = -1;	
	
	// Print Parameters
	private static final boolean printSizeAll = true;
	private static final boolean printAll = false;
	private static final boolean printBest = true;
	private static final boolean printTime = true;
	private static final boolean printRandom = true;
	
	// Random Parameters
	private static final int length = 5; // AFBLIJVEN!!!
	private static final int nrOfExamples = 5;
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
	
	/**
	 * Executes the P versus NP experiment
	 * @param numberOfIterations
	 * 			Number of iterations for this experiment
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void PvsNP(double numberOfIterations) throws FileNotFoundException, IOException {
		// runStatistics variables
		boolean useRealRandom = false;
		boolean stopAfterOne = true;
		
		
		// needed for excel document
		final Object[][] statisticsAll_P = new Object[(int) (numberOfIterations*nrOfExamples)][3];
		final Object[][] statisticsAll_NP = new Object[(int) (numberOfIterations*nrOfExamples)][3];
		final Object[][] statisticsTuple_P = new Object[(int) numberOfIterations*nrOfExamples][3];
		final Object[][] statisticsTuple_NP = new Object[(int) numberOfIterations*nrOfExamples][3];
		final Object[][] random = new Object[(int) numberOfIterations*nrOfExamples][length];

		
		// Run statistics for ObjectAll, Tuple for P and NP
		for(double i = 0; i < numberOfIterations; i++) {	
			// print progress bar
			System.out.print("Running: [");
			for(int j = 0; j < i; j++) {
				System.out.print("#");
			}
			for(double j = i; j < numberOfIterations; j++) {
				System.out.print(" ");
			}
			System.out.print("] " + (i/numberOfIterations*100) + "%\r");
			
			// Run experiments
			List<List<Double>> numbers = genetereNumbers(useRealRandom, length, nrOfExamples, minRange, maxRange);
			runStatistic(Runner.OBJECTALL, inputP, numbers, useRealRandom, statisticsAll_P, (int) i, stopAfterOne);
			runStatistic(Runner.OBJECTALL, inputNP, numbers, useRealRandom, statisticsAll_NP, (int) i, stopAfterOne);
			runStatistic(Runner.TUPLE, inputP, numbers, useRealRandom, statisticsTuple_P, (int) i, stopAfterOne);
			runStatistic(Runner.TUPLE, inputNP, numbers, useRealRandom, statisticsTuple_NP, (int) i, stopAfterOne);
			getRandomString(useRealRandom, numbers, false, random, (int) i*nrOfExamples);
		}
		
		// print the 100% bar
		System.out.print("Running: [");
		for(int j = 0; j < numberOfIterations; j++) {
			System.out.print("#");
		}
		System.out.print("] 100.0%\r");
		
		// write every data to a separate .csv file
		writeToCSV(statisticsAll_NP, "all_NP");
		writeToCSV(statisticsAll_P, "all_P");
		writeToCSV(statisticsTuple_NP, "tuple_NP");
		writeToCSV(statisticsTuple_P, "tuple_P");
		// Random is not written to the csv file
		
		// combine all of the object to one big table
		final Object[][] allCombined = new Object[(int) numberOfIterations*nrOfExamples][12+length];
		for(int i = 0; i < numberOfIterations*nrOfExamples; i++) {
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
		String[] columns = new String[] {"Time_All_NP", "Number_All_NP", "Best Solution_All_NP", "Time_All_P", "Number_All_P", "Best Solution_All_P", "Time_Tuple_NP", "Number_Tuple_NP", "Best Solution_Tuple_NP", "Time_Tuple_P", "Number_Tuple_P", "Best Solution_Tuple_P", "Random", "Random","Random","Random","Goal"};

		TableModel allComb = new DefaultTableModel(allCombined, columns);
		// Save the data to an ODS file and open it.
		final File file = new File("PvsNP");
		SpreadSheet.createEmpty(allComb).saveAs(file);
	}
	
	/**
	 * Writes a two dimensional object to a csv file in following format
	 * Time, Solutions, Best Solution
	 * a, b, c
	 * d, e, f
	 * @param allCombined
	 * 			The two dimensional object to write
	 * @param fileName
	 * 			The fileName where to write the object (.csv automatically added)
	 */
	public static void writeToCSV(Object[][] allCombined, String fileName) {
		try {
		    FileWriter writer = new FileWriter(fileName + ".csv");
	 
		    // define header
		    writer.append("Time");
		    writer.append(',');
		    writer.append("Solutions");
		    writer.append(',');
		    writer.append("Best Solution");
		    writer.append('\n');
		    
		    // write data
		    for(Object[] row : allCombined) {
		    	boolean nullRow = true;
		    	for(int i = 0; i < row.length; i++) {
		    		if(row[i] == null) {
		    			// no value in cell thus don't write it
		    		} else {
		    			nullRow = false;
		    			writer.append(row[i].toString());
			    		if(i < row.length-1) {
			    			writer.append(',');
			    		}
		    		}
		    	}
		    	if(!nullRow) {
		    		writer.append('\n');
		    	}
		    }
	 
		    writer.flush();
		    writer.close();
		} catch(IOException e) {
		     e.printStackTrace();
		} 
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
	 * @param b 
	 */
	public static void runStatistic(Runner runner, Input input, List<List<Double>> numbers, boolean useRealRandom, Object[][] statisticsAll_P, int indexOfExel, boolean stopAfterOne) {
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
		runMaster(master, input, DEADLINE, stopAfterOne, numbers, printSizeAll, printAll, printBest, printTime, printRandom, statisticsAll_P, indexOfExel);
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
		for(int i = 0 ; i < numbers.get(0).size(); i++) {
			for(int j = 0; j < nrOfExamples; j++) {
				random[iterator+j][i] = numbers.get(j).get(i);
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
	private static void runMaster(Master master, Input input, int deadline, boolean stopAfterOne, List<List<Double>> numbers,boolean printSizeAll, boolean printAll, boolean printBest, boolean printTime, boolean printRandom, Object[][] statisticsAll_P, int i) {
		// run master
		long time = System.currentTimeMillis();
		master.run(deadline, stopAfterOne, numbers, input);
		time = System.currentTimeMillis() - time;
		statisticsAll_P[nrOfExamples*i] = new Object[] {time, master.getAllSolutions().size(), master.getBestSolution()};
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
