package tree.symbols.operands;

@SuppressWarnings("serial")
public class Division extends Operand {

	public Division() {
		super.representation = "/";
	}

	@Override
	public Double calculateValue(Double value1, Double value2) {
		return value1 / value2;
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
		return new Multiplication();
	}
}
