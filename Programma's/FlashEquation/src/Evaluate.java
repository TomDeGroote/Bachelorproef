import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class can evaluate if a given equation is equal to the goal
 *
 */
public class Evaluate {

	private final int goal;
	private final HashSet<Double> columns;
	private HashMap<String, HashSet<Double>> alreadyCalculated = new HashMap<>(); 
	
	/**
	 * 
	 * @param goal
	 * 		The goal to be achived
	 */
	public Evaluate(int goal, HashSet<Double> columns) {
		this.goal = goal;
		this.columns = columns;
	}
	
	public HashSet<Equation> evaluate(ArrayList<Equation> equations) {
		HashSet<Equation> solutions = new HashSet<>();
		// We know only the two first symbols will change (if we compare it to the previous equation)
		for(Equation eq : equations) {
			HashSet<Double> values = evaluateEquation(eq.getElements());
			alreadyCalculated.put(eq.getString(), values);
			for(Double value : values) {
				if(value == goal) {
					solutions.add(eq);
					break;
				}
			}
		}
		return solutions;
	}
	
	private HashSet<Double> evaluateEquation(ArrayList<Symbol> elements) {
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
				return columns;
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
	private HashSet<Double> calculateParts(HashSet<Double> values1, HashSet<Double> values2, Symbol operand) {
		HashSet<Double> result = new HashSet<>();
		for(Double value1 : values1) {
			for(Double value2: values2) {
				if(operand.getRepresentation().equals("+")) {
					result.add(value1+value2);
				} else if(operand.getRepresentation().equals("-")) {
					result.add(value1-value2);
				} else if(operand.getRepresentation().equals("*")) {
					result.add(value1*value2);
				} else if(operand.getRepresentation().equals("/")) {
					result.add(value1/value2);
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
	 * 			The lis of values of all possible total equations (= E * or / knownVlues)
	 */
	private HashSet<Double> calculateMulDiv(ArrayList<Symbol> elements, HashSet<Double> knownValues) {
		HashSet<Double> result = new HashSet<>();
		if(elements.size() > 2) {
			System.out.println("Error: more than two elements"); // TODO
		}
		for(Double value : knownValues) {	
			for(Double columnValue : columns) {
				if(elements.get(1).getRepresentation().equals("*")) {
					result.add(columnValue*value);
				} else { // enkel /
					result.add(columnValue/value);
				}
			}
		}	
		return result;
	}
	
	private HashSet<Double> calculateThreeElements(ArrayList<Symbol> elements) {
		HashSet<Double> result = new HashSet<>();
		for(Double value1 : columns) {
			for(Double value2 : columns) {
				if(elements.get(1).getRepresentation().equals("+")) {
					result.add(value1 + value2);
				} else if(elements.get(1).getRepresentation().equals("*")) {
					result.add(value1 * value2);
				} else if(elements.get(1).getRepresentation().equals("-")) {
					result.add(value1 - value2);
				} else if(elements.get(1).getRepresentation().equals("/")) {
					result.add(value1 / value2);
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