package tree.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import tree.Equation;
import tree.NonTerminal;
import tree.Operand;
import tree.Symbol;

public class EquationTest {
	
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
		// generate equation 1 E+E*E+E
		List<Symbol> inputEq1 = new ArrayList<Symbol>();
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+"));
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("*"));
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+"));
		inputEq1.add(new NonTerminal("E"));
		Equation eq1 = new Equation(inputEq1);
		Assert.assertEquals("E+E*E+E", eq1.toString());
	}
	
	@Test
	public void equationSumbolList() {
		// generate equation 1 E+E*E+E
		List<Symbol> inputEq1 = new ArrayList<Symbol>();
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+"));
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("*"));
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+"));
		inputEq1.add(new NonTerminal("E"));
		Equation eq1 = new Equation(inputEq1);
		Assert.assertEquals(inputEq1, eq1.getListOfSymbols());
	}

}
