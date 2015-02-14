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
		Assert.assertEquals(2, object.evaluateTrivalTwo(new Equation(symbols)).size());
	}
	
	@Test
	public void evaluateEquationTest() {
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
		
		for(Tuple<Equation, Double> res :  object.evaluateEquation(eq)) {
			System.out.println(res.y + "   " + res.x.toString());
		}
		Assert.assertEquals(2, object.bufferSolutions.size());

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
		Assert.assertEquals(2, object.evaluateTrivialOne().size());
	}
}
