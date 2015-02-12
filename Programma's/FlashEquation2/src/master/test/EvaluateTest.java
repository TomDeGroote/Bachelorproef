package master.test;

import java.util.ArrayList;

import master.Evaluate;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import tree.Grammar;
import tree.Tree;

public class EvaluateTest {

	private static Evaluate eval;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Grammar.initialize();
		ArrayList<Double> mainExample = new ArrayList<Double>();
		mainExample.add(1.0);
		mainExample.add(1.0);
		mainExample.add(2.0);
		Tree tree = new Tree(2, true);
		eval = new Evaluate(tree,mainExample);
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void splitInSplittableStrings() {
		String testStr = "E*E+E-E*E";
		ArrayList<String> solution = new ArrayList<String>();
		solution.add("E*E");
		solution.add("+");
		solution.add("E");
		solution.add("-");
		solution.add("E*E");
		
		Assert.assertEquals(solution, eval.splitStringSplittable(testStr));
	}
	
	@Test
	public void splitStringInThreePartsTest() {
		
		String testStr = "E*E+E-E*E";
		ArrayList<String> solution = new ArrayList<String>();
		solution.add("E*E");
		solution.add("+");
		solution.add("E-E*E");
		Assert.assertEquals(solution,eval.splitStringInThreeParts(testStr));
	}
	
	@Test
	public void splitStringInThreePartsAllNonSplittableTest() {
		
		String testStr = "E*E*E*E*E";
		ArrayList<String> solution = new ArrayList<String>();
		solution.add("E");
		solution.add("*");
		solution.add("E*E*E*E");
		Assert.assertEquals(solution,eval.splitStringInThreeParts(testStr));
	}
	
	@Test
	public void namesByValueTest() {
		double first = 1.0;
		double second = 1.0;
		Assert.assertEquals("K1",eval.getNamesByValue().get(first).get(0));
		Assert.assertEquals("K2",eval.getNamesByValue().get(second).get(1));
	}

	@Test
	public void evaluateEquationTest() {
		String equation1 = "E";
		ArrayList<String> solution1 = new ArrayList<String>();
		solution1.add("K1");
		solution1.add("K2");
		
		Assert.assertEquals(solution1,eval.evaluateEquation(equation1).get(1.0));
		eval.getAlreadySolved().put("E", eval.evaluateEquation(equation1));
		String equation = "E+E";
		ArrayList<String> solution = new ArrayList<String>();
		solution.add("K1+K1");
		solution.add("K1+K2");
		solution.add("K2+K1");
		solution.add("K2+K2");
		
		Assert.assertEquals(solution,eval.evaluateEquation(equation).get(2.0));

	}
	
	@Test
	public void checkAgainstExamplesTest() {
		
		ArrayList<Double> example = new ArrayList<Double>();
		example.add(2.0);
		example.add(2.0);
		example.add(4.0);
		eval.getAllExamples().add(example);
		
		String equation1 = "E";
		ArrayList<String> solution1 = new ArrayList<String>();
		solution1.add("K1");
		solution1.add("K2");
		
		Assert.assertEquals(solution1,eval.evaluateEquation(equation1).get(1.0));
		eval.getAlreadySolved().put("E", eval.evaluateEquation(equation1));
		String equation = "E+E";
		ArrayList<String> solution = new ArrayList<String>();
		solution.add("K1+K1");
		solution.add("K1+K2");
		solution.add("K2+K1");
		solution.add("K2+K2");
		
		Assert.assertEquals(true,eval.checkAgainstAllExamples("K1+K2"));
		Assert.assertEquals(solution,eval.evaluateEquation(equation).get(2.0));

	}

	@Test
	public void testEvaluate() {
		ArrayList<Double> example = new ArrayList<Double>();
		example.add(1.0);
		example.add(1.0);
		example.add(2.0);
		System.out.println("Giving example 1");
		eval.evaluate(example);
		for(String eq : eval.getBufferSolutions()) {
			System.out.println(eq);
		}
	}
	
}
