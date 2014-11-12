import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The hart of the program
 */
public class Main {

	private static ArrayList<Equation> equations = new ArrayList<>(); // contains all equations on this level
	private static Evaluate evaluation; // contains the evaluation class

	/**
	 * 1. create the starting equation : E 
	 * 2. loop: calculate next level and evaluate if a solution is in it
	 */
	public static void main(String[] args) {
		equations.add(new Equation(null, 0));
		HashSet<Double> colunms1 = new HashSet<>(); // TODO columns moeten nu uniek zijn
		colunms1.add(3.0);
		colunms1.add(8.0);
		colunms1.add(12.0);
		evaluation = new Evaluate(19, colunms1);
		for(int i = 0; i < 3; i++) {
			nextLevel();
			System.out.println("Level " + (i+1));
			//printAll();
			evaluate();
		}
	}
	
	/**
	 * Calculates the next level for all equations on the current level
	 */
	private static void nextLevel(){
		ArrayList<Equation> newEquations = new ArrayList<>();
		HashMap<String, ArrayList<Equation>> newPrevious = new HashMap<>();
		for(Equation equation : equations) {
			ArrayList<Equation> expansion = equation.Expand();
			String key = equation.getString();
			newPrevious.put(key, expansion);
			for(Equation e : expansion) {
				newEquations.add(e);
			}
		}
		equations = newEquations;
	}
	
	/**
	 * Prints the current equations in the equations list
	 */
	private static void printAll() {
		for(Equation eq : equations) {
			System.out.println(eq.getString());
		}
	}
	
	/**
	 * Evaluates the current equation 
	 */
	private static void evaluate() {
		for(Equation solution : evaluation.evaluate(equations)) {
			System.out.println(solution.getString());
		}
	}
	
}
