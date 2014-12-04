package tree;
import java.util.List;


/**
 * Represents the tree created from the grammar.
 * @author Jeroen en Tom
 *
 */
public class TreeMain {

	private static int levelCount;
	private static Equation startEquation;
	private static List<List<Equation>> listOfLevels;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private static void expand(){
		for(Equation currentEquation : listOfLevels.get(levelCount)){
			Grammar.expand(currentEquation);
		}
	}
	
	private static void prune(){
		PruneRules.prune(listOfLevels.get(levelCount));
	}
	

}
