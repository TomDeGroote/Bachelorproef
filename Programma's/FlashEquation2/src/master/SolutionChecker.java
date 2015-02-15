package master;

import java.util.ArrayList;
import java.util.List;

import master.all.ObjectMasterAllSolutions;
import master.normal.ObjectMaster;
import tree.Equation;

public class SolutionChecker {

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
		
		for(int i = 0; i < list2.size(); i++){
			if(!list1.contains(list2.get(i)))
				return false;
		}
		
		for(int i = 0; i < list1.size(); i++){
			if(!list2.contains(list1.get(i)))
				return false;
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
		for(Equation eq : list2)
			if(!list.contains(eq))
				list.add(eq);
		
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
	public static List<Equation> subtract(List<Equation> list1, List<Equation> list2)
	{
		List<Equation> result = new ArrayList<Equation>(list2);
	    result.removeAll(list1);
	    return result;
	}

}
