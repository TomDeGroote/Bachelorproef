package master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import master.all.ObjectEvaluateAllSolutions;
import tree.Equation;
import tree.Grammar;
import tree.Operand;
import tree.Symbol;

public class SolutionChecker {

	
	private static boolean removeEqui = true;
	/**
	 * Takes 2 lists and returns het elements that are in the first but not in the second.
	 * If both lists contain the same elements, then return an empty list.
	 * 
	 * @param list1
	 * 			A list of equations
	 * @param list2
	 * 			A list of equations
	 * @return
	 * 			A list containing the elements that are unique to the first list.
	 */
	public static List<List<Equation>> run(List<Equation> list1, List<Equation> list2){
		List<List<Equation>>  result = new ArrayList<List<Equation>>();
		List<Equation> common = new ArrayList<Equation>();
		List<Equation> unique1 = new ArrayList<Equation>();
		List<Equation> unique2 = new ArrayList<Equation>();
		if(removeEqui){
			list1 = removeEquivalentEquations(list1);
			list2 = removeEquivalentEquations(list2);
		}
		
		
		if(compareLists(list1,list2)){
			result.add(list1);
			result.add(unique1);
			result.add(unique2);
			return result;
		}

		common.addAll(subtract(list1,subtract(list1,list2)));
		unique1.addAll(subtract(list1, list2));
		unique2.addAll(subtract(list2, list1));

		result.add(common);
		result.add(unique1);
		result.add(unique2);

		return result;

	}

	/**
	 *  Checks if lists of equations contain the same elements.
	 * @param list1
	 * 			A list of equations
	 * @param list2
	 * 			A list of equations
	 * @return
	 * 			Returns a boolean dependend on the fact that both lists contain exactly the same elements.
	 */
	public static boolean compareLists(List<Equation> list1, List<Equation> list2) {
		if(list1.size() != list2.size())
			return false;
		for(int j = 0; j < list1.size(); j++){
			for(int i = 0; i < list2.size(); i++){
				if(!compareEquations(list1.get(j),list2.get(i)))
					return false;
			}
		}

		return (!list1.isEmpty() && !list2.isEmpty());
	}


	/**
	 * Joins 2 lists of equations without having multiple occasions of the same element.
	 * @param list1
	 * 		A list of equations
	 * @param list2
	 * 		A list of equations
	 * @return
	 * 		The joined list of equations
	 */
	public static List<Equation> joinLists(List<Equation> list1, List<Equation> list2) {
		List<Equation> list = new ArrayList<Equation>();
		list = list1;
		for(Equation eq1 : list1){
			for(Equation eq2 : list2)
				if(!compareEquations(eq1,eq2))
					list.add(eq2);
		}
		return list;
	}

	/**
	 * Removes the equations of the second list which were present in the first.
	 * @param list1
	 * 		A list of equations
	 * @param list2
	 * 		A list of equations
	 * @return
	 * 		The first list without the elements of the second
	 */
	public static List<Equation> subtract(List<Equation> list1, List<Equation> list2) {
		List<Equation> result = new ArrayList<Equation>(list1);
		for(Equation eq1 : list1){
			for(Equation eq2 : list2) {
				if(compareEquations(eq1,eq2)){
					result.remove(eq2);
					//break;
				}
			}
		}
		return result;
	}

	/**
	 * Rewrites a list of equations.
	 * @param list
	 * @return
	 */
	public static List<Equation> rewriteEquations(List<Equation> list) {
		List<Equation> updatedList = new ArrayList<Equation>(list.size());
		for(Equation eq: list){
			Equation newEq = Grammar.convertEquation(eq);
			updatedList.add(newEq);
		}
		return updatedList;
	}

	public static boolean compareEquations(Equation eq1, Equation eq2){
		if(eq1.getListOfSymbols().size() != eq2.getListOfSymbols().size())
			return false;
		
		HashMap<Symbol,Integer> temporary1 = new HashMap<Symbol,Integer>();
		
		for(Symbol sym : eq1.getListOfSymbols()){
			if(!temporary1.containsKey(sym))
				temporary1.put(sym,1);
			else
				temporary1.put(sym,temporary1.get(sym)+1);
		}
		
		for(Symbol sym : eq2.getListOfSymbols()){
			if(!temporary1.containsKey(sym))
				return false;
			else
				temporary1.put(sym,temporary1.get(sym)-1);
		}
		
		for(Map.Entry<Symbol,Integer> entry : temporary1.entrySet()){
			if(entry.getValue() != 0)
				return false;
		}
		

		List<List<Symbol>> temp1 = ObjectEvaluateAllSolutions.splitOnEverySplitable(eq1);
		List<List<Symbol>> temp2 = ObjectEvaluateAllSolutions.splitOnEverySplitable(eq2);

		if(temp1.size() != temp2.size())
			return false;
		
		List<List<Equation>> listEq1 = new ArrayList<List<Equation>>();
		List<List<Equation>> listEq2 = new ArrayList<List<Equation>>();;

		for(int i = 0; i < temp1.size(); i++){
			listEq1.add(splitWithOperator(temp1.get(i)));
			listEq2.add(splitWithOperator(temp2.get(i)));
		}

		Boolean wasInSecondList = false;
		for(int i = 0; i < listEq1.size(); i++){
			wasInSecondList = false;
			for(int j = 0; j < listEq2.size(); j++){
				wasInSecondList = compareListEquations(listEq1.get(i),listEq2.get(j));
				if(wasInSecondList){
					listEq2.remove(listEq2.get(j));
					break;
				}
			}
			if(!wasInSecondList)
				return false;
		}

		return (listEq2.isEmpty());
	}

	/**
	 * @param eq
	 * @return
	 */
	public static List<Equation> splitWithOperator(List<Symbol> eq){
		List<Equation> result = new ArrayList<Equation>();
		List<Symbol> tempSym = new ArrayList<Symbol>();

		for(int i = 0; i < eq.size(); i++){
			tempSym = new ArrayList<Symbol>();
			Equation temp = new Equation();
			if(eq.get(i).isOperand()){
				tempSym.add(eq.get(i));
				temp = new Equation(tempSym);
			} else if(eq.get(i).isTerminal())
				if(i == 0) {
					tempSym.add(new Operand("*",false,true,1));
					tempSym.add(eq.get(i));
					temp = new Equation(tempSym);
				} else {
					tempSym.add(eq.get(i-1));
					tempSym.add(eq.get(i));
					temp = new Equation(tempSym);
				}
			result.add(temp);
		}

		return result;

	}

	/**
	 * @param eq1
	 * @param eq2
	 * @return
	 */
	public static boolean compareListEquations(List<Equation> eq1, List<Equation> eq2){
		if(eq1.size() != eq2.size())
			return false;
		for(Equation eq : eq1){
			if(eq2.contains(eq))
				eq2.remove(eq);
			else
				return false;
		}
		if(eq2.isEmpty())
			return true;
		else
			return false;
	}
	
	public static List<Equation> removeEquivalentEquations(List<Equation> list){
		List<Equation> result = new ArrayList<Equation>();
		Boolean theSame = false;
		for(int i = 0; i < list.size(); i++){
			theSame = false;
			for(int j = i-1; j > -1; j--){
				if(compareEquations(list.get(i),list.get(j))){
					theSame = true;
					break;
				}
			}
			if(!theSame){
				result.add(list.get(i));
			}
		}
		
		return result;
	}

}
