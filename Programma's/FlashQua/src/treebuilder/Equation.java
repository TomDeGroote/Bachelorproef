package treebuilder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import treebuilder.symbols.Symbol;
import treebuilder.symbols.Terminal;
import treebuilder.symbols.operands.Division;
import treebuilder.symbols.operands.Multiplication;
import treebuilder.symbols.operands.Operand;
import treebuilder.symbols.operands.Sum;
import exceptions.EquationHasSolutionException;
import exceptions.UselessEquationException;

/**
 * Represents an equation that can be formed by combining symbols.
 * @author Jeroen en Tom
 *
 */
@SuppressWarnings("serial")
public class Equation implements Serializable {
	
	private List<NonSplittable> nonSplitableParts = new ArrayList<NonSplittable>();
	private double valueRestOfEquation = 0.0;
	private boolean pruned = false;
	private double value = 0.0;
	private HashMap<Symbol, Integer> terminalCounter = new HashMap<Symbol, Integer>();
	private final int HASHCODE;
	
	/**
	 * Constructor when no parameters are given
	 * This constructor will generate an equation = E
	 * 
	 */
	public Equation(Terminal t) {
		NonSplittable firstPart = new NonSplittable(new Sum(), t);
		nonSplitableParts.add(firstPart);
		this.value = firstPart.getValue();
		this.HASHCODE = getHashCode();
		terminalCounter.put(t, 1);
	}
	

	public Equation(List<NonSplittable> nonSplitableParts, double valueRestOfEquation, double value, HashMap<Symbol, Integer> terminalCounter) {
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
			
		}
		
		
		
		
		return null;
	}
	
	/**
	 * @return
	 * 			The split parts of this equation
	 */
	public List<NonSplittable> getEquationParts() {
		return nonSplitableParts;
	}
	
	/**
	 * 
	 * @return
	 * 		The value of this equation
	 */
	public double getValueOfEquation() {
		return value;
	}
	
	/**
	 * 
	 * @return
	 * 		The value of this equation
	 */
	public static double calculateValueOfEquation(Operand lastSplitableOperand, double valueRestOfEquation, double valueOfLastSplitable) {
		// operand between rest of equation and lastNonSplitablePart can be found at the first position of the lastNonSplitable part
		return lastSplitableOperand.calculateValue(valueRestOfEquation, valueOfLastSplitable);
	}

	/**
	 * @return
	 * 		Returns the list of symbols of this equation, the symbols are in order.
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
	 * Returns a string version of this equation
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
	 * @return
	 */
	private int getHashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
		            append(getValueOfEquation()).
		            append(nonSplitableParts.size());
		for(Symbol s : getTerminalCounter().keySet()) {
			builder.append(s + " " + getTerminalCounter().get(s));
		}
		return builder.toHashCode();
	}
	
	/**
	 * @return the number of appearances of all terminals in this equation
	 */
	public HashMap<Symbol, Integer> getTerminalCounter() {
		return terminalCounter;
	}


	public boolean isPruned() {
		return pruned;
	}


	public void setPruned(boolean pruned) {
		this.pruned = pruned;
	}	
}
