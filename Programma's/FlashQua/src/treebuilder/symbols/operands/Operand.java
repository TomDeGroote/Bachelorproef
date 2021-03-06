package treebuilder.symbols.operands;

import treebuilder.symbols.Symbol;


/**
 * Represents the operand symbols.
 * @author Jeroen & Tom
 *
 */
@SuppressWarnings("serial")
public abstract class Operand extends Symbol {
	
	/**
	 * Calculates the result if this operation is done between the two given values
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 * 		= value1 THIS value2
	 */
	public abstract double calculateValue(double value1, double value2);

	/**
	 * Calculates the value of this operation and a value
	 * @param value
	 * @return
	 *		= THIS value
	 */
	public abstract Double calculateValue(Double value);

	/**
	 * @return
	 * 			true: if the operand is splitable
	 * 			false: if the operand is non splitable
	 */
	public abstract boolean isSplitable();
	
	/**
	 * @return
	 * 			true: if the operand is commutative
	 * 			false: if the operand is non commutative
	 */
	public abstract boolean isCommutative();

	/**
	 * @return 
	 * 			the neutralElement
	 */
	public abstract Double getNeutralElement();
	
	/**
	 * @return 
	 * 			the inverse operand of this operand
	 */
	public abstract Operand getInverseOperand();
	
	/**
	 * @return 
	 * 			True if this is an operand
	 */
	@Override
	public boolean isOperand() {
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		Operand otherOperand = (Operand) obj;
		return otherOperand.representation.equals(this.representation);
	}
}
