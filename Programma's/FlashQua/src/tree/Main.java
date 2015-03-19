package tree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import exceptions.MaxLevelReachedException;
import exceptions.OutOfTimeException;

/**
 * The main class of the tree program. From here a tree will be created and written to a file named fileName (A constant of this class) (as an object)
 * @author Jeroen & Tom
 *
 */
public class Main {
	
	public static Double[] WEIGHTS = new Double[]{1.0, 2.0, 3.0, 5.0, 7.0};
	public static Double[] KS = new Double[]{5.0, 6.0};
	
	public final static int DEADLINE = -1;
	public final static int MAXLEVEL = 4;
	
	public final static boolean INPUT = true;
	public final static boolean PRINTTOFILE = false;
	
	public static void main(String[] args) throws IOException {
		System.out.println();
		List<Double[]> input = readFile();
		Grammar.setColumnValues(input, WEIGHTS);
		
		Tree tree = new Tree();
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
		for(Equation eq : Grammar.solutions) {
			System.out.println(eq + " => " + eq.hashCode());
		}
		if(PRINTTOFILE) {
			System.out.println("Writing to file!");
			printTreeToFile(tree);
		}
		System.out.println("Done!");
	}
	
	
	/**
	 * @return
	 * 			The filename where you can find back the tree object (in workspace)
	 */
	public static String getFileNameTreeP() {
		return Tree.FILENAME_P;
	}
	
	/**
	 * @return
	 * 			The filename where you can find back the tree object (in workspace)
	 */
	public static String getFileNameTreeNP() {
		return Tree.FILENAME_NP;
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
	private static List<Double[]> readFile() throws IOException {
		InputStream in = Main.class.getResourceAsStream("/inputExample.txt");
		//FileInputStream fis = new FileInputStream(fin);
		
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(in));//fis));
		
		List<Double[]> input = new ArrayList<Double[]>();
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] splitOnSpace = line.split(" ");
			Double[] primLine = new Double[splitOnSpace.length];
			// add all K elements to hashmap
			for(int i = 0; i < splitOnSpace.length-1; i++) {
				double value = Double.parseDouble(splitOnSpace[i]);
				primLine[i] = value;
			}
			// add last element, the goal to hashmap
			double value = Double.parseDouble(splitOnSpace[splitOnSpace.length-1]);		
			primLine[primLine.length-1] = value;
			
			input.add(primLine);
		}
		br.close();
		return input;
	}
}
