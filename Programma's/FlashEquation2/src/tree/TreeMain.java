package tree;
import java.util.*;


/**
 * Represents the tree created from the grammar.
 * @author Jeroen & Tom
 *
 */
public class TreeMain {

	private static int levelCount;
	private final static int nrOfLevels = 10;
	private static Equation startEquation;
	private static List<List<Equation>> listOfLevels;
	
	/**
	 * Main method of the tree-program
	 * This will initialize the tree, expand it and prune it
	 * 
	 * When the tree is build it will be written to a file (as an object)
	 */
	public static void main(String[] args) {
		initialize();
		for(int i = 0; i < nrOfLevels; i++) {
			expand();
			prune();
			levelCount++;
		}
		// TODO write to file
	}
	
	/**
	 * Creates the first level, and initializes levelCount
	 */
	private static void initialize() {
		levelCount = 1;
		startEquation = new Equation();
		listOfLevels = new ArrayList<List<Equation>>();
		ArrayList<Equation> firstLevel = new ArrayList<Equation>();
		firstLevel.add(startEquation);
		listOfLevels.add(firstLevel);
	}
	
	/**
	 * Expands the current level (given by levelCount)
	 * 		After expansion the level will be added to the level list
	 */
	private static void expand() {
		List<Equation> newLevel = new ArrayList<Equation>();
		for(Equation currentEquation : listOfLevels.get(levelCount)) {
			for(Equation newEquation : Grammar.expand(currentEquation)) {
				newLevel.add(newEquation);
			}
		}
	}
	
	/**
	 * Prunes the current level (given by levelCount)
	 * 		Because a pointer will be given, the current level will be changed
	 */
	private static void prune() {
		PruneRules.prune(listOfLevels.get(levelCount));
	}
	

}
