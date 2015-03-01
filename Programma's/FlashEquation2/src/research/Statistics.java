package research;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

import tree.Grammar;
import tree.Tree;

// TODO error

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
	private static final int nrOfExamples = 1;
	private static final int minRange = 0;
	private static final int maxRange = 100;
	
	private static Statistics statistics = new Statistics();
	private static Thread[] threads;


	// inputs
	private static Input inputP = null;
	private static Input inputNP = null;

	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		inputP = new Input(Tree.FILENAME_P);
		inputNP = new Input(Tree.FILENAME_NP);
		// In case of Error
		//runPvsNP();
		long time = System.currentTimeMillis();
		runWeightsvsNoWeights(); // put on true to recover data
		System.out.print("Total");
		printTime(time);
		System.out.println("Done!");
	}

	/**
	 * Prints the time it took
	 * @param prevTime
	 * 		The previous time
	 */
	private static void printTime(long prevTime) {
		long x = (System.currentTimeMillis() - prevTime) / 1000;
		int seconds = (int) (x % 60);
		x = x/60;
		int minutes = (int) (x % 60);
		x = x/60;
		int hours = (int) (x % 24);
		System.out.println(" Time: " + hours + "h" + minutes + "min" + seconds + "s" + "   ");
	}
	
	/**
	 * Runs weights versus no weights method
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	private static void runWeightsvsNoWeights() throws FileNotFoundException, IOException, InterruptedException {
		int numberOfIterations = 100;
		int length = 5; // length with the solution included
		boolean printStatus = true;
		
		// Define all Four
		Object[][] allFourTable = new Object[numberOfIterations*nrOfExamples][(int) (length+1)];
			
		List<Object[][]> allFour = noVsWeights(numberOfIterations, printStatus, length);	
		writeData(allFourTable, allFour);
	}

	/**
	 * Writes data to corresponding CSV file
	 * @param allFourTable
	 * @param allFour
	 */
	private static void writeData(Object[][] allFourTable, List<Object[][]> allFour) {
		// Put only number of results in All results
		Object[][] tempNoWeights = allFour.get(0);
		for(int j = 0; j < tempNoWeights.length; j++) {
			allFourTable[j][0] = tempNoWeights[j][1];
		}
		
		// Put only number of results in All results
		Object[][] tempPrime = allFour.get(1);
		for(int j = 0; j < tempPrime.length; j++) {
			allFourTable[j][1] = tempPrime[j][1];
		}
					
		// Put only number of results in All results
		Object[][] tempFive = allFour.get(2);
		for(int j = 0; j < tempFive.length; j++) {
			allFourTable[j][2] = tempFive[j][1];
		}
					
		// Put only number of results in All results
		Object[][] tempTen = allFour.get(3);
		for(int j = 0; j < tempTen.length; j++) {
			allFourTable[j][3] = tempTen[j][1];
		}	
		
		
		// prints the random numbers
		System.out.println("Random: ");
		Object[][] r = allFour.get(4);
		for(Object[] row : r) {
			for(Object elem : row) {
				System.out.print(elem + " ");
			}
			System.out.println();
		}

		String[] headers = new String[] {"noWeights", "primeWeights", "twoWeights", "tenWeights"};
		// Write All Four
		writeToCSV(allFourTable, "allFour", headers);
	}
	
	/**
	 * 
	 * @param numberOfIterations
	 * @param write
	 * @param printStatus
	 * @return
	 * 		List: 	0: statisticsNo
	 * 				1: statisticsPrime
	 * 				2: statisticsFive
	 * 				3: statisticsTen
	 * 				4: random
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	private static List<Object[][]> noVsWeights(double numberOfIterations, boolean printStatus, int length) throws FileNotFoundException, IOException, InterruptedException {
		// runStatistics variables
		boolean useRealRandom = true;
		boolean stopAfterOne = false;
		
		// needed for excel document
		final Object[][] statisticsNo = new Object[(int) (numberOfIterations*nrOfExamples)][3];
		final Object[][] statisticsPrime = new Object[(int) (numberOfIterations*nrOfExamples)][3];
		final Object[][] statisticsTwo = new Object[(int) numberOfIterations*nrOfExamples][3];
		final Object[][] statisticsTen = new Object[(int) numberOfIterations*nrOfExamples][3];
		final Object[][] random = new Object[(int) numberOfIterations*nrOfExamples][length];

		// Run statistics for ObjectAll, Tuple for P and NP
		List<List<List<Double>>> allRandom = new ArrayList<List<List<Double>>>();
		for(double i = 0; i < numberOfIterations; i++) {
			// Create random numbers
			List<List<Double>> numbers = genetereNumbers(useRealRandom, length, nrOfExamples, minRange, maxRange);
			getRandomString(useRealRandom, numbers, false, random, (int) i*nrOfExamples);
			allRandom.add(numbers);
		}
		
		// Run no weights
		long time = System.currentTimeMillis();
		runThreads(Runner.TUPLE, numberOfIterations, useRealRandom, stopAfterOne, statisticsNo, allRandom, 1);	
		printTime(time);
		
		// Run two weights
		Grammar.setWeights(new Double[] {1.0, 2.0}); 			// Set grammar weights
		time = System.currentTimeMillis();
		runThreads(Runner.TUPLEWEIGHT, numberOfIterations, useRealRandom, stopAfterOne, statisticsTwo, allRandom, 2);
		printTime(time);

		// Run prime weights
		Grammar.setWeights(new Double[] {1.0, 2.0, 3.0, 5.0, 7.0}); // Set grammar weights
		time = System.currentTimeMillis();
		runThreads(Runner.TUPLEWEIGHT, numberOfIterations, useRealRandom, stopAfterOne, statisticsPrime, allRandom, 3);
		printTime(time);

		// Run ten weights
//		Grammar.setWeights(new Double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}); // Set grammar weights
//		time = System.currentTimeMillis();
//		runThreads(Runner.TUPLEWEIGHT, numberOfIterations, useRealRandom, stopAfterOne, statisticsTen, allRandom, 4);
//		printTime(time);

		List<Object[][]> result = new ArrayList<Object[][]>();
		result.add(statisticsNo);
		result.add(statisticsPrime);
		result.add(statisticsTwo);
		result.add(statisticsTen);
		result.add(random);
		return result;
	}

	/**
	 * Runs the threads of a given
	 * @param numberOfIterations
	 * @param useRealRandom
	 * @param stopAfterOne
	 * @param statisticsNo
	 * @param allRandom
	 * @throws InterruptedException
	 */
	private static void runThreads(Runner runner, double numberOfIterations, boolean useRealRandom, boolean stopAfterOne, Object[][] statisticsNo, List<List<List<Double>>> allRandom, int experimentNr) throws InterruptedException {
		if(experimentNr == 1) {
			// print basis for status bar
			System.out.print("0: [");
			for(int i = 0; i < numberOfIterations; i++) {
				System.out.print(" ");
			}		
			System.out.println("]");
		}
		
		// for status bar
		threads = new Thread[(int) numberOfIterations];
		for(int i = 0; i < numberOfIterations; i++) {
			runStatistic(runner, inputP, allRandom.get(i), useRealRandom, statisticsNo, (int) (i), stopAfterOne);
		}
		
		// wait for all of the above threads to finish
		System.out.print(experimentNr + ": [");
		for(int i = 0; i < threads.length; i++) {
		  threads[i].join();
		  System.out.print("#");
		}
		System.out.print("]");
	}
	
	/**
	 * Executes the P vs NP experiment for a number of times and writes away the data as a CSV 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void runPvsNP() throws FileNotFoundException, IOException {
		int numberOfIterations = 100;
		double startLength = 2;
		double stopLength = 11;
		boolean write = false;
		boolean printStatus = true;
		
		// Define all Four
		Object[][] All_P = new Object[numberOfIterations*nrOfExamples][(int) (stopLength-startLength+1)];
		Object[][] All_NP = new Object[numberOfIterations*nrOfExamples][(int) (stopLength-startLength+1)];
		Object[][] Tuple_P = new Object[numberOfIterations*nrOfExamples][(int) (stopLength-startLength+1)];
		Object[][] Tuple_NP = new Object[numberOfIterations*nrOfExamples][(int) (stopLength-startLength+1)];

		
		for(int i = (int) startLength; i <= stopLength; i++) {
			System.out.print("Running: [");
			for(int j = 0; j < i-startLength; j++) {
				System.out.print("#");
			}
			for(double j = i-startLength; j < stopLength-startLength+1; j++) {
				System.out.print(" ");
			}
			System.out.print("] " + ((i-startLength)/(stopLength+1-startLength)*100) + "%\r");

			
			List<Object[][]> allFour = PvsNP(numberOfIterations, write, printStatus, i);
			
			// Put only time results in All results
			Object[][] temp_All_P = allFour.get(1);
			for(int j = 0; j < temp_All_P.length; j++) {
				All_P[j][(int) (i-startLength)] = temp_All_P[j][0];
			}
			
			// Put only time results in All results
			Object[][] temp_All_NP = allFour.get(0);
			for(int j = 0; j < temp_All_NP.length; j++) {
				All_NP[j][(int) (i-startLength)] = temp_All_NP[j][0];
			}
						
			// Put only time results in All results
			Object[][] temp_Tuple_P = allFour.get(3);
			for(int j = 0; j < temp_Tuple_P.length; j++) {
				Tuple_P[j][(int) (i-startLength)] = temp_Tuple_P[j][0];
			}
						
			// Put only time results in All results
			Object[][] temp_Tuple_NP = allFour.get(2);
			for(int j = 0; j < temp_Tuple_NP.length; j++) {
				Tuple_NP[j][(int) (i-startLength)] = temp_Tuple_NP[j][0];
			}					
		}
		// print the 100% bar
		System.out.print("Running: [");
		for(int j = 0; j < stopLength-startLength+1; j++) {
			System.out.print("#");
		}
		System.out.print("] 100.0%\r");
		
		String[] headers = new String[(int) (stopLength-startLength+1)];
		for(int i = 0; i < headers.length; i++) {
			headers[i] = "length: " + (startLength + i);
		}
		// Write All Four
		writeToCSV(All_P, "Big_All_P", headers);
		writeToCSV(All_NP, "Big_All_NP", headers);
		writeToCSV(Tuple_P, "Big_Tuple_P", headers);
		writeToCSV(Tuple_NP, "Big_Tuple_NP", headers);

	}
	
	/**
	 * Executes the P versus NP experiment
	 * @param numberOfIterations
	 * 			Number of iterations for this experiment
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @return
	 * 		A list with results for for experiments
	 * 		0. All_NP
	 * 		1. All_P
	 * 		2. Tuple_NP
	 * 		3. Tuple_P
	 */
	private static List<Object[][]> PvsNP(double numberOfIterations, boolean write, boolean printStatus, int length) throws FileNotFoundException, IOException {
		// runStatistics variables
		boolean useRealRandom = true;
		boolean stopAfterOne = false;
		
		// needed for excel document
		final Object[][] statisticsAll_P = new Object[(int) (numberOfIterations*nrOfExamples)][3];
		final Object[][] statisticsAll_NP = new Object[(int) (numberOfIterations*nrOfExamples)][3];
		final Object[][] statisticsTuple_P = new Object[(int) numberOfIterations*nrOfExamples][3];
		final Object[][] statisticsTuple_NP = new Object[(int) numberOfIterations*nrOfExamples][3];
		final Object[][] random = new Object[(int) numberOfIterations*nrOfExamples][length];

		// Run statistics for ObjectAll, Tuple for P and NP
		for(double i = 0; i < numberOfIterations; i++) {
			// print progress bar
			if(printStatus) {
				System.out.print("Running: [");
				for(int j = 0; j < i; j++) {
					System.out.print("#");
				}
				for(double j = i; j < numberOfIterations; j++) {
					System.out.print(" ");
				}
				System.out.print("] " + (i/numberOfIterations*100) + "%\r");
			}
			
			// Run experiments
			List<List<Double>> numbers = genetereNumbers(useRealRandom, length, nrOfExamples, minRange, maxRange);
			runStatistic(Runner.OBJECTALL, inputP, numbers, useRealRandom, statisticsAll_P, (int) i, stopAfterOne);
			runStatistic(Runner.OBJECTALL, inputNP, numbers, useRealRandom, statisticsAll_NP, (int) i, stopAfterOne);
			runStatistic(Runner.TUPLE, inputP, numbers, useRealRandom, statisticsTuple_P, (int) i, stopAfterOne);
			runStatistic(Runner.TUPLE, inputNP, numbers, useRealRandom, statisticsTuple_NP, (int) i, stopAfterOne);
			getRandomString(useRealRandom, numbers, false, random, (int) i*nrOfExamples);
		}
		
		if(printStatus) {
			// print the 100% bar
			System.out.print("Running: [");
			for(int j = 0; j < numberOfIterations; j++) {
				System.out.print("#");
			}
			System.out.print("] 100.0%\r");
		}
		if(write) {
			String[] headers = new String[] {"Time", "Size", "Best Solution"};
			// write every data to a separate .csv file
			writeToCSV(statisticsAll_NP, "all_NP", headers);
			writeToCSV(statisticsAll_P, "all_P", headers);
			writeToCSV(statisticsTuple_NP, "tuple_NP", headers);
			writeToCSV(statisticsTuple_P, "tuple_P", headers);
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
		
		List<Object[][]> result = new ArrayList<Object[][]>();
		result.add(statisticsAll_NP);
		result.add(statisticsAll_P);
		result.add(statisticsTuple_NP);
		result.add(statisticsTuple_P);

		return result;
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
	public static void writeToCSV(Object[][] allCombined, String fileName, String[] headers) {
		try {
		    FileWriter writer = new FileWriter(fileName + ".csv");
	 
		    for(String header : headers) {
			    // define header
			    writer.append(header);
			    if(!headers[headers.length-1].equals(header)) {
				    writer.append(',');
			    }
		    }
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
			numbers = RandomGenerator.generateComplexRandom(length-2, length, nrOfExamples, minimum, maximum); // Real Random
			System.out.println("To be found: " + RandomGenerator.getLastGeneratedEquation());
		}
		return numbers;
	}
	
	/**
	 * Runs Master whose name is in the parameter Runner
	 * @param statisticsAll_P 
	 * @param b 
	 */
	public static void runStatistic(Runner runner, Input input, List<List<Double>> numbers, boolean useRealRandom, Object[][] statisticsAll_P, int indexOfExcel, boolean stopAfterOne) {
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
		

		RunnableMaster runMaster = statistics.new RunnableMaster(master, input, DEADLINE, stopAfterOne, numbers, printSizeAll, printAll, printBest, printTime, printRandom, statisticsAll_P, indexOfExcel);
        Thread t = new Thread(runMaster);
        threads[indexOfExcel] = t;
        t.start();
		// run the master
//		runMaster(master, input, DEADLINE, stopAfterOne, numbers, printSizeAll, printAll, printBest, printTime, printRandom, statisticsAll_P, indexOfExel);
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
		try {
			for(int i = 0 ; i < numbers.get(0).size(); i++) {
				for(int j = 0; j < nrOfExamples; j++) {
					random[iterator+j][i] = numbers.get(j).get(i);
				}
			}
		} catch(Exception e) {
			System.out.println("Error: " + numbers.size() + " " + numbers.get(0).size() + " " + random.length + " " + random[0].length);
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
	@SuppressWarnings("unused")
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
	
	/**
	 * Thread class
	 */
	public class RunnableMaster implements Runnable {

	    private Master master;
	    private Input input;
	    private int deadline;
	    private boolean stopAfterOne;
	    private List<List<Double>> numbers;
	    private Object[][] statisticsAll_P;
	    private int i;

	    public RunnableMaster(Master master, Input input, int deadline, boolean stopAfterOne, List<List<Double>> numbers,boolean printSizeAll, 
	    		boolean printAll, boolean printBest, boolean printTime, boolean printRandom, Object[][] statisticsAll_P, int i) {
	        this.master = master;
	        this.input = input;
	        this.deadline = deadline;
	        this.stopAfterOne = stopAfterOne;
	        this.numbers = numbers;
	        this.statisticsAll_P = statisticsAll_P;
	        this.i = i;
	    }

	    public void run() {
			long time = System.currentTimeMillis();
			master.run(deadline, stopAfterOne, numbers, input);
			time = System.currentTimeMillis() - time;
			statisticsAll_P[nrOfExamples*i] = new Object[] {time, master.getAllSolutions().size(), master.getBestSolution()};
	    }
	}
}
