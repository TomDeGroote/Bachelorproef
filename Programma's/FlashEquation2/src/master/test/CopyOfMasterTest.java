package master.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import master.CopyOfMaster;

import org.junit.Assert;
import org.junit.Test;

import tree.Equation;
import tree.Operand;
import tree.Symbol;
import tree.Terminal;

public class CopyOfMasterTest {

	@Test
	public void checkSolutionSpaceTest() {
		List<Symbol> symbols = new ArrayList<Symbol>();
		symbols.add(new Terminal("K0", 1.0));
		symbols.add(new Operand("+", true, true));
		symbols.add(new Terminal("K1", 2.0));
		Equation solution = new Equation(symbols);
		CopyOfMaster.solutionSpace.add(solution);
		
		HashMap<String, Double> Ks = new HashMap<String, Double>();
		Ks.put("K0", 3.0);
		Ks.put("K1", 5.0);
		Ks.put(CopyOfMaster.getNameOfGoalK(), 8.0);
		
		CopyOfMaster.checkSolutionSpace(Ks);
		Assert.assertEquals(true, CopyOfMaster.solutionSpace.contains(solution));
		
	}

}
