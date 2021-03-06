/**
 * This class represents the terminals
 */
public class Terminal extends Symbol {

	private final double value;
	public Terminal(double value) {
		super.representation = value + "";
		super.terminal = true;
		this.value = value;
	}
	
	public double getNumericValue() {
		return value;
	}
	
	public String getValue() {
		return value + "";
	}
}
