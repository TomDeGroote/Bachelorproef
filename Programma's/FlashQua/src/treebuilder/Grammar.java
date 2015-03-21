package treebuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import treebuilder.symbols.ColumnValue;
import treebuilder.symbols.Terminal;
import treebuilder.symbols.Weight;
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
 * TODO weights not supported anymore
 */
public class Grammar implements Runnable {
	// The column values, weights and operands for all grammars
	private static ColumnValue[] KS;
	private static Weight[] WEIGTHS;
	private static final Operand[] OPERANDS = new Operand[]{new Multiplication(), new Substraction(), new Division(), new Sum(), new Power()};
	// The goal for all grammars
	private static double GOAL;
	// The other equations that should be fulfilled
	private static List<HashMap<String, Double>> otherEqs = new ArrayList<HashMap<String, Double>>();
	// The found solutions
	private static HashSet<Equation> solutions = new HashSet<Equation>();
	
	
	/**
	 * Set the Terminal values for this grammar
	 * 
	 * @param Ks
	 * 			The column values
	 * @param weights
	 * 			The weights
	 */
	public static void setColumnValues(List<double[]> multiInput, double[] weights) {
		// initialize the space for the other equations
		for(int i = 1; i < multiInput.size(); i++) {
			otherEqs.add(new HashMap<String, Double>());
		}
		
		// set first column values for all grammars
		KS = new ColumnValue[multiInput.get(0).length];
		for(int i = 0; i < KS.length; i++) {
			KS[i] = new ColumnValue(multiInput.get(0)[i], i);
			// add the column value to all other equations
			for(int j = 0; j < otherEqs.size(); j++) {
				otherEqs.get(j).put(KS[i].toString(), multiInput.get(j+1)[i]);
			}
		}
		
		// set weights for all grammars
		WEIGTHS = new Weight[weights.length];
		for(int i = 0; i < WEIGTHS.length; i++) {
			WEIGTHS[i] = new Weight(weights[i]);
		}
	}
	
	/**
	 * @return The first equation of the Grammar
	 */
	public static HashSet<Equation> getStartEquation() {
		HashSet<Equation> eqs = new HashSet<Equation>();
		for(Terminal K : KS) {
			eqs.add(new Equation(K, KS.length+1));
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
				Equation possibleNewEquation = Equation.createEquation(equation, operand, K, GOAL);
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
	public static boolean addPossibleSolution(Equation eq) {
		List<NonSplittable> nonSplittableParts = eq.getEquationParts();
		
		for(HashMap<String, Double> otherEq : otherEqs) { // check for every other input if equation is possible
			
			List<Double> values = new ArrayList<Double>();	// the values of every seperate part
			for(NonSplittable part : nonSplittableParts) {
				double value = ((Operand) part.getSymbols().get(0)).calculateValue(otherEq.get(((Terminal) part.getSymbols().get(1)).toString()));
				for(int i = 2; i < part.getSymbols().size(); i = i+2) {
					value = ((Operand) part.getSymbols().get(0)).calculateValue(value, otherEq.get(((Terminal) part.getSymbols().get(1)).toString()));				}
				values.add(value);
			}
			
			double result = nonSplittableParts.get(0).getFirstOperand().calculateValue(values.get(0));
			for(int i = 1; i < values.size(); i++) {
				result = nonSplittableParts.get(i).getFirstOperand().calculateValue(result, values.get(i));
			}
			
			if(result != GOAL) {
				return false;
			}
		}
		solutions.add(eq);
		return true;
	}
	
	/**
	 * @return the solotions found with this grammar
	 */
	public static HashSet<Equation> getSolutions() {
		return Grammar.solutions;
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
				Equation possibleNewEquation = Equation.createEquation(this.toExpand, operand, K, GOAL);
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