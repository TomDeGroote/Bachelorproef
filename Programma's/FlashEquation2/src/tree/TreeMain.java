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
	
	public static void main(String[] args) {
		initialize();
		for(int i = 0; i < nrOfLevels; i++) {
			expand();
			prune();
		}
	}
	
	private static void initialize() {
		levelCount = 1;
		startEquation = new Equation();
		listOfLevels = new ArrayList<List<Equation>>();
		ArrayList<Equation> firstLevel = new ArrayList<Equation>();
		firstLevel.add(startEquation);
		listOfLevels.add(firstLevel);
	}
	
	private static void expand() {
		List<Equation> newLevel = new ArrayList<Equation>();
		for(Equation currentEquation : listOfLevels.get(levelCount)) {
			for(Equation newEquation : Grammar.expand(currentEquation)) {
				newLevel.add(newEquation);
			}
		}
	}
	
	private static void prune() {
		PruneRules.prune(listOfLevels.get(levelCount));
	}
	

}
