package tree;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import exceptions.EquationHasSolutionException;
import exceptions.UselessEquationException;
import tree.symbols.Symbol;
import tree.symbols.Terminal;
import tree.symbols.operands.Operand;
import tree.symbols.operands.Sum;

/**
 * Represents an equation that can be formed by combining symbols.
 * @author Jeroen en Tom
 *
 */
@SuppressWarnings("serial")
public class Equation implements Serializable {
	
	public List<List<Symbol>> nonSplitableParts = new ArrayList<List<Symbol>>();
	public Operand lastSplitableOperand;
	public Double valueOfLastSplitable;
	public Double valueRestOfEquation = 0.0;
	public boolean pruned = false;
	public Double value = 0.0;
	private HashMap<Symbol, Integer> terminalCounter = new HashMap<Symbol, Integer>();
	public final int HASHCODE;
	
	public static int countNeutral = 0;
	public static int countWeightReplace = 0;
	public static int countUndoCreating = 0;
	public static int countDivision = 0;
	public static int countUndoLastPart = 0;
	public static int countUndoAnyPart = 0;
	public static int countReplacementWeight = 0;

	public static long countNeutralTime = 0;
	public static long countWeightReplaceTime = 0;
	public static long countUndoCreatingTime = 0;
	public static long countDivisionTime = 0;
	public static long countUndoLastPartTime = 0;
	public static long countUndoAnyPartTime = 0;
	public static long countReplacementWeightTime = 0;

	public static long totalConstructorTime = 0;
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
	

	public Equation(List<List<Symbol>> nonSplitableParts, Operand lastSplitableOperand, Double valueRestOfEquation, Double valueOfLastSplitable, Double value, HashMap<Symbol, Integer> terminalCounter) {
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
		Double valueOfLastSplitable = previous.valueOfLastSplitable;
		Double valueRestOfEquation = previous.valueRestOfEquation;
		
		long time = System.nanoTime();
		if(terminal.getValue().equals(operand.getNeutralElement())) {
			countNeutral++;
			return null;
		}
		countNeutralTime += System.nanoTime() - time;
		
		// fields needed of previous equation
		for(List<Symbol> prevNon : previous.nonSplitableParts) {
			nonSplitableParts.add(new ArrayList<Symbol>(prevNon));
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
			
			time = System.nanoTime();
			// Check if we are not adding something that already exists, could be replaced by a weight
			for(int i = 0; i < nonSplitableParts.size()-1; i++) {
				if(nonSplitableParts.get(i).equals(newLastNonSplitable)) {
					countWeightReplace++;
					return null;
				}
			}
			countWeightReplaceTime += System.nanoTime() - time;

			
			time = System.nanoTime();
			// Check if the adding of this part does not undo an other creating of a part
			if(nonSplitableParts.contains(inverse)) {
				countUndoCreating++;
				return null;
			}
			countUndoCreatingTime += System.nanoTime() - time;
		} else {	
			// create the equation TODO hardcoded
			List<Symbol> lastPart = nonSplitableParts.get(nonSplitableParts.size()-1);
			if(operand.toString().equals("*")) {
				int i = 1;
				boolean broke = false;
				for(i = 1; i < lastPart.size(); i+=2) {
					if(lastPart.get(i).toString().compareTo(terminal.toString()) >= 0) {
//						System.out.println(lastPart.get(i).toString() + " >= " + terminal.toString());
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
			
			time = System.nanoTime();
			// Check if the first element is not the same as the second if / is doing TODO hardcoded
			if(operand.toString().equals("/")) {
				if(nonSplitableParts.get(0).get(1).equals(terminal)) {
					countDivision++;
					return null;
				}
			}
			countDivisionTime += System.nanoTime() - time;
			
			time = System.nanoTime();
			// Check if the adding of this operand and terminal does not undo anything in the last part
			boolean inverseOperand = false;
			for(Symbol s : nonSplitableParts.get(nonSplitableParts.size()-1)) {
				if(inverseOperand) {
					if(s.equals(terminal)) {
						countUndoLastPart++;
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
			countUndoLastPartTime += System.nanoTime() - time;
			
			time = System.nanoTime();
			// Check if the adding of this part does not undo an other creating of a part
			List<Symbol> inverse = new ArrayList<Symbol>(nonSplitableParts.get(nonSplitableParts.size()-1));
			inverse.set(0, ((Operand) inverse.get(0)).getInverseOperand()); 
			if(nonSplitableParts.contains(inverse)) {
				countUndoAnyPart++;
				return null;
			}
			countUndoAnyPartTime += System.nanoTime() - time;

			time = System.nanoTime();
			// Check if we are not adding something that already exists, could be replaced by a weight TODO
			for(int i = 0; i < nonSplitableParts.size()-1; i++) {
				if(nonSplitableParts.get(nonSplitableParts.size()-1).equals(nonSplitableParts.get(i))) {
					countReplacementWeight++;
					return null;
				}
			}	
			countReplacementWeightTime += System.nanoTime() - time;

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
	
	public static boolean checkIfUsefullEquation(Equation previous, Operand operand, Terminal terminal) {
		// Check if the added part is a neutral element for the nonSplitable operand

		
		return true;
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
	public Double getValueOfEquation() {
		return value;
	}
	
	/**
	 * 
	 * @return
	 * 		The value of this equation
	 */
	public static Double calculateValueOfEquation(Operand lastSplitableOperand, Double valueRestOfEquation, Double valueOfLastSplitable) {
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
		if(this.hashCode() != otherEquation.hashCode()) {
			return false;
		}
//		if (!(obj instanceof Equation))
//			return false;
//		if (obj == this)
//			return true;
//		
		// the value needs to be equal
//		if(!this.getValueOfEquation().equals(otherEquation.getValueOfEquation())) {
//			return false;
//		}
//		if(this.nonSplitableParts.size() != otherEquation.nonSplitableParts.size()) {
//			return false;
//		}
		// the last nonSplitablePart needs be equal
//		if(!this.nonSplitableParts.get(this.nonSplitableParts.size()-1).equals(otherEquation.nonSplitableParts.get(otherEquation.nonSplitableParts.size()-1))) {
//			return false;
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
