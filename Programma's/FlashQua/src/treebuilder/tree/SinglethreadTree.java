package treebuilder.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import treebuilder.Equation;
import treebuilder.grammar.SinglethreadedGrammar;
import exceptions.MaxLevelReachedException;
import exceptions.OutOfTimeException;

public class SinglethreadTree extends Tree {

	@Override
	public void expand(long startTime, int deadline, int maxlevel) throws OutOfTimeException, MaxLevelReachedException {
		while(true) {
			alreadyFound = new HashSet<Equation>();
			List<Equation> previousLevel = new ArrayList<Equation>(tree.get(level-1));
			for (; equation < previousLevel.size(); equation++) {
				if((System.currentTimeMillis() - startTime) < deadline || deadline < 0) {
					Equation eq = previousLevel.get(equation);
					alreadyFound.addAll(SinglethreadedGrammar.expand(alreadyFound, eq));
				} else {
					throw new OutOfTimeException("Out of time");
				}
			}
			equation = 0;
			level++;
			tree.add(alreadyFound);
			if(level > maxlevel-1 && maxlevel > 0) {
				throw new MaxLevelReachedException("Maximum level reached");
			}
		}
	}
}
