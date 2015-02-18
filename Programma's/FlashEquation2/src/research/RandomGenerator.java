package research;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import master.normal.ObjectEvaluate;
import tree.Equation;
import tree.Grammar;
import tree.NonTerminal;
import tree.Operand;
import tree.Symbol;
import tree.Terminal;

public class RandomGenerator {
	
	private static Equation lastGeneratedEquation = null;
	private static double lastGeneratedGoal = 0;
	
	/**
	 * Generates list of random numbers between startRange and endRange
	 * Every row represents column values with the last value of the row the goal value
	 * @param length
	 * 			The number of column values (inclusive goal value)
	 * 			Has to be > 1
	 * @param nrOfExamples 
	 * 			The number of rows
	 * 			Has to be > 0
	 * @param minimum
	 * 			The start of the range for the random numbers
	 * 			Has to be >= 0
	 * 			(Does not include the goal range)
	 * @param maximum 
	 * 			The end of the range for the random numbers
	 * 			(Does not include the goal range)
	 * @return
	 * 			A two dimensional list containing numbers, the columns represent KN and the Goal
	 */
	public static List<List<Double>> generateCertainAllK(int length, int nrOfExamples, int minimum, int maximum) {
		List<List<Double>> result = new ArrayList<List<Double>>();
		List<Operand> possibleOperands = Grammar.getPossibleOperands(); // possible operands
		List<Double> numbers = new ArrayList<Double>(); // the numbers of a row
		List<Operand> operands = new ArrayList<Operand>(); // used operands in first row
		double number = 0;
		Random rn = new Random();
		List<Symbol> symbols = new ArrayList<Symbol>();
		boolean firstExampleOK = false;
		while(!firstExampleOK) {
			possibleOperands = Grammar.getPossibleOperands(); // possible operands
			numbers = new ArrayList<Double>(); // the numbers of a row
			operands = new ArrayList<Operand>(); // used operands in first row
			number = 0;
			symbols = new ArrayList<Symbol>();

			
			// generate the first row
			for(int l = 0; l < length-2; l++) {
				number = rn.nextInt((maximum - minimum) + 1) + minimum;
				numbers.add(number);
				symbols.add(new Terminal("K"+l, number));
				Operand operand = possibleOperands.get(rn.nextInt(possibleOperands.size()));
				operands.add(operand);			
				symbols.add(operand);
			}
			// last number of first equation
			number = rn.nextInt((maximum - minimum) + 1) + minimum;
			numbers.add(number);
			symbols.add(new Terminal("K"+ (length-2), number));
	
			// equation of the first row
			Equation eq = new Equation(symbols);
			
			// set the first row as last generated equation
			lastGeneratedEquation = eq;
			
			// evaluate the value of the equation and add it as Goal of the row
			double goal = ObjectEvaluate.evaluateTerminalEquation(eq);
			numbers.add(goal);
			
			// set last generated goal equal to goal of first row
			lastGeneratedGoal = goal;
			
			double checkGoal = goal;
			checkGoal = checkGoal*100000;
			checkGoal = (int) (checkGoal%10);
			if(checkGoal == 0) {
				firstExampleOK = true;
			} else {
				firstExampleOK = false;
			}
		}
		// add first row to result
		result.add(numbers);
		
		
		// generate the next rows
		for(int i = 1; i < nrOfExamples; i++) {
			List<Double> nextNumbers = new ArrayList<Double>();
			boolean exampleOK = false;
			while(!exampleOK) {
				// reset symbols and numbers
				List<Symbol> nextSymbols = new ArrayList<Symbol>();
				nextNumbers = new ArrayList<Double>();
				
				// generate the i'th row
				for(int l = 0; l < length - 2; l++) {
					number = rn.nextInt((maximum - minimum) + 1) + minimum;
					nextNumbers.add(number);
					nextSymbols.add(new Terminal("K"+l, number));
					nextSymbols.add(operands.get(l));
				}
				// last number of i'th equation
				number = rn.nextInt((maximum - minimum) + 1) + minimum;
				nextNumbers.add(number);
				nextSymbols.add(new Terminal("K"+ (length-2), number));
				
				// equation of the first row
				Equation nextEq = new Equation(nextSymbols);
				
				// evaluate the value of the equation and add it as Goal of the row
				double nextGoal = ObjectEvaluate.evaluateTerminalEquation(nextEq);
				nextNumbers.add(nextGoal);
				
				double checkGoal = nextGoal;
				checkGoal = checkGoal*100000;
				checkGoal = (int) (checkGoal%10);
				if(checkGoal == 0) {
					exampleOK = true;
				} else {
					exampleOK = false;
				}		
			}
			// add first row to result
			result.add(nextNumbers);
		}
		
		// return the result
		return result;
	}
	
	/**
	 * Generates list of random numbers between startRange and endRange
	 * Every row represents column values with the last value of the row the goal value
	 * @param length
	 * 			The number of column values (inclusive goal value)
	 * 			Has to be > 1
	 * @param nrOfExamples 
	 * 			The number of rows
	 * 			Has to be > 0
	 * @param minimum
	 * 			The start of the range for the random numbers
	 * 			Has to be >= 0
	 * 			(Does not include the goal range)
	 * @param maximum 
	 * 			The end of the range for the random numbers
	 * 			(Does not include the goal range)
	 * @return
	 * 			A two dimensional list containing numbers, the columns represent KN and the Goal
	 */
	public static List<List<Double>> generateRealRandom(int length, int nrOfExamples, int minimum, int maximum) {
		List<List<Double>> result = new ArrayList<List<Double>>();
		Random rn = new Random();

		for(int i = 0; i < nrOfExamples; i++) {
			List<Double> row = new ArrayList<Double>();
			for(int j = 0; j < length; j++) {
				row.add((double) (rn.nextInt((maximum - minimum) + 1) + minimum));
			}
			result.add(row);
		}
		
		List<Symbol> s = new ArrayList<Symbol>();
		s.add(new NonTerminal("Unknown"));
		lastGeneratedEquation = new Equation(s);
		
		// return the result
		return result;
	}
	
	/**
	 * @return
	 * 			The last random generated equation
	 */
	public static String getLastGeneratedEquation() {
		String result = lastGeneratedEquation + " => for first equation this means: ";
		for(Symbol s : lastGeneratedEquation.getListOfSymbols()) {
			if(s.isTerminal()) {
				result += "" + ((Terminal) s).getValue();
			} else {
				result += s.toString();
			}
		}
		
		result += " = " + lastGeneratedGoal;		
		return result;
	}
}
