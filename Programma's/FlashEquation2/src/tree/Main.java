package tree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * The main class of the tree program. From here a tree will be created and written to a file named fileName (A constant of this class) (as an object)
 * @author Jeroen & Tom
 *
 */
public class Main {

	private static final int NROFLEVELS = 9;
	private static final String FILENAME = "treeObject";
	private static final boolean REMOVEPRUNED = true;
	private static final boolean EXECUTE_STATISTICS = false;
	/**
	 * Main method of the tree-program
	 * 
	 * This will generate a Tree and
	 * when the tree is build it will be written to a file (as an object)
	 */
	public static void main(String[] args) {
		if(EXECUTE_STATISTICS) {
			mainStatistics(NROFLEVELS);
		} else {
			Tree tree = new Tree(NROFLEVELS, REMOVEPRUNED);
			printTreeToFile(tree);
			writeTree(tree);
			System.out.println("Done!");
		}
	}
	
	/**
	 * Executes some statistics for a tree of a number of levels and all trees smaller than this number of levels
	 * 
	 * Calculates elements per level and the total in the tree for prooned and non prooned
	 * The time it takes to calculate a tree of a given level prooned and non prooned
	 * 
	 * These statistics will be written to files named Nr Of Elements and Time Needed
	 */
	public static void mainStatistics(int nrOfLevels) {
		String timeNeeded = "";
		String nrOfElements = "";
		
		Tree treeProoned = null;
		Tree treeNotProoned = null;
		int totalElementsProoned = 0;
		int totalElementsNotProoned = 0;
		
		try {
			for(int i = 0; i < nrOfLevels; i++) {
				int iNot = i;
				System.out.print("#Levels: " + (i+1) +" => Started");
				// creates a prooned tree with i levels and times it
				long timeStarted = System.currentTimeMillis();
				treeProoned = new Tree(i+1, true);
				long timeNeededProoned = System.currentTimeMillis() - timeStarted;			
				// calculates the total elements in a tree of size i
				totalElementsProoned += treeProoned.getTree().get(i).size();
				
				long timeNeededNotProoned = 0;
				System.out.print("..");
				if(i < 8) {
					// creates a non prooned tree with i levels and times it
					timeStarted = System.currentTimeMillis();
					treeNotProoned = new Tree(i+1, false);
					timeNeededNotProoned = System.currentTimeMillis() - timeStarted;
					totalElementsNotProoned += treeNotProoned.getTree().get(i).size();
				} else {
					iNot = 0;
				}
				
				System.out.print("... ");
				// will add information about the time needed to calculate a tree of 
				timeNeeded += 	" ---------------------\n" +
								"| Number of Levels: " + (i+1) + " |\n" + 
								" ---------------------\n" +
								"Prooned:     " + (timeNeededProoned) + "ms\n" +
								"Not Prooned: " + (timeNeededNotProoned) + "ms\n\n";

				
				// will add information about the size of the current level and the size of the tree
				nrOfElements += " ---------------------\n" + 
								"| Number of Levels: " + (i+1) + " |\n" + 
								" ---------------------\n" +
								"Prooned:           " + treeProoned.getTree().get(i).size() + "\n" +
								"Not Prooned:       " + treeNotProoned.getTree().get(iNot).size() + "\n\n" +
								"Total Prooned:     " + totalElementsProoned + "\n" +
								"Total Not Prooned: " + totalElementsNotProoned + "\n\n";
				System.out.println(" Done in " + (timeNeededProoned+timeNeededNotProoned) + "ms");
				
				// Write some temporary results, so we can stop the process and still have results
				// Writing these things takes a lot of time for big trees
				System.out.print("           => Writing");
				writeToFile(timeNeeded, "Time Needed");
				System.out.print(".");
				writeToFile(nrOfElements, "Nr Of Elements");
				System.out.print(".");
				writeTree(treeProoned); // writes tree object of current calculated thing, takes a lot of time
				System.out.print(".");
				if(i < 6) // only write tree to string for the first 5 levels
					writeToFile(treeProoned.toString(), "Tree Prooned");
				System.out.print(".");
				if(i < 6) // only write tree to string for the first 5 levels
					writeToFile(treeNotProoned.toString(), "Tree Not Prooned");
				System.out.println(".  Done");
			}
		} catch (Error e) {
			e.printStackTrace();
		} finally {
			System.out.println("Done");
		}
	}
	
	/**
	 * @return
	 * 			The filename where you can find back the tree object (in workspace)
	 */
	public static String getFileNameTreeObject() {
		return FILENAME;
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
	 * Writes the created tree to a file (name is given as a constant fileName)
	 * As an object
	 */
	private static void writeTree(Tree tree) {
		try {
			FileOutputStream fout = new FileOutputStream(FILENAME);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(tree);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * Returns the filename of tree
	 */
	public static String getFileNameTree() {
		return FILENAME;
	}
	
}
