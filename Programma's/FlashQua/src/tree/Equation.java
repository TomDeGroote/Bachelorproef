package tree;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	
	private List<List<Symbol>> nonSplitableParts = new ArrayList<List<Symbol>>();
	private Operand lastSplitableOperand;
	private Double valueOfLastSplitable;
	private Double valueRestOfEquation = 0.0;
	private boolean pruned = false;
	private Double value = 0.0;
	
	/**
	 * Constructor when no parameters are given
	 * This constructor will generate an equation = E
	 * 
	 */
	public Equation(Terminal t) {
		lastSplitableOperand = new Sum();
		List<Symbol> firstPart = new ArrayList<Symbol>();
		firstPart.add(lastSplitableOperand);
		firstPart.add(t);
		nonSplitableParts.add(firstPart);
		this.valueOfLastSplitable = t.getValue();
		setValueOfEquation();
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
	 */
	public Equation(Equation previous, Operand operand, Terminal terminal) throws UselessEquationException {
		// fields needed of previous equation
		for(List<Symbol> prevNon : previous.nonSplitableParts) {
			nonSplitableParts.add(new ArrayList<Symbol>(prevNon));
		}
		valueRestOfEquation = previous.valueRestOfEquation;
		lastSplitableOperand = previous.lastSplitableOperand;
		valueOfLastSplitable = previous.valueOfLastSplitable;

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
				throw new UselessEquationException("Equation will not add extra information: " + this.toString());
			}
		} else {	
			// create the equation
			nonSplitableParts.get(nonSplitableParts.size()-1).add(operand);
			nonSplitableParts.get(nonSplitableParts.size()-1).add(terminal);
			valueOfLastSplitable = operand.calculateValue(valueOfLastSplitable, terminal.getValue());
			
			// Check if the adding of this operand and terminal does not undo anything in the last part
			
			
			// Check if the adding of this part does not undo an other creating of a part
			List<Symbol> inverse = new ArrayList<Symbol>(nonSplitableParts.get(nonSplitableParts.size()-1));
			inverse.set(0, ((Operand) inverse.get(0)).getInverseOperand()); 
			if(nonSplitableParts.contains(inverse)) {
				throw new UselessEquationException("Equation will not add extra information: " + this.toString());
			}
			
			

			
		}
		
		setValueOfEquation();
		if(getValueOfEquation().equals(Grammar.GOAL)) {
			System.out.println(this.toString());
		}
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
	public void setValueOfEquation() {
		// operand between rest of equation and lastNonSplitablePart can be found at the first position of the lastNonSplitable part
		this.value = lastSplitableOperand.calculateValue(valueRestOfEquation, valueOfLastSplitable);
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
	 * this method sees if a given equation and this equation are equal // TODO
	 */
	@Override
    public boolean equals(Object obj) {
		if (!(obj instanceof Equation))
			return false;
		if (obj == this)
			return true;

		Equation otherEquation = (Equation) obj;
		// the value needs to be equal
		if(!this.getValueOfEquation().equals(otherEquation.getValueOfEquation())) {
			return false;
		}
		// the last nonSplitablePart needs be equal
		if(!this.nonSplitableParts.get(this.nonSplitableParts.size()-1).equals(otherEquation.nonSplitableParts.get(otherEquation.nonSplitableParts.size()-1))) {
			return false;
		}
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

	public boolean isPruned() {
		return pruned;
	}


	public void setPruned(boolean pruned) {
		this.pruned = pruned;
	}	
}
