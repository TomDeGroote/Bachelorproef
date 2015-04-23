package runner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import research.RandomGenerator;
import treebuilder.Equation;
import treebuilder.comparators.Comparator;
import treebuilder.comparators.Equals;
import treebuilder.comparators.GreaterThan;
import treebuilder.comparators.GreaterThanOrEquals;
import treebuilder.comparators.SmallerThan;
import treebuilder.comparators.SmallerThanOrEquals;
import treebuilder.grammar.Grammar;
import treebuilder.tree.MultithreadedTree;
import treebuilder.tree.SinglethreadTree;
import treebuilder.tree.Tree;
import exceptions.MaxLevelReachedException;
import exceptions.OutOfTimeException;

/**
 * The main class of the tree program. From here a tree will be created and written to a file named fileName (A constant of this class) (as an object)
 * @author Jeroen & Tom
 *
 */
public class Main {
	// the weights to be used (basically constants)
	//private static double[] WEIGHTS = new double[]{1.0, 2.0, 3.0, 5.0, 7.0};

	// Deadline and maxlevel parameters
	private static int DEADLINE = -1;
	private final static int MAXLEVEL = 5;

	// Print the tree to a file or not, warning if you create many levels this writing will 
	// take a very long time
	private final static boolean PRINTTOFILE = false;

	// Multithreaded or not
	private final static boolean MULTITHREADED = true;

	// Random settings
	private final static boolean USERANDOM = true;
	private static int NROFKS;
	private final static int LENGTH = 4;
	private static int NROFEXAMPLES;
	private final static int MIN = 0;
	private final static int MAX = 100;
	private final static int nrOfIterations = 100;
	public static boolean USEOPTIMALISATIONS;
	public static boolean USINGWEIGHTS = true;

	public static void main(String[] args) throws IOException, InterruptedException {
		comparingWeights();
	}
	
	private static void comparingRandomGenerators() throws IOException, InterruptedException {
		String s = "";
		
		ArrayList<Double> solutions = new ArrayList<Double>();
		List<double[]> weights = new ArrayList<double[]>();
		
		double[] zeroWeights = new double[]{}; weights.add(zeroWeights); 
		
		s += ",realRandom, ComplexRandom, easyRandom";
		String r = ""; 
		DEADLINE = -1;
		USINGWEIGHTS = false;
		for(int j = 0; j < 2; j++){
			solutions.add(0.0);
		}
		List<KindOfRandom> kors = new ArrayList<KindOfRandom>();
		kors.add(KindOfRandom.REAL);kors.add(KindOfRandom.COMPLEX);kors.add(KindOfRandom.EASY);
		for(int i = 0; i < nrOfIterations; i++){
			double percentage = i % (nrOfIterations/100.0);
			if(percentage == 0.0)
				System.out.println(((double)i)/(nrOfIterations/100) + "%");
			USEOPTIMALISATIONS = false;
			NROFKS = 3;
			NROFEXAMPLES = 2;
			r += "\n Next Compare:";
			
			for(int j = 0; j < kors.size()-1; j++){
				Tuple<List<double[]>, List<Comparator>> fileInput = generateRandomInput(kors.get(j));
				r += "\n solutions:";
				main2(zeroWeights,fileInput);
				if(Grammar.getSolutions().size() > 0){
					for(Equation eq : Grammar.getSolutions())
						r += "\n"+eq.toString();
					solutions.set(j,solutions.get(j)+1);
				}
			}
		}
		s += "\nSolution%";
		for(double temp : solutions)
			s += ", " + temp;
		s += ",";
		writeToFile(s, "compareGenerators.csv");
		writeToFile(r, "solutionsGeneratorsCompare.txt");
	}
	
	private static void comparingOptimalisationsNoWeights() throws IOException, InterruptedException {
		String s = "";
		ArrayList<Double> time = new ArrayList<Double>();
		ArrayList<Double> solutions = new ArrayList<Double>();
		List<double[]> weights = new ArrayList<double[]>();
		double[] oneWeight = new double[]{1.0}; weights.add(oneWeight); 
		s += ",noOptimalisations, optimalized";
		String r = ""; 
		DEADLINE = -1;
		USINGWEIGHTS = false;
		for(int j = 0; j < 2; j++){
			time.add(0.0);
			solutions.add(0.0);
		}
		for(int i = 0; i < nrOfIterations; i++){
			double percentage = i % (nrOfIterations/100.0);
			if(percentage == 0.0)
				System.out.println(((double)i)/(nrOfIterations/100) + "%");
			NROFKS = 3;
			NROFEXAMPLES = 2;
			r += "\n Next Compare:";
			Tuple<List<double[]>, List<Comparator>> fileInput = generateRandomInput(KindOfRandom.COMPLEX);
			for(double[] temp : fileInput.x) 
				for(double t : temp)
					r += "\n"+t;
			for(int j = 0; j < 2; j++){
				r += "\n solutions:";
				USEOPTIMALISATIONS = (j == 0) ? false : true;
				main2(oneWeight,fileInput);
				time.set(j,time.get(j)+elapsedTime);
				if(Grammar.getSolutions().size() > 0){
					for(Equation eq : Grammar.getSolutions())
						r += "\n"+eq.toString();
					solutions.set(j,solutions.get(j)+1);
				}
			}
		}
		for(int i = 0; i < 2; i++){
			time.set(i,time.get(i)/nrOfIterations);
			solutions.set(i,solutions.get(i)/nrOfIterations);
		}
		s += "\nTime ";
		for(double temp : time)
			s += ", "+temp;
		
		s += "\nSolution%";
		for(double temp : solutions)
			s += ", " + temp;
		s += ",";
		writeToFile(s, "compareOptimalisations.csv");
		writeToFile(r, "solutionsCompare.txt");
	}

	private static void comparingWeights() throws IOException, InterruptedException{
		String s = "";
		ArrayList<Double> time = new ArrayList<Double>();
		ArrayList<Double> solutions = new ArrayList<Double>();
		List<double[]> weights = new ArrayList<double[]>();
		
		double[] noWeights = new double[]{}; weights.add(noWeights); s += ",noWeights";
		double[] threeWeights = new double[]{1.0, 2.0, 3.0}; weights.add(threeWeights); s += ",threeWeights";
		double[] primeWeights = new double[]{1.0, 2.0, 3.0, 5.0, 7.0};weights.add(primeWeights); s += ",primeWeights";
		double[] fiveWeights = new double[]{1.0, 2.0, 3.0, 4.0, 5.0}; weights.add(fiveWeights); s += ",fiveWeights";
//		double[] tenWeights = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};weights.add(tenWeights); s += ",tenWeights";

		DEADLINE = -1;
		for(int j = 0; j < weights.size(); j++){
			time.add(0.0);
			solutions.add(0.0);
		}
		for(int i = 0; i < nrOfIterations; i++){
			double percentage = i % (nrOfIterations/100.0);
			if(percentage == 0.0)
				System.out.println(((double)i)/(nrOfIterations/100) + "%");
			NROFKS = 3;
			NROFEXAMPLES = 2;
			Tuple<List<double[]>, List<Comparator>> fileInput = generateRandomInput(KindOfRandom.COMPLEX);
			for(int j = 0; j < weights.size(); j++){
				USINGWEIGHTS = (j == 0) ? false : true;
				main2(weights.get(j),fileInput);
				time.set(j,time.get(j)+elapsedTime);
				if(Grammar.getSolutions().size() > 0)
					solutions.set(j,solutions.get(j)+1);
			}
		}
		for(int i = 0; i < weights.size(); i++){
			time.set(i,time.get(i)/nrOfIterations);
			solutions.set(i,solutions.get(i)/nrOfIterations);
		}
		s += "\nTime ,";
		for(double temp : time)
			s += temp+", ";

		s += "\nSolution% ,";
		for(double temp : solutions)
			s += temp+", ";
		writeToFile(s, "weightsComparison.csv");
	}

	private static double elapsedTime;

	public static void main2(double[] weights, Tuple<List<double[]>, List<Comparator>> fileInput) throws IOException, InterruptedException {
		//System.out.println();

		// Set the grammar to use this input and the weights
		Grammar.clean();
		Grammar.setColumnValues(fileInput.x, weights, fileInput.y);

		// initialize the right tree
		Tree tree;
		Tree.clean();
		if(MULTITHREADED) {
			tree = new MultithreadedTree();
		} else {
			tree = new SinglethreadTree();
		}

		// expand the tree untill the given deadline or max level
		long start = System.currentTimeMillis();
		try {
			tree.expand(start, DEADLINE, MAXLEVEL);
		} catch(OutOfTimeException e) {
			System.out.println(e.getMessage());
		} catch(MaxLevelReachedException e) {
			System.out.println(e.getMessage());
		}
		elapsedTime = (System.currentTimeMillis()-start);
		//System.out.println("Done: " + elapsedTime);

		// print the solutions
//		System.out.println("\nSolutions:");
//		for(Equation eq : Grammar.getSolutions()) {
//			System.out.println(eq);
//		}

		// Write the tree to the file if nessecary
		if(PRINTTOFILE) {
			System.out.println("Writing to file!");
			printTreeToFile(tree);
		}
//		System.out.println("Done!");
	}


	private enum KindOfRandom {REAL, COMPLEX, EASY};
	/**
	 * @return The random input generated and the comparators to be used
	 */
	private static Tuple<List<double[]>, List<Comparator>> generateRandomInput(KindOfRandom kor) {
		List<List<Double>> random;
		switch (kor){
		case REAL:
			random = RandomGenerator.generateRealRandom(LENGTH, NROFEXAMPLES, MIN, MAX);
			break;
		case COMPLEX:
			random = RandomGenerator.generateComplexRandom(NROFKS,LENGTH, NROFEXAMPLES, MIN, MAX);
			break;
		case EASY:
			random = RandomGenerator.generateCertainAll(LENGTH, NROFEXAMPLES, MIN, MAX);
			break;
		default:
			random = RandomGenerator.generateComplexRandom(NROFKS,LENGTH, NROFEXAMPLES, MIN, MAX);
			break;
			
		}
	
		List<double[]> input = new ArrayList<double[]>();
		List<Comparator> comparers = new ArrayList<Comparator>();
		for(List<Double> r : random) {
			double[] row = new double[r.size()];
			for(int i = 0; i < r.size(); i++) {
				row[i] = r.get(i);
				//System.out.println(row[i]);
			}
			input.add(row);
		}
		//System.out.println("To be found: " + RandomGenerator.getLastGeneratedEquation());
		for(int i = 0; i < input.size(); i++) {
			comparers.add(new Equals());
		}
		Main m = new Main();
		return m.new Tuple<List<double[]>, List<Comparator>>(input, comparers);
	}


	/**
	 * Writes a text form of a tree to a text file
	 * @param tree
	 * 			the tree to be written to a text file
	 */
	private static void printTreeToFile(Tree tree) {
		writeToFile(tree.toString(), "TreeText");
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

	/**
	 * Reads a file for input
	 * Inputfile format expected: n n r c
	 * With n a number (double)
	 * With r the expected result (double)
	 * with c the comparator to be used
	 * @return 
	 * 		A tuple containing the input numbers and a list of comparators to be used
	 * 
	 */
	private static Tuple<List<double[]>, List<Comparator>> readFile() throws IOException {
		InputStream in = Main.class.getResourceAsStream("/inputExample.txt");
		//FileInputStream fis = new FileInputStream(fin);

		//Construct BufferedReader from InputStreamReader
		Scanner sc = new Scanner(new BufferedReader(new InputStreamReader(in)));//fis));

		List<double[]> input = new ArrayList<double[]>();
		List<Comparator> comparators = new ArrayList<Comparator>();
		while(sc.hasNextLine()) {
			String[] line = sc.nextLine().split(" ");
			double[] inputLine = new double[line.length-1];
			for(int i = 0; i < line.length-1; i++) {
				inputLine[i] = Double.parseDouble(line[i]);
				System.out.println("Value: " + inputLine[i]);
			}
			comparators.add(parseComparator(line[line.length-1]));
			input.add(inputLine);
		}	
		sc.close();
		Main m = new Main(); // BAH!!
		return m.new Tuple<List<double[]>, List<Comparator>>(input, comparators);
	}


	/**
	 * Parses the comparator to be used, default -> =
	 * @param comparator
	 * 			The comparator to be parsed
	 * @return
	 * 			The Comparator (as a class)
	 */
	private static Comparator parseComparator(String comparator) {
		switch (comparator) {
		case "=":
			return new Equals();
		case "<":
			return new SmallerThan();
		case "<=":
			return new SmallerThanOrEquals();
		case ">":
			return new GreaterThan();
		case ">=":
			return new GreaterThanOrEquals();
		default:
			return new Equals();
		}
	}

	/**
	 * To Support multiple type returns
	 * 
	 * @param <X>
	 * @param <Y>
	 */
	public class Tuple<X, Y> {
		public final X x;
		public final Y y;

		public Tuple(X x, Y y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return x.toString() + " = " + y;
		}
	}
}
