package tree;

import java.util.ArrayList;
import java.util.List;

public class Tree {

	// Filenames
	public final static String FILENAME_P = "pruned";
	public final static String FILENAME_NP = "not_pruned";

	// The tree itself
	private List<List<Equation>> tree = new ArrayList<List<Equation>>();

	public Tree(int nrOfLevels, boolean prune) {
		// set the start equation on the first level
		tree.add(Grammar.getStartEquation());
	}

	/**
	 * Expands the tree to the next level
	 */
	public void expand() {
		tree.add(new ArrayList<Equation>());
		for (Equation eq : tree.get(tree.size() - 2))
			tree.get(tree.size()-1).addAll(Grammar.expand(tree.get(tree.size() - 1), eq));
	}

	public List<List<Equation>> getTree() {
		return this.tree;
	}

	@Override
	public String toString() {
		String result = "";
		for (List<Equation> level : this.tree) {
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
