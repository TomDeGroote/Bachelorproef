import java.util.ArrayList;

/**
 * The hart of the program
 */
public class Main {

	private static ArrayList<Equation> equations = new ArrayList<>(); // contains all equations on this level
	private static Evaluate evaluation = new Evaluate(4); // contains the evaluation class

	/**
	 * 1. create the starting equation : E 
	 * 2. loop: calculate next level and evaluate if a solution is in it
	 */
	public static void main(String[] args) {
		equations.add(new Equation(null, 0));
		for(int i = 0; i < 10; i++) {
			nextLevel();
			System.out.println("Level " + (i+1));
			//print();
			evaluate();
		}
	}
	
	/**
	 * Calculates the next level for all equations on the current level
	 */
	private static void nextLevel(){
		ArrayList<Equation> newEquations = new ArrayList<>();
		for(Equation equation : equations) {
			for(Equation e : equation.Expand()) {
				newEquations.add(e);
			}
		}
		equations = newEquations;
	}
	
	/**
	 * Prints the current equations in the equations list
	 */
	private static void print() {
		for(Equation eq : equations) {
			printEquation(eq);
		}
	}
	
	/**
	 * Prints equation
	 */
	private static void printEquation(Equation eq) {
		for(Symbol s : eq.getElements()) {
			System.out.print(s.getRepresentation());
		}
		System.out.println();
	}
	
	/**
	 * Evaluates the current equation 
	 */
	private static void evaluate() {
		for(Equation eq : equations) {	
			if(!eq.containsNonTerminal()) {
				if(evaluation.evaluate(eq)) {
					printEquation(eq);
				}
			}
		}
	}
	
}
