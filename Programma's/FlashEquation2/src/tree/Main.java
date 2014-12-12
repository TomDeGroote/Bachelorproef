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

	private static final int NROFLEVELS = 5;
	private static final String FILENAME = "tree";
	private static final boolean REMOVEPRUNED = true;
	/**
	 * Main method of the tree-program
	 * 
	 * This will generate a Tree and
	 * when the tree is build it will be written to a file (as an object)
	 */
	public static void main(String[] args) {
		Tree tree = new Tree(NROFLEVELS, REMOVEPRUNED);
		printTree(tree);
		writeTree(tree);
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
	private static void printTree(Tree tree) {
        BufferedWriter writer = null;
        try {
            File treeText = new File("TreeText");

            // This will output the full path where the file will be written to...
            System.out.println(treeText.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(treeText));
            writer.write(tree.toString());
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
