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
	
	private static double[] WEIGHTS = new double[]{1.0, 2.0, 3.0, 5.0, 7.0};
	
	private final static int DEADLINE = -1;
	private final static int MAXLEVEL = 5;
	
	private final static boolean PRINTTOFILE = true;
	
	private final static boolean USERANDOM = false;
	private final static int NROFKS = 3;
	private final static int LENGTH = 5;
	private final static int NROFEXAMPLES = 2;
	private final static int MIN = 0;
	private final static int MAX = 100;

	
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println();
		List<double[]> input = new ArrayList<double[]>();
		List<Comparator> comparers = new ArrayList<Comparator>();
		if(USERANDOM) {
			List<List<Double>> random = RandomGenerator.generateComplexRandom(NROFKS, LENGTH, NROFEXAMPLES, MIN, MAX);		
			for(List<Double> r : random) {
				double[] row = new double[r.size()];
				for(int i = 0; i < r.size(); i++) {
					row[i] = r.get(i);
				}
				input.add(row);
			}
			System.out.println("To be found: " + RandomGenerator.getLastGeneratedEquation());
			for(double[] i : input) {
				comparers.add(new Equals());
			}
		} else {
			Tuple<List<double[]>, List<Comparator>> fileInput = readFile();
			input = fileInput.x;
			comparers = fileInput.y;
		}

		Grammar.setColumnValues(input, WEIGHTS, comparers);
		
		Tree tree = new SinglethreadTree();
		long start = System.currentTimeMillis();
		try {
			tree.expand(start, DEADLINE, MAXLEVEL);
		} catch(OutOfTimeException e) {
			System.out.println(e.getMessage());
		} catch(MaxLevelReachedException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Done: " + (System.currentTimeMillis()-start));
		
		System.out.println("\nSolutions:");
		for(Equation eq : Grammar.getSolutions()) {
			System.out.println(eq + " => " + eq.hashCode());
		}
		if(PRINTTOFILE) {
			System.out.println("Writing to file!");
			printTreeToFile(tree);
		}
		System.out.println("Done!");
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
	 * Reads a file. This will convert a text file with column values to a list of HashMap<String, Double>
	 * e.g. text file: 3 6 8 -> [3, 6, 8]
	 * @param
	 * 		The file to be read
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
			input.add(inputLine);
			comparators.add(parseComparator(line[line.length-1]));
		}	
		sc.close();
		Main m = new Main(); // BAH!!
		return m.new Tuple<List<double[]>, List<Comparator>>(input, comparators);
	}


	private static Comparator parseComparator(String string) {
		switch (string) {
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
