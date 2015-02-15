package tree.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import tree.Equation;
import tree.Grammar;
import tree.NonTerminal;
import tree.Operand;
import tree.Symbol;

import com.fathzer.soft.javaluator.DoubleEvaluator;

public class GrammarTest {

	@Test
	public void expand() {
		List<Equation> expansion = new ArrayList<Equation>();
		// E*E
		List<Symbol> mul = new ArrayList<Symbol>();
		mul.add(new NonTerminal("E"));
		mul.add(new Operand("*", false,true, 1));
		mul.add(new NonTerminal("E"));
		expansion.add(new Equation(mul));
		
		// E/E
		List<Symbol> div = new ArrayList<Symbol>();
		div.add(new NonTerminal("E"));
		div.add(new Operand("/", false,false, 1));
		div.add(new NonTerminal("E"));
		expansion.add(new Equation(div));
		
		// E+E
		List<Symbol> sum = new ArrayList<Symbol>();
		sum.add(new NonTerminal("E"));
		sum.add(new Operand("+", false,true, 0));
		sum.add(new NonTerminal("E"));
		expansion.add(new Equation(sum));
		
		// E-E
		List<Symbol> sub = new ArrayList<Symbol>();
		sub.add(new NonTerminal("E"));
		sub.add(new Operand("-", false,false, 0));
		sub.add(new NonTerminal("E"));
		expansion.add(new Equation(sub));
		
		Assert.assertEquals(expansion, Grammar.expand(new Equation()));
	}
	
	/**
	 * TestClass to show how slow javaluator is
	 */
	@Test
	public void getValueTest() {
		Assert.assertEquals(true, 5.0 == new DoubleEvaluator().evaluate(4.0 + new Operand("+", true, true, 0).toString() + 1.0));
		Assert.assertEquals(true, 4.0 == new DoubleEvaluator().evaluate(4.0 + new Operand("*", true, true, 1).toString() + 1.0));
		Assert.assertEquals(true, 4.0 == new DoubleEvaluator().evaluate(4.0 + new Operand("/", true, true, 1).toString() + 1.0));
		Assert.assertEquals(true, 3.0 == new DoubleEvaluator().evaluate(4.0 + new Operand("-", true, true, 0).toString() + 1.0));
	}
	
	@Test
	public void getValue2Test() {
		Assert.assertEquals(true, 5.0 == Grammar.getValue(4.0, new Operand("+", true, true, 0), 1.0));
		Assert.assertEquals(true, 4.0 == Grammar.getValue(4.0, new Operand("*", false, true, 1), 1.0));
		Assert.assertEquals(true, 2.0 == Grammar.getValue(4.0, new Operand("/", false, false, 1), 2.0));
		Assert.assertEquals(true, 4.0 == Grammar.getValue(5.0, new Operand("-", true, false, 0), 1.0));
	}

}
