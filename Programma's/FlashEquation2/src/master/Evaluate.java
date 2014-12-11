package master;

import tree.*;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import com.fathzer.soft.javaluator.DoubleEvaluator;

/**
 * This class can evaluate if a given equation is equal to the goal
 *
 */
public class Evaluate {

	private final double goal;
	private final ArrayList<Double> columns;
	private HashMap<String, HashMap<Double, ArrayList<String>>> alreadyCalculated = new HashMap<>(); 

	/**
	 * 
	 * @param goal
	 * 		The goal to be achived
	 */
	public Evaluate(double goal,Terminal t) {
		this.goal = goal;
		this.columns = (ArrayList<Double>) t.getColumnValue();
	}

	public HashMap<Equation, ArrayList<String>> evaluate(ArrayList<Equation> equations) {
		HashMap<Equation, ArrayList<String>> solutions = new HashMap<>();
		// We know only the two first symbols will change (if we compare it to the previous equation)
		for(Equation eq : equations) {
			HashMap<Double, ArrayList<String>> values = evaluateEquation(new ArrayList<Symbol>(eq.getListOfSymbols()));
			alreadyCalculated.put(eq.toString(), values);
			for(Double value : values.keySet()) {
				if(value == goal) {
					solutions.put(eq, values.get(value));
					break;
				}
			}
		}
		return solutions;
	}

	/**
	 * Evaluates an expression recursively
	 * 
	 * By dividing the equation into smaller parts and checking if those parts are already calculated
	 * @param elements
	 * @return
	 */
	private HashMap<Double, ArrayList<String>> evaluateEquation(ArrayList<Symbol> elements) {
		String stringOfElements = createStringFromSymbols(elements);
		if(alreadyCalculated.containsKey(stringOfElements)) {
			return alreadyCalculated.get(stringOfElements);
		} else {
			int size = elements.size();
			ArrayList<Symbol> part1 = new ArrayList<>();
			Operand currentOperand = (Operand) elements.get(1);
			ArrayList<Symbol> part2 = new ArrayList<>();
			if(size == 1) {
				HashMap<Double, ArrayList<String>> c = new HashMap<>();
				for(int i = 0; i < columns.size(); i++) {
					ArrayList<String> s = new ArrayList<>();
					s.add("K"+i);
					c.put(columns.get(i), s);
				}
				return c;
			} else if(size == 2) {
				//TODO
				System.out.println("two elements, wtf.. IMPOSSIBRUUUUUUUUUUUU");
			} else if(size == 3){
				return calculateThreeElements(elements);
			} else if(currentOperand.isSplitable()) {
				part1.add(elements.get(0));
				part2.addAll(elements.subList(2, size)) ;
				return calculateParts(evaluateEquation(part1), evaluateEquation(part2), currentOperand);	
			} else if(!currentOperand.isSplitable()) {
				Symbol splitOperand = currentOperand;
				int i;
				for(i = 0; i < size; i++) {
					Symbol symbol = elements.get(i);
					if(symbol.isOperand()){
						if(((Operand) symbol).isSplitable()) {
							splitOperand = symbol;
							break;
						} else	{
							part1.add(symbol);
						}
					}
				}

				for(int j = i+1; j < size; j++) {
					part2.add(elements.get(j));
				}

				return calculateParts(evaluateEquation(part1), evaluateEquation(part2), splitOperand);

			} 
			throw new EmptyStackException();
		}
	}

	/**
	 * Calculates all possible results of Par1 Operand Part2
	 * @param values1
	 * 			values of Part1
	 * @param values2
	 * 			values of Part2
	 * @param operand
	 * 			the operand
	 * @return
	 * 			values of Part1 Operand Part2
	 */
	private HashMap<Double, ArrayList<String>> calculateParts(HashMap<Double, ArrayList<String>> values1, HashMap<Double, ArrayList<String>> values2, Symbol operand) {
		HashMap<Double, ArrayList<String>> result = new HashMap<>();
		DoubleEvaluator evaluator = new DoubleEvaluator();
		for(Double value1 : values1.keySet()) {
			for(Double value2: values2.keySet()) {
				double value = evaluator.evaluate(value1 + operand.toString() + value2);
				for(String v1 : values1.get(value1)) {
					for(String v2 : values2.get(value2)) {
						String s =  v1 + " " + operand.toString() + " " + v2;
						if(result.containsKey(value)) {
							result.get(value).add(s);
						} else {
							ArrayList<String> newString = new ArrayList<>();
							newString.add(s);
							result.put(value,newString);
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Creates all possible results of an equation with 3 elements
	 */
	private HashMap<Double, ArrayList<String>> calculateThreeElements(ArrayList<Symbol> elements){
		HashMap<Double, ArrayList<String>> result = new HashMap<Double, ArrayList<String>>();
		DoubleEvaluator evaluator = new DoubleEvaluator();

		//TODO Commutativiteit if in operand
		if(elements.get(1).toString().equals("/") || elements.get(1).toString().equals("-") ) {
			for(int i = 0; i < columns.size(); i++) {
				for(int j = 0; j < columns.size(); j++) {
					double value1 = columns.get(i);
					double value2 = columns.get(j);
					String expression = value1 + elements.get(1).toString() + value2;
					double value = evaluator.evaluate(expression);
					String s = "K" + i + " " + elements.get(1).toString()+ " " + "K" + j;
					if(result.containsKey(value)) {
						result.get(value).add(s);
					} else {
						ArrayList<String> newString = new ArrayList<>();
						newString.add(s);
						result.put(value, newString);
					}
				}
			}
		} else {
			for(int i = 0; i < columns.size(); i++) {
				double value1 = columns.get(i);
				for(int j = i; j < columns.size(); j++) {
					double value2 = columns.get(j);
					String expression = value1 + elements.get(1).toString() + value2;
					double value = evaluator.evaluate(expression);
					String s = "K" + i + " " + elements.get(1).toString() + " " + "K" + j;
					if(result.containsKey(value)) {
						result.get(value).add(s);
					} else {
						ArrayList<String> newString = new ArrayList<>();
						newString.add(s);
						result.put(value, newString);
					}
				}
			}				
		}
		return result;
	}


	/**
	 * Creates an string from a given list of symbols
	 */
	private String createStringFromSymbols(ArrayList<Symbol> symbols) {
		String s = "";
		for(Symbol symbol : symbols) {
			s += symbol.toString();
		}
		return s;
	}
}