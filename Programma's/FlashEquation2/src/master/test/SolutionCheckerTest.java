package master.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import master.SolutionChecker;
import master.all.ObjectEvaluateAllSolutions;
import master.normal.ObjectMaster;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import tree.Equation;
import tree.NonTerminal;
import tree.Operand;
import tree.Symbol;
import tree.Terminal;
import tree.Tree;

public class SolutionCheckerTest {

	private Equation eq1;
	private Equation eq2;
	private Equation eqTerminal1;
	private Equation eqTerminal2;
	private Equation eqTerminal3;
	
	
	@Before
	public void initialize() throws ClassNotFoundException, IOException {
		
		List<Symbol> inputEq1 = new ArrayList<Symbol>();
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+", true,true, 0));
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("*", false,true, 1));
		inputEq1.add(new NonTerminal("E"));
		inputEq1.add(new Operand("+", true,true, 0));
		inputEq1.add(new NonTerminal("E"));
		eq1 = new Equation(inputEq1);
		
		List<Symbol> inputEq2 = new ArrayList<Symbol>();
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("-", true,false, 0));
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("+", true,true, 0));
		inputEq2.add(new NonTerminal("E"));
		inputEq2.add(new Operand("*", false,true, 1));
		inputEq2.add(new NonTerminal("E"));
		eq2 = new Equation(inputEq2);
		
		List<Symbol> inputEqTerminal1 = new ArrayList<Symbol>();
		inputEqTerminal1.add(new Terminal("K1", 3.0));
		inputEqTerminal1.add(new Operand("+", true,true, 0));
		inputEqTerminal1.add(new Terminal("K0", 5.0));
		inputEqTerminal1.add(new Operand("*", false,true, 1));
		inputEqTerminal1.add(new Terminal("K1", 3.0));
		inputEqTerminal1.add(new Operand("+", true,true, 0));
		inputEqTerminal1.add(new Terminal("K0", 5.0));
		eqTerminal1 = new Equation(inputEqTerminal1);
		
		List<Symbol> inputEqTerminal2 = new ArrayList<Symbol>();
		inputEqTerminal2.add(new Terminal("K1", 3.0));
		inputEqTerminal2.add(new Operand("+", true,true, 0));
		inputEqTerminal2.add(new Terminal("K1", 3.0));
		inputEqTerminal2.add(new Operand("*", false,true, 1));
		inputEqTerminal2.add(new Terminal("K0", 5.0));
		inputEqTerminal2.add(new Operand("+", true,true, 0));
		inputEqTerminal2.add(new Terminal("K0", 5.0));
		eqTerminal2 = new Equation(inputEqTerminal2);
		
		List<Symbol> inputEqTerminal3 = new ArrayList<Symbol>();
		inputEqTerminal3.add(new Terminal("K1", 3.0));
		inputEqTerminal3.add(new Operand("+", true,true, 0));
		inputEqTerminal3.add(new Terminal("K1", 3.0));
		inputEqTerminal3.add(new Operand("+", false,true, 1));
		inputEqTerminal3.add(new Terminal("K0", 5.0));
		inputEqTerminal3.add(new Operand("+", true,true, 0));
		inputEqTerminal3.add(new Terminal("K0", 5.0));
		eqTerminal3 = new Equation(inputEqTerminal3);

	}
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void sameEquation() {
		Assert.assertEquals(true,SolutionChecker.compareEquations(eqTerminal1, eqTerminal1));
		
	}
	
	@Test
	public void equivalentEquation() {
		Assert.assertEquals(true, SolutionChecker.compareEquations(eqTerminal1,eqTerminal2));
	}
	
	@Test
	public void differentEquations() {
		Assert.assertEquals(false, SolutionChecker.compareEquations(eqTerminal1, eqTerminal3));
	}

}
