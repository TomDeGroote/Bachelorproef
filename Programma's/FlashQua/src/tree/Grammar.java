package tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import tree.symbols.Symbol;
import tree.symbols.Terminal;
import tree.symbols.operands.Division;
import tree.symbols.operands.Multiplication;
import tree.symbols.operands.Operand;
import tree.symbols.operands.Substraction;
import tree.symbols.operands.Sum;
import exceptions.UselessEquationException;

/**
 * Represents the grammar that is going to be used to construct the Equation
 * tree.
 * 
 * @author Jeroen & Tom
 *
 */
public class Grammar {
	public static final String NONTERMINALREP = "E";
	public static final String TERMINALREP = "E";
	public static Terminal[] KS;
	public static final Operand[] OPERANDS = new Operand[]{new Multiplication(), new Substraction(), new Division(), new Sum()};
	public static Double GOAL;
	public static List<HashMap<String, Double>> otherEqs = new ArrayList<HashMap<String,Double>>();
	
	public static HashSet<Equation> solutions = new HashSet<Equation>();
	public static int AVOIDED = 0;
	
	
	/**
	 * Set the Terminal values for this grammar
	 * 
	 * @param Ks
	 * 			The column values
	 * @param weights
	 * 			The weights
	 */
	public static void setColumnValues(List<Double[]> multiInput, Double[] weights) {
		// set first equation that will be used to evaluate the rest TODO pick hardest equation
		Double[] input = multiInput.get(0);
		Grammar.KS = new Terminal[weights.length+input.length-1]; 
		int i = 0;
		for(i = 0; i < input.length-1; i++) {
			Grammar.KS[i] = new Terminal("K" + i, input[i]);
		}
		for(Double weight : weights) {
			Grammar.KS[i++] = new Terminal("W"+ weight.intValue(), weight);
		}
		GOAL = input[input.length-1];
		
		for(i = 1; i < multiInput.size(); i++) {
			HashMap<String, Double> terms = new HashMap<String, Double>();
			for(int j = 0; j < multiInput.get(i).length-1; j++) {
				terms.put("K" + j, multiInput.get(i)[j]);
			}
			for(Double weight : weights) {
				terms.put("W"+ weight.intValue(), weight);
			}
			terms.put("G", multiInput.get(i)[multiInput.get(i).length-1]);
			otherEqs.add(terms);
		}
	}
	
	/**
	 * @return
	 * 			The first equation of the Grammar
	 */
	public static HashSet<Equation> getStartEquation() {
		HashSet<Equation> eqs = new HashSet<Equation>();
		for(Terminal K : KS) {
			eqs.add(new Equation(K));
		}
		return eqs;
	}

	/**
	 * This method expands a given equation
	 * 
	 * @param equation
	 *            An equation ending with a nonTerminal
	 * @return A list of equations which represent the expansions of the given
	 *         equation
	 * @throws IllegalArgumentException
	 * 			If the last element is not a nonTerminal
	 */
	public static HashSet<Equation> expand(HashSet<Equation> alreadyFound, Equation equation) throws IllegalArgumentException {
		for (Operand operand : OPERANDS) { // for every possible operand generate the expansion
			for(Terminal K : KS) { // expand for every possible K
				// add the made expansion to the list of expansion equations
				try {
					Equation possibleNewEquation = new Equation(equation, operand, K);
					if(!alreadyFound.contains(possibleNewEquation)) {
						if(possibleNewEquation.getValueOfEquation().equals(GOAL)) {
							addPossibleSolution(possibleNewEquation);
						}
						alreadyFound.add(possibleNewEquation);
					}
				} catch(UselessEquationException e) {
//					System.out.println(e.getMessage());
				}
			}
		}
		return alreadyFound;
	}
	
	/**
	 * Add possible solutions
	 */
	public static void addPossibleSolution(Equation eq) {
		List<List<Symbol>> parts = eq.getEquationParts();
		for(HashMap<String, Double> otherEq : otherEqs) {
			List<Double> values = new ArrayList<Double>();
			for(List<Symbol> part : parts) {
				Double value = 0.0;
				for(int i = 0; i < part.size(); i += 2) {
					Operand op = (Operand) part.get(i);
					Terminal v1 = (Terminal) part.get(i+1);
					value = op.calculateValue(value, otherEq.get(v1.toString()));
				}
				values.add(value);
			}
			Double value = 0.0;
			for(int i = 0; i < parts.size(); i++) {
				Operand op = new Sum();
				value = op.calculateValue(value, values.get(i));
			}
			if(!value.equals(otherEq.get("G"))) {
				return;
			}
		}
		solutions.add(eq);
	}

	/**
	 * @param valueSolution1
	 *            A double
	 * @param operand
	 *            the operand
	 * @param valueSolution2
	 *            A double
	 * @return result of value1 operand value2
	 */
	public static Double getValue(Double value1, Operand operand, Double value2) {
		return operand.calculateValue(value1, value2);
	}
}