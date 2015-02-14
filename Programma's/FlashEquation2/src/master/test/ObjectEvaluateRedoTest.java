package master.test;

import java.util.ArrayList;
import java.util.List;

import master.CopyOfObjectEvaluate;

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
		Assert.assertEquals(1, object.bufferSolutions.size());
		Assert.assertEquals("K1", object.bufferSolutions.get(0).getListOfSymbols().get(1).toString());

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
		Assert.assertEquals(1, object.bufferSolutions.size());
		Assert.assertEquals("K1", object.bufferSolutions.get(0).getListOfSymbols().get(0).toString());

	}
}
