package tree;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Given a list of pruning rules it reduces the amount of valid equations in a certain list
 * @author Jeroen en Tom
 *
 */
public class PruneRules {
	
	/**
	 * @param listOfEquations
	 * 			The list of equations to prune
	 * @return
	 * 			A pruned list of equations
	 */
	public static List<Equation> prune(List<Equation> listOfEquations, boolean remove) {		
		// every equation will be split and added to splitEquations
		List<List<String>> splitEquations = splitIntoParts(listOfEquations);
		
		// Divide the equations by splitLength (to be equivalent two equations should count the same number of parts)
		HashMap<String, List<List<String>>> buckets = divideInBuckets(splitEquations);
		
		// Searches in the buckets which equation are equivalent to another one
		// every equation of every group of equivalent equations, except one per group, will be in this list
		List<String> equationsToRemoveString = searchBuckets(buckets);
		
		// Given a list of strings the corresponding equations will be given back
		List<Equation> equationsToRemove = representiveEquations(equationsToRemoveString, listOfEquations);
		
		if(remove) {
			// removes the equations to remove
			// no return needed since listOfEquations is an ArrayList and thus a pointer will be given
			// that means that the equations will be removed from listOfEquations
			removeEquations(equationsToRemove, listOfEquations);
		} else {
			// sets the equations boolean prooned on true
			// no return needed since listOfEquations is an ArrayList and thus a pointer will be given
			// that means that the equations will be removed from listOfEquations
			noRemoveEquations(equationsToRemove, listOfEquations);
		}
		
		return listOfEquations;
	}
	
	/**
	 * @param equationsToRemove
	 * 			List of equations which should be removed from listOfEquations
	 * @param listOfEquations
	 * 			The list of equations where we will remove equations				
	 * 			The removes will happen in the listOfEquations since a pointer is given this will 
	 * 			happen (no return needed)
	 */
	public static void removeEquations(List<Equation> equationsToRemove, List<Equation> listOfEquations) {
		for(Equation eq : equationsToRemove) {
			listOfEquations.remove(eq);
		}
	}
	
	/**
	 * @param equationsToRemove
	 * 			List of equations which should be removed from listOfEquations
	 * @param listOfEquations
	 * 			The list of equations where we will remove equations				
	 * 			The removes will happen in the listOfEquations since a pointer is given this will 
	 * 			happen (no return needed)
	 */
	public static void noRemoveEquations(List<Equation> equationsToRemove, List<Equation> listOfEquations) {
		for(Equation eq : equationsToRemove) {
			eq.setProoned(true);
		}
	}

	/**
	 * Given a list of strings the corresponding equations will be given back
	 * @param listOfEquationsString
	 * 			A list of equations in String form from who the equivalent equation needs to be found
	 * @param listOfEquations
	 * 			A list of equations
	 * @return
	 * 			The list of equations corresponding to the strings
	 * 			Null if not all the strings have matching equations
	 * 
	 * TODO efficiënter schrijven?
	 */
	public static List<Equation> representiveEquations(List<String> listOfEquationsString, List<Equation> listOfEquations) {
		List<Equation> result = new ArrayList<Equation>();
		for(Equation equation : listOfEquations) {
			for(String string : listOfEquationsString) {
				if(equation.toString().equals(string)) {
					result.add(equation);
					break;
				}
			}
		}
		return result;
	}

	/**
	 * @param buckets
	 * 			The buckets containing the equations to be checked if they are equivalent
	 * @return
	 * 			A list of equivalent equations which can be removed
	 * 			(thus one equation of the equivalent group will not be in this list)
	 */
	public static List<String> searchBuckets(HashMap<String, List<List<String>>> buckets) {
		List<String> equationsToRemove = new ArrayList<String>();
		
		// search in every bucket
		for(String bucketName : buckets.keySet()) {
			List<List<String>> bucket = buckets.get(bucketName);
			
			// search for equivalent equations in a given bucket (given by bucketName)
			List<List<String>> equalEquations = getEquivalentEqautions(bucket);
			
			// generates a String of all equations (At this time the are represented by a List<String>)
			// The string will then be added to the equationsToRemove
			for(List<String> equationInEquals : equalEquations) {
				equationsToRemove.add(createOneStringEquation(equationInEquals));
			}
		}

		return equationsToRemove;
	}
	

	/**
	 * @param equation
	 * 			A list of terms (String) which represents a given representation
	 * @return
	 * 			A String which will represent the complete equation
	 */
	public static String createOneStringEquation(List<String> equation) {
		String completeEquation = "";
		for(String term : equation) {
			completeEquation += term;
		}
		return completeEquation;
	}

	/**
	 * @param bucket
	 * 			The bucket to be searched for equivalent equations
	 * @return
	 * 			All the equivalent equations in the bucket that can be removed
	 */
	public static List<List<String>> getEquivalentEqautions(List<List<String>> bucket) {
		List<List<String>> equalEquations = new ArrayList<List<String>>();
		
		while(bucket.size() >= 2) {
			List<List<String>> whichToRemove = new ArrayList<List<String>>(); // Contains the indexes of the equations which will be in equivalentEquation or will have no equivalents
			List<String> eq1 = bucket.get(0); // get the first Equation in the bucket
			whichToRemove.add(eq1); // the first equation will never be in equivalentEquations but it's equivalents (if they exist) will
			
			// checks which equations still in the bucket are equivalent to the first one 
			for(int i = 1; i < bucket.size(); i++) {
				List<String> eq2 = bucket.get(i);
				List<String> eq2Clone = new ArrayList<String>(eq2);
				if(areTheseEquationsEquivalent(eq1, eq2)) {
					equalEquations.add(eq2Clone);
					whichToRemove.add(eq2Clone);
				}
			}
			
			// removes equations from the bucket
			// this means the first equation and all its equivalents
			for(List<String> removeEquation : whichToRemove) {
				bucket.remove(removeEquation);
			}
		}
		
		return equalEquations;
	}
	
	
	/**
	 * Checks whether two given equations are equivalent to each other
	 * 
	 * The equations should be of equivalent length
	 * 
	 * @param eq1
	 * 			The first equation
	 * @param eq2
	 * 			The second equation
	 * @return
	 * 			A boolean
	 * 			True if the equations are equivalent
	 * 			False if the equations are not equivalent
	 */
	public static boolean areTheseEquationsEquivalent(List<String> eq1, List<String> eq2) {
		if(eq1.isEmpty() || eq2.isEmpty()) { // TODO Deze check zou niet nodig moeten zijn?
			return false;
		}
		// the first term of an equation should be equivalent
		if(eq1.get(0).equals(eq2.get(0))) {
			for(String term : eq1) {
				eq2 = equationContainsTerm(term, eq2);
				if(eq2 == null) {
					return false;
				}
			}
			return true;
		} 	
		return false;
	}

	/**
	 * Checks whether the equation contains the term and removes it one time
	 * @param term
	 * 			The term to be searched for in the equation
	 * @param eq
	 * 			The equation to be searched
	 * @return
	 * 			The equation which will contain the term one time less
	 * 			Or return null if the equation doesn't contain the term
	 */
	public static List<String> equationContainsTerm(String term, List<String> eq) {
		for(String termEq : eq) {
			if(termEq.equals(term)) {
				eq.remove(termEq);
				return eq;
			}
		}
		return null;
	}

	/**
	 * Divides the given list of Equation parts into buckets
	 * 
	 * The buckets: nrOfParts_#OperandOccurrences_
	 * 			with #OperandOccurrences_ repeated for every operand given by Grammar
	 * 
	 * @param splitEquations
	 * 			List of equation parts
	 * @return
	 * 			HashMap containing the buckets (represented by strings) and there content
	 */
	public static HashMap<String, List<List<String>>> divideInBuckets(List<List<String>> splitEquations) {
		// initialize place where we will keep the buckets
		HashMap<String, List<List<String>>> buckets = new HashMap<String, List<List<String>>>();
		
		// for every equation see what bucket it belongs to and add it
		for(List<String> equation : splitEquations) {	
			// calculate the name in which bucket this equation will go
			String bucketName = equation.size() + "_"; // the first part will be the number of parts
			String equationString = createOneStringEquation(equation); // get the complete equation string
			
			// for every possible operand calculate the number of occurrences for the bucket name
			for(Operand operand : Grammar.getPossibleOperands()) {
				 bucketName += StringUtils.countMatches(equationString, operand.toString()) + "_";	
			}
			
			// check if bucket name exists
			// if so add to that bucket else create a new bucket
			if(buckets.containsKey(bucketName)) {
				buckets.get(bucketName).add(equation);
			} else {
				List<List<String>> newBucket =  new ArrayList<List<String>>();
				newBucket.add(equation);
				buckets.put(bucketName, newBucket);
			}
		}
		return buckets;
	}

	/**
	 * @param equations
	 * 			The equations to be split into parts
	 * @return
	 * 			A list of of a list of strings
	 * 			f.e. {E+E, E*E} becomes {{E, E}, {E*E}}
	 */
	public static List<List<String>> splitIntoParts(List<Equation> equations) {
		List<List<String>> splitEquations = new ArrayList<List<String>>();
		for(Equation eq : equations) {
			splitEquations.add(splitEquation(eq));
		}
		return splitEquations;
	}	
	
	/**
	 * @param 
	 * 		The equation to be split
	 * @return
	 * 		A list of strings, the different strings represent the different parts of the equation
	 * 		f.e. equation : E*E + E - E becomes {"E*E", "+E", "-E"}
	 */
	public static List<String> splitEquation(Equation eq) {
		List<String> split = new ArrayList<String>();
		String temporary = "";
		for(Symbol symbol : eq.getListOfSymbols()) {
			if(symbol.isNonTerminal()) {
				temporary += symbol.toString();
			} else if(symbol.isOperand()) {
				if(!((Operand) symbol).isSplitable()) {
					temporary += symbol.toString();
				} else {
					split.add(temporary);
					temporary = symbol.toString();
				} 
			} else if(symbol.isTerminal()) {
				// TODO terminals not supported, are we going use them? or say if nonTerminal becomes Terminal
				temporary += symbol.toString();
			}
		}	
		split.add(temporary); // adds the final term
		return split;
	}	

}
