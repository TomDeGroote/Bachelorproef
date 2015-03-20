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
	
	private static Thread[] threads;

	// The tree itself
	private List<HashSet<Equation>> tree = new ArrayList<HashSet<Equation>>();
	
	public static HashSet<Equation> alreadyFound;
	public Tree() {
		// set the start equation on the first level
		tree.add(Grammar.getStartEquation());
		level++;
	}

	/**
	 * Expands the tree to the next level
	 * @throws OutOfTimeException 
	 * @throws MaxLevelReachedException 
	 * @throws InterruptedException 
	 */
	public void expand(long startTime, int deadline, int maxlevel) throws OutOfTimeException, MaxLevelReachedException, InterruptedException {
		while(true) {
			alreadyFound = new HashSet<Equation>();
			List<Equation> previousLevel = new ArrayList<Equation>(tree.get(level-1));
			threads = new Thread[previousLevel.size()];
			for (; equation < previousLevel.size(); equation++) {
				if((System.currentTimeMillis() - startTime) < deadline || deadline < 0) {
					Equation eq = previousLevel.get(equation);
					threads[equation] = new Thread(new Grammar(eq)); // start a new thread to expand the equation
					threads[equation].start();
					Grammar.expand(tree.get(tree.size()-1), eq);
				} else {
					throw new OutOfTimeException("Out of time");
				}
			}
			for(int i = 0; i < threads.length; i++) {
				threads[i].join();
			}
			equation = 0;
			level++;
			if(level > maxlevel-2 && maxlevel > 0) {
				throw new MaxLevelReachedException("Maximum level reached");
			}
			tree.add(alreadyFound);
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
