import java.util.ArrayList;

/**
 * This class can evaluate if a given equation is equal to the goal
 *
 * This class can't handle equations with nonTerminals!
 */
public class Evaluate {

	private int goal;
	
	/**
	 * 
	 * @param goal
	 * 		The goal to be achived
	 */
	public Evaluate(int goal) {
		this.goal = goal;
	}
	
	/**
	 * Evaluates a given equation and checks it with the goal
	 * @param equation
	 * 		The equation to be evaluated
	 * @return
	 * 		True: the evaluation is equal to the goal
	 * 		False: the evaluation isn't equal to the goal
	 */
	public boolean evaluate(Equation equation) {
		ArrayList<Symbol> symbols = equation.getElements();
		double result = ((Terminal) symbols.get(0)).getNumericValue();
		for(int i = 1; i < symbols.size(); i = i+2) {
			if(symbols.get(i).getRepresentation().equals("*")) {
				// calculation of *
				result = result*(((Terminal) symbols.get(i+1)).getNumericValue());
			} else if(symbols.get(i).getRepresentation().equals("+")) {
				// calculation of  +
				result = result+(((Terminal) symbols.get(i+1)).getNumericValue());
			} else if(symbols.get(i).getRepresentation().equals("-")) {
				// calculation of  -
				result = result-(((Terminal) symbols.get(i+1)).getNumericValue());
			} else if(symbols.get(i).getRepresentation().equals("/")) {
				// calculation of  /
				result = result/(((Terminal) symbols.get(i+1)).getNumericValue());
			}
		}
		
		// Check evaluation is equal to the goal
		if(result == goal) {
			return true;
		} else {
			return false;
		}
	}

}
