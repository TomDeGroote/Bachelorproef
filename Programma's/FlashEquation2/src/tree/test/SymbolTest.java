package tree.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import tree.NonTerminal;
import tree.Operand;
import tree.Terminal;

public class SymbolTest {

	@Before
	public void setup() {
		
	}
	
	@Test
	public void nonTerminal() {
		NonTerminal nonTerminal = new NonTerminal("E");
		Assert.assertEquals("E", nonTerminal.toString());
		Assert.assertEquals(true, nonTerminal.isNonTerminal());
		Assert.assertEquals(false, nonTerminal.isOperand());
		Assert.assertEquals(false, nonTerminal.isTerminal());
	}
	
	@Test
	public void terminal() {
		Double value = 5.0;
		Terminal terminal = new Terminal("T", value);
		Assert.assertEquals("T", terminal.toString());
		Assert.assertEquals(false, terminal.isNonTerminal());
		Assert.assertEquals(false, terminal.isOperand());
		Assert.assertEquals(true, terminal.isTerminal());
		Assert.assertEquals(value, terminal.getValue());

	}
	
	@Test
	public void operand() {
		Operand operand = new Operand("*", false, true, 1);
		Assert.assertEquals("*", operand.toString());
		Assert.assertEquals(false, operand.isNonTerminal());
		Assert.assertEquals(true, operand.isOperand());
		Assert.assertEquals(false, operand.isTerminal());
	}
	
	@Test
	public void isEqual() {
		Operand o1 = new Operand("*", false, true, 1);
		Operand o2 = new Operand("*", false, true, 1);
		Operand o3 = new Operand("-", true, false, 0);
		Assert.assertEquals(o1, o2);
		Assert.assertNotEquals(o1, o3);
	}

}
