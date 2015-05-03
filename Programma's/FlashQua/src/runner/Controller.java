package runner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import exceptions.MaxLevelReachedException;
import exceptions.OutOfTimeException;
import treebuilder.Equation;
import treebuilder.comparators.Comparator;
import treebuilder.grammar.Grammar;
import treebuilder.tree.MultithreadedTree;
import treebuilder.tree.Tree;

public class Controller {

	private static Equation bestEquation;
	private static int nrOfSolutions;
	private static HashSet<Equation> allEquations;
	
	// Deadline and maxlevel parameters
	private final static int DEADLINE = 20000;
	private final static int MAXLEVEL = -1;
	
	// the weights to be used (basically constants)
	private final static double[] WEIGHTS = new double[]{1.0, 2.0, 3.0, 5.0, 7.0};	

	private static Tree tree;
	
	public void excecute(List<double[]> input, Object comparator) {		
		Comparator comp = convertToComparator(comparator);
		List<Comparator> comparators = new ArrayList<>();
		comparators.add(comp);
		Grammar.setColumnValues(input, WEIGHTS, comparators);
		expandTree();
		allEquations = Grammar.getSolutions();
		nrOfSolutions = allEquations.size();
		bestEquation = getBestEquation(allEquations);
	}

	/**
	 * Converts an object (String to a comparator)
	 * @param comparator
	 * 			The object to convert
	 * @return
	 * 			The comparator
	 */
	private Comparator convertToComparator(Object comparator) {
		return Comparator.parseComparator((String) comparator);
	}

	/**
	 * Expands the tree
	 */
	private void expandTree() {
		long start = System.currentTimeMillis();
		if(tree == null) {
			tree = new MultithreadedTree();
		}
		try {
			tree.expand(start, DEADLINE, MAXLEVEL);
		} catch(OutOfTimeException e) {
			System.out.println(e.getMessage());
		} catch(MaxLevelReachedException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Done: " + (System.currentTimeMillis()-start));
	}
	
	/**
	 * @return the best equation
	 */
	public Equation getBestEquation() {
		return bestEquation;
	}
	
	/**
	 * @return number of equations
	 */
	public int getNrOfSolutions() {
		return nrOfSolutions;
	}
	
	/**
	 * @return all equations
	 */
	public HashSet<Equation> getAllEquations() {
		return allEquations;
	}
	
	/**
	 * @param numbers
	 * 			The numbers to use in the best equation
	 * @return the result of the equation if these numbers are filled in
	 */
	public double calculateAccordingToBestEquation(double[] numbers) {
		return Grammar.calculateValue(getBestEquation(), numbers);
	}
	
	/**
	 * @param allEquations
	 * 			The equations in which to search for the best equation
	 * @return The best equation
	 */
	private Equation getBestEquation(HashSet<Equation> allEquations) {
		Equation best = (Equation) allEquations.toArray()[0];
		for(Equation eq : allEquations) {
			if(best.getEquationQuality() < eq.getEquationQuality()) {
				best = eq;
			} else if(best.getEquationQuality() == eq.getEquationQuality() && best.size() > eq.size()) {
				best = eq;
			}
		}
		return best;
	}
}
