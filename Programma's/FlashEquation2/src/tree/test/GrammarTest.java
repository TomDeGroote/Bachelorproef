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

public class GrammarTest {

	@Test
	public void expand() {
		List<Equation> expansion = new ArrayList<Equation>();
		// E*E
		List<Symbol> mul = new ArrayList<Symbol>();
		mul.add(new NonTerminal("E"));
		mul.add(new Operand("*", false,true));
		mul.add(new NonTerminal("E"));
		expansion.add(new Equation(mul));
		
		// E/E
		List<Symbol> div = new ArrayList<Symbol>();
		div.add(new NonTerminal("E"));
		div.add(new Operand("/", false,false));
		div.add(new NonTerminal("E"));
		expansion.add(new Equation(div));
		
		// E+E
		List<Symbol> sum = new ArrayList<Symbol>();
		sum.add(new NonTerminal("E"));
		sum.add(new Operand("+", false,true));
		sum.add(new NonTerminal("E"));
		expansion.add(new Equation(sum));
		
		// E-E
		List<Symbol> sub = new ArrayList<Symbol>();
		sub.add(new NonTerminal("E"));
		sub.add(new Operand("-", false,false));
		sub.add(new NonTerminal("E"));
		expansion.add(new Equation(sub));
		
		Assert.assertEquals(expansion, Grammar.expand(new Equation()));
	}

}
