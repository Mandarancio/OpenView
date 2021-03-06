package gui.components;

import gui.components.nodes.InNode;
import gui.components.nodes.OVLine;
import gui.components.nodes.NodeGroup;
import gui.components.nodes.OutNode;
import gui.components.nodes.PolyInNode;
import gui.components.nodes.PolyOutNode;
import gui.components.ovnode.OVComment;
import gui.constants.ComponentSettings;
import gui.enums.DragAction;
import gui.enums.EditorMode;
import gui.interfaces.NodeListener;
import gui.interfaces.OVContainer;
import gui.interfaces.OVNode;
import gui.layers.NodeLayer;
import gui.support.OVMaker.OVMakerMode;
import gui.support.OVToolTip;
import gui.support.XMLParser;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.SwingUtilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import run.window.ObjectManager;
import run.window.ObjectTree;
import core.ValueType;
import core.support.OrientationEnum;

public class OVComponentContainer extends OVComponent implements OVContainer,
		NodeListener {

	/**
     *
     */
	private static final long serialVersionUID = 1L;
	protected static final Integer commentsLayer = new Integer(1);
	protected static final Integer linesLayer = new Integer(2);
	protected static final Integer componentsLayer = new Integer(3);

	protected ArrayList<OVComponent> components_ = new ArrayList<>();
	protected ArrayList<OVLine> lines_ = new ArrayList<>();

	protected ArrayList<NodeGroup> innerNodes_ = new ArrayList<>();
	protected ArrayList<NodeGroup> outterNodes_ = new ArrayList<>();

	public OVComponentContainer(OVContainer father) {
		super(father);
		this.setLayout(null);
		container_ = true;
		addDynamicInNode();
		addDynamicOutNode();
		getSetting(ComponentSettings.SizeW).setValue(120);
		getSetting(ComponentSettings.SizeH).setValue(120);
		getSetting(ComponentSettings.Name).setValue("Container");
	}

	public OVComponentContainer(Element e, OVContainer father) {
		super(e, father);
		this.setLayout(null);
		container_ = true;

		NodeList nl = e.getElementsByTagName("InnerNode");
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n != null && n instanceof Element) {
				Element el = (Element) n;
				if (el.getParentNode().equals(e)) {

					PolyOutNode out = new PolyOutNode(el, this);
					String uuid = el.getAttribute("connected");
					InNode in = (InNode) getNode(uuid);
					innerNodes_.add(new NodeGroup(in, out));
				}
			}
		}

		nl = e.getElementsByTagName("OutterNode");
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n != null && n instanceof Element) {
				Element el = (Element) n;
				if (el.getParentNode().equals(e)) {
					PolyInNode in = new PolyInNode(el, this);
					String uuid = el.getAttribute("connected");
					OutNode out = (OutNode) getNode(uuid);
					outterNodes_.add(new NodeGroup(in, out));
				}
			}
		}

		// load components
		loadXML((Element) e.getElementsByTagName("container").item(0));

		for (NodeGroup g : innerNodes_) {
			g.getInNode().addNodeListener(this);
			g.getOutNode().addNodeListener(this);
		}

		for (NodeGroup g : outterNodes_) {
			g.getInNode().addNodeListener(this);
			g.getOutNode().addNodeListener(this);
		}
	}

	protected void loadXML(Element el) {
		NodeList nl = el.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n != null && n instanceof Element) {
				Element e = (Element) n;
				if (e.getParentNode().equals(el)) {
					if (!e.getTagName().equals(OVLine.class.getSimpleName())) {
						XMLParser.loadComponent(e, this);
					}
				}
			}
		}

		nl = el.getElementsByTagName(OVLine.class.getSimpleName());
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n != null && n instanceof Element) {
				Element e = (Element) n;
				if (e.getParentNode().equals(el)) {
					OVLine l = XMLParser.parseLine(e, this);
					if (l != null) {
						lines_.add(l);
						this.add(l, linesLayer, 0);
						this.moveToBack(l);
						l.setSelected(false);
						l.addKeyListener((KeyListener) superParent());
						if (mode_ == EditorMode.RUN || mode_ == EditorMode.GUI) {
							l.setVisible(false);
						}
					}
				}
			}
		}
	}

	protected void addDynamicInNode() {
		InNode in = addInput("input", ValueType.VOID);
		Point p = new Point(in.getLocation());
		p.x += 10;
		PolyOutNode on = new PolyOutNode(p, "input", this);
		in.addNodeListener(this);
		on.addNodeListener(this);
		NodeGroup ng = new NodeGroup(in, on);
		innerNodes_.add(ng);
	}

	protected void addDynamicOutNode() {
		OutNode on = addOutput("output", ValueType.VOID);
		on.setPolyvalent(true);
		Point p = new Point(on.getLocation());
		p.x -= 10;
		PolyInNode in = new PolyInNode(p, "output", this);
		in.addNodeListener(this);
		on.addNodeListener(this);
		NodeGroup ng = new NodeGroup(in, on);

		outterNodes_.add(ng);
	}

	@Override
	public void addComponent(OVComponent c) {
		for (OVComponent ovc : components_) {
			if (!ovc.equals(c) && ovc.isContainer()) {
				if (((OVContainer) ovc).contains(c)
						&& ((OVContainer) ovc).compatible(c)) {
					((OVContainer) ovc).addComponent(c);
					return;
				}
			}
		}
		if (!c.getFather().equals(this)) {
			Point p = superParent().getAbsoluteLocation(this, new Point(0, 0));
			p.x = c.getX() - p.x;
			p.y = c.getY() - p.y;
			c.setFather(this);
			c.moveTo(p.x, p.y);
		}
		components_.add(c);
		c.addKeyListener((KeyListener) superParent());
		if (getObjectTree() != null && getObjectTree().hasComponent(this))
			getObjectTree().addComponent(c);
		ObjectManager.addComponent(c);
		if (c instanceof OVComment) {
			this.add(c, commentsLayer);
		} else {
			this.add(c, componentsLayer);
		}
		select(c);
		c.setMode(getMode());
	}

	@Override
	public void removeComponent(OVComponent c) {
		deselect(c);
		components_.remove(c);
		c.delete();
		if (getObjectTree() != null)
			getObjectTree().removeComponent(c);
		ObjectManager.removeComponent(c);
		this.remove(c);
		this.requestFocus();
	}

	@Override
	public void select(OVComponent c) {
		superParent().select(c);
		moveToFront(c);
	}

	@Override
	public void deselect(OVComponent c) {
		superParent().deselect(c);
	}

	@Override
	public void deselectAll() {
		this.setSelected(false);
		for (OVComponent c : components_) {
			if (c.isSelected()) {
				c.setSelected(false);
				c.repaint();
				if (c.isContainer()) {
					((OVContainer) c).deselectAll();
				}
			}
		}
		for (OVLine l : lines_) {
			if (l.isSelected()) {
				l.setSelected(false);
				l.repaint();
			}
		}
	}

	@Override
	public Point validate(Point p) {
		return superParent().validate(p);
	}

	@Override
	public Dimension validate(Dimension d) {
		return superParent().validate(d);
	}

	@Override
	public OVToolTip showToolTip(String tooltip, Point p,
			OrientationEnum orientation) {
		return superParent().showToolTip(tooltip,
				superParent().getAbsoluteLocation(this, p), orientation);
	}

	@Override
	public void hideToolTip(OVToolTip tooltip) {
		superParent().hideToolTip(tooltip);
	}

	@Override
	public Point getAbsoluteLocation(OVComponent c, Point location) {
		return SwingUtilities.convertPoint(c, location, this);
	}

	@Override
	public OVLine createLine(OVNode n, OVComponent ovComponent) {

		OVLine l = new OVLine(n, ovComponent, this);
		lines_.add(l);
		this.add(l, linesLayer, 0);
		this.moveToBack(l);
		l.setSelected(true);

		for (OVComponent c : components_) {
			c.hideNodes(n);
		}
		hideInnerNodes(n);
		return l;
	}

	private void hideInnerNodes(OVNode n) {

		for (NodeGroup g : innerNodes_) {
			if (!g.getOutNode().equals(n) && !g.getOutNode().compatible(n)) {
				g.getOutNode().hide();
			}
		}
		for (NodeGroup g : outterNodes_) {
			if (!g.getInNode().equals(n) && !g.getInNode().compatible(n)) {
				g.getInNode().show();
			}
		}

		repaint();
	}

	@Override
	public void confirmLine(OVLine l) {
		for (OVComponent c : components_) {
			c.showNodes();
		}
		showOutterNodes();
		if (l == null) {
			return;
		}
		Point p = l.p2;

		if (l.a.getLocation().x + l.ca.getX() == p.x) {
			p = l.p1;
		}

		for (NodeGroup g : innerNodes_) {
			OutNode n = g.getOutNode();
			if (n.contains(p)) {
				if (n.compatible(l.a) && l.a.compatible(n)) {
					l.connect(n, this);
					l.setSelected(false);
					l.addKeyListener((KeyListener) superParent());
					return;
				}
			}
		}
		for (NodeGroup g : outterNodes_) {
			InNode n = g.getInNode();
			if (n.contains(p)) {
				if (n.compatible(l.a) && l.a.compatible(n)) {
					l.connect(n, this);
					l.setSelected(false);
					l.addKeyListener((KeyListener) superParent());
					return;
				}
			}
		}

		for (OVComponent c : components_) {
			if (c.isVisible() && c.getBounds().contains(p)) {
				OVNode n = c.getNode(SwingUtilities.convertPoint(this, p, c));
				if (n != null && n.compatible(l.a) && l.a.compatible(n)) {
					l.connect(n, c);
					l.setSelected(false);
					l.addKeyListener((KeyListener) superParent());
					return;
				}
			}
		}
		this.remove(l);
		this.repaint(l.getBounds());
		l.delete();
		lines_.remove(l);
		return;
	}

	private void showOutterNodes() {
		for (NodeGroup g : innerNodes_) {
			g.getOutNode().show();
		}
		for (NodeGroup g : outterNodes_) {
			g.getInNode().show();
		}
		repaint();
	}

	@Override
	public void removeLine(OVLine line) {
		if (lines_.contains(line)) {
			lines_.remove(line);
			this.repaint(line.getBounds());
			this.remove(line);
		}

		this.requestFocus();
	}

	@Override
	public void showMenu(Point point) {
		superParent().showMenu(superParent().getAbsoluteLocation(this, point));
	}

	public void showMenu(Point point, OVMakerMode mode) {
		superParent().showMenu(superParent().getAbsoluteLocation(this, point),
				mode);
	}

	/**
	 * 
	 * @param p
	 * @param m
	 */
	@Override
	public void showCustomMenu(Point p, JMenu m) {
		superParent().showCustomMenu(
				superParent().getAbsoluteLocation(this, p), m);
	}

	@Override
	public OVContainer parent() {

		return getFather();
	}

	@Override
	public OVContainer superParent() {
		if (getFather() != null) {
			return getFather().superParent();
		}
		return null;
	}

	@Override
	public void rightClick(Point point) {
		super.rightClick(point);
		showMenu(point);
	}

	@Override
	public boolean contains(OVComponent c) {
		if (c.getParent() != null) {
			Rectangle r = new Rectangle(
					getAbsoluteLocation(c, new Point(0, 0)), c.getSize());
			return getBounds().contains(r);

		} else {
			Rectangle r = new Rectangle(superParent().getAbsoluteLocation(this,
					new Point()), getSize());
			return r.contains(c.getBounds());
		}
	}

	@Override
	public void setMode(EditorMode mode) {
		if (this.getMode() != mode) {
			super.setMode(mode);

			deselectAll();
			for (OVComponent component : components_) {
				component.setMode(mode);
			}
			if (mode == EditorMode.NODE || mode == EditorMode.DEBUG) {
				for (OVLine l : lines_) {
					l.setVisible(true);
				}
			} else {
				for (OVLine l : lines_) {
					l.setVisible(false);
				}
			}

			repaint();

		}

	}

	@Override
	protected void paintNodes(Graphics2D g2d) {
		super.paintNodes(g2d);
		paintInnerNodes(g2d);
	}

	protected void paintInnerNodes(Graphics2D g2d) {
		for (NodeGroup g : innerNodes_) {
			g.getOutNode().paint(g2d);
		}
		for (NodeGroup g : outterNodes_) {
			g.getInNode().paint(g2d);
		}
	}

	@Override
	public DragAction drag(Point point) {
		if (getMode() == EditorMode.NODE) {
			for (NodeGroup g : innerNodes_) {
				if (g.getOutNode().contains(point)) {
					__line = this.createLine(g.getOutNode(), this);
					setSelected(false);
					return DragAction.LINE;
				}
			}
			for (NodeGroup g : outterNodes_) {
				if (g.getInNode().contains(point)) {
					__line = this.createLine(g.getInNode(), this);
					setSelected(false);
					return DragAction.LINE;

				}
			}
		}

		return super.drag(point);
	}

	@Override
	public void drop(DragAction dragAction_) {
		if (dragAction_ == DragAction.LINE && this.lines_.contains(__line)) {
			this.confirmLine(__line);
			__line = null;
		} else {
			super.drop(dragAction_);
		}
	}

	@Override
	public void removeSelected() {
		ArrayList<OVComponent> list = new ArrayList<>();
		for (OVComponent c : components_) {
			if (c.isSelected()) {
				list.add(c);
			}
			if (c.isContainer()) {
				((OVContainer) c).removeSelected();
			}

		}
		for (OVComponent c : list) {
			repaint(c.getBounds());
			superParent().removeComponent(c);
			c.getFather().removeComponent(c);
		}
		ArrayList<OVLine> ll = new ArrayList<>(lines_);
		for (OVLine l : ll) {
			if (l.isSelected()) {
				l.deconnect();

				l.getFather().removeLine(l);
				l.removeKeyListener((KeyListener) superParent());
			}
		}

		this.requestFocus();
	}

	@Override
	public void connected(OVNode n) {

		refreshDynamicNodes();
	}

	@Override
	public void deconneced(OVNode n) {

		refreshDynamicNodes();
	}

	@Override
	protected void updateNodes() {
		super.updateNodes();
		if (innerNodes_ != null && outterNodes_ != null) {
			for (NodeGroup g : innerNodes_) {
				OutNode o = g.getOutNode();
				InNode i = g.getInNode();
				o.setLocation(new Point(i.getLocation().x + 10,
						i.getLocation().y));
			}

			for (NodeGroup g : outterNodes_) {
				OutNode o = g.getOutNode();
				InNode i = g.getInNode();
				i.setLocation(new Point(o.getLocation().x - 10,
						o.getLocation().y));
			}
		}
	}

	protected void refreshDynamicNodes() {
		ArrayList<NodeGroup> toRemove = new ArrayList<>();
		for (NodeGroup g : innerNodes_) {
			if (g.isFree()) {
				toRemove.add(g);
			}
		}

		for (NodeGroup n : toRemove) {
			removeInput(n.getInNode());
			n.getOutNode().delete();
			n.delete();
			innerNodes_.remove(n);
		}

		toRemove.clear();

		for (NodeGroup g : outterNodes_) {
			if (g.isFree()) {
				toRemove.add(g);
			}
		}

		for (NodeGroup n : toRemove) {
			removeOutput(n.getOutNode());
			n.getInNode().delete();
			n.delete();
			outterNodes_.remove(n);
		}

		toRemove.clear();
		updateNodes();

		addDynamicInNode();
		addDynamicOutNode();
	}

	@Override
	public void clickEvent(Point p, Object source) {
		for (OVLine l : lines_) {
			if (!l.equals(source) && l.contains(p)) {
				l.click(new Point(p.x - l.getX(), p.y - l.getY()), source);
			}
		}
	}

	@Override
	public boolean compatible(OVComponent c) {
		return true;
	}

	@Override
	public OVNode getNode(String parent, String uuid) {
		if (parent.equals(getUUID().toString())) {
			return getNode(uuid);
		}

		for (OVComponent c : components_) {
			if (c.getUUID().toString().equals(parent)) {
				return c.getNode(uuid);
			}
		}
		return null;
	}

	@Override
	public Element getXML(Document doc) {
		Element el = super.getXML(doc);
		Element e = doc.createElement("container");

		for (NodeGroup g : innerNodes_) {
			Element ee = g.getOutNode().getXML(doc);
			ee.setAttribute("connected", g.getInNode().getUUID().toString());
			doc.renameNode(ee, null, "InnerNode");
			el.appendChild(ee);
		}

		for (NodeGroup g : outterNodes_) {
			Element ee = g.getInNode().getXML(doc);
			ee.setAttribute("connected", g.getOutNode().getUUID().toString());
			doc.renameNode(ee, null, "OutterNode");
			el.appendChild(ee);
		}

		for (OVComponent c : components_) {
			e.appendChild(c.getXML(doc));
		}
		for (OVLine l : lines_) {
			e.appendChild(l.getXML(doc));
		}

		el.appendChild(e);
		return el; // To change body of generated methods, choose Tools |
		// Templates.
	}

	@Override
	public OVNode getNode(String uuid) {
		OVNode n = super.getNode(uuid);
		if (n == null) {
			for (NodeGroup g : innerNodes_) {
				if (g.getOutNode().getUUID().toString().equals(uuid)) {
					return g.getOutNode();
				}
			}
			for (NodeGroup g : outterNodes_) {
				if (g.getInNode().getUUID().toString().equals(uuid)) {
					return g.getInNode();
				}
			}
		} else {
			return n;
		}
		return null;
	}

	@Override
	public ObjectTree getObjectTree() {
		return superParent().getObjectTree();
	}

	@Override
	public void setNodeLayer(NodeLayer n) {
		super.setNodeLayer(n);
		if (mode_ == EditorMode.DEBUG || mode_ == EditorMode.NODE) {
			for (OVComponent c : components_) {
				c.setNodeLayer(n);
			}
		}
	}
}
