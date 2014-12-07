import java.util.ArrayList;
import java.util.HashMap;

/**
 * The hart of the program
 */
public class Main {

	private static final double K0 = 4.0;
	private static final double K1 = 16.0;
	private static final double K2 = 2.0;
	private static final double K3 = 8.0;
	private static final double GOAL = 666.0;
	private static final int NROFLEVELS = 5;

	
	private static ArrayList<Equation> equations = new ArrayList<>(); // contains all equations on this level
	private static Evaluate evaluation; // contains the evaluation class

	/**
	 * 1. create the starting equation : E 
	 * 2. loop: calculate next level and evaluate if a solution is in it
	 */
	public static void main(String[] args) {
		equations.add(new Equation(null, 0));
		ArrayList<Double> colunms1 = new ArrayList<>(); // TODO columns moeten nu uniek zijn
		colunms1.add(K0);
		colunms1.add(K1);
		colunms1.add(K2);
		colunms1.add(K3);
		evaluation = new Evaluate(GOAL, colunms1);
		for(int i = 0; i < NROFLEVELS; i++) {
			nextLevel();
			System.out.println("Level " + (i+1));
			printAll();
			//evaluate();
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
	@SuppressWarnings("unused")
	private static void evaluate() {
		HashMap<Equation, ArrayList<String>> sol = evaluation.evaluate(equations);
		for(ArrayList<String> s : sol.values()) {
			for(int i = 0; i < s.size(); i++) { 
				System.out.println(s.get(i));
			}
		}
	}
	
}
