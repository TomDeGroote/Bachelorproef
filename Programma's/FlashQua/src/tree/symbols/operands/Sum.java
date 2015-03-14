package tree.symbols.operands;

@SuppressWarnings("serial")
public class Sum extends Operand {

	public Sum() {
		super.representation = "+";
	}

	@Override
	public Double calculateValue(Double value1, Double value2) {
		return value1 + value2;
	}

	@Override
	public boolean isSplitable() {
		return true;
	}

	@Override
	public boolean isCommutative() {
		return true;
	}

	@Override
	public Double getNeutralElement() {
		return 0.0;
	}

	@Override
	public Double calculateValue(Double value) {
		return calculateValue(getNeutralElement(), value);
	}

	@Override
	public Operand getInverseOperand() {
		return new Substraction();
	}
}
