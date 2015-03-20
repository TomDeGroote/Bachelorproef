package treebuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import treebuilder.Equation;
import treebuilder.Grammar;
import exceptions.MaxLevelReachedException;
import exceptions.OutOfTimeException;

public class Tree {

	// Filenames
	public final static String FILENAME_P = "pruned";
	public final static String FILENAME_NP = "not_pruned";
	
	private static int level = 0;
	private static int equation = 0;

	// The tree itself
	private List<HashSet<Equation>> tree = new ArrayList<HashSet<Equation>>();

	public Tree() {
		// set the start equation on the first level
		tree.add(Grammar.getStartEquation());
	}

	/**
	 * Expands the tree to the next level
	 * @throws OutOfTimeException 
	 * @throws MaxLevelReachedException 
	 */
	public void expand(long startTime, int deadline, int maxlevel) throws OutOfTimeException, MaxLevelReachedException {
		while(true) {
			tree.add(new HashSet<Equation>());
			List<Equation> hashEquations = new ArrayList<Equation>(tree.get(level));
			for (; equation < tree.get(level).size(); equation++) {
				if((System.currentTimeMillis() - startTime) < deadline || deadline < 0) {
					Equation eq =hashEquations.get(equation);
					Grammar.expand(tree.get(tree.size()-1), eq);
				} else {
					throw new OutOfTimeException("Out of time");
				}
			}
			equation = 0;
			level++;
			if(level > maxlevel-2 && maxlevel > 0) {
				throw new MaxLevelReachedException("Maximum level reached");
			}
		}
	}

	/**
	 * @return
	 * 		The tree
	 */
	public List<HashSet<Equation>> getTree() {
		return this.tree;
	}

	@Override
	public String toString() {
		String result = "";
		for (HashSet<Equation> level : this.tree) {
			for (Equation eq : level) {
				if (eq.isPruned()) {
					result += eq.toString().toLowerCase() + "  ";
				} else {
					result += eq.toString() + "  ";
				}
			}
			result += "\n";
		}
		return result;
	}
}
