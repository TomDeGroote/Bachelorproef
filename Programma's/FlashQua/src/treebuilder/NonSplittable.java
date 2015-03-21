package treebuilder;

import java.util.ArrayList;
import java.util.List;

import treebuilder.symbols.Symbol;
import treebuilder.symbols.Terminal;
import treebuilder.symbols.operands.Multiplication;
import treebuilder.symbols.operands.Operand;
import treebuilder.symbols.operands.Sum;

public class NonSplittable {

	private final List<Symbol> symbols;
	private final double value;
	
	/**
	 * @param nextOperand
	 * 			Should be splittableOperand
	 * @param nextTerminal
	 */
	public NonSplittable(Operand nextOperand, Terminal nextTerminal) {
		this.symbols = new ArrayList<Symbol>();
		this.value = (new Sum()).calculateValue(nextTerminal.getValue());
		symbols.add(nextOperand);
		symbols.add(nextTerminal);
	}
	
	private NonSplittable(List<Symbol> symbols, double value) {
		this.symbols = symbols;
		this.value = value;
	}
	
	/**
	 * 
	 * @param previous
	 * @param nextOperand
	 * 			Should be NonSplittable operand
	 * @param nextTerminal
	 */
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
	 * @return The symbols of this nonSplitable
	 */
	public NonSplittable getInverseNonSplittable() {
		List<Symbol> inverseSymbols = new ArrayList<Symbol>(getSymbols());
		inverseSymbols.set(0, ((Operand) inverseSymbols.get(0)).getInverseOperand());
		return new NonSplittable(inverseSymbols, (new Sum()).calculateValue(getValue()));
	}

	/**
	 * @return the value of this nonSplitable
	 * always positive!
	 */
	public double getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		NonSplittable otherNonSplittable = (NonSplittable) obj;
		
		if(!otherNonSplittable.getFirstOperand().equals(this.getFirstOperand())) {
			return false;
		}
		
		if(otherNonSplittable.getValue() != this.getValue()) {
			return false;
		}
		
		if(otherNonSplittable.getSymbols().size() != this.getSymbols().size()) {
			return false;
		}
		
		List<Symbol> copyOfSymbolsThis = new ArrayList<Symbol>(this.getSymbols());
		List<Symbol> copyOfSymbolsOther = new ArrayList<Symbol>(this.getSymbols());
		
		copyOfSymbolsOther.set(0, new Multiplication());
		copyOfSymbolsThis.set(0, new Multiplication());
		
		for(int i = 0; i < copyOfSymbolsOther.size(); i = i+2) {
			boolean found = false;
			for(int j = 0; j < copyOfSymbolsThis.size(); j = j+2) {
				if(copyOfSymbolsOther.get(i).equals(copyOfSymbolsThis.get(j))) {
					if(copyOfSymbolsOther.get(i+1).equals(copyOfSymbolsThis.get(j+1))) {
						copyOfSymbolsThis.remove(j);
						copyOfSymbolsThis.remove(j);
						found = true;
						break;
					}
				}
			}
			if(!found) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		String result = "";
		for(Symbol s : this.getSymbols()) {
			result += s.toString();
		}
		return result;
	}
	
	
}
