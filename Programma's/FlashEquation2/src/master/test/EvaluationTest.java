package master.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import tree.Equation;
import tree.NonTerminal;
import tree.Operand;
import tree.Symbol;

public class EvaluationTest {

	@Test
	public void testPointersHashmap() {
		HashMap<Equation, Boolean> testMap = new HashMap<Equation, Boolean>();
		
		// generate equation 1 E+E*E+E
		List<Symbol> inputEq1 = new ArrayList<Symbol>();
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+", true,true));
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("*", false,true));
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+", true,true));
		inputEq1.add(new NonTerminal("E"));
		Equation eq1 = new Equation(inputEq1);
		
		// generate equation 2 E+E*E+E
		List<Symbol> inputEq2 = new ArrayList<Symbol>();
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("+", true,true));
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("*", false,true));
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("+", true,true));
		inputEq2.add(new NonTerminal("E"));
		Equation eq2 = new Equation(inputEq2);	
		
		testMap.put(eq1, true);
		Assert.assertEquals(true, testMap.containsKey(eq2));
		Assert.assertEquals(true, testMap.get(eq1));
	}
	
	

}
