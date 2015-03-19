package tree;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import tree.symbols.Symbol;
import tree.symbols.Terminal;
import tree.symbols.operands.Division;
import tree.symbols.operands.Multiplication;
import tree.symbols.operands.Operand;
import tree.symbols.operands.Sum;
import exceptions.EquationHasSolutionException;
import exceptions.UselessEquationException;

/**
 * Represents an equation that can be formed by combining symbols.
 * @author Jeroen en Tom
 *
 */
@SuppressWarnings("serial")
public class Equation implements Serializable {
	
	public List<List<Symbol>> nonSplitableParts = new ArrayList<List<Symbol>>();
	public Operand lastSplitableOperand;
	public double valueOfLastSplitable;
	public double valueRestOfEquation = 0.0;
	public boolean pruned = false;
	public double value = 0.0;
	private HashMap<Symbol, Integer> terminalCounter = new HashMap<Symbol, Integer>();
	public final int HASHCODE;
	
	/**
	 * Constructor when no parameters are given
	 * This constructor will generate an equation = E
	 * 
	 */
	public Equation(Terminal t) {
		System.nanoTime(); // Increases time.. A Lot..
		lastSplitableOperand = new Sum();
		List<Symbol> firstPart = new ArrayList<Symbol>();
		firstPart.add(lastSplitableOperand);
		firstPart.add(t);
		nonSplitableParts.add(firstPart);
		this.valueOfLastSplitable = t.getValue();
		this.value = t.getValue();
		this.HASHCODE = getHashCode();
	}
	

	public Equation(List<List<Symbol>> nonSplitableParts, Operand lastSplitableOperand, double valueRestOfEquation, double valueOfLastSplitable, double value, HashMap<Symbol, Integer> terminalCounter) {
		System.nanoTime(); // Increases time.. A Lot..
		this.nonSplitableParts = nonSplitableParts;
		this.lastSplitableOperand = lastSplitableOperand;
		this.valueOfLastSplitable = valueOfLastSplitable;
		this.valueRestOfEquation = valueRestOfEquation;
		this.value = value;
		this. terminalCounter = terminalCounter;
		this.HASHCODE = getHashCode();
	}
	
	/**
	 * This constructor will generate a equation containing the symbols
	 * of inputList using those symbols in the same order.
	 * 
	 * @param inputlist (operand, terminal)
	 * 			List of symbols in order for this equation
	 * @param previous
	 * 			The previous equation
	 * @throws UselessEquationException 
	 * @throws EquationHasSolutionException 
	 */
	public static Equation createEquation(Equation previous, Operand operand, Terminal terminal) {		
		List<List<Symbol>> nonSplitableParts = new ArrayList<List<Symbol>>();
		Operand lastSplitableOperand = previous.lastSplitableOperand;
		double valueOfLastSplitable = previous.valueOfLastSplitable;
		double valueRestOfEquation = previous.valueRestOfEquation;
		
		// Check if the neutral element
		if(terminal.isWeight() && terminal.getValue().equals(operand.getNeutralElement())) {
			return null;
		}
		
		// fields needed of previous equation
		for(List<Symbol> prevNon : previous.nonSplitableParts) {
			nonSplitableParts.add(new ArrayList<Symbol>(prevNon));
		}
		
		List<Symbol> lastPart = nonSplitableParts.get(nonSplitableParts.size()-1);
		Terminal lastT = (Terminal) lastPart.get(1);
		if(lastT.isWeight() && lastPart.size() == 2) {
			// Check that in case of + or * the first part is bigger than the second, only if weight
			if((operand.equals(new Sum()) || operand.equals(new Multiplication())) && terminal.isWeight() &&lastT.getValue() > terminal.getValue()) {
				return null;
			}
			// Check that the last part isn't a neutral value for the next operand, only if weight
			if(lastT.getValue().equals(operand.getNeutralElement())) {
				return null;
			}
		}
		
		if(operand.isSplitable()) {	
			// this represents the inverse nonSplitable part of the possibly added part
			List<Symbol> inverse = new ArrayList<Symbol>();
			inverse.add(operand.getInverseOperand());
			inverse.add(terminal);
			
			// this will be the added part
			List<Symbol> newLastNonSplitable = new ArrayList<Symbol>();
			newLastNonSplitable.add(operand);
			newLastNonSplitable.add(terminal);
			
			// Create the equation
			valueRestOfEquation = lastSplitableOperand.calculateValue(valueRestOfEquation, valueOfLastSplitable);
			nonSplitableParts.add(newLastNonSplitable);
			valueOfLastSplitable = terminal.getValue();
			lastSplitableOperand = operand;
			
			// Check if the adding of this part does not undo an other creating of a part
			if(nonSplitableParts.contains(inverse)) {
				return null;
			}
			
			// Check if we are not adding something that already exists, could be replaced by a weight
			// Can be left away because most of them are already checked with the > than test above,
			// And the test does take to long to remove the others, no gain is added
//			for(int i = 0; i < nonSplitableParts.size()-1; i++) {
//				if(nonSplitableParts.get(i).equals(newLastNonSplitable)) {
//					return null;
//				}
//			}
		} else {	
			// check if the combination OT doesn't already exist, could be replaced by weight
//			for(int i = 1; i < lastPart.size()-1; i = i+2) {
//				if(lastPart.get(i).equals(terminal)) {
//					if(lastPart.get(i-1).equals(operand)) {
//						return null;
//					}
//				}
//			}
			
			// create the equation, add sorted (first * then /)
			if(operand.equals(new Multiplication())) {
				// Check if the multiplication of this terminal doesn't already exist, could be replaced by weight
				if(lastPart.get(1).equals(terminal)) {
					return null;
				}
				
				// create the equation
				int i = 1;
				boolean broke = false;
				for(i = 1; i < lastPart.size(); i+=2) {
					if(lastPart.get(i).toString().compareTo(terminal.toString()) >= 0) {
						broke = true;
						break;
					}
				}
				if(broke) {
					lastPart.add(i, operand);
					lastPart.add(i, terminal);
				} else {
					lastPart.add(operand);
					lastPart.add(terminal);
				}
			} else {
				lastPart.add(operand);
				lastPart.add(terminal);
			}
			valueOfLastSplitable = operand.calculateValue(valueOfLastSplitable, terminal.getValue());
			
			// Check if the first element is not the same as the second if / is doing
			if(operand.equals(new Division())) {
				if(lastPart.get(1).equals(terminal)) {
					return null;
				}
			}
			
			// Check if the adding of this operand and terminal does not undo anything in the last part
			boolean inverseOperand = false;
			for(Symbol s : lastPart) {
				if(inverseOperand) {
					if(s.equals(terminal)) {
						return null;
					}
				} else {
					if(s.equals(operand.getInverseOperand())) {
						inverseOperand = true;
					} else {
						inverseOperand = false;
					}
				}
			}

			// Check if we are not adding something that already exists, could be replaced by a weight TODO
			for(int i = 0; i < nonSplitableParts.size()-1; i++) {
				if(lastPart.equals(nonSplitableParts.get(i))) {
					return null;
				}
			}	
			
			// Check if the adding of this part does not undo an other creating of a part
			List<Symbol> inverse = new ArrayList<Symbol>(lastPart);
			inverse.set(0, ((Operand) inverse.get(0)).getInverseOperand()); 
			if(nonSplitableParts.contains(inverse)) {
				return null;
			}
		}
		
		HashMap<Symbol, Integer> terminalCounter = new HashMap<Symbol, Integer>(previous.terminalCounter);
		
		// count the terminals
		if(terminalCounter.containsKey(terminal)) {
			terminalCounter.put(terminal, terminalCounter.get(terminal)+1);
		} else {
			terminalCounter.put(terminal, 1);
		}
		
		return new Equation(nonSplitableParts, lastSplitableOperand, valueRestOfEquation, valueOfLastSplitable, calculateValueOfEquation(lastSplitableOperand, valueRestOfEquation, valueOfLastSplitable), terminalCounter);
	}
	
	/**
	 * @return
	 * 			The split parts of this equation
	 */
	public List<List<Symbol>> getEquationParts() {
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
		for(List<Symbol> part : nonSplitableParts) {
			for(Symbol symbol : part) {
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
		for(List<Symbol> part : nonSplitableParts) {
			for(Symbol symbol : part) {
				result += symbol.toString();
			}
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
		List<List<Symbol>> otherEq = new ArrayList<List<Symbol>>(otherEquation.nonSplitableParts);
		for(List<Symbol> part : nonSplitableParts) {
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
	
	 private int getHashCode() {
			return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
		            append(getValueOfEquation()).
		            append(terminalCounter).
		            append(nonSplitableParts.size()).
		            toHashCode();
	}
	 
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
