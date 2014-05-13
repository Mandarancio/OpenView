package ui.components.gauge;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ui.components.listeners.ValueEvent;
import ui.components.listeners.ValueListener;

public class GaugeMonitor extends JComponent {

	private static final long serialVersionUID = -364547702251119522L;
	private double min_ = 0, max_ = 1, value_ = 0.5, reference_ = Double.NaN;
	private boolean referenceEnabled_ = true;
	private boolean editable_ = true;
	private String unit_ = "";
	private String title_ = "";
	private double minWarning_ = Double.NaN;
	private double maxWarning_ = Double.NaN;
	private double minAlert_ = Double.NaN;
	private double maxAlert_ = Double.NaN;
	private int numberOfScaleSteps_ = 5;

	private DecimalFormat formatter_ = new DecimalFormat("0.###");

	private Path2D.Double gostPolygon_ = null;
	private Path2D.Double refPolygon_ = null;

	private ArrayList<GaugeMarker> markers_ = new ArrayList<GaugeMarker>();

	private MouseAdapter mouseAdapter_ = new MouseAdapter() {
		private boolean dragRef = false;
		private double gostValue;

		@Override
		public void mouseMoved(java.awt.event.MouseEvent e) {
			boolean somethigChange_ = false;
			for (GaugeMarker m : markers_) {
				if (m.shape != null && m.shape.contains(e.getPoint())) {
					m.active = true;
					somethigChange_ = true;
				} else if (m.active) {
					m.active = false;
					somethigChange_ = true;
				}
			}
			if (somethigChange_)
				repaint();
		}

		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 && refPolygon_ != null
					&& !Double.isNaN(reference_) && referenceEnabled_
					&& editable_ && refPolygon_.contains(getMousePosition())) {
				dragRef = true;
				gostValue = reference_;
				gostPolygon_ = new Path2D.Double(refPolygon_);
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (dragRef)
				computeRef(e.getPoint());
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (dragRef && e.getButton() == MouseEvent.BUTTON1) {
				dragRef = false;
				computeRef(e.getPoint());
				triggerReferenceEvent();
				gostPolygon_ = null;
			}
		}

		private void computeRef(Point p) {
			double w = getWidth() - 10;
			w -= w * 0.1 + 10;
			double dx = (getWidth() - w) / 2.0;
			double x = p.x - getWidth() / 2.0;
			double y = dx + w / 2 - p.y;
			if (y < 0)
				y = 0;
			double angle = Math.PI * 2 - Math.atan2(y, x) * 2;
			if (angle < 0)
				angle = 0;
			else if (angle > Math.PI * 2)
				angle = Math.PI * 2;
			double val = angle / (Math.PI * 2.0) * (max_ - min_) + min_;

			// System.out.println("quadrante: "+(x>0 && y>0 ? "primo" : x<0 &&
			// y>0 ? "secondo" : x<0 && y<0 ? "terzo" :
			// "quarto"));
			// System.out.println(formatter_.format(Math.atan2(y,
			// x)/Math.PI*90.0)+":     "+formatter_.format(val));
			if (Math.abs(val - gostValue) < (max_ - min_) / 100) {
				val = gostValue;
			}
			reference_ = val;
			repaint();
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() > 1) {
				for (GaugeMarker m : markers_) {
					if (m.shape != null && m.active
							&& m.shape.contains(e.getPoint())) {
						reference_ = m.value;
						repaint();
						triggerReferenceEvent();
					}
				}
			}
		}
	};

	public GaugeMonitor() {
		this.addMouseMotionListener(mouseAdapter_);
		this.addMouseListener(mouseAdapter_);
	}

	public GaugeMonitor(double val, double min, double max) {
		this();
		value_ = val;
		min_ = min;
		max_ = max;
	}

	public GaugeMonitor(String title, double val, double min, double max) {
		this(val, min, max);
		title_ = title;
	}

	public GaugeMonitor(String title, String unit, double val, double min,
			double max) {
		this(title, val, min, max);
		unit_ = unit;
	}

	public void addValueListener(ValueListener list) {
		listenerList.add(ValueListener.class, list);
	}

	public void setValue(double val) {
		value_ = val;
		repaint();
	}

	public double getValue() {
		return value_;
	}

	public void setRefernce(double ref) {
		reference_ = ref;
		repaint();
	}

	public double getReference() {
		return reference_;
	}

	public void setMin(double min) {
		min_ = min;
		repaint();
	}

	public double getMin() {
		return min_;
	}

	public void setMax(double max) {
		max_ = max;
		repaint();
	}

	public double getMax() {
		return max_;
	}

	public void setUnit(String unit) {
		unit_ = unit;
	}

	public String getUnit() {
		return unit_;
	}

	public void setTitle(String title) {
		title_ = title;
	}

	public String getTitle() {
		return title_;
	}

	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		Color TC = Color.white, WC = new Color(255, 255, 0), AC = new Color(
				255, 0, 0);

		g2d.setColor(getForeground());
		int w = getWidth() - 10;
		w -= w * 0.1 + 10;
		int dx = (getWidth() - w) / 2;
		int h = w;

		int strokeSize = (int) Math.round(w * 0.2);

		Rectangle area = new Rectangle(dx - strokeSize / 2,
				dx - strokeSize / 2, w + strokeSize, h / 2 + strokeSize / 2);
		g2d.setStroke(new BasicStroke(strokeSize + 2, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER));
		g2d.drawArc(dx, dx, w, h, 0, 180);

		g2d.setColor(TC);

		g2d.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER));
		g2d.drawArc(dx, dx, w, h, 0, 180);

		// Limit color
		g2d.setStroke(new BasicStroke((int) (strokeSize - strokeSize * 0.1),
				BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));

		g2d.setColor(WC);
		if (!Double.isNaN(maxWarning_)) {
			double vl = (max_ - maxWarning_) / (max_ - min_) * 180;
			g2d.drawArc(dx, dx, w, h, 1, (int) Math.round(vl));
		}
		if (!Double.isNaN(minWarning_)) {
			double vl = (minWarning_ - min_) / (max_ - min_) * 180;
			g2d.drawArc(dx, dx, w, h, 179, -(int) Math.round(vl));
		}

		// g2d.drawArc(dx, dx, w, h, 18, 18); // 10% 180
		// g2d.drawArc(dx, dx, w, h, 162, -18); // 10% 180

		g2d.setColor(AC);

		if (!Double.isNaN(maxAlert_)) {
			double vl = (max_ - maxAlert_) / (max_ - min_) * 180;
			g2d.drawArc(dx, dx, w, h, 1, (int) Math.round(vl));
		}
		if (!Double.isNaN(minAlert_)) {
			double vl = (minAlert_ - min_) / (max_ - min_) * 180;
			g2d.drawArc(dx, dx, w, h, 179, -(int) Math.round(vl));
		}

		g2d.setStroke(new BasicStroke());
		int cx = getWidth() / 2;
		int cy = dx + h / 2;

		// draw scale
		if (numberOfScaleSteps_ > 0) {
			double step = 180.0 / (numberOfScaleSteps_ + 2);

			Polygon stepline = new Polygon();
			stepline.addPoint(0, -strokeSize / 2 + 2);
			stepline.addPoint(0, 0);
			stepline.translate(getWidth() / 2, dx);
			double a = -90 + step;
			g2d.setColor(new Color(130, 130, 130));

			for (int i = 0; i <= numberOfScaleSteps_; i++) {

				g2d.rotate(a, cx, cy);
				g2d.drawPolygon(stepline);
				g2d.rotate(-a, cx, cy);
				a += step;
			}

		}

		int fSize = strokeSize / 2;

		g2d.setColor(getForeground());

		Font f = new Font(g2d.getFont().getFamily(), Font.PLAIN, fSize);

		String min = String.valueOf(min_);
		int size1 = f.getStringBounds(min, g2d.getFontRenderContext())
				.getBounds().width;
		int x1 = (strokeSize - size1) / 2;
		String max = String.valueOf(max_);
		int size2 = f.getStringBounds(max, g2d.getFontRenderContext())
				.getBounds().width;
		int x2 = (strokeSize - size2) / 2;

		g2d.setFont(f);

		x1 = dx - strokeSize / 2 + x1;
		if (x1 < 0)
			x1 = 0;
		g2d.drawString(min, x1, cy + fSize + 5);

		x2 = getWidth() - dx - strokeSize / 2 + x2;
		if (x2 + size2 > getWidth())
			x2 = getWidth() - size2;
		g2d.drawString(max, x2, cy + fSize + 5);

		f = new Font(g2d.getFont().getFamily(), Font.PLAIN, fSize + 2);

		size1 = f.getStringBounds(
				cropString(title_, f, g2d.getFontRenderContext(),
						getWidth() - 4), g2d.getFontRenderContext())
				.getBounds().width;
		x1 = (getWidth() - size1) / 2;
		g2d.setFont(f);

		g2d.drawString(
				cropString(title_, f, g2d.getFontRenderContext(),
						getWidth() - 4), x1, cy + fSize * 2 + 10);

		f = new Font(g2d.getFont().getFamily(), Font.BOLD, fSize * 2);
		String stringValue = formatter_.format(value_);
		f = getFont(stringValue, f, g2d.getFontRenderContext(), getWidth()
				- (strokeSize * 2 + dx * 2));

		int size3 = f.getStringBounds(stringValue, g2d.getFontRenderContext())
				.getBounds().width;
		int x3 = -size3 / 2;

		g2d.setFont(f);
		g2d.drawString(stringValue, getWidth() / 2 + x3, cy);

		int valueHeight = f.getStringBounds(stringValue,
				g2d.getFontRenderContext()).getBounds().height;

		f = new Font(g2d.getFont().getFamily(), Font.PLAIN, fSize);

		size3 = f.getStringBounds(unit_, g2d.getFontRenderContext())
				.getBounds().width;
		x3 = -size3 / 2;

		g2d.setFont(f);
		g2d.drawString(unit_, getWidth() / 2 + x3, cy + fSize);

		g2d.setClip(area);

		double v = Double.NaN;

		v = (Double.isNaN(value_) ? min_ : value_);

		if (v < min_)
			v = min_;
		else if (v > max_)
			v = max_;

		double range = max_ - min_;
		double value = (v - min_) / range;

		double angle;
		Polygon polygon;

		if (!Double.isNaN(reference_) && referenceEnabled_) {
			angle = Math.PI * ((reference_ - min_) / range) - Math.PI / 2;

			AffineTransform at = new AffineTransform();
			at.rotate(angle, cx, cy);

			if (gostPolygon_ != null) {
				g2d.setColor(Color.LIGHT_GRAY);
				g2d.fill(gostPolygon_);
			}

			if (Math.abs(reference_ - value_) < 1.0 / 200.0) {
				g2d.setColor(new Color(87, 238, 255));
			} else {
				if (getBackground().getRGB() < Color.gray.getRGB())
					g2d.setColor(getBackground().brighter());
				else
					g2d.setColor(getBackground().darker());
			}

			Polygon refPolygon = new Polygon();
			refPolygon.addPoint(-4, -strokeSize / 2 + 2);
			refPolygon.addPoint(0, -strokeSize / 4 + 2);
			refPolygon.addPoint(4, -strokeSize / 2 + 2);

			refPolygon.translate(getWidth() / 2, dx);
			refPolygon_ = new Path2D.Double();
			refPolygon_.append(refPolygon, true);
			refPolygon_.transform(at);
			g2d.fill(refPolygon_);

			g2d.setColor(getForeground().darker());
			String refValue = formatter_.format(reference_);
			int refSize = f.getStringBounds(refValue,
					g2d.getFontRenderContext()).getBounds().width;
			int xref = -refSize / 2;
			g2d.drawString(
					refValue,
					getWidth() / 2 + xref,
					cy
							- valueHeight
							- (int) f.getStringBounds(refValue,
									g2d.getFontRenderContext()).getCenterY());
		}
		polygon = new Polygon();
		polygon.addPoint(0, -strokeSize / 3);
		polygon.addPoint(-strokeSize / 4, strokeSize / 2);
		polygon.addPoint(+strokeSize / 4, strokeSize / 2);

		polygon.translate(getWidth() / 2, dx);
		angle = Math.PI * value - Math.PI / 2;

		g2d.rotate(angle, cx, cy);

		if (getBackground().getRGB() < Color.gray.getRGB()) {
			g2d.setColor(getBackground());
		} else
			g2d.setColor(getBackground().darker().darker());

		g2d.fillPolygon(polygon);

		g2d.rotate(-angle, cx, cy);
		g2d.setClip(0, 0, getWidth(), getHeight());
		paintMarkers(g2d, cx, cy, dx, strokeSize);
	}

	private void paintMarkers(Graphics2D g2d, int cx, int cy, int dx,
			int strokeSize) {
		if (markers_.size() <= 0)
			return;
		double val, angle;

		for (GaugeMarker m : markers_) {
			Path2D.Double markerOut = new Path2D.Double();
			Path2D.Double markerIn = new Path2D.Double();
			markerOut.moveTo(getWidth() / 2, dx + strokeSize / 2 - 5);
			markerOut.append(new Arc2D.Double(getWidth() / 2 - 5, dx
					+ strokeSize / 2, 10, 10, 170, 200, Arc2D.OPEN), true);
			markerOut.closePath();
			// markerOut.append(new Ellipse2D.Double(getWidth() / 2 - 5, dx +
			// strokeSize / 2 - 5, 10, 10), false);
			markerIn.append(new Ellipse2D.Double(getWidth() / 2 - 2, dx
					+ strokeSize / 2 + 4, 4, 4), false);
			AffineTransform at = new AffineTransform();
			val = (m.value - min_) / (max_ - min_);
			if (val > 1)
				val = 1;
			else if (val < 0)
				val = 0;

			angle = Math.PI * val - Math.PI / 2;
			at.rotate(angle, cx, cy);
			markerIn.transform(at);
			markerOut.transform(at);
			m.shape = markerOut;

			g2d.setColor(getBackground().darker());
			g2d.fill(markerOut);
			g2d.setColor(getBackground().darker().darker());
			g2d.draw(markerOut);
			if (m.value == reference_
					&& Math.abs(m.value - value_) < 1.0 / 200.0) {
				g2d.setColor(new Color(87, 238, 255));
			} else
				g2d.setColor(m.active ? new Color(87, 238, 255)
						: getBackground());
			g2d.fill(markerIn);

		}
		for (GaugeMarker m : markers_) {
			if (m.active && m.text.length() > 0) {
				if ((m.value - min_) / (max_ - min_) <= 0.5)
					paintLabel(g2d, (int) m.shape.getBounds().getMaxX(),
							(int) m.shape.getBounds().getCenterY(), false,
							m.text);
				else
					paintLabel(g2d, (int) m.shape.getBounds().getMinX(),
							(int) m.shape.getBounds().getCenterY(), true,
							m.text);
			}
		}
	}

	/**
	 * @param g2d
	 * @param text
	 */
	private void paintLabel(Graphics2D g2d, int cx, int cy, boolean reverse,
			String text) {
		int x, y, w, h;
		FontMetrics fm = g2d.getFontMetrics();
		w = fm.getStringBounds(text, g2d).getBounds().width + 8;
		h = fm.getStringBounds(text, g2d).getBounds().height + 4;

		if (!reverse)
			x = cx + 5;
		else
			x = cx - 5 - w;

		y = cy - h / 2;

		if (x + w > getWidth() - 5) {
			x = getWidth() - 5 - w;
		} else if (x < 5)
			x = 5;

		g2d.setColor(new Color(0, 0, 0, 180));
		g2d.fillRoundRect(x, y, w, h, 8, 8);
		g2d.setColor(Color.black);
		g2d.drawRoundRect(x, y, w, h, 8, 8);

		g2d.setColor(Color.white);
		g2d.drawString(text, x + 4, y + 2 + fm.getHeight() / 2
				- (int) fm.getStringBounds(text, g2d).getCenterY());
	}

	private String cropString(String s, Font f, FontRenderContext frc, int w) {
		if (s == null)
			return "";
		else if (f.getStringBounds(s, frc).getBounds().width <= w)
			return s;
		else if (s.length() >= 7)
			return cropString(s.substring(0, s.length() - 7) + "...", f, frc, w);
		else
			return "...";
	}

	private Font getFont(String s, Font f, FontRenderContext frc, int width) {
		if (f.getStringBounds(s, frc).getWidth() <= width)
			return f;
		else
			return getFont(s,
					new Font(f.getFontName(), f.getStyle(), f.getSize() - 1),
					frc, width);
	}

	public double getMinWarning() {
		return minWarning_;
	}

	public void setMinWarning(double minWarning_) {
		this.minWarning_ = minWarning_;
	}

	public double getMaxWarning() {
		return maxWarning_;
	}

	public void setMaxWarning(double maxWarning_) {
		this.maxWarning_ = maxWarning_;
	}

	public double getMinAlert() {
		return minAlert_;
	}

	public void setMinAlert(double minAlert_) {
		this.minAlert_ = minAlert_;
	}

	public double getMaxAlert() {
		return maxAlert_;
	}

	public void setMaxAlert(double maxAlert_) {
		this.maxAlert_ = maxAlert_;
	}

	public void setSimmetricalAlert(double val) {
		minAlert_ = min_ + val;
		maxAlert_ = max_ - val;
	}

	public void setSimmetricalWarning(double val) {
		minWarning_ = min_ + val;
		maxWarning_ = max_ - val;
	}

	public int getNumberOfScaleSteps() {
		return numberOfScaleSteps_;
	}

	public void setNumberOfScaleSteps(int numberOfScaleSteps_) {
		this.numberOfScaleSteps_ = numberOfScaleSteps_;
	}

	public ArrayList<GaugeMarker> getMarkers() {
		return markers_;
	}

	public void addMarker(GaugeMarker marker) {
		markers_.add(marker);
	}

	public void removeMarker(GaugeMarker marker) {
		markers_.remove(marker);
	}

	protected void triggerReferenceEvent() {
		ValueListener listeners[] = listenerList
				.getListeners(ValueListener.class);
		if (listeners == null || listeners.length == 0)
			return;
		ValueEvent ev = new ValueEvent(value_, reference_);
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].referencePositionChanged(ev);
		}
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setSize(220, 200);
		f.setTitle("Test");
		f.setResizable(false);

		JPanel p = new JPanel(null);
		f.setContentPane(p);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final GaugeMonitor gm = new GaugeMonitor("test gauge", "W", 0.5, -1, 10);

		gm.addMarker(new GaugeMarker(5.5, "ref 1"));
		gm.addMarker(new GaugeMarker(0, "ref 2"));

		gm.addMarker(new GaugeMarker(-0.9, "ref 3"));

		gm.setRefernce(0.3);
		gm.setMaxWarning(8);
		gm.setMaxAlert(9);
		gm.setMinWarning(0);
		gm.setMinAlert(-0.5);

		p.add(gm);
		gm.setBounds(5, 5, 200, 160);

		final ArrayList<Double> val = new ArrayList<Double>();
		val.add(new Double(0.4));
		gm.addValueListener(new ValueListener() {
			public void referencePositionChanged(ValueEvent e) {
				val.set(0, new Double(e.getReference()));
			}
		});

		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				double step = 1 / 50.0;
				if (Math.abs(gm.getValue() - gm.getReference()) > step / 4) {
					double dist = val.get(0).doubleValue() - gm.getValue();
					double dir = dist / Math.abs(dist);
					dist = Math.abs(dist);
					dir *= step * dist * 2;
					gm.setValue(gm.getValue() + dir);
				} else {
					double v = step / 4 * (Math.random() - 0.5);
					gm.setValue(gm.getValue() + v);
				}
			}
		}, 0, 100);
	}

	public boolean isReferenceEnabled() {
		return referenceEnabled_;
	}

	public void setReferenceEnabled(boolean referenceEnabled_) {
		this.referenceEnabled_ = referenceEnabled_;
	}

	public void setEditable(boolean edit) {
		editable_ = edit;
	}

	public boolean isEditable() {
		return editable_;

	}
}
