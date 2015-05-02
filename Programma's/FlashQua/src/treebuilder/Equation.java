package treebuilder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import treebuilder.grammar.Grammar;
import treebuilder.symbols.Symbol;
import treebuilder.symbols.Terminal;
import treebuilder.symbols.operands.Multiplication;
import treebuilder.symbols.operands.Operand;
import treebuilder.symbols.operands.Sum;
import runner.Main;

/**
 * Represents an equation that can be formed by combining symbols.
 * @author Jeroen en Tom
 *
 */
@SuppressWarnings("serial")
public class Equation implements Serializable {

	public List<NonSplittable> nonSplitableParts = new ArrayList<NonSplittable>();
	private double valueRestOfEquation = 0.0;
	public double value = 0.0;
	private List<Integer> terminalCounter = new ArrayList<Integer>();
	private final int HASHCODE;

	/**
	 * Constructor of a basic equation
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


	private Equation(List<NonSplittable> nonSplitableParts, double valueRestOfEquation, double value, List<Integer> terminalCounter) {

		this.nonSplitableParts = nonSplitableParts;
		this.valueRestOfEquation = valueRestOfEquation;
		this.value = value;
		this.terminalCounter = terminalCounter;
		this.HASHCODE = getHashCode();
	}

	/**
	 * Constructor for an equation that is an expansion of another equation
	 * 
	 * @param previous
	 * 			The equation to expand to a new equation
	 * @param operand
	 * 			The operand to be added
	 * @param terminal
	 * 			The terminal to be added
	 * @return
	 * 			The equation
	 */
	public static Equation createEquation(Equation previous, Operand operand, Terminal terminal) {
		if(Main.USINGWEIGHTS){
			// there should only be one alone standing constant in the equation TODO zorg dat die in de equation staat
			if(operand.isSplitable() && terminal.isWeight()) {
				return null;
			}
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
			if(Main.USINGWEIGHTS){
				// There should not be a +K1 if there is a +K1 (replaced by weights)
				if(previous.getEquationParts().contains(newNonSplittable)) {
					return null;
				}
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
			if(Main.USEOPTIMALISATIONS){
				// W1.0*K1 should not exist
				if(previous.getLastNonSplittable().getLastTerminal().isWeight() && previous.getLastNonSplittable().getLastTerminal().getValue() == operand.getNeutralElement() && !operand.toString().equals("/")) {
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
			if(Main.USEOPTIMALISATIONS){
				// There should not be a K0/K0
				if(Main.USINGWEIGHTS){
					List<Symbol> nonSplittablePrevious = new ArrayList<Symbol>(previous.getLastNonSplittable().getSymbols());
					nonSplittablePrevious.set(0, new Multiplication());
					for(int i = 0; i < nonSplittablePrevious.size(); i += 2) {
						if((nonSplittablePrevious.get(i).equals(operand.getInverseOperand()) || nonSplittablePrevious.get(i).equals(operand)) && nonSplittablePrevious.get(i+1).equals(terminal)) {
							return null;
						}
					}
				}
			}

			// There should not be a -K0*K1 if there is a +K0*K1
			NonSplittable inverseNonSplittable = newNonSplittable.getInverseNonSplittable();
			if(Main.USEOPTIMALISATIONS){
				if(previous.getEquationParts().contains(inverseNonSplittable)) {
					return null;
				}

				if(Main.USINGWEIGHTS){
					// There should not be a +K0*K1 if it already has a +K0*K1, replace by weights
					if(previous.getEquationParts().contains(newNonSplittable)) {
						return null;
					}
				}
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
		boolean firstSymbol = true;
		for(Symbol symbol : getListOfSymbols()) {
			if(!firstSymbol) {
				result += symbol.toString();
			}
			firstSymbol = false;
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
		// the last nonSplitablePart needs be equal TODO
				if(!this.nonSplitableParts.get(this.nonSplitableParts.size()-1).equals(otherEquation.nonSplitableParts.get(otherEquation.nonSplitableParts.size()-1))) {
					return false; 
				}
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
	
	/**
	 * @return the size of this equation
	 */
	public int size() {
		return this.toString().length();
	}


	/**
	 * @return the quality of this equation, does not encapsulate it's length
	 */
	public double getEquationQuality() {
		double nrOfKs = Grammar.getNrOfKs();
		double nrOfKsContained = 0;
		for(int i = 0; i < nrOfKs; i++) {
			if(terminalCounter.get(i) > 0) {
				nrOfKsContained++;
			}
		}
		return nrOfKsContained/nrOfKs;
	}
}
