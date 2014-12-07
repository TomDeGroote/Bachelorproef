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

	List<Symbol> inputEq1;
	List<Symbol> inputEq2;
	List<Symbol> inputEq3;
	Equation eq1;
	Equation eq2;
	Equation eq3;
	String eqString1 = "E+E*E+E";


	@Before
	public void setup() {
		// generate equation 1 E+E*E+E
		inputEq1 = new ArrayList<Symbol>();
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+"));
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("*"));
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+"));
		inputEq1.add(new NonTerminal("E"));
		eq1 = new Equation(inputEq1);
		
		// generate equation 2 E+E+E*E
		inputEq2 = new ArrayList<Symbol>();
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("+"));
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("+"));
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("*"));
		inputEq2.add(new NonTerminal("E"));
		eq2 = new Equation(inputEq2);
		
		// generate equation 3 E+E*E+E
		inputEq3 = new ArrayList<Symbol>();
		inputEq3.add(new NonTerminal("E"));
		inputEq3.add(new Operand("+"));
		inputEq3.add(new NonTerminal("E"));
		inputEq3.add(new Operand("*"));
		inputEq3.add(new NonTerminal("E"));
		inputEq3.add(new Operand("+"));
		inputEq3.add(new NonTerminal("E"));
		eq3 = new Equation(inputEq1);
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
		Assert.assertEquals(symbols, eq.getListOfSymbols());
	}
	
	@Test
	public void equationToString() {
		Assert.assertEquals("E+E*E+E", eq1.toString());
	}
	
	@Test
	public void equationSumbolList() {
		Assert.assertEquals(inputEq1, eq1.getListOfSymbols());
	}
	
	@Test
	public void isEqual() {
		Assert.assertEquals(eq1, eq3);
		Assert.assertNotEquals(eq1, eq2);
	}

}
