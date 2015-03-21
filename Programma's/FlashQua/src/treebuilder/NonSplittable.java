package treebuilder;

import java.util.ArrayList;
import java.util.List;

import treebuilder.symbols.Symbol;
import treebuilder.symbols.Terminal;
import treebuilder.symbols.operands.Operand;

public class NonSplittable {

	private final List<Symbol> symbols;
	private final double value;
	
	public NonSplittable(Operand nextOperand, Terminal nextTerminal) {
		this.symbols = new ArrayList<Symbol>();
		this.value = nextOperand.calculateValue(nextTerminal.getValue());
		symbols.add(nextOperand);
		symbols.add(nextTerminal);
	}
	
	public NonSplittable(NonSplittable previous, Operand nextOperand, Terminal nextTerminal) {
		this.symbols = new ArrayList<Symbol>(previous.getSymbols());
		this.value = nextOperand.calculateValue(previous.getValue(), nextTerminal.getValue());
		symbols.add(nextOperand);
		symbols.add(nextTerminal);
	}

	/**
	 * @return The first operand of this NonSplitable
	 */
	public Operand getFirstOperand() {
		return (Operand) symbols.get(0);
	}
	
	/**
	 * @return The symbols of this nonSplitable
	 */
	public List<Symbol> getSymbols() {
		return symbols;
	}

	/**
	 * @return the value of this nonSplitable
	 */
	public double getValue() {
		return value;
	}
	
}
