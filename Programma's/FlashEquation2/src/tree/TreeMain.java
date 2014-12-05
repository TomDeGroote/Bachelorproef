package tree;
import java.util.*;


/**
 * Represents the tree created from the grammar.
 * @author Jeroen & Tom
 *
 */
public class TreeMain {

	private static int levelCount;
	private static Equation startEquation;
	private static List<List<Equation>> listOfLevels;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TreeMain tree = new TreeMain();

	}
	
	public TreeMain() {
		levelCount = 1;
		startEquation = new Equation();
		listOfLevels = new ArrayList<List<Equation>>();
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
