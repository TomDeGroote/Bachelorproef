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
	Equation eq3;
	String eqString1 = "E+E*E+E";
	String eqString2 = "E+E+E*E";
	String eqString3 = "E*E*E-E";
	
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
		
		// generate equation 3 E*E*E-E
		List<Symbol>inputEq3 = new ArrayList<Symbol>();
		inputEq3.add(new NonTerminal("E"));
		inputEq3.add(new Operand("*"));
		inputEq3.add(new NonTerminal("E"));
		inputEq3.add(new Operand("*"));
		inputEq3.add(new NonTerminal("E"));
		inputEq3.add(new Operand("-"));
		inputEq3.add(new NonTerminal("E"));
		eq3 = new Equation(inputEq3);
	}
	
	@Test
	public void representiveEquations() {
		List<String> strings = new ArrayList<String>();
		strings.add("E+E*E+E");
		List<Equation> equations = new ArrayList<Equation>();
		equations.add(eq1);
		equations.add(eq2);
		equations.add(eq3);
		List<Equation> expected = new ArrayList<Equation>();
		expected.add(eq1);
		List<Equation> actual = PruneRules.representiveEquations(strings, equations);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void splitEquation() {
		List<String> split = new ArrayList<String>();
		// based on eq1
		split.add("E");
		split.add("+E*E");
		split.add("+E");
		Assert.assertEquals(split, PruneRules.splitEquation(eq1));
	}
	
	@Test
	public void splitIntoParts() {
		// based on eq1
		List<String> split1 = new ArrayList<String>();
		split1.add("E");
		split1.add("+E*E");
		split1.add("+E");
		// based on eq3
		List<String> split3 = new ArrayList<String>();
		split3.add("E*E*E");
		split3.add("-E");
		// the list of a list of strings
		List<List<String>> splits = new ArrayList<List<String>>();
		splits.add(split1);
		splits.add(split3);
		// puts eq1 and eq3 in a list
		List<Equation> eqs = new ArrayList<Equation>();
		eqs.add(eq1);
		eqs.add(eq3);
		Assert.assertEquals(splits, PruneRules.splitIntoParts(eqs));
	}
	
	@Test
	public void equationContainsTerm() {
		// based on eq1
		List<String> split1 = new ArrayList<String>();
		split1.add("E");
		split1.add("+E*E");
		split1.add("+E");
		// based on eq1 after removing term +E*E
		List<String> splitAfterRemove = new ArrayList<String>();
		splitAfterRemove.add("E");
		splitAfterRemove.add("+E");
		Assert.assertEquals(splitAfterRemove, PruneRules.equationContainsTerm("+E*E", split1));
		Assert.assertEquals(null, PruneRules.equationContainsTerm("E*E", split1));
	}
	
	@Test
	public void createOneStringEquation() {
		// based on eq1
		List<String> split1 = new ArrayList<String>();
		split1.add("E");
		split1.add("+E*E");
		split1.add("+E");
		Assert.assertEquals(eqString1, PruneRules.createOneStringEquation(split1));
	}
	
	@Test
	public void areTheseEquationsEquivalent() {
		// based on eq1
		List<String> split1 = new ArrayList<String>();
		split1.add("E");
		split1.add("+E*E");
		split1.add("+E");
		// based on eq2
		List<String> split2 = new ArrayList<String>();
		split2.add("E");
		split2.add("+E");
		split2.add("+E*E");
		// based on eq3
		List<String> split3 = new ArrayList<String>();
		split3.add("E*E*E");
		split3.add("-E");
		Assert.assertEquals(true, PruneRules.areTheseEquationsEquivalent(split1, split2));
		Assert.assertEquals(false, PruneRules.areTheseEquationsEquivalent(split1, split3));
	}
	
	
	/**
	 * Generates an equation with length =
	 * 			length if length is odd
	 * 			length + 1 if length is even
	 */
	@SuppressWarnings("unused")
	private Equation generateRandomEquation(int length) {
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
