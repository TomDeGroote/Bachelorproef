package tree;


/**
 * Represents the operand symbols.
 * @author Jeroen & Tom
 *
 */
@SuppressWarnings("serial")
public class Operand extends Symbol {

	private final boolean splitable;
	private final boolean commutative;
	
	/**
	 * The constructor of the operand
	 * @param value
	 * @param splitable
	 */
	public Operand(String value, boolean splitable, boolean communtative) {
		super.representation = value;
		super.operand = true;
		this.splitable = splitable;
		this.commutative = communtative;
	}

	/**
	 * @return
	 * 			true: if the operand is splitable
	 * 			false: if the operand is non splitable
	 */
	public boolean isSplitable() {
		return splitable;
	}
	
	/**
	 * @return
	 * 			true: if the operand is commutative
	 * 			false: if the operand is non commutative
	 */
	public boolean isCommutative() {
		return commutative;
	}
	
	
}
