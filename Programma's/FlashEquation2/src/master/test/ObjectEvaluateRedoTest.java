package master.test;

import java.util.ArrayList;
import java.util.List;

import master.CopyOfObjectEvaluate;
import master.CopyOfObjectEvaluate.Tuple;

import org.junit.Assert;
import org.junit.Test;

import tree.Equation;
import tree.NonTerminal;
import tree.Operand;
import tree.Symbol;
import tree.Terminal;
import tree.Tree;

public class ObjectEvaluateRedoTest {

	@Test
	public void splitEquationsTest() {
		List<Symbol> symbols = new ArrayList<Symbol>();
		symbols.add(new Operand("+", true, true, 0));
		symbols.add(new NonTerminal("E"));
		symbols.add(new Operand("*", false, true, 1));
		symbols.add(new NonTerminal("E"));
		symbols.add(new Operand("+", true, true, 0));
		symbols.add(new NonTerminal("E"));
		symbols.add(new Operand("-", true, false, 0));
		symbols.add(new NonTerminal("E"));
		symbols.add(new Operand("/", false, false, 1));
		symbols.add(new NonTerminal("E"));

		Tree tree = new Tree(1, true);//readTree();

		CopyOfObjectEvaluate object = new CopyOfObjectEvaluate(tree);
		Assert.assertEquals(3, object.splitEquations(new Equation(symbols)).size());
	}
	
	@Test
	public void evaluateTrivalTwoTest() {
		List<Symbol> symbols = new ArrayList<Symbol>();
		symbols.add(new Operand("+", true, true, 0));
		symbols.add(new NonTerminal("E"));

		Tree tree = new Tree(1, true);//readTree();
		CopyOfObjectEvaluate object = new CopyOfObjectEvaluate(tree);
		Double[] d = new Double[3];
		d[0] = 1.0;
		d[1] = 3.0;
		d[2] = 3.0; // the goal
		object.examples.add(d);
		Assert.assertEquals(2, object.evaluateTrivalTwo(new Equation(symbols), 0, true).size());
	}
	
	@Test
	public void evaluateTrivalTwoTerminalTest() {
		List<Symbol> symbols = new ArrayList<Symbol>();
		symbols.add(new Operand("+", true, true, 0));
		symbols.add(new Terminal("K1", 3.0));

		Tree tree = new Tree(1, true);//readTree();
		CopyOfObjectEvaluate object = new CopyOfObjectEvaluate(tree);
		Double[] d = new Double[3];
		d[0] = 1.0;
		d[1] = 3.0;
		d[2] = 3.0; // the goal
		object.examples.add(d);
		Assert.assertEquals(1, object.evaluateTrivalTwo(new Equation(symbols), 0, false).size());
	}
	
	@Test
	public void evaluateEquationTest() {
		System.out.println("evaluateEquationTest");
		// the equation
		List<Symbol> symbols = new ArrayList<Symbol>();
		symbols.add(new NonTerminal("E"));
		symbols.add(new Operand("*", false, true, 1));
		symbols.add(new NonTerminal("E"));
		symbols.add(new Operand("+", true, true, 0));
		symbols.add(new NonTerminal("E"));
		symbols.add(new Operand("-", true, false, 0));
		symbols.add(new NonTerminal("E"));
		symbols.add(new Operand("/", false, false, 1));
		symbols.add(new NonTerminal("E"));
		Equation eq = new Equation(symbols);
		
		Tree tree = new Tree(1, true); //readTree();
		CopyOfObjectEvaluate object = new CopyOfObjectEvaluate(tree);
		Double[] d = new Double[3];
		d[0] = 1.0;
		d[1] = 3.0;
		d[2] = 3.0; // the goal
		object.examples.add(d);
		
		for(Tuple<Equation, Double> res :  object.evaluateEquation(eq, 0, true)) {
			System.out.println(res.y + "   " + res.x.toString());
		}
		Assert.assertEquals(2, object.bufferSolutions.size());

	}
	
	@Test
	public void evaluateEquationTerminalTest() {
		System.out.println("evaluateEquationTerminalTest");
		// the equation
		List<Symbol> symbols = new ArrayList<Symbol>();
		symbols.add(new Terminal("K1", 1.0));
		symbols.add(new Operand("*", false, true, 1));
		symbols.add(new Terminal("K2", 3.0));
		symbols.add(new Operand("+", true, true, 0));
		symbols.add(new Terminal("K4", 3.0));
		symbols.add(new Operand("-", true, false, 0));
		symbols.add(new Terminal("K0", 2.0));
		symbols.add(new Operand("/", false, false, 1));
		symbols.add(new Terminal("K1", 3.0));
		Equation eq = new Equation(symbols);
		
		Tree tree = new Tree(1, true); //readTree();
		CopyOfObjectEvaluate object = new CopyOfObjectEvaluate(tree);
		Double[] d = new Double[3];
		d[0] = 1.0;
		d[1] = 3.0;
		d[2] = 3.0; // the goal
		object.examples.add(d);
		
		for(Tuple<Equation, Double> res :  object.evaluateEquation(eq, 0, false)) {
			System.out.println(res.y + "   " + res.x.toString());
		}

	}
	
	@Test
	public void evaluateTrivialOneTest() {
		Tree tree = new Tree(1, true);//readTree();
		CopyOfObjectEvaluate object = new CopyOfObjectEvaluate(tree);
		Double[] d = new Double[3];
		d[0] = 1.0;
		d[1] = 3.0;
		d[2] = 3.0; // the goal
		object.examples.add(d);
		Assert.assertEquals(2, object.evaluateTrivialOne(0, true, null).size());
	}
	
	@Test
	public void checkAgainstOtherExamplesTest() {		
		List<Symbol> symbols = new ArrayList<Symbol>();
		symbols.add(new Terminal("K1", 3.0));
		symbols.add(new Operand("*", false, true, 1));
		symbols.add(new Terminal("K1", 3.0));


		Tree tree = new Tree(1, true);//readTree();
		CopyOfObjectEvaluate object = new CopyOfObjectEvaluate(tree);
		Double[] d = new Double[3];
		d[0] = 1.0;
		d[1] = 3.0;
		d[2] = 9.0; // the goal
		object.examples.add(d);
		object.examples.add(d);
		
		Assert.assertEquals(true, object.checkAgainstOtherExamples(new Equation(symbols)));

		object.bufferSolutions = new ArrayList<Equation>();
		Double[] d2 = new Double[3];
		d2[0] = 1.0;
		d2[1] = 4.0;
		d2[2] = 3.0; // the goal
		object.examples.add(d2);

		
		Assert.assertEquals(false, object.checkAgainstOtherExamples(new Equation(symbols)));
	}
	
	@Test
	public void evaluateTrivialOneTerminalTest() {
		List<Symbol> symbols = new ArrayList<Symbol>();
		symbols.add(new Terminal("K1", 3.0));
		Equation eq = new Equation(symbols);
		
		Tree tree = new Tree(1, true);//readTree();
		CopyOfObjectEvaluate object = new CopyOfObjectEvaluate(tree);
		Double[] d = new Double[3];
		d[0] = 1.0;
		d[1] = 3.0;
		d[2] = 3.0; // the goal
		object.examples.add(d);
		Assert.assertEquals(1, object.evaluateTrivialOne(0, false, eq).size());
	}
}
