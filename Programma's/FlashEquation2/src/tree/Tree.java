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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; // default serialVersionUID
	private int levelCount;
	private final int nrOfLevels = 10;
	private Equation startEquation;
	private List<List<Equation>> listOfLevels;
	private List<Equation> temporaryLevel;

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
	 * 		After expansion the level will be added to temporaryLevel
	 */
	private void expand() {
		List<Equation> newLevel = new ArrayList<Equation>();
		for(Equation currentEquation : listOfLevels.get(levelCount)) {
			for(Equation newEquation : Grammar.expand(currentEquation)) {
				newLevel.add(newEquation);
			}
		}
		temporaryLevel = newLevel;
	}
	
	/**
	 * Prunes the current level (given by temporaryLevel)
	 * 
	 * After pruning the level will be added to listOfLevels
	 */
	private void prune() {
		listOfLevels.add(PruneRules.prune(temporaryLevel));
	}
	
	/**
	 * @return
	 * 		The tree created with the main method
	 */
	public List<List<Equation>> getTree() {
		return listOfLevels;
	}
	

}
