package tree.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import tree.Equation;
import tree.NonTerminal;
import tree.Operand;
import tree.PruneRules;
import tree.Symbol;

public class PruneRulesTest {
	
	Equation eq1;
	Equation eq2;
	String eqString1 = "E+E*E+E";
	String eqString2 = "E+E+E*E";
	
	@Before
	public void setUp() {
		// generate equation 1 E+E*E+E
		List<Symbol> inputEq1 = new ArrayList<Symbol>();
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+"));
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("*"));
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+"));
		inputEq1.add(new NonTerminal("E"));
		eq1 = new Equation(inputEq1);
		
		// generate equation 2 E+E+E*E
		List<Symbol> inputEq2 = new ArrayList<Symbol>();
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("+"));
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("+"));
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("*"));
		inputEq2.add(new NonTerminal("E"));
		eq2 = new Equation(inputEq2);	
	}
	
	@Test
	public void representiveEquations() {
		List<String> strings = new ArrayList<String>();
		strings.add("E+E*E+E");
		List<Equation> equations = new ArrayList<Equation>();
		equations.add(eq1);
		equations.add(eq2);
		List<Equation> expected = new ArrayList<Equation>();
		expected.add(eq1);
		List<Equation> actual = PruneRules.representiveEquations(strings, equations);
		Assert.assertEquals(expected.size(), actual.size());
		Assert.assertEquals(expected.get(0), actual.get(0));
	}
	
	
	
	
	/**
	 * Generates an equation with length =
	 * 			length if length is odd
	 * 			length + 1 if length is even
	 */
	public Equation generateRandomEquation(int length) {
		Random randomGenerator = new Random();
		
		// generate the input list for the equation
		List<Symbol> inputEq = new ArrayList<Symbol>();
		inputEq.add(new NonTerminal("E"));
		for(int i = 1; i < length; i = i+2) {
			// add a random operand
			int random = randomGenerator.nextInt(4);
			switch (random) {
			case 0:
				inputEq.add(new Operand("+"));
				break;
			case 1:
				inputEq.add(new Operand("-"));
				break;
			case 2:
				inputEq.add(new Operand("*"));
				break;
			case 3:
				inputEq.add(new Operand("/"));
				break;	
			}
			// add a nonTerminal
			inputEq.add(new NonTerminal("E"));
		}
		
		return new Equation(inputEq);	
		
	}
	

}
