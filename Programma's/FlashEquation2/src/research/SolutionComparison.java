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
	private static final int length = 5; // inclusive solution
	private static final int nrOfExamples = 4;
	private static final int minimum = -100;
	private static final int maximum = 100;

	public static void main(String[] args) {
		for(int i = 0; i < 10; i++)
			run();

	}

	public static void run(){
		Input input = new Input(Tree.FILENAME_P);
		Master masterAll = new ObjectMasterAllSolutions();
		Master masterTuple = new ObjectTupleMaster();

		numbers = RandomGenerator.generateCertainAllK(length, nrOfExamples, minimum, maximum);
		//numbers = null;
		
		System.out.println("Random numbers");
		for(List<Double> t : numbers){
			System.out.println();
			for(Double tt:t)
				System.out.print(tt + " ");
		}
		System.out.println();
		System.out.println("End random numbers");
		

		masterAll.run(deadline, stopAfterOne, numbers, input);
		List<Equation> allSolution = masterAll.getAllSolutions();

		masterTuple.run(deadline, stopAfterOne, numbers, input);
		List<Equation> tupleSolution = masterTuple.getAllSolutions();

		//System.out.println("Got all lists");
		List<Equation> tupleSolutionChanged = SolutionChecker.rewriteEquations(tupleSolution);
		//System.out.println("Rewrite equation, done.");
		List<List<Equation>> result = SolutionChecker.run(allSolution, tupleSolutionChanged);
		//System.out.println("SolutionChecker runned.");
		List<Equation> withOutDoubles = SolutionChecker.removeEquivalentEquations(result.get(0));

		System.out.println();
		System.out.println("All Solutions Short: ");
		for(int i = 0; i < withOutDoubles.size(); i++){
			System.out.println(withOutDoubles.get(i));
		}
		if(result.get(1).size() != 0 || result.get(2).size() != 0){
			System.out.println("THERE ARE UNIQUES!!!!");
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
}
