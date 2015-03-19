package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import tree.Equation;
import tree.symbols.Symbol;
import tree.symbols.Terminal;
import tree.symbols.operands.Multiplication;
import tree.symbols.operands.Sum;

public class EquationTest {

	@Test
	public void equalEquationTest() {
		List<List<Symbol>> parts1 = new ArrayList<List<Symbol>>();
		List<Symbol> part1 = new ArrayList<Symbol>();
		part1.add(new Sum());
		part1.add(new Terminal("K0", 5.0, false));
		part1.add(new Multiplication());
		part1.add(new Terminal("W3", 3.0, true));
		List<Symbol> part2 = new ArrayList<Symbol>();
		part2.add(new Sum());
		part2.add(new Terminal("K1", 6.0, false));
		parts1.add(part1);
		parts1.add(part2);
		
		List<List<Symbol>> parts2 = new ArrayList<List<Symbol>>();
		List<Symbol> part3 = new ArrayList<Symbol>();
		part3.add(new Sum());
		part3.add(new Terminal("K1", 6.0, false));
		List<Symbol> part4 = new ArrayList<Symbol>();
		part4.add(new Sum());
		part4.add(new Terminal("K0", 5.0, false));
		part4.add(new Multiplication());
		part4.add(new Terminal("W3", 3.0, true));
		parts2.add(part3);
		parts2.add(part4);
		
		Equation eq1 = new Equation(new Terminal("Fuck", 2.0, false));
		eq1.value = 31.0;
		eq1.nonSplitableParts = parts1;
		
		Equation eq2 = new Equation(new Terminal("Fuck", 2.0, false));
		eq2.value = 31.0;
		eq2.nonSplitableParts = parts2;
		
		System.out.println(eq1);
		System.out.println(eq2);
		Assert.assertEquals(true, eq1.equals(eq2));
	}

}
