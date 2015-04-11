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
			MultithreadedGrammar[] grammarThreads = new MultithreadedGrammar[THREADSIZE];
			
			// expand the equation in a multithreaded way
			for (; equation < previousLevel.size(); equation++) {
				// check if the program is overdue
				if((System.currentTimeMillis() - startTime) < deadline || deadline < 0) {
					Equation eq = previousLevel.get(equation);
					
					// while the number of threads running is smaller than THREADSIZE generate threads else wait
					grammarThreads[equation%THREADSIZE] = new MultithreadedGrammar(eq);
					threads[equation%THREADSIZE] = new Thread(grammarThreads[equation%THREADSIZE]); // start a new thread to expand the equation
					threads[equation%THREADSIZE].start();
					
					// wait for threads to be finished
					if(equation%THREADSIZE+1 == THREADSIZE) {
						for(int i = 0; i < threads.length; i++) {
							threads[i].join();
							alreadyFound.addAll(grammarThreads[i].getFound());
						}
						threads = new Thread[THREADSIZE];
					}
				} else {
					throw new OutOfTimeException("Out of time");
				}
			}
			// wait for the treads of this level to be finished
			for(int i = 0; i < threads.length; i++) {
				if(threads[i] != null) {
					threads[i].join();
					alreadyFound.addAll(grammarThreads[i].getFound());
				}
			}
			// reset the equation place and increase the level depth
			equation = 0;
			level++;
			
			// add the found equations to already found
			tree.add(alreadyFound);
			
			// check if maximum level is reached
			if(level > maxlevel-1 && maxlevel > 0) {
				//throw new MaxLevelReachedException("Max Level");//TODO"Maximum level reached");
				break;
			}
		}
	}
}
