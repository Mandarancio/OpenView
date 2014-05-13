package ui.components.listeners;

public class ValueEvent {
	private double reference_;
	private double value_;

	public ValueEvent(double position, double referencePosition) {
		value_ = position;
		reference_ = referencePosition;
	}

	public double getReference() {
		return reference_;
	}

	public void setReference(double reference) {
		this.reference_ = reference;
	}

	public double getValue() {
		return value_;
	}

	public void setValue(double value) {
		this.value_ = value;
	}

}