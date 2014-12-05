package tree;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * The main class of the tree program. From here a tree will be created and written to a file named fileName (A constant of this class) (as an object)
 * @author Jeroen & Tom
 *
 */
public class Main {

	private static final String fileName = "tree";
	/**
	 * Main method of the tree-program
	 * 
	 * This will generate a Tree and
	 * when the tree is build it will be written to a file (as an object)
	 */
	public static void main(String[] args) {
		Tree tree = new Tree();
		writeTree(tree);
	}
	
	
	/**
	 * Writes the created tree to a file (name is given as a constant fileName)
	 */
	private static void writeTree(Tree tree) {
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(tree);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
