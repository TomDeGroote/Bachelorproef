/**
 * Represents the operands
 *
 */
public class Operand extends Symbol {

	private final String value;
	public Operand(String value) {
		super.operand = true;
		super.representation = value;
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
