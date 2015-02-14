package master.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import master.ObjectEvaluateAllSolutions;
import master.StringMaster;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import tree.Equation;
import tree.Main;
import tree.NonTerminal;
import tree.Operand;
import tree.Symbol;
import tree.Terminal;
import tree.Tree;

public class ObjectEvaluateAllSolutionsTest {

	private ObjectEvaluateAllSolutions evaluate;
	private HashMap<String, Double> KsExample1;
	private HashMap<String, Double> KsExample2;
	private Equation eq1;
	private Equation eq2;
	private Equation eq3;
	private Equation eqTerminal1;
	private Equation eqTerminal2;


	
	@Before
	public void initialize() throws ClassNotFoundException, IOException {
		Tree tree = new Tree(1, true);//readTree();
		evaluate = new ObjectEvaluateAllSolutions(tree);
		
		// put K1 = 2, K2 = 3, Goal = 6
		KsExample1 = new HashMap<String, Double>();
		KsExample1.put("K1", 3.0);
		KsExample1.put("K2", 3.0);
		KsExample1.put(StringMaster.getNameOfGoalK(), 3.0);
		
		// put K1 = 3, K2 = 3, Goal = 9
		KsExample2 = new HashMap<String, Double>();
		KsExample2.put("K1", 6.0);
		KsExample2.put("K2", 6.0);
		KsExample2.put(StringMaster.getNameOfGoalK(), 12.0);
		
		// generate equation 1 E+E*E+E
		List<Symbol> inputEq1 = new ArrayList<Symbol>();
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+", true,true, 0));
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("*", false,true, 1));
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+", true,true, 0));
		inputEq1.add(new NonTerminal("E"));
		eq1 = new Equation(inputEq1);
		
		// generate equation 2 E-E+E*E
		List<Symbol> inputEq2 = new ArrayList<Symbol>();
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("-", true,false, 0));
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("+", true,true, 0));
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("*", false,true, 1));
		inputEq2.add(new NonTerminal("E"));
		eq2 = new Equation(inputEq2);
		
		// generate equation 3 E*E/E*E
		List<Symbol> inputEq3 = new ArrayList<Symbol>();
		inputEq3.add(new NonTerminal("E"));
		inputEq3.add(new Operand("*", false,true, 1));
		inputEq3.add(new NonTerminal("E"));
		inputEq3.add(new Operand("/", false,false, 1));
		inputEq3.add(new NonTerminal("E"));
		inputEq3.add(new Operand("*", false,true, 1));
		inputEq3.add(new NonTerminal("E"));
		eq3 = new Equation(inputEq3);
		
		// generate equation 1 T+T*T+T = 15
		List<Symbol> inputEqTerminal = new ArrayList<Symbol>();
		inputEqTerminal.add(new Terminal("T", 3.0));
		inputEqTerminal.add(new Operand("+", true,true, 0));
		inputEqTerminal.add(new Terminal("T", 3.0));
		inputEqTerminal.add(new Operand("*", false,true, 1));
		inputEqTerminal.add(new Terminal("T", 3.0));
		inputEqTerminal.add(new Operand("+", true,true, 0));
		inputEqTerminal.add(new Terminal("T", 3.0));
		eqTerminal1 = new Equation(inputEqTerminal);
		
		// generate equation 1 T*T*T/T = 3*3*3/2 = 13.5
		List<Symbol> inputEqTerminal2 = new ArrayList<Symbol>();
		inputEqTerminal2.add(new Terminal("T", 3.0));
		inputEqTerminal2.add(new Operand("*", false,true, 1));
		inputEqTerminal2.add(new Terminal("T", 3.0));
		inputEqTerminal2.add(new Operand("*", false,false, 1));
		inputEqTerminal2.add(new Terminal("T", 3.0));
		inputEqTerminal2.add(new Operand("/", false,false, 1));
		inputEqTerminal2.add(new Terminal("T", 2.0));
		eqTerminal2 = new Equation(inputEqTerminal2);

	}
	

	@Test
	public void testEvaluate() {
		System.out.println("Giving example 1");
		evaluate.evaluate(KsExample1);
		for(Equation eq : evaluate.getBufferSolutions()) {
			System.out.println(eq.toString());
		}
	}

	@Test
	public void testEvaluateEquation() {
		// generate equation 1 E+E*E+E
		List<Symbol> inputEq1 = new ArrayList<Symbol>();
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+", true,true, 0));
		inputEq1.add(new NonTerminal("E"));
		Equation eq1 = new Equation(inputEq1);
		HashMap<String, Double> Ks = new HashMap<String, Double>();
		Ks.put("K1", 3.0);
		Ks.put("K2", 4.0);
		Ks.put(StringMaster.getNameOfGoalK(), 4.0);
		evaluate.examples.add(Ks);
		HashMap<Double, List<Equation>> allPossibleResults = evaluate.evaluateEquation(eq1);
		
		Assert.assertEquals(3, allPossibleResults.keySet().size());
		Assert.assertEquals(true, allPossibleResults.containsKey(7.0));
		Assert.assertEquals(true, allPossibleResults.containsKey(6.0));
		Assert.assertEquals(true, allPossibleResults.containsKey(8.0));

	}

	@Test
	public void testConcatenateResults() {
		// generate equation 1 E+E
		List<Symbol> inputEq1 = new ArrayList<Symbol>();
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+", true,true, 0));
		inputEq1.add(new NonTerminal("E"));
		Equation eq1 = new Equation(inputEq1);
		
		// generate equation 2 E-E
		List<Symbol> inputEq2 = new ArrayList<Symbol>();
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("-", true,false, 0));
		inputEq2.add(new NonTerminal("E"));
		Equation eq2 = new Equation(inputEq2);
		
		// generate equation 3 E/E
		List<Symbol> inputEq3 = new ArrayList<Symbol>();
		inputEq3.add(new NonTerminal("E"));
		inputEq3.add(new Operand("/", false,false, 1));
		inputEq3.add(new NonTerminal("E"));
		Equation eq3 = new Equation(inputEq3);
		
		// generate equation 3 E/E*E
		List<Symbol> inputEq4 = new ArrayList<Symbol>();
		inputEq4.add(new NonTerminal("E"));
		inputEq4.add(new Operand("/", false,false, 1));
		inputEq4.add(new NonTerminal("E"));
		inputEq4.add(new Operand("*", false,true, 1));
		inputEq4.add(new NonTerminal("E"));
		Equation eq4 = new Equation(inputEq4);
		
		List<Equation> list1 = new ArrayList<Equation>();
		list1.add(eq1);
		list1.add(eq2);
		
		List<Equation> list2 = new ArrayList<Equation>();
		list2.add(eq3);
		list2.add(eq4);
		
		HashMap<Double, List<Equation>> part1 = new HashMap<Double, List<Equation>>();
		part1.put(3.0, list1);
		part1.put(6.2, list2);
		HashMap<Double, List<Equation>> part2 = new HashMap<Double, List<Equation>>();
		part2.put(9.0, list1);
		part2.put(5.2, list2);
		
		HashMap<String, Double> Ks = new HashMap<String, Double>();
		Ks.put("K1", 3.0);
		Ks.put("K2", 4.0);
		Ks.put(StringMaster.getNameOfGoalK(), 4.0);
		evaluate.examples.add(Ks);
		
		HashMap<Double, List<Equation>> result = evaluate.concatenateResults(part1, new Operand("+", true, true, 0), part2);
		Assert.assertEquals(4, result.keySet().size());
		Assert.assertEquals(true, result.containsKey(12.0));
		Assert.assertEquals(4, result.get(12.0).size());
		Assert.assertEquals("E+E+E+E", result.get(12.0).get(0).toString());
		Assert.assertEquals(true, result.containsKey(8.2));
		Assert.assertEquals(true, result.containsKey(15.2));
		Assert.assertEquals(true, result.containsKey(11.4));
	}

//	@Test
//	public void testAddPossibelSolutions() {
//		// generate equation 1 3+2
//		List<Symbol> inputEq1 = new ArrayList<Symbol>();
//		inputEq1.add(new Terminal("K1", 3.0));
//		inputEq1.add(new Operand("+", true,true, 0));
//		inputEq1.add(new Terminal("K2", 2.0));
//		Equation eq1 = new Equation(inputEq1);
//		HashMap<String, Double> Ks1 = new HashMap<String, Double>();
//		Ks1.put("K1", 3.0);
//		Ks1.put("K2", 2.0);
//		Ks1.put(StringMaster.getNameOfGoalK(), 5.0);
//		evaluate.examples.add(Ks1);
//		
//		List<Equation> list = new ArrayList<Equation>();
//		list.add(eq1);
//		
//		
//		// generate equation 1 1 O 2 = 3
//		HashMap<String, Double> Ks2 = new HashMap<String, Double>();
//		Ks2.put("K1", 1.0);
//		Ks2.put("K2", 2.0);
//		Ks2.put(StringMaster.getNameOfGoalK(), 3.0);
//		evaluate.examples.add(Ks2);
//				
//		evaluate.addPossibelSolutions(5.0, list);
//		Assert.assertEquals(1, evaluate.getBufferSolutions().size());
//		Assert.assertEquals("K1+K2", evaluate.getBufferSolutions().get(0).toString());
//	}

//	@Test
//	public void testEvaluateTerminalEquation() {
//		Assert.assertEquals(true, ObjectEvaluateAllSolutions.evaluateTerminalEquation(eqTerminal2, 13.5));
//		Assert.assertEquals(true, ObjectEvaluateAllSolutions.evaluateTerminalEquation(eqTerminal1, 15.0));
//	}
//
//	@Test
//	public void testCalculateTerm() {
//		Double d = 13.5;
//		Assert.assertEquals(d, ObjectEvaluateAllSolutions.calculateTerm(eqTerminal2.getListOfSymbols()));
//	}

	@Test
	public void testSplitOnEverySplitable() {
		List<List<Symbol>> split = ObjectEvaluateAllSolutions.splitOnEverySplitable(eqTerminal1);
		Assert.assertEquals(5, split.size());
		Assert.assertEquals("T", new Equation(split.get(0)).toString());
		Assert.assertEquals("+", new Equation(split.get(1)).toString());
		Assert.assertEquals("T*T", new Equation(split.get(2)).toString());
		Assert.assertEquals("+", new Equation(split.get(3)).toString());
		Assert.assertEquals("T", new Equation(split.get(4)).toString());

	}

	@Test
	public void testConcatenateEquationLists() {
		// generate equation 1 E+E
		List<Symbol> inputEq1 = new ArrayList<Symbol>();
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+", true,true, 0));
		inputEq1.add(new NonTerminal("E"));
		Equation eq1 = new Equation(inputEq1);
		
		// generate equation 2 E-E
		List<Symbol> inputEq2 = new ArrayList<Symbol>();
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("-", true,false, 0));
		inputEq2.add(new NonTerminal("E"));
		Equation eq2 = new Equation(inputEq2);
		
		// generate equation 3 E/E
		List<Symbol> inputEq3 = new ArrayList<Symbol>();
		inputEq3.add(new NonTerminal("E"));
		inputEq3.add(new Operand("/", false,false, 1));
		inputEq3.add(new NonTerminal("E"));
		Equation eq3 = new Equation(inputEq3);
		
		// generate equation 3 E/E*E
		List<Symbol> inputEq4 = new ArrayList<Symbol>();
		inputEq4.add(new NonTerminal("E"));
		inputEq4.add(new Operand("/", false,false, 1));
		inputEq4.add(new NonTerminal("E"));
		inputEq4.add(new Operand("*", false,true, 1));
		inputEq4.add(new NonTerminal("E"));
		Equation eq4 = new Equation(inputEq4);
		
		List<Equation> list1 = new ArrayList<Equation>();
		list1.add(eq1);
		list1.add(eq2);
		
		List<Equation> list2 = new ArrayList<Equation>();
		list2.add(eq3);
		list2.add(eq4);
		
		List<Equation> result = evaluate.concatenateEquationLists(list1, new Operand("*", false, true, 1), list2);
		HashSet<String> resultStrings = new HashSet<String>();
		for(Equation eq : result) {
			resultStrings.add(eq.toString());
		}
		
		Assert.assertEquals(4, result.size());
		Assert.assertEquals(true, resultStrings.contains("E+E*E/E"));
		Assert.assertEquals(true, resultStrings.contains("E+E*E/E*E"));
		Assert.assertEquals(true, resultStrings.contains("E-E*E/E"));
		Assert.assertEquals(true, resultStrings.contains("E-E*E/E*E"));
	}

	@Test
	public void testConcatenateEquations() {
		Assert.assertEquals(eq1.toString() + "*" +  eq2.toString(), evaluate.concatenateEquations(eq1, new Operand("*", false, true, 0), eq2).toString());
	}

//	@Test
//	public void testSplitNonSplitableInThreeParts() {
//		List<Equation> split = evaluate.splitNonSplitableInThreeParts(eq3);
//		Assert.assertEquals(3, split.size());
//		Assert.assertEquals("E", split.get(0).toString());
//		Assert.assertEquals("*", split.get(1).toString());
//		Assert.assertEquals("E/E*E", split.get(2).toString());
//	}
//
//	@Test
//	public void testSplitSplitableInThreeParts() {
//		// Test possibility 1: get back three parts if splitable
//		List<Equation> split = evaluate.splitSplitableInThreeParts(eq1);
//		Assert.assertEquals(3, split.size());
//		Assert.assertEquals("E", split.get(0).toString());
//		Assert.assertEquals("+", split.get(1).toString());
//		Assert.assertEquals("E*E+E", split.get(2).toString());
//		
//		// Test possibility2: get back on part because equation is nonSplitable
//		split = evaluate.splitSplitableInThreeParts(eq3);
//		Assert.assertEquals(1, split.size());
//		Assert.assertEquals("E*E/E*E", split.get(0).toString());
//	}
	
	/**
	 * Reads the tree from file (FILENAME given by tree.MAIN
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unused")
	private Tree readTree() throws IOException, ClassNotFoundException {
		Tree tree;
		FileInputStream fis = new FileInputStream(new File(Main.getFileNameTree()));
		ObjectInputStream ois = new ObjectInputStream(fis);
		
		// read object Tree
		tree = (Tree) ois.readObject();
		ois.close();
		return tree;
	}
}
