package master;

import java.util.*;
import tree.*;

/**
 * The evaluate class
 * @author Jeroen and Tom
 *
 */
public class Evaluate{

	//List of all examples until now.
	private List<List<Double>> allExamples;
	//Tree on which the entire evualate class is based.
	private final List<List<Equation>> TREE;
	//Main example
	private final List<Double> MAIN_EXAMPLE;
	private final double GOAL;
	//List of solutions until the current point in time.
	private List<String> bufferSolutions;
	//Counters to keep the correct position in the TREE.
	private int levelCounter = 0;
	private int elementCounter = 0; 
	//The HashMap with all equations that are already solved.
	private HashMap<String,HashMap<Double,List<String>>> alreadySolved;
	//HashMap to find the right string to the example value.
	private HashMap<Double,List<String>> nameByValue;

	/**
	 * Creates the evaluate class from the variable tree.
	 * @param tree
	 */
	public Evaluate(Tree tree, ArrayList<Double> mainExample){
		//Sets the given tree as final value TREE
		this.TREE = tree.getTree();
		this.GOAL = mainExample.remove(mainExample.size());
		this.MAIN_EXAMPLE = mainExample;
		this.alreadySolved = new HashMap<String,HashMap<Double,List<String>>>();
		this.nameByValue = new HashMap<Double,List<String>>();
		for(int i = 0; i < mainExample.size(); i++) {
			if(!nameByValue.containsKey(mainExample.get(i))) {
				ArrayList<String> newList = new ArrayList<String>();
				newList.add("K"+(i+1));
				nameByValue.put(mainExample.get(i), newList);
			} else {
				nameByValue.get(mainExample.get(i)).add("K"+(i+1));
			}
		}
	}

	/**
	 * Continues the evaluation and addes a newExample if provided.
	 * @param newExample
	 * 	A list of doubles that contains the 'K'-values and goal value for a new example
	 * 
	 */
	public void evaluate(ArrayList<Double> newExample) {
		//Adding the new example to the list of already known examples.
		if(newExample != null)
			allExamples.add(newExample);

		//Finds the latest equation that has not yet been evaluated.
		for(;levelCounter < TREE.size(); levelCounter++) {
			List<Equation> level = TREE.get(levelCounter);
			for(;elementCounter < level.size(); elementCounter++) {
				Equation currentEquation = level.get(elementCounter);
				//Gets all possible options for a certain equation.
				HashMap<Double,List<String>> options = evaluateEquation(currentEquation.toString());
				//Add these options to the alreadySolved hashmap for easier lookup.
				alreadySolved.put(currentEquation.toString(),options);
			}

		}
	}

	/**
	 * Finds all possible options there are for a certain equation 
	 * by filling in all possible values.
	 * 
	 * @param stringEQ
	 * 	The equation that is to be evaluated represented as a string
	 * 
	 * @return 
	 * 	A hashmap containing all possible combinations for all possible values 
	 * 	for the given equation
	 */
	public HashMap<Double,List<String>> evaluateEquation(String stringEQ) {
		HashMap<Double,List<String>> options = new HashMap<Double,List<String>>();
		//If the length of the equation is equal to 1, then fill in possible values.
		//In this case het K-values of the main example.
		if(stringEQ.length() == 1) {
			for(double current : MAIN_EXAMPLE) {
				List<String> currentStrings = nameByValue.get(current);
				for(String name : currentStrings) {
					if(options.containsKey(current)){
						options.get(current).add(name);
					} else {
						ArrayList<String> newList = new ArrayList<String>();
						newList.add(name);
						options.put(current, newList);
					}
				}
			}
		} else {
			//The length of the equation is an odd number higher than 1.
			//Spit the string into 3 parts.
			List<String> splittedEquation = splitStringInThreeParts(stringEQ);
			String operand = splittedEquation.get(1);
			//For each of the substrings calculate all possible combinations.
			//These substrings are known in the alreadySolved HashMap.
			for(double value1 : alreadySolved.get(splittedEquation.get(0)).keySet()) {

				for(String eq1 : alreadySolved.get(splittedEquation.get(0)).get(value1)) {

					for(double value2 : alreadySolved.get(splittedEquation.get(2)).keySet()) {

						for(String eq2 : alreadySolved.get(splittedEquation.get(2)).get(value2)) {

							//Calculate the totalValue of the substrings in function of the operand.
							double totalValue = Grammar.getValue(value1, operand, value2);
							//Generate the string of the new equation.
							String newEquation = eq1+operand+eq2;
							//If the value that is calculated == GOAL and it's correct for the other 
							//examples, then add to bufferSolutions.
							if(totalValue == GOAL && checkAgainstAllExamples(newEquation)) {
								bufferSolutions.add(newEquation);
							}
							//Add the value and equation combination to the possible options.
							if(options.containsKey(totalValue)){
								options.get(totalValue).add(newEquation);
							} else {
								ArrayList<String> newList = new ArrayList<String>();
								newList.add(newEquation);
								options.put(totalValue, newList);
							}

						}

					}

				}

			}

		}
		return options;
	}

	/**
	 * 	Checks the equation versus all of the examples excluding the main example.
	 * 
	 * @param equationStr
	 * 	The equation in string form that has to be checked against all examples 
	 * 	provided so far by the user/program, except the main example.
	 * @return 
	 * 	True 	if the equation is correct for all examples or when there are no examples
	 *  False 	if the equation is false for one of the examples
	 */
	public boolean checkAgainstAllExamples(String equationStr) {
		//Split the entire equation on all splittable operands
		List<String> listOfParts = splitStringSplittable(equationStr);
		//If there are no examples, then it is true.
		Boolean correctForAll = true;
		//Go over all the examples that are given
		for(List<Double> example: getAllExamples()) {
			HashMap<String,Double> valueByName = new HashMap<String,Double>();
			ArrayList<String> listOfValuesAndOperands = new ArrayList<String>();
			//Getting the goal from the example
			double goal = example.remove(example.size());
			//Combining the K values and their corresponding "name"
			for(int i = 0; i < example.size(); i++) {
				valueByName.put(("K"+(i+1)), example.get(i));
			}
			//init
			double tempValue = 0;
			//Go over each part of the equation individually 
			for(String str : listOfParts) {
				//If the string is of the size < 3 and it's not an operand
				//Then add the corresponding value as a string to the list
				if(str.length() < 3 && !Grammar.isOperand(str)) {
					listOfValuesAndOperands.add(valueByName.get(str).toString());
				} else if(Grammar.isOperand(str)) {
					//The string is an operand. Add it to the list
					listOfValuesAndOperands.add(str);
				}
				else {
					//The string contains multiple variables.
					//Split the string in seperate parts in the form of [variable,operand,variable,...]
					List<String> splitStr = new ArrayList<String>(Arrays.asList(str.split("(?<=[+*/-])|(?=[+*/-])")));
					//Evaluate this string and add it to the list of values and operands
					for(int i = 0; i < splitStr.size(); i+=2) {
						if(i == 0) {
							//Evaluate the first string
							tempValue = valueByName.get(splitStr.get(i));
						} else {
							//The previous string is an operand
							//create a new tempValue from the current tempValue in combination of the operand and the next variable
							double val = valueByName.get(splitStr.get(i));
							tempValue = Grammar.getValue(tempValue, splitStr.get(i-1), val);
						}
					}
					listOfValuesAndOperands.add(tempValue+"");
				}
			}
			//Evaluate the entire equation without any unsplittable operands.
			double solution = 0;
			for(int i = 0; i < listOfValuesAndOperands.size(); i+=2) {
				if(i == 0) {
					solution = Double.parseDouble(listOfValuesAndOperands.get(i));
				} else {
					double val = Double.parseDouble(listOfValuesAndOperands.get(i));
					solution = Grammar.getValue(solution, listOfValuesAndOperands.get(i-1), val);
				}
			}
			//If the solution generated by filling in the corresponding values does not give the goal value,
			//Then the given equation is not correct for all the examples.
			if(goal != solution) {
				correctForAll = false;
				return correctForAll;
			}
		}
		return correctForAll;
	}

	/**
	 * Splits the equation in three seperate parts. 
	 * 
	 * @param 
	 * 		The equation to be split
	 * @return
	 * 		A list of strings, the different strings represent the different parts of the equation
	 * 		f.e. equation : E*E + E - E becomes {"E*E", "+" , "E"}
	 */
	public List<String> splitStringInThreeParts(String str) {
		//Initialize
		List<String> split = new ArrayList<String>();
		String firstPart = "";
		String operandPart = "";
		String secondPart = "";

		int i = 0;
		for(; i < str.length(); i++) {
			String currentSymbol = str.substring(i,i);

			//if operand is splittable, then split the string at that position.
			if(Grammar.isSplittableOperand(currentSymbol)){
				split.add(firstPart);
				operandPart += currentSymbol;
				split.add(operandPart);
				break;
			} else {
				firstPart += currentSymbol;
			}
		}

		for(i++; i < str.length(); i++) {
			secondPart += (str.substring(i,i));
		}

		//If the secondPart is empty, meaning there was no splittable operand.
		//Then the first operand becomes a splittable operand.
		if(secondPart.isEmpty()) {
			firstPart = str.substring(0, 0);
			operandPart = str.substring(1,1);
			secondPart = str.substring(2);

			split.add(firstPart);
			split.add(operandPart);
			split.add(secondPart);
		} 

		return split;
	}

	/**
	 * Splits a given string on all possible splittable operands.
	 * 
	 * @param str
	 * 	string to be splitten
	 * @return
	 * 	List of strings, which are either an operand or an equation.
	 */
	public List<String> splitStringSplittable(String str) {
		List<String> split = new ArrayList<String>();
		String tempStr = "";
		for(int i = 0; i < str.length(); i++) {
			String currentSymbol = str.substring(i,i);

			if(Grammar.isSplittableOperand(currentSymbol)){
				split.add(tempStr);
				split.add(str);
				tempStr = "";
			} else {
				tempStr += str;
			}
		}

		return split;
	}


	/**
	 * 
	 * @return
	 *  currentList of all Examples
	 */
	private List<List<Double>> getAllExamples(){
		return this.allExamples;
	}

}