package tree;
/**
 * Represents the operand symbols.
 * @author Jeroen & Tom
 *
 */
public class Operand extends Symbol {

	public Operand(String value) {
		super.representation = value;
		super.operand = true;
	}
}
