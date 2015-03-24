package treebuilder.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import treebuilder.Equation;
import treebuilder.grammar.Grammar;
import exceptions.MaxLevelReachedException;
import exceptions.OutOfTimeException;

public abstract class Tree {
	
	protected static int level = 0;
	protected static int equation = 0;

	// The tree itself
	protected List<HashSet<Equation>> tree = new ArrayList<HashSet<Equation>>();
	
	protected static HashSet<Equation> alreadyFound;
	
	public Tree() {
		// set the start equation on the first level
		tree.add(Grammar.getStartEquation());
		level++;
	}

	/**
	 * Expands the tree until the maximum level is reached or the deadline is reached
	 * 
	 * @param startTime
	 * 			The time the program started in ms
	 * @param deadline
	 * 			How long the program can run in ms
	 * @param
	 * 			The maximum level this tree should expand to
	 * @throws OutOfTimeException 
	 * @throws MaxLevelReachedException 
	 * @throws InterruptedException 
	 */
	public abstract void expand(long startTime, int deadline, int maxlevel) throws OutOfTimeException, MaxLevelReachedException, InterruptedException;

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
				result += eq.toString() + "  ";
			}
			result += "\n";
		}
		return result;
	}
	
	/**
	 * @return The equation that are yet found on the current level
	 */
	public static HashSet<Equation> getAlreadyFound() {
		return Tree.alreadyFound;
	}
}
