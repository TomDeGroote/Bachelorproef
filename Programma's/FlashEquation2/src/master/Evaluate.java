package master;

import java.util.*;

import com.fathzer.soft.javaluator.DoubleEvaluator;

import tree.*;

/**
 * The evaluate class
 * @author Jeroen
 *
 */
public class Evaluate{

	//List of all examples until now.
	private List<List<Terminal>> allExamples;
	//Tree on which the entire evualate class is based.
	private final Tree TREE;
	//Main example
	private final List<Terminal> MAIN_EXAMPLE;
	//List of solutions until the current point in time.
	private List<Equation> solutions;
	//Counters to keep the correct position in the TREE.
	private int levelCounter;
	private int elementCounter; 

	/**
	 * Creates the evaluate class from the variable tree.
	 * @param tree
	 */
	public Evaluate(Tree tree, ArrayList<Terminal> mainExample){
		//Sets the given tree as final value TREE
		this.TREE = tree;
		this.MAIN_EXAMPLE = mainExample;

	}

	public void evaluate(ArrayList<Terminal> newExample) {
		//Adding the new example to the list of already known examples.
		allExamples.add(newExample);
		
	}

	public boolean checkAgainstAllExamples(Equation eq) {
		List<String> listOfParts = this.splitEquation(eq);
		DoubleEvaluator evaluator = new DoubleEvaluator();
		//If no examples then it is true.
		Boolean correctForAll = true;
		String currentEQ;
		String tempEQ;
		for(List<Terminal> example: getAllExamples()) {
			currentEQ = "";
			Terminal goalTerminal = example.remove(example.size()-1);
			for(String str : listOfParts) {
				if(str.length() == 1 && !Grammar.isOperand(str)) {
					for(Terminal ter : example) {
						if(str.equals(ter.toString()))
							currentEQ += ter.getValue().toString();
					}
				} else if(str.length() == 1 && Grammar.isOperand(str)) {
					currentEQ += str.toString();
				}
				else {
					String[] splitStr = str.split("");
					tempEQ = "";
					for(String tempStr : splitStr) {
						for(Terminal ter : example) {
							if(tempStr.equals(ter.toString()))
								tempEQ += ter.getValue().toString();
						}	
						tempEQ += str.toString();
					}
					currentEQ += evaluator.evaluate(tempEQ);
				}
			}
			if(goalTerminal.getValue() != evaluator.evaluate(currentEQ))
				correctForAll = false;
		}
		return correctForAll;
	}

	/**
	 * @param 
	 * 		The equation to be split
	 * @return
	 * 		A list of strings, the different strings represent the different parts of the equation
	 * 		f.e. equation : E*E + E - E becomes {"E*E", "+" , "E", "-" , "E"}
	 */
	public List<String> splitEquation(Equation eq) {
		List<String> split = new ArrayList<String>();
		String temporary = "";
		for(Symbol symbol : eq.getListOfSymbols()) {
			if(symbol.isTerminal()) {
				temporary += symbol.toString();
			} else if(symbol.isOperand()) {
				if(!((Operand) symbol).isSplitable()) {
					temporary += symbol.toString();
				} else {
					split.add(temporary);
					split.add(symbol.toString());
					temporary = "";
				} 
			} else if(symbol.isNonTerminal()) {
				// TODO non-terminals not supported, are we going use them? or say if nonTerminal becomes Terminal
				temporary += symbol.toString();
			}
		}	
		split.add(temporary); // adds the final term
		return split;
	}


	/**
	 * 
	 * @return
	 *  currentList of all Examples
	 */
	private List<List<Terminal>> getAllExamples(){
		return this.allExamples;
	}

}