package ui.components.gauge;

import java.awt.Shape;

public class GaugeMarker {
	public double value;
	public String text;
	public boolean active = false;
	public Shape shape;
	public boolean clickable = true;

	public GaugeMarker(double val, String text) {
		value = val;
		this.text = text;
	}

}
