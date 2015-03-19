package tree.symbols.operands;

@SuppressWarnings("serial")
public class Root extends Operand {

	public Root() {
		super.representation = "r";
	}

	@Override
	public double calculateValue(double value1, double value2) {
		throw new IllegalArgumentException();
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
		return new Power();
	}
}
