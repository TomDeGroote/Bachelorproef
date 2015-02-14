package master;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import research.RandomGenerator;
import tree.Equation;

public class SolutionChecker {

	private static final int DEADLINE = 1000;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			ObjectMasterAllSolutions.run(DEADLINE, false, null);
			ObjectMaster.run(DEADLINE, false, null);
			
			System.out.println("Exacte match: " + compareLists(ObjectMaster.getAllSolutions(),ObjectMasterAllSolutions.getAllSolutions()));
			System.out.println("All solutions:");
			for(Equation eq : joinLists(ObjectMaster.getAllSolutions(),ObjectMasterAllSolutions.getAllSolutions())) {
				System.out.println(eq);
			}
			System.out.println("Unique first:");
			
			for(Equation eq : subtract(ObjectMaster.getAllSolutions(),ObjectMasterAllSolutions.getAllSolutions())) {
				System.out.println(eq);
			}
			System.out.println("Unique second:");
			for(Equation eq : subtract(ObjectMasterAllSolutions.getAllSolutions(),ObjectMaster.getAllSolutions())) {
				System.out.println(eq);
			}
			
			
//			System.out.println("Best: ");
//			System.out.println(ObjectMaster.getBestSolution());
//		
//			StringMaster.run();
//			System.out.println("All: ");
//			for(String eq : StringMaster.getAllSolutions()) {
//				System.out.println(eq);
//			}
//			System.out.println("Best: ");
//			System.out.println(StringMaster.getBestSolution());
//
//			ObjectMaster.run(-1, false, null);
//			System.out.println("All: ");
//			for(Equation eq : ObjectMaster.getAllSolutions()) {
//				System.out.println(eq);
//			}
//			System.out.println("Best: ");
//			System.out.println(ObjectMaster.getBestSolution());
//
//		
//			ObjectMasterAllSolutions.run(DEADLINE, false, null);
//			System.out.println("All: ");
//			for(Equation eq : ObjectMasterAllSolutions.getAllSolutions()) {
//				System.out.println(eq);
//			}
//			System.out.println("Best: ");
//			System.out.println(ObjectMasterAllSolutions.getBestSolution());
		
		
		
	}
	
	public static boolean compareLists(List<Equation> list1, List<Equation> list2) {
		
		for(int i = 0; i < list2.size(); i++){
			if(!list1.contains(list2.get(i)))
				return false;
		}
		
		return (!list1.isEmpty() && !list2.isEmpty());
	}
	
	public static List<Equation> joinLists(List<Equation> list1, List<Equation> list2) {
		List<Equation> list = new ArrayList<Equation>();
		list = list1;
		for(Equation eq : list2)
			if(!list.contains(eq))
				list.add(eq);
		
		return list;
	}

	public static List<Equation> subtract(List<Equation> list1, List<Equation> list2)
	{
		List<Equation> result = new ArrayList<Equation>(list2);
	    result.removeAll(list1);
	    return result;
	}

}
