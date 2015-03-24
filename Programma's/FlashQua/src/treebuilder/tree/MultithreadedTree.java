package treebuilder.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import treebuilder.Equation;
import treebuilder.grammar.MultithreadedGrammar;
import exceptions.MaxLevelReachedException;
import exceptions.OutOfTimeException;

public class MultithreadedTree extends Tree {
	
	protected static Thread[] threads;
	protected static int THREADSIZE = 20;
	
	@Override
	public void expand(long startTime, int deadline, int maxlevel) throws OutOfTimeException, MaxLevelReachedException, InterruptedException {
		while(true) {
			alreadyFound = new HashSet<Equation>();
			List<Equation> previousLevel = new ArrayList<Equation>(tree.get(level-1));
			threads = new Thread[THREADSIZE];
			MultithreadedGrammar[] grammars = new MultithreadedGrammar[THREADSIZE];
			for (; equation < previousLevel.size(); equation++) {
				if((System.currentTimeMillis() - startTime) < deadline || deadline < 0) {
					Equation eq = previousLevel.get(equation);
					grammars[equation%THREADSIZE] = new MultithreadedGrammar(eq);
					threads[equation%THREADSIZE] = new Thread(grammars[equation%THREADSIZE]); // start a new thread to expand the equation
					threads[equation%THREADSIZE].start();
					if(equation%THREADSIZE+1 == THREADSIZE) {
						for(int i = 0; i < threads.length; i++) {
							threads[i].join();
							alreadyFound.addAll(grammars[i].found);
						}
						threads = new Thread[THREADSIZE];
					}
				} else {
					throw new OutOfTimeException("Out of time");
				}
			}
			for(int i = 0; i < threads.length; i++) {
				if(threads[i] != null) {
					threads[i].join();
					alreadyFound.addAll(grammars[i].found);
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
