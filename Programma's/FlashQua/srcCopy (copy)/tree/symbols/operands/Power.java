package tree.symbols.operands;

@SuppressWarnings("serial")
public class Power extends Operand {

	public Power() {
		super.representation = "^";
	}

	@Override
	public double calculateValue(double value1, double value2) {
		return Math.pow(value1, value2);
	}

	@Override
	public boolean isSplitable() {
		return false;
	}

	@Override
	public boolean isCommutative() {
		return false;
	}

	@Override
	public Double getNeutralElement() {
		return 1.0;
	}

	@Override
	public Double calculateValue(Double value) {
		return calculateValue(getNeutralElement(), value);
	}

	@Override
	public Operand getInverseOperand() {
		return new Root();
	}
}
