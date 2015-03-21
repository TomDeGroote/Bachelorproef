package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import treebuilder.Equation;
import treebuilder.NonSplittable;
import treebuilder.symbols.ColumnValue;
import treebuilder.symbols.Weight;
import treebuilder.symbols.operands.Multiplication;
import treebuilder.symbols.operands.Sum;

public class EquationTest {

	@Test
	public void equalEquationTest() {
		List<NonSplittable> parts1 = new ArrayList<NonSplittable>();
		NonSplittable part1 = new NonSplittable(new Sum(), new ColumnValue(5.0, 0));
		part1 = new NonSplittable(part1, new Multiplication(), new Weight(3.0));
		NonSplittable part2 = new NonSplittable(new Sum(), new ColumnValue(6.0, 1));
		parts1.add(part1);
		parts1.add(part2);
		
		List<NonSplittable> parts2 = new ArrayList<NonSplittable>();
		NonSplittable part3 = new NonSplittable(new Sum(), new ColumnValue(6.0, 1));
		NonSplittable part4 = new NonSplittable(new Sum(), new ColumnValue(5.0, 0));
		part4 = new NonSplittable(part4, new Multiplication(), new Weight(3.0));
		parts2.add(part3);
		parts2.add(part4);
		
		Equation eq1 = new Equation(new ColumnValue(2.0, 5), 5);
		eq1.value = 31.0;
		eq1.nonSplitableParts = parts1;
		
		Equation eq2 = new Equation(new ColumnValue(2.0, 5), 5);
		eq2.value = 31.0;
		eq2.nonSplitableParts = parts2;
		
		System.out.println(eq1);
		System.out.println(eq2);
		Assert.assertEquals(true, eq1.equals(eq2));
		Assert.assertEquals(true, eq1.hashCode() == eq2.hashCode());

	}
	
	@Test
	public void equalNonSplittablePartsTest() {
		
		// +K0*W3 = 15
		NonSplittable part1 = new NonSplittable(new Sum(), new ColumnValue(5.0, 0));
		part1 = new NonSplittable(part1, new Multiplication(), new Weight(3.0));
		// +K1 = 6
		NonSplittable part2 = new NonSplittable(new Sum(), new ColumnValue(6.0, 1));
		
		// +K1 = 6
		NonSplittable part3 = new NonSplittable(new Sum(), new ColumnValue(6.0, 1));
		// +K0*W3 = 15
		NonSplittable part4 = new NonSplittable(new Sum(), new ColumnValue(5.0, 0));
		part4 = new NonSplittable(part4, new Multiplication(), new Weight(3.0));
		
		Assert.assertEquals(true, part1.equals(part4));
		Assert.assertEquals(true, part2.equals(part3));
	}

}
