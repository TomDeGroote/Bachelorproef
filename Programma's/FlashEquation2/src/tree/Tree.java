package tree;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents the tree created from the grammar.
 * @author Jeroen & Tom
 *
 */
public class Tree implements Serializable{

	private static final long serialVersionUID = 1L; // default serialVersionUID
	private static int levelCount;
	private final static int nrOfLevels = 10;
	private static Equation startEquation;
	private static List<List<Equation>> listOfLevels;

	/**
	 * Constructor of the tree
	 * 
	 * Will initialize the tree and expand it to a constant level
	 */
	public Tree() {
		// initialises the tree
		levelCount = 1;
		startEquation = new Equation();
		listOfLevels = new ArrayList<List<Equation>>();
		ArrayList<Equation> firstLevel = new ArrayList<Equation>();
		firstLevel.add(startEquation);
		listOfLevels.add(firstLevel);
		
		// expands and prunes the tree (level per level)
		for(int i = 0; i < nrOfLevels; i++) {
			expand();
			prune();
			levelCount++;
		}
	}
	
	/**
	 * Expands the current level (given by levelCount)
	 * 		After expansion the level will be added to the level list
	 */
	private void expand() {
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
	private void prune() {
		PruneRules.prune(listOfLevels.get(levelCount));
	}
	
	/**
	 * @return
	 * 		The tree created with the main method
	 */
	public List<List<Equation>> getTree() {
		return listOfLevels;
	}
	

}
