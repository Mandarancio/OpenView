package gui.components;

import gui.adapters.DragMouseAdapter;
import gui.components.nodes.InNode;
import gui.components.nodes.Line;
import gui.components.nodes.OutNode;
import gui.constants.ComponentSettings;
import gui.enums.DragAction;
import gui.enums.EditorMode;
import gui.interfaces.DragComponent;
import gui.interfaces.OVContainer;
import gui.interfaces.OVNode;
import gui.interfaces.SettingListener;
import gui.layers.AssociatedNodeLayer;
import gui.layers.NodeLayer;
import gui.support.OVToolTip;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import core.Setting;
import core.Value;
import core.ValueType;
import core.support.OrientationEnum;

public class OVComponent extends JLayeredPane implements DragComponent,
		SettingListener {

	/**
     *
     */
	private static final long serialVersionUID = -1690496040368405673L;
	private OVContainer father_;
	private boolean selected_ = false;
	protected DragMouseAdapter mouseAdapter_;
	protected HashMap<String, ArrayList<Setting>> settings_ = new HashMap<>();
	private HashMap<String, ArrayList<Setting>> nodeSettings_ = new HashMap<>();

	protected Line __line = null;

	protected ArrayList<OutNode> outputs_ = new ArrayList<>();
	protected ArrayList<InNode> inputs_ = new ArrayList<>();
	protected boolean resizable_ = true;
	protected EditorMode mode_ = EditorMode.GUI;
	protected boolean over_ = false;
	protected boolean container_ = false;
	protected int __minY = 0;
	private OVToolTip toolTip_;

	private UUID uuid_;
	private ArrayList<AssociatedNodeLayer> layers_ = new ArrayList<>();
	private AssociatedNodeLayer currentLayer_;

	public OVComponent(OVContainer father) {
		father_ = (father);
		uuid_ = UUID.randomUUID();
		this.setMinimumSize(new Dimension(30, 30));
		this.setBackground(new Color(69, 70, 64));
		this.setForeground(new Color(200, 200, 200));
		this.setBounds(0, 0, 60, 60);
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setOpaque(false);
		this.setLayout(new BorderLayout());

		initBasicSettings();

		mouseAdapter_ = new DragMouseAdapter(this);
		this.addMouseListener(mouseAdapter_);
		this.addMouseMotionListener(mouseAdapter_);

	}

	public OVComponent(Element e, OVContainer father) {
		father_ = father;
		this.setMinimumSize(new Dimension(30, 30));
		this.setBackground(new Color(69, 70, 64));
		this.setForeground(new Color(200, 200, 200));
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.setBounds(0, 0, 60, 60);
		uuid_ = UUID.fromString(e.getAttribute("uuid"));

		loadSettings(e);
		loadNodes(e);
		loadLayers(e);

		mouseAdapter_ = new DragMouseAdapter(this);
		this.addMouseListener(mouseAdapter_);
		this.addMouseMotionListener(mouseAdapter_);
	}

	private void loadLayers(Element el) {
		NodeList nl = el.getElementsByTagName(AssociatedNodeLayer.class
				.getSimpleName());
		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			if (e.getParentNode().equals(el)) {
				layers_.add(new AssociatedNodeLayer(e));
			}
		}
	}

	protected void initBasicSettings() {
		Setting s = new Setting(ComponentSettings.Name, getClass()
				.getSimpleName().replace("OV", ""), this);
		addBothSetting(ComponentSettings.GenericCategory, s);
		s.setNodeMode(false);
		s.setGuiMode(true);

		s = new Setting(ComponentSettings.Family, getClass().getSimpleName(),
				this);
		s.setConstant(true);
		addBothSetting(ComponentSettings.GenericCategory, s);
		s.setNodeMode(false);
		s.setGuiMode(true);

		s = new Setting(ComponentSettings.Enable, new Boolean(true), this);
		addBothSetting(ComponentSettings.GenericCategory, s);
		s.setNodeMode(true);
		s.setGuiMode(true);

		s = new Setting(ComponentSettings.PosX, 0, 0, 1920, this);
		addSetting(ComponentSettings.GeometryCategory, s);
		s = new Setting(ComponentSettings.PosY, 0, 0, 1200, this);
		addSetting(ComponentSettings.GeometryCategory, s);
		s = new Setting(ComponentSettings.SizeW, 50, 30, 1920, this);
		addSetting(ComponentSettings.GeometryCategory, s);
		s = new Setting(ComponentSettings.SizeH, 50, 30, 1200, this);
		addSetting(ComponentSettings.GeometryCategory, s);

	}

	protected void loadSettings(Element e) {
		Element gs = (Element) e.getElementsByTagName("GUISettings").item(0);
		NodeList gsl = gs.getElementsByTagName("category");
		for (int i = 0; i < gsl.getLength(); i++) {
			Element c = (Element) gsl.item(i);
			String cname = c.getAttribute("name");
			NodeList nl = c.getElementsByTagName(Setting.class.getSimpleName());
			for (int j = 0; j < nl.getLength(); j++) {
				addSetting(cname, new Setting((Element) nl.item(j), this));
			}
		}

		gs = (Element) e.getElementsByTagName("NODESettings").item(0);
		gsl = gs.getElementsByTagName("category");
		for (int i = 0; i < gsl.getLength(); i++) {
			Element c = (Element) gsl.item(i);
			String cname = c.getAttribute("name");
			NodeList nl = c.getElementsByTagName(Setting.class.getSimpleName());
			for (int j = 0; j < nl.getLength(); j++) {
				Element el = (Element) nl.item(j);

				Setting s = getSetting(el.getAttribute("name"));
				if (s != null) {
					updateNodeSetting(cname, s);
				} else {
					addNodeSetting(cname, new Setting(el, this));
				}
			}
		}
		mode_ = EditorMode.GUI;
		triggerSettings();
	}

	protected void triggerSettings() {
		for (String s : settings_.keySet()) {
			for (Setting stg : settings_.get(s)) {
				stg.trigg();
			}
		}
	}

	protected void loadNodes(Element el) {
		NodeList nl = el.getElementsByTagName(InNode.class.getSimpleName());
		for (int i = 0; i < nl.getLength(); i++) {

			Element e = (Element) nl.item(i);
			if (e.getParentNode().equals(el)) {

				String uuid = e.getAttribute("uuid");
				if (!nodeExist(uuid)) {
					InNode n = new InNode(e, this);
					addInput(n);
				}
			}
		}

		nl = el.getElementsByTagName(OutNode.class.getSimpleName());
		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			if (e.getParentNode().equals(el)) {
				String uuid = e.getAttribute("uuid");
				if (!nodeExist(uuid)) {
					OutNode n = new OutNode(e, this);
					addOutput(n);
				}
			}
		}
	}

	private boolean nodeExist(String uuid) {
		for (InNode n : inputs_) {
			if (n.getUUID().toString().equals(uuid)) {
				return true;
			}
		}
		for (OutNode n : outputs_) {
			if (n.getUUID().toString().equals(uuid)) {
				return true;
			}
		}
		return false;
	}

	protected void addSetting(String cat, Setting s) {
		if (settings_.containsKey(cat)) {
			settings_.get(cat).add(s);
		} else {
			ArrayList<Setting> c = new ArrayList<>();
			c.add(s);
			settings_.put(cat, c);
		}
		s.addListener(this);
	}

	protected void addNodeSetting(String cat, Setting s) {
		if (nodeSettings_.containsKey(cat)) {
			nodeSettings_.get(cat).add(s);
		} else {
			ArrayList<Setting> c = new ArrayList<>();
			c.add(s);
			nodeSettings_.put(cat, c);
		}
		s.setGuiMode(false);
		s.addListener(this);
	}

	protected void addBothSetting(String cat, Setting s) {

		if (nodeSettings_.containsKey(cat)) {
			nodeSettings_.get(cat).add(s);
		} else {
			ArrayList<Setting> c = new ArrayList<>();
			c.add(s);
			nodeSettings_.put(cat, c);
		}

		if (settings_.containsKey(cat)) {
			settings_.get(cat).add(s);
		} else {
			ArrayList<Setting> c = new ArrayList<>();
			c.add(s);
			settings_.put(cat, c);
		}
		s.setGuiMode(false);
		s.addListener(this);
	}

	protected void updateNodeSetting(String cat, Setting s) {

		if (nodeSettings_.containsKey(cat)) {
			nodeSettings_.get(cat).add(s);
		} else {
			ArrayList<Setting> c = new ArrayList<>();
			c.add(s);
			nodeSettings_.put(cat, c);
		}
	}

	public Set<String> getSettingCategories() {
		return settings_.keySet();
	}

	public ArrayList<Setting> getSettingCategory(String name) {
		return settings_.get(name);
	}

	public Setting getSetting(String name) {
		for (String c : settings_.keySet()) {
			for (Setting s : settings_.get(c)) {
				if (s.getName().equals(name)) {
					return s;
				}
			}
		}
		return null;
	}

	public ArrayList<Setting> getSettings() {
		ArrayList<Setting> stg = new ArrayList<>();
		for (String key : settings_.keySet()) {
			stg.addAll(settings_.get(key));
		}
		return stg;
	}

	public Set<String> getNodeSettingCategories() {
		return nodeSettings_.keySet();
	}

	public ArrayList<Setting> getNodeSettingCategory(String name) {
		return nodeSettings_.get(name);
	}

	public Setting getNodeSetting(String name) {
		for (String c : nodeSettings_.keySet()) {
			for (Setting s : nodeSettings_.get(c)) {
				if (s.getName().equals(name)) {
					return s;
				}
			}
		}
		return null;
	}

	public ArrayList<Setting> getNodeSettings() {
		ArrayList<Setting> stg = new ArrayList<>();
		for (String key : nodeSettings_.keySet()) {
			stg.addAll(nodeSettings_.get(key));
		}
		return stg;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if (mode_ != EditorMode.RUN) {
			g2d.setColor(isSelected() ? getBackground().brighter()
					: getBackground());
			g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

			g2d.setColor(isSelected() ? Color.lightGray : Color.lightGray
					.darker());
			g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
		}
		if (mode_ != EditorMode.NODE) {
			paintOVComponent(g2d);
		} else {
			paintOVNode(g2d);
		}

	}

	protected void paintOVNode(Graphics2D g2d) {
		g2d.setColor(Color.lightGray);
		paintText(getName(), g2d, new Rectangle(0, 0, getWidth(), getHeight()),
				OrientationEnum.CENTER);
		// g2d.setFont(getFont().deriveFont(10.0f));
		// paintText(getClass().getSimpleName(), g2d, new Rectangle(0,
		// getHeight() / 4, getWidth(), getHeight() * 3 / 4),
		// OrientationEnum.CENTER);
	}

	public OVContainer getFather() {
		return father_;
	}

	public void setFather(OVContainer father_) {
		this.father_ = father_;
	}

	public boolean isSelected() {
		return selected_;
	}

	public void setSelected(boolean selected_) {
		this.selected_ = selected_;
	}

	protected void paintNodes(Graphics2D g2d) {
		for (InNode n : inputs_) {
			n.paint(g2d);
		}
		for (OutNode n : outputs_) {
			n.paint(g2d);
		}
	}

	public void select() {
		if (!selected_ && father_ != null) {
			father_.select(this);
		}
	}

	@Override
	public void leftClick(Point point) {
		select();
	}

	@Override
	public void doubleClick(Point point) {
		select();
	}

	@Override
	public void rightClick(Point point) {
		select();
	}

	@Override
	public DragAction drag(Point point) {
		select();
		if (mode_.isExec()) {
			return DragAction.PRESSED;
		} else if (mode_ == EditorMode.NODE) {
			for (OutNode n : outputs_) {
				if (n.contains(point)) {
					__line = father_.createLine(n, this);

					return DragAction.LINE;
				}
			}
			for (InNode n : inputs_) {
				if (n.contains(point)) {
					__line = father_.createLine(n, this);

					return DragAction.LINE;

				}
			}
		}
		if (point.x > getWidth() - 10 && point.y > getHeight() - 10) {
			return DragAction.RESIZE;
		}
		return DragAction.DRAG;
	}

	@Override
	public void drop(DragAction dragAction_) {

		if (dragAction_ == DragAction.DRAG) {
			Point p = father_.validate(getLocation());
			getSetting(ComponentSettings.PosX).setValue(p.x);
			getSetting(ComponentSettings.PosY).setValue(p.y);
		} else if (dragAction_ == DragAction.RESIZE) {
			Dimension d = father_.validate(getSize());
			getSetting(ComponentSettings.SizeW).setValue(d.width);
			getSetting(ComponentSettings.SizeH).setValue(d.height);
		} else if (dragAction_ == DragAction.LINE) {
			father_.confirmLine(__line);
			__line = null;
		}
	}

	@Override
	public void moveOf(int dx, int dy) {
		// this.setLocation(getX() + dx, getY() + dy);
		//
		getSetting(ComponentSettings.PosX).setValue(getX() + dx);
		getSetting(ComponentSettings.PosY).setValue(getY() + dy);
	}

	public void moveTo(int x, int y) {
		getSetting(ComponentSettings.PosX).setValue(x);
		getSetting(ComponentSettings.PosY).setValue(y);
	}

	@Override
	public void resizeTo(int w, int h) {
		if (!resizable_) {
			return;
		}
		getSetting(ComponentSettings.SizeW).setValue(w);
		getSetting(ComponentSettings.SizeH).setValue(h);

	}

	@Override
	public void valueUpdated(Setting s, Value v) {
		String setting = s.getName();
		if (setting.equals(ComponentSettings.Name)) {
			setName(v.getString());
			repaint();
		} else if (setting.equals(ComponentSettings.PosX)) {
			try {
				setLocation(v.getInt(), getY());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (setting.equals(ComponentSettings.PosY)) {
			try {
				setLocation(getX(), v.getInt());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (setting.equals(ComponentSettings.SizeW)) {
			if (!resizable_) {
				return;
			}
			try {
				setSize(v.getInt(), getHeight());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (setting.equals(ComponentSettings.SizeH)) {
			if (!resizable_) {
				return;
			}
			try {
				setSize(getWidth(), v.getInt());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (setting.equals(ComponentSettings.Enable)) {
			try {
				boolean e = v.getBoolean();
				if (e != isEnabled()) {
					this.setEnabled(e);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		updateNodes();
		revalidate();
	}

	public EditorMode getMode() {
		return mode_;
	}

	public void setMode(EditorMode mode) {
		if (mode != this.mode_) {
			this.mode_ = mode;
			for (Setting stg : getSettings()) {
				stg.setMode(getMode());
			}

			for (Setting s : getNodeSettings()) {
				s.setMode(mode);
			}

			if (!isContainer() && mode == EditorMode.NODE) {
				for (Component c : getComponents()) {
					c.setVisible(false);
				}
			} else {
				for (Component c : getComponents()) {
					c.setVisible(true);
				}
			}
		}
		repaint();

	}

	protected void paintOVComponent(Graphics2D g2d) {
	}

	protected InNode addInput(String label, ValueType type) {
		int y = __minY + 2 + (2 + InNode.radius * 2) * (inputs_.size())
				+ InNode.radius;
		int x = 2 + InNode.radius;

		InNode node = new InNode(new Point(x, y), label, type, this);

		inputs_.add(node);
		repaint();

		return node;
	}

	public OutNode addOutput(String label, ValueType type) {
		int x = getWidth() - (OutNode.radius * 2 - 1);
		int y = __minY + 2 + (2 + OutNode.radius * 2) * (outputs_.size())
				+ OutNode.radius;

		OutNode node = new OutNode(new Point(x, y), label, type, this);
		outputs_.add(node);

		repaint();
		return node;
	}

	protected void updateNodes() {
		int x = getWidth() - (OutNode.radius * 2 - 1);
		int y = 2 + OutNode.radius + __minY;

		for (OutNode n : outputs_) {
			n.setLocation(new Point(x, y));
			y += 2 + OutNode.radius * 2;

		}

		y = 2 + OutNode.radius + __minY;

		for (InNode n : inputs_) {
			n.setLocation(new Point(n.getLocation().x, y));
			y += 2 + OutNode.radius * 2;
		}

	}

	@Override
	public void mouseMoved(Point p) {
		if (over_ == false) {
			over_ = true;
			repaint();
		}
		if (mode_ == EditorMode.NODE) {
			for (OutNode n : outputs_) {
				if (n.over && !n.contains(p)) {
					father_.hideToolTip(toolTip_);
					n.over = false;
				} else if (!n.over && n.contains(p)) {
					n.over = true;
					if (toolTip_ != null) {
						father_.hideToolTip(toolTip_);
					}

					toolTip_ = father_.showToolTip(n.getLabel(),
							father_.getAbsoluteLocation(this, n.getLocation()),
							OrientationEnum.RIGHT);
				}
			}
			for (InNode n : inputs_) {
				if (n.over && !n.contains(p)) {
					father_.hideToolTip(toolTip_);
					n.over = false;
				} else if (!n.over && n.contains(p)) {
					n.over = true;
					if (toolTip_ != null) {
						father_.hideToolTip(toolTip_);
					}
					toolTip_ = father_.showToolTip(n.getLabel(),
							father_.getAbsoluteLocation(this, n.getLocation()),
							OrientationEnum.LEFT);
				}
			}
		}
	}

	@Override
	public void mouseExited() {
		if (father_ != null)
			father_.hideToolTip(toolTip_);
		over_ = false;
		repaint();
	}

	@Override
	public void moveLineTo(int x, int y) {
		if (__line != null) {
			Point p = SwingUtilities.convertPoint(this, new Point(x, y),
					__line.ca);
			__line.updateP2(p);
		}
	}

	public OVNode getNode(Point p) {
		for (OutNode n : outputs_) {
			if (n.contains(p)) {
				return n;
			}
		}

		for (InNode n : inputs_) {
			if (n.contains(p)) {
				return n;
			}
		}

		return null;
	}

	public void refreshInputs(Setting s, boolean selected) {
		if (selected) {
			s.setInputNode(this.addInput(s.getName(), s.getType()));
		} else {
			this.inputs_.remove(s.getInputNode());
			s.removeNode();

			updateNodes();
			repaint();
		}

	}

	public void refreshOutputs(Setting s, boolean selected) {
		if (selected) {
			s.setOutputNode(this.addOutput(s.getName(), s.getType()));
		} else {
			this.outputs_.remove(s.getOutputNode());
			s.removeOutputNode();

			updateNodes();
			repaint();
		}
	}

	protected void removeInput(InNode n) {
		inputs_.remove(n);
		n.delete();
		updateNodes();
		repaint();
	}

	protected void removeOutput(OutNode n) {
		outputs_.remove(n);
		n.delete();
		updateNodes();
		repaint();
	}

	public Element getXML(Document doc) {
		Element node = doc.createElement(getClass().getSimpleName());
		node.setAttribute("uuid", uuid_.toString());
		Element el = doc.createElement("GUISettings");
		for (String key : settings_.keySet()) {
			Element subNode = doc.createElement("category");
			subNode.setAttribute("name", key);
			for (Setting s : settings_.get(key)) {
				subNode.appendChild(s.getXML(doc));
			}
			el.appendChild(subNode);
		}
		node.appendChild(el);

		el = doc.createElement("NODESettings");
		for (String key : nodeSettings_.keySet()) {
			Element subNode = doc.createElement("category");
			subNode.setAttribute("name", key);
			for (Setting s : nodeSettings_.get(key)) {
				subNode.appendChild(s.getXML(doc));
			}
			el.appendChild(subNode);
		}
		node.appendChild(el);

		for (InNode in : inputs_) {
			node.appendChild(in.getXML(doc));
		}

		for (OutNode out : outputs_) {
			node.appendChild(out.getXML(doc));
		}

		for (AssociatedNodeLayer l : layers_) {
			node.appendChild(l.getXML(doc));
		}

		return node;
	}

	public void delete() {
		for (InNode n : inputs_) {
			n.delete();
		}
		for (OutNode n : outputs_) {
			n.delete();
		}

	}

	@Override
	protected void paintChildren(Graphics grphcs) {

		super.paintChildren(grphcs); // To change body of generated methods,

		// choose Tools | Templates.
		if (mode_ == EditorMode.NODE || mode_ == EditorMode.DEBUG) {
			paintNodes((Graphics2D) grphcs);
		}
	}

	protected void paintText(String s, Graphics2D g, OrientationEnum o) {
		paintText(s, g, new Rectangle(0, 0, getWidth(), getHeight()), o);
	}

	protected void paintText(String s, Graphics2D g, Rectangle bounds,
			OrientationEnum orientation) {
		Rectangle2D r = g.getFontMetrics().getStringBounds(s, g);
		int x = bounds.x + 2;
		if (orientation == OrientationEnum.CENTER) {
			x = bounds.x
					+ (int) Math.round((bounds.width - r.getWidth()) / 2.0);
		} else if (orientation == OrientationEnum.RIGHT) {
			x = bounds.x + bounds.width - 2 - (int) r.getWidth();
		}
		g.drawString(
				s,
				x,
				bounds.y
						+ (int) Math.round(bounds.height / 2.0 - r.getCenterY()));
	}

	public void hideNodes(OVNode n) {
		for (OutNode o : outputs_) {
			if (!n.equals(o) && !o.compatible(n)) {
				o.hide();
			}
		}
		for (InNode i : inputs_) {
			if (!n.equals(i) && !i.compatible(n)) {
				i.hide();
			}
		}
		repaint();
	}

	public void showNodes() {
		for (OutNode o : outputs_) {
			o.show();
		}
		for (InNode i : inputs_) {
			i.show();
		}
		repaint();
	}

	public boolean isContainer() {
		return container_;
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (enabled != isEnabled()) {
			super.setEnabled(enabled);
			for (Component c : getComponents()) {
				c.setEnabled(enabled);
			}
		}
	}

	@Override
	public String toString() {
		return getName() + " - " + getClass().getSimpleName();
	}

	public UUID getUUID() {
		return uuid_;
	}

	public void addOutput(OutNode node) {
		if (outputs_.size() > 0) {
			int i = 0;
			boolean flag = false;
			int y = node.getLocation().y;
			for (; i < outputs_.size(); i++) {
				if (outputs_.get(i).getLocation().y > y) {
					flag = true;
					break;
				}
			}
			if (flag) {
				outputs_.add(i - 1, node);
			} else {
				outputs_.add(node);
			}
		} else {
			outputs_.add(node);
		}
		repaint();
	}

	public void addInput(InNode node) {
		if (inputs_.size() > 0) {
			int i = 0;
			boolean flag = false;
			int y = node.getLocation().y;
			for (; i < inputs_.size(); i++) {
				if (inputs_.get(i).getLocation().y > y) {
					flag = true;

					break;
				}
			}
			if (flag) {
				inputs_.add(i - 1, node);
			} else {
				inputs_.add(node);
			}
		} else {
			inputs_.add(node);
		}
		repaint();
	}

	public OVNode getNode(String uuid) {
		for (InNode n : inputs_) {
			if (n.getUUID().toString().equals(uuid)) {
				return n;
			}
		}
		for (OutNode n : outputs_) {
			if (n.getUUID().toString().equals(uuid)) {
				return n;
			}
		}

		return null;
	}

	public InNode getInNode(String label) {
		for (InNode n : inputs_) {
			if (n.getLabel().equals(label))
				return n;
		}
		return null;
	}

	public OutNode getOutNode(String label) {
		for (OutNode n : outputs_) {
			if (n.getLabel().equals(label))
				return n;
		}
		return null;
	}

	public void resetUUID() {
		uuid_ = UUID.randomUUID();
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
	}

	public void setNodeLayer(NodeLayer n) {
		if (n == null) {
			currentLayer_ = null;
			setVisible(true);
		} else {
			for (AssociatedNodeLayer l : layers_) {
				if (l.getUUID().equals(n.getUUID())) {
					currentLayer_ = l;
					if (mode_ == EditorMode.DEBUG || mode_ == EditorMode.NODE) {
						setVisible(l.isVisible());
					}
					return;
				}
			}
			setVisible(true);
			currentLayer_ = new AssociatedNodeLayer(n);
			layers_.add(currentLayer_);
		}
	}

	public void removeNodeLayer(NodeLayer n) {
		if (n != null) {
			this.setVisible(true);
			ArrayList<AssociatedNodeLayer> copy = new ArrayList<>(layers_);
			for (AssociatedNodeLayer l : copy) {
				if (l.getUUID().equals(n.getUUID())) {
					layers_.remove(l);
					return;
				}
			}
		}
	}

	public ArrayList<AssociatedNodeLayer> getNodeLayers() {
		return layers_;
	}

	public void checkLayer() {
		if (mode_ == EditorMode.NODE || mode_ == EditorMode.DEBUG) {
			if (currentLayer_ != null) {
				System.err.println("visible");
				setVisible(currentLayer_.isVisible());
			}
		}
	}

	public AssociatedNodeLayer getActiveLayer() {
		return currentLayer_;
	}

}
