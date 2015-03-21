package treebuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import treebuilder.Equation;
import treebuilder.symbols.Symbol;
import treebuilder.symbols.Terminal;
import treebuilder.symbols.operands.Division;
import treebuilder.symbols.operands.Multiplication;
import treebuilder.symbols.operands.Operand;
import treebuilder.symbols.operands.Power;
import treebuilder.symbols.operands.Substraction;
import treebuilder.symbols.operands.Sum;

/**
 * Represents the grammar that is going to be used to construct the Equation
 * tree.
 * 
 * @author Jeroen & Tom
 *
 */
public class Grammar implements Runnable {
	public static final String NONTERMINALREP = "E";
	public static final String TERMINALREP = "E";
	public static Terminal[] KS;
	public static final Operand[] OPERANDS = new Operand[]{new Multiplication(), new Substraction(), new Division(), new Sum(), new Power()};
	public static double GOAL;
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
	public static void setColumnValues(List<double[]> multiInput, double[] weights) {
		// set first equation that will be used to evaluate the rest TODO pick hardest equation
		double[] input = multiInput.get(0);
		Grammar.KS = new Terminal[weights.length+input.length-1]; 
		int i = 0;
		for(i = 0; i < input.length-1; i++) {
			Grammar.KS[i] = new Terminal("K" + i, input[i], false);
		}
		for(double weight : weights) {
			Grammar.KS[i++] = new Terminal("W"+ (int) weight, weight, true);
		}
		GOAL = input[input.length-1];
		
		for(i = 1; i < multiInput.size(); i++) {
			HashMap<String, Double> terms = new HashMap<String, Double>();
			for(int j = 0; j < multiInput.get(i).length-1; j++) {
				terms.put("K" + j, multiInput.get(i)[j]);
			}
			for(double weight : weights) {
				terms.put("W"+ (int) weight, weight);
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
				Equation possibleNewEquation = Equation.createEquation(equation, operand, K);
				if(possibleNewEquation != null) {
					if(!alreadyFound.contains(possibleNewEquation)) {
						if(possibleNewEquation.getValueOfEquation() == GOAL) {
							addPossibleSolution(possibleNewEquation);
						}
						alreadyFound.add(possibleNewEquation);
					}
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
				double value = 0.0;
				for(int i = 0; i < part.size(); i += 2) {
					Operand op = (Operand) part.get(i);
					Terminal v1 = (Terminal) part.get(i+1);
					value = op.calculateValue(value, otherEq.get(v1.toString()));
				}
				values.add(value);
			}
			double value = 0.0;
			for(int i = 0; i < parts.size(); i++) {
				Operand op = new Sum();
				value = op.calculateValue(value, values.get(i));
			}
			if(value != otherEq.get("G")) {
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
	public static double getValue(double value1, Operand operand, double value2) {
		return operand.calculateValue(value1, value2);
	}
	
	public static Operand[] getPossibleOperands() {
		return OPERANDS;
	}

	private Equation toExpand;
	public List<Equation> found = new ArrayList<Equation>();
	public Grammar(Equation equation) {
		this.toExpand = equation;
	}
	
	@Override
	public void run() {
		for (Operand operand : OPERANDS) { // for every possible operand generate the expansion
			for(Terminal K : KS) { // expand for every possible K
				// add the made expansion to the list of expansion equations
				Equation possibleNewEquation = Equation.createEquation(this.toExpand, operand, K);
				if(possibleNewEquation != null) {
					if(!Tree.alreadyFound.contains(possibleNewEquation)) {
						if(possibleNewEquation.getValueOfEquation() == GOAL) {
							addPossibleSolution(possibleNewEquation);
						}
						found.add(possibleNewEquation);
//						Tree.alreadyFound.add(possibleNewEquation);
					}
				}
			}
		}
		
	}
}