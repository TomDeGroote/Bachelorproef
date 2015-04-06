package treebuilder.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import treebuilder.Equation;
import treebuilder.NonSplittable;
import treebuilder.comparators.Comparator;
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
 * TODO choose most difficult equation
 * @author Jeroen & Tom
 */
public abstract class Grammar {
	// possible operands
	protected static final Operand[] OPERANDS = new Operand[]{new Multiplication(), new Substraction(), new Division(), new Sum(), new Power()};

	// first equation
	protected static ColumnValue[] KS;

	protected static Weight[] WEIGTHS;
	protected static Comparator comparator;
	protected static double GOAL;

	// other equations
	protected static List<HashMap<String, Double>> otherEqs = new ArrayList<HashMap<String, Double>>();
	protected static List<Double> otherGoals = new ArrayList<Double>();
	protected static List<Comparator> otherComparators = new ArrayList<Comparator>();
	
	// The found solutions
	protected static HashSet<Equation> solutions = new HashSet<Equation>();
	
	
	/**
	 * Set the Terminal values for this grammar
	 * 
	 * @param Ks
	 * 			The column values
	 * @param weights
	 * 			The weights
	 */
	public static void setColumnValues(List<double[]> multiInput, double[] weights, List<Comparator> comparators) {
		// initialize the space for the other equations
		for(int i = 1; i < multiInput.size(); i++) {
			otherEqs.add(new HashMap<String, Double>());
		}
		
		// set first column values for all grammars
		KS = new ColumnValue[multiInput.get(0).length-1];
		for(int i = 0; i < KS.length; i++) {
			KS[i] = new ColumnValue(multiInput.get(0)[i], i);
			// add the column value to all other equations
			for(int j = 0; j < otherEqs.size(); j++) {
				otherEqs.get(j).put(KS[i].toString(), multiInput.get(j+1)[i]);
			}
		}
		// set GOALs
		GOAL = multiInput.get(0)[multiInput.get(0).length-1];
		for(int i = 1; i < multiInput.size(); i++) {
			otherGoals.add(multiInput.get(i)[multiInput.get(i).length-1]);
		}
		
		// set weights for all grammars
		WEIGTHS = new Weight[weights.length];
		for(int i = 0; i < WEIGTHS.length; i++) {
			WEIGTHS[i] = new Weight(weights[i], KS.length+i);
		}

		// set comparator first equation
		comparator = comparators.get(0);
		for(int i = 0; i < otherEqs.size(); i++) {
			if(i >= comparators.size()) {
				otherComparators.add(comparators.get(0));
			} else {
				otherComparators.add(comparators.get(i));
			}
		}	
	}
	
	/**
	 * @return The first equation of the Grammar
	 */
	public static HashSet<Equation> getStartEquation() {
		HashSet<Equation> eqs = new HashSet<Equation>();
		for(Terminal K : KS) {
			int terminalCounterSpace = KS.length+WEIGTHS.length;
			eqs.add(new Equation(K, terminalCounterSpace));
		}
		for(Terminal K : WEIGTHS) {
			int terminalCounterSpace = KS.length+WEIGTHS.length;
			eqs.add(new Equation(K, terminalCounterSpace));
		}
		return eqs;
	}
	
	/**
	 * Add possible solutions
	 */
	public static boolean addPossibleSolution(Equation eq) {
		List<NonSplittable> nonSplittableParts = eq.getEquationParts();
		for(int j = 0; j < otherEqs.size(); j++) { // check for every other input if equation is possible
			HashMap<String, Double> otherEq = otherEqs.get(j);
			List<Double> values = new ArrayList<Double>();	// the values of every seperate part
			for(NonSplittable part : nonSplittableParts) {
				double value = 0.0;
				for(int i = 0; i < part.getSymbols().size(); i += 2) {
					if(((Terminal) part.getSymbols().get(i+1)).isWeight()) {
						value = ((Operand) part.getSymbols().get(i)).calculateValue(value, ((Terminal) part.getSymbols().get(i+1)).getValue());
					} else {
						value = ((Operand) part.getSymbols().get(i)).calculateValue(value, otherEq.get(((Terminal) part.getSymbols().get(i+1)).toString()));
					}
				}
				values.add(value);
			}
			
			double result = 0.0;
			for(double v : values) {
				result += v;
			}
			
			if(!otherComparators.get(j).compareOK(result, otherGoals.get(j))) {
				return false;
			}
		}
		solutions.add(eq);
		return true;
	}
	

	/**
	 * Calculates the value of column values based on a given equation
	 * 
	 * @param eq
	 * 			The given equation
	 * @param ks
	 * 			The column values to be used
	 * @return The result
	 */
	public static double calculateValue(Equation eq, double[] ks) {
		List<NonSplittable> nonSplittableParts = eq.getEquationParts();
		double result = 0.0;
		List<Double> values = new ArrayList<Double>();	// the values of every seperate part
		for(NonSplittable part : nonSplittableParts) {
			double value = 0.0;
			for(int i = 0; i < part.getSymbols().size(); i += 2) {
				if(((Terminal) part.getSymbols().get(i+1)).isWeight()) {
					value = ((Operand) part.getSymbols().get(i)).calculateValue(value, ((Terminal) part.getSymbols().get(i+1)).getValue());
				} else {
					value = ((Operand) part.getSymbols().get(i)).calculateValue(value, ks[((Terminal) part.getSymbols().get(i+1)).getNumber()]);
				}
			}
			values.add(value);
		}
		
		for(double v : values) {
			result += v;
		}
		return result;
	}
	
	/**
	 * @return the solotions found with this grammar
	 */
	public static HashSet<Equation> getSolutions() {
		return Grammar.solutions;
	}
	
	/**
	 * @return the possible operands 
	 */
	public static Operand[] getPossibleOperands() {
		return OPERANDS;
	}	
	
	/**
	 * @return the nr of column values
	 */
	public static int getNrOfKs() {
		return KS.length;
	}
}