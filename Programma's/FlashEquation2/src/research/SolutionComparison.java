package research;

import java.util.List;

import master.Input;
import master.Master;
import master.SolutionChecker;
import master.all.ObjectMasterAllSolutions;
import master.tuple.ObjectTupleMaster;
import tree.Equation;
import tree.Tree;


public class SolutionComparison {

	
	private static int deadline = -1;
	private static boolean stopAfterOne = false ;
	private static List<List<Double>> numbers;
	
	// RandomGenerator parameters
	private static final int length = 3; // inclusive solution
	private static final int nrOfExamples = 3;
	private static final int minimum = -100;
	private static final int maximum = 100;
	
	public static void main(String[] args) {
		Input input = new Input(Tree.FILENAME_P);
		Master masterAll = new ObjectMasterAllSolutions();
		Master masterTuple = new ObjectTupleMaster();
		
		numbers = RandomGenerator.generateCertainAllK(length, nrOfExamples, minimum, maximum);
		numbers = null;
		
		masterAll.run(deadline, stopAfterOne, numbers, input);
		List<Equation> allSolution = masterAll.getAllSolutions();
		
		masterTuple.run(deadline, stopAfterOne, numbers, input);
		List<Equation> tupleSolution = masterTuple.getAllSolutions();
	
		List<Equation> tupleSolutionChanged = SolutionChecker.rewriteEquations(tupleSolution);
		
		List<List<Equation>> result = SolutionChecker.run(allSolution, tupleSolutionChanged);
		
		List<Equation> withOutDoubles = SolutionChecker.removeEquivalentEquations(result.get(0));
		
		System.out.println("All Solutions: ");
		for(int i = 0; i < result.get(0).size(); i++){
			System.out.println(result.get(0).get(i));
		}
		System.out.println();
		System.out.println("All Solutions Short: ");
		for(int i = 0; i < withOutDoubles.size(); i++){
			System.out.println(withOutDoubles.get(i));
		}
		
		System.out.println("Unique 1: ");
		for(int i = 0; i < result.get(1).size(); i++){
			System.out.println(result.get(1).get(i));
		}
		
		List<Equation> unique2 = SolutionChecker.removeEquivalentEquations(result.get(2));
		System.out.println("Unique 2: ");
		
		for(int i = 0; i < unique2.size(); i++){
			System.out.println(unique2.get(i));
		}
	}
}
