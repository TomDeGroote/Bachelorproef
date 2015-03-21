package treebuilder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import treebuilder.symbols.Symbol;
import treebuilder.symbols.Terminal;
import treebuilder.symbols.operands.Multiplication;
import treebuilder.symbols.operands.Operand;
import treebuilder.symbols.operands.Sum;

/**
 * Represents an equation that can be formed by combining symbols.
 * @author Jeroen en Tom
 *
 */
@SuppressWarnings("serial")
public class Equation implements Serializable {
	
	public List<NonSplittable> nonSplitableParts = new ArrayList<NonSplittable>();
	private double valueRestOfEquation = 0.0;
	private boolean pruned = false;
	public double value = 0.0;
	private List<Integer> terminalCounter = new ArrayList<Integer>();
	private final int HASHCODE;
	
	/**
	 * Constructor when no parameters are given
	 * This constructor will generate an equation = E
	 * 
	 */
	public Equation(Terminal t, int nrOfColumns) {
		NonSplittable firstPart = new NonSplittable(new Sum(), t);
		nonSplitableParts.add(firstPart);
		this.value = firstPart.getValue();
		this.HASHCODE = getHashCode();
		for(int i = 0; i < nrOfColumns; i++) {
			terminalCounter.add(0);
		}
		terminalCounter.add(t.getNumber(), 1);
	}
	

	public Equation(List<NonSplittable> nonSplitableParts, double valueRestOfEquation, double value, List<Integer> terminalCounter) {
//		System.nanoTime(); // Increases time.. A Lot..
		this.nonSplitableParts = nonSplitableParts;
		this.valueRestOfEquation = valueRestOfEquation;
		this.value = value;
		this. terminalCounter = terminalCounter;
		this.HASHCODE = getHashCode();
	}
	

	public static Equation createEquation(Equation previous, Operand operand, Terminal terminal, double goal) {
		
		// there should only be one alone standing constant in the equation TODO zorg dat die in de equation staat
		if(operand.isSplitable() && terminal.isWeight()) {
			return null;
		}
		
		// if the weight is the neutral element of the operand than it is useless
		if(terminal.isWeight() && operand.getNeutralElement().equals(terminal.getValue())) {
			return null;
		}
		
		if(operand.isSplitable()) {
			// The value of the terminal we are going to add should be bigger than (or equal) to the last nonSplittable part that starts with the same operand
			for(int i = previous.getEquationParts().size()-1; i >= 0; i--) {
				if(previous.getEquationParts().get(i).getFirstOperand().equals(operand)) {
					if(previous.getEquationParts().get(i).getValue() > terminal.getValue()) {
						return null;
					}
					break;
				}
			}
			
			// There should not be a -K1 if there is a +K1
			if(previous.getEquationParts().contains(new NonSplittable(operand.getInverseOperand(), terminal))) {
				return null;
			}
			
			// the new part that may be added 
			NonSplittable newNonSplittable = new NonSplittable(operand, terminal);
			
			// There should not be a +K1 if there is a +K1 (replaced by weights)
			if(previous.getEquationParts().contains(newNonSplittable)) {
				return null;
			}
			
			// create equation and return it
			List<NonSplittable> newNonSplittableParts = new ArrayList<NonSplittable>(previous.getEquationParts());
			newNonSplittableParts.add(newNonSplittable);
			
			double newRestOfEquationValue = previous.getLastNonSplittable().getFirstOperand().calculateValue(previous.getValueRestOfEquation(), previous.getLastNonSplittable().getValue());
			double newValue = operand.calculateValue(newRestOfEquationValue, terminal.getValue());
			
			List<Integer> newTerminalCounter = new ArrayList<Integer>(previous.getTerminalCounter());
			newTerminalCounter.set(terminal.getNumber(), newTerminalCounter.get(terminal.getNumber()) + 1);
			
			return new Equation(newNonSplittableParts, newRestOfEquationValue, newValue, newTerminalCounter);
			
		} else {
			// W1.0*K1 should not exist
			if(previous.getLastNonSplittable().getLastTerminal().isWeight() && previous.getLastNonSplittable().getLastTerminal().getValue() == operand.getNeutralElement()) {
				return null;
			}
			
			// The value of the terminal we are going to add should be bigger than (or equal) to the last terminal part that starts with the same operand
			for(int i = previous.getLastNonSplittable().getSymbols().size()-2; i >= 0; i = i-2) {
				if(previous.getLastNonSplittable().getSymbols().get(i).equals(operand)) {
					if(((Terminal) previous.getLastNonSplittable().getSymbols().get(i+1)).getValue() > terminal.getValue()) {
						return null;
					}
					break;
				}
			}
			
			NonSplittable newNonSplittable = new NonSplittable(previous.getLastNonSplittable(), operand, terminal);

			// The value of the terminal we are going to add keep the new NonSplittable part bigger than (or equal) to the last nonSplittable part that starts with the same operand
			// Can not be done because the equation can't start with a minus!!
//			for(int i = previous.getEquationParts().size()-2; i >= 0; i--) {
//				if(previous.getEquationParts().get(i).getFirstOperand().equals(newNonSplittable.getFirstOperand())) {
//					if(previous.getEquationParts().get(i).getValue() > newNonSplittable.getValue()) {
//						return null;
//					}
//					break;
//				}
//			}
			
			// There should not be a K0/K0
			List<Symbol> nonSplittablePrevious = new ArrayList<Symbol>(previous.getLastNonSplittable().getSymbols());
			nonSplittablePrevious.set(0, new Multiplication());
			for(int i = 0; i < nonSplittablePrevious.size(); i += 2) {
				if(nonSplittablePrevious.get(i).equals(operand.getInverseOperand()) && nonSplittablePrevious.get(i+1).equals(terminal)) {
					return null;
				}
			}
			
			// There should not be a -K0*K1 if there is a +K0*K1
			NonSplittable inverseNonSplittable = newNonSplittable.getInverseNonSplittable();
			if(previous.getEquationParts().contains(inverseNonSplittable)) {
				return null;
			}
			
			// There should not be a +K0*K1 if it already has a +K0*K1, replace by weights
			if(previous.getEquationParts().contains(newNonSplittable)) {
				return null;
			}
			
			// create equation
			List<NonSplittable> newNonSplittableParts = new ArrayList<NonSplittable>(previous.getEquationParts());
			newNonSplittableParts.set(newNonSplittableParts.size()-1, newNonSplittable);
			
			double newValue = newNonSplittable.getFirstOperand().calculateValue(previous.getValueRestOfEquation(), newNonSplittable.getValue());
			
			List<Integer> newTerminalCounter = new ArrayList<Integer>(previous.getTerminalCounter());
			newTerminalCounter.set(terminal.getNumber(), newTerminalCounter.get(terminal.getNumber()) + 1);

			return new Equation(newNonSplittableParts, previous.getValueRestOfEquation(), newValue, newTerminalCounter);
		}
	}
	
	/**
	 * @return The last nonSplittable part of this equation
	 */
	public NonSplittable getLastNonSplittable() {
		return this.getEquationParts().get(this.getEquationParts().size()-1);
	}
	
	/**
	 * @return The split parts of this equation
	 */
	public List<NonSplittable> getEquationParts() {
		return nonSplitableParts;
	}
	
	/**
	 * @return The value of this equation
	 */
	public double getValueOfEquation() {
		return value;
	}
	
	/**
	 * @return The value of the rest of this equation
	 */
	public double getValueRestOfEquation() {
		return valueRestOfEquation;
	}
	
	/**
	 * @return The value of this equation
	 */
	public static double calculateValueOfEquation(Operand lastSplitableOperand, double valueRestOfEquation, double valueOfLastSplitable) {
		// operand between rest of equation and lastNonSplitablePart can be found at the first position of the lastNonSplitable part
		return lastSplitableOperand.calculateValue(valueRestOfEquation, valueOfLastSplitable);
	}

	/**
	 * @return Returns the list of symbols of this equation, the symbols are in order.
	 */
	public List<Symbol> getListOfSymbols() {
		List<Symbol> symbols = new ArrayList<Symbol>();
		for(NonSplittable part : nonSplitableParts) {
			for(Symbol symbol : part.getSymbols()) {
				symbols.add(symbol);
			}
		}
		return symbols;
	}
	
	/**
	 * @return a string version of this equation
	 */
	@Override
	public String toString() {
		String result = "";
		for(Symbol symbol : getListOfSymbols()) {
			result += symbol.toString();
		}
		return result;
	}
	
	/**
	 * Overrides the equal method
	 * this method sees if a given equation and this equation are equal
	 */
	@Override
    public boolean equals(Object obj) {
		Equation otherEquation = (Equation) obj;
		// the last nonSplitablePart needs be equal
//		if(!this.nonSplitableParts.get(this.nonSplitableParts.size()-1).equals(otherEquation.nonSplitableParts.get(otherEquation.nonSplitableParts.size()-1))) {
//			return false; TODO
//		}
		List<NonSplittable> otherEq = new ArrayList<NonSplittable>(otherEquation.getEquationParts());
		for(NonSplittable part : nonSplitableParts) {
			if(otherEq.contains(part)) {
				otherEq.remove(part);
			} else {
				return false;
			}
		}
		return true;
	}
	
	@Override
    public int hashCode() {
		return HASHCODE;
    }
	
	/**
	 * Calculates the HashCode of this equation
	 * @return the hashCode
	 */
	private int getHashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
		            append(getValueOfEquation()).
		            append(nonSplitableParts.size());
		for(int s : getTerminalCounter()) {
			builder.append(s);
		}
		return builder.toHashCode();
	}
	
	/**
	 * @return the number of appearances of all terminals in this equation
	 */
	public List<Integer> getTerminalCounter() {
		return terminalCounter;
	}


	public boolean isPruned() {
		return pruned;
	}


	public void setPruned(boolean pruned) {
		this.pruned = pruned;
	}	
}
