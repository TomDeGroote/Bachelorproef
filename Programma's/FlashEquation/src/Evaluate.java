import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;

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
	public Evaluate(double goal, ArrayList<Double> columns) {
		this.goal = goal;
		this.columns = columns;
	}
	
	public HashMap<Equation, ArrayList<String>> evaluate(ArrayList<Equation> equations) {
		HashMap<Equation, ArrayList<String>> solutions = new HashMap<>();
		// We know only the two first symbols will change (if we compare it to the previous equation)
		for(Equation eq : equations) {
			HashMap<Double, ArrayList<String>> values = evaluateEquation(eq.getElements());
			alreadyCalculated.put(eq.getString(), values);
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
			Symbol operand = null;
			ArrayList<Symbol> part2 = new ArrayList<>();

			// if contains + or -
			// 		splits on +/- and calculate separate parts
			// else splits on * or / and calculate separate parts
			if(elements.size() == 1) {
				HashMap<Double, ArrayList<String>> c = new HashMap<>();
				for(int i = 0; i < columns.size(); i++) {
					ArrayList<String> s = new ArrayList<>();
					s.add("K"+i);
					c.put(columns.get(i), s);
				}
				return c;
			} else if(elements.size() == 2) {
				System.out.println("two elements, wtf..");// TODO
				return null;
			} else if(elements.size() == 3){
				return calculateThreeElements(elements);
			} else if(stringOfElements.contains("+") || stringOfElements.contains("-")) {
				int i;
				for(i = 0; i < size; i++) {
					Symbol symbol = elements.get(i);
					if(symbol.getRepresentation().equals("+") || symbol.getRepresentation().equals("-")) {
						operand = symbol;
						break;
					} else	{
						part1.add(symbol);
					}
				}
				
				for(int j = i+1; j < size; j++) {
					part2.add(elements.get(j));
				}
				
				return calculateParts(evaluateEquation(part1), evaluateEquation(part2), operand);
				
			} else { // contains only * and/or /
				int k;
				for(k = 2; k < size; k = k+2) {
					String substring = stringOfElements.substring(k);
					part1.add(elements.get(k-2));
					part1.add(elements.get(k-1));
					if(alreadyCalculated.containsKey(substring)) {
						return calculateMulDiv(part1, alreadyCalculated.get(substring));
					}
				}
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
		for(Double value1 : values1.keySet()) {
			for(Double value2: values2.keySet()) {
				double value = 0.0;
				if(operand.getRepresentation().equals("+")) {
					value = value1+value2;
				} else if(operand.getRepresentation().equals("-")) {
					value = value1-value2;
				} else if(operand.getRepresentation().equals("*")) {
					value = value1*value2;
				} else if(operand.getRepresentation().equals("/")) {
					value = value1/value2;
				}
				
				for(String v1 : values1.get(value1)) {
					for(String v2 : values2.get(value2)) {
						String s =  v1 + " " + operand.getRepresentation() + " " + v2;
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
		}
		return result;
	}
	
	/**
	 * Calculates an equation with already a known part
	 * @param elements
	 * 			Contains two elements E and * or /
	 * @param knownValues
	 * 			Contains the known solutions with whom this equation should be combined
	 * @return
	 * 			The list of values of all possible total equations (= E * or / knownVlues)
	 */
	private HashMap<Double, ArrayList<String>> calculateMulDiv(ArrayList<Symbol> elements, HashMap<Double, ArrayList<String>> knownValues) {
		HashMap<Double, ArrayList<String>> result = new HashMap<Double, ArrayList<String>>();
		if(elements.size() > 2) {
			System.out.println("Error: more than two elements"); // TODO
		}
		for(Double knownSolution : knownValues.keySet()) {	
			for(int i = 0; i < columns.size(); i++) {
				double columnValue = columns.get(i);
				if(elements.get(1).getRepresentation().equals("*")) {
					double realValue = columnValue*knownSolution;
					if(result.containsKey(realValue)) {
						for(String knownValue : knownValues.get(knownSolution)) {
							String s = "K" + i + " * " + knownValue;
							result.get(realValue).add(s);
						}
					} else {
						ArrayList<String> knownValuesString =  knownValues.get(knownSolution);
						ArrayList<String> newString = new ArrayList<>();
						newString.add("K" + i + " * " + knownValuesString.get(0));
						result.put(realValue, newString);
						for(int j = 1; j < knownValuesString.size(); j++) {
							String s = "K" + i + " * " + knownValuesString.get(j);
							newString.add(s);
						}
						
					}
				} else { // enkel / // TODO de errors uit * moeten hier ook ergens zitten
					double realValue = columnValue/knownSolution;
					if(result.containsKey(realValue)) {
						for(String knownValue : knownValues.get(knownSolution)) {
							String s = "K" + i + " / " + knownValue;
							result.get(realValue).add(s);
						}
					} else {
						ArrayList<String> knownValuesString =  knownValues.get(knownSolution);
						ArrayList<String> newString = new ArrayList<>();
						newString.add("K" + i + " / " + knownValuesString.get(0));
						result.put(realValue, newString);
						for(int j = 1; j < knownValuesString.size(); j++) {
							String s = "K" + i + " / " + knownValuesString.get(j);
							newString.add(s);
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
	private HashMap<Double, ArrayList<String>> calculateThreeElements(ArrayList<Symbol> elements) {
		HashMap<Double, ArrayList<String>> result = new HashMap<Double, ArrayList<String>>();
		// TODO Min verplaatsen!!!! niet equivalent op deze manier
		if(elements.get(1).getRepresentation().equals("/")) {
			for(int i = 0; i < columns.size(); i++) {
				for(int j = 0; j < columns.size(); j++) {
					double value1 = columns.get(i);
					double value2 = columns.get(j);
					double value = value1 / value2;
					String s = "K" + i + " / " + "K" + j;
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
				Double value1 = columns.get(i);
				for(int j = i; j < columns.size(); j++) {
					Double value2 = columns.get(j);
					String s = "";
					double value = 0;
					if(elements.get(1).getRepresentation().equals("+")) {
						value = value1 + value2;
						s = "K" + i + " + " + "K" + j;
					} else if(elements.get(1).getRepresentation().equals("*")) {
						value = value1 * value2;
						s = "K" + i + " * " + "K" + j;
					} else if(elements.get(1).getRepresentation().equals("-")) {
						value = value1 - value2;
						s = "K" + i + " - " + "K" + j;
					}
					
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
			s += symbol.getRepresentation();
		}
		
		return s;
	}
}