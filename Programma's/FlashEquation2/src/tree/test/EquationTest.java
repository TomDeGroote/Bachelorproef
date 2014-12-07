package tree.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import tree.Equation;
import tree.NonTerminal;
import tree.Operand;
import tree.Symbol;

public class EquationTest {

	Equation eq1;
	Equation eq2;
	
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
	public void constructorNoInputToString() {
		Equation eq = new Equation();
		List<Symbol> symbols = new ArrayList<Symbol>();
		symbols.add(new NonTerminal("E"));
		Assert.assertEquals("E", eq.toString());
	}
	
	@Test
	public void constructorNoInputSymbolList() {
		Equation eq = new Equation();
		List<Symbol> symbols = new ArrayList<Symbol>();
		symbols.add(new NonTerminal("E"));
		Assert.assertEquals(1, eq.getListOfSymbols().size());
		Assert.assertEquals(symbols, eq.getListOfSymbols());
	}

}
