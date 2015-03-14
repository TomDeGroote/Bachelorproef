package tree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * The main class of the tree program. From here a tree will be created and written to a file named fileName (A constant of this class) (as an object)
 * @author Jeroen & Tom
 *
 */
public class Main {
	
	private final static int NROFLEVELS = 7;
	private final static boolean PRUNE = true;
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Tree tree = new Tree(NROFLEVELS, PRUNE);
		for(int i = 1; i < NROFLEVELS; i++) {
			tree.expand();
		}
		printTreeToFile(tree);
		System.out.println("Done: " + (System.currentTimeMillis()-start));
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
}
