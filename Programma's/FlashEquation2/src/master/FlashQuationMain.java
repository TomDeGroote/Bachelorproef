package master;

import java.util.ArrayList;
import java.util.List;

import research.RandomGenerator;
import tree.Equation;

public class FlashQuationMain {

	private static final String runMethod = "random";
	private static final int DEADLINE = 1000;

	public static void main(String[] args) {
		if(runMethod.equals("string")) {
			StringMaster.run();
			System.out.println("All: ");
			for(String eq : StringMaster.getAllSolutions()) {
				System.out.println(eq);
			}
			System.out.println("Best: ");
			System.out.println(StringMaster.getBestSolution());
		} else if(runMethod.equals("object")) {
			ObjectMaster.run(DEADLINE, false, null);
			System.out.println("All: ");
			for(Equation eq : ObjectMaster.getAllSolutions()) {
				System.out.println(eq);
			}
			System.out.println("Best: ");
			System.out.println(ObjectMaster.getBestSolution());
		} else if(runMethod.equals("jar")){
			System.out.println(getFormula(getInputList(), -1));
		}
		else if(runMethod.equals("objectAll")) {
			ObjectMasterAllSolutions.run(-1, false, null);
			System.out.println("All: ");
			for(Equation eq : ObjectMasterAllSolutions.getAllSolutions()) {
				System.out.println(eq);
			}
			System.out.println("Best: ");
			System.out.println(ObjectMasterAllSolutions.getBestSolution());
		} else if(runMethod.equals("random")) {	
			List<List<Double>> randomGenerated = RandomGenerator.generate(4, 4, 0, 30);
			System.out.println(RandomGenerator.getLastGeneratedEquation());
			// best objectmaster
			long time = System.currentTimeMillis();
//			ObjectMaster.run(-1, true, randomGenerated);
//			System.out.print("Best ObjectMaster: ");
//			System.out.println(ObjectMaster.getBestSolution());
//			System.out.println("time: " + (System.currentTimeMillis() - time));
//			System.out.println("All: ");
//			for(Equation eq : ObjectMaster.getAllSolutions()) {
//				System.out.println(eq);
//			}
			
			time = System.currentTimeMillis();
			// best all solution
			ObjectMasterAllSolutions.run(-1, false, randomGenerated);
			System.out.print("Best ObjectMasterAllSolutions: ");
			System.out.println(ObjectMasterAllSolutions.getBestSolution());
			System.out.println("time: " + (System.currentTimeMillis() - time));
			
			// best tuple solution
			time = System.currentTimeMillis();
			ObjectTupleMaster.run(-1, false, randomGenerated);
			System.out.print("Best ObjectTupleMaster: ");
			System.out.println(ObjectTupleMaster.getBestSolution());
			System.out.println("time: " + (System.currentTimeMillis() - time));

			for(List<Double> row : randomGenerated) {
				for(double number : row) {
					System.out.print(number + " ");
				}
				System.out.println("");
			}
			
//			StringMaster.run(DEADLINE, true, randomGenerated); TODO
//			System.out.println("Best StringMaster: ");
//			System.out.println(StringMaster.getBestSolution());
		} else if(runMethod.equals("tuple")) {
			ObjectTupleMaster.run(-1, true, null);
			System.out.println("All: ");
			for(Equation eq : ObjectTupleMaster.getAllSolutions()) {
				System.out.println(eq);
			}
			System.out.println("Best: ");
			System.out.println(ObjectTupleMaster.getBestSolution());
		}
	}

	/**
	 * Calculates the formula
	 * 
	 * WARNING: This method currently only calculates possible equations that are shorter than length of 6 values
	 * 			e.g. 5+5+1*8 will be found but
	 * 				 6+2/4-9+3-9+5 will never be found
	 * @param inputList
	 * 			A two dimensional list containing values
	 * @param deadline
	 * 			<= 0  then no deadline
	 * 			Else the maximum time it can run in milliseconds
	 * @return
	 * 			The formula in String format
	 * 				K0 means the first element in the InputList
	 * 				K1 means the second element in the InputList
	 * 				...
	 * 				KN means the N'th element in the InputList
	 * 				Possible operators: +, -, *, /
	 * 				A number is a given constant (not supported yet) TODO
	 * 			Or "Empty" if no formula was found
	 */
	public static String getFormula(List<List<Double>> inputList, int deadline) {
		return ObjectMaster.run(deadline, true, inputList);
	}

	/**
	 * @return
	 * 			A manually generated InputList
	 */
	private static List<List<Double>> getInputList() {
		// the result
		List<List<Double>> inputList = new ArrayList<List<Double>>();

		// input 1
		List<Double> input1 = new ArrayList<Double>();
		input1.add(4.0);
		input1.add(2.0);
		input1.add(8.0);
		inputList.add(input1);

		// input 2
		List<Double> input2 = new ArrayList<Double>();
		input2.add(3.0);
		input2.add(3.0);
		input2.add(9.0);
		inputList.add(input2);

		// input 3
		List<Double> input3 = new ArrayList<Double>();
		input3.add(9.0);
		input3.add(5.0);
		input3.add(19.0);
		inputList.add(input3);


		return inputList;
	}


}
