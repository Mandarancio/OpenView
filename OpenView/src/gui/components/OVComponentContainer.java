package gui.components;

import gui.components.nodes.InNode;
import gui.components.nodes.Line;
import gui.components.nodes.NodeGroup;
import gui.components.nodes.OutNode;
import gui.components.nodes.PolyInNode;
import gui.components.nodes.PolyOutNode;
import gui.components.ovprocedural.OVProceduralNode;
import gui.constants.ComponentSettings;
import gui.enums.DragAction;
import gui.enums.EditorMode;
import gui.interfaces.NodeListener;
import gui.interfaces.OVContainer;
import gui.interfaces.OVNode;
import gui.support.OVMaker.OVMakerMode;
import gui.support.OVToolTip;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import core.ValueType;
import core.support.OrientationEnum;

public class OVComponentContainer extends OVComponent implements OVContainer,
		NodeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ArrayList<OVComponent> components_ = new ArrayList<>();
	protected ArrayList<Line> lines_ = new ArrayList<>();

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
				if (((OVContainer) ovc).contains(c) && ((OVContainer)ovc).compatible(c)) {
					((OVContainer) ovc).addComponent(c);
					return;
				}
			}
		}
		Point p = superParent().getAbsoluteLocation(this, new Point(0, 0));
		p.x = c.getX() - p.x;
		p.y = c.getY() - p.y;
		c.setFather(this);
		components_.add(c);
		c.moveTo(p.x, p.y);
		this.add(c);
		select(c);
		c.setMode(getMode());
	}

	@Override
	public void removeComponent(OVComponent c) {
		deselect(c);
		components_.remove(c);
		c.delete();

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
		for (Line l : lines_) {
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
	public Line createLine(OVNode n, OVComponent ovComponent) {

		Line l = new Line(n, ovComponent, this);
		lines_.add(l);
		this.add(l);
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
			if (!g.getOutNode().equals(n) && !g.getOutNode().compatible(n))
				g.getOutNode().hide();
		}
		for (NodeGroup g : outterNodes_) {
			if (!g.getInNode().equals(n) && !g.getInNode().compatible(n))
				g.getInNode().show();
		}

		repaint();
	}

	@Override
	public void confirmLine(Line l) {
		for (OVComponent c : components_) {
			c.showNodes();
		}
		showOutterNodes();
		if (l == null)
			return;
		Point p = l.p2;

		if (l.a.getLocation().x + l.ca.getX() == p.x)
			p = l.p1;

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
	public void removeLine(Line line) {
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

	@Override
	public OVContainer parent() {

		return getFather();
	}

	@Override
	public OVContainer superParent() {
		if (getFather() != null)
			return getFather().superParent();
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
				for (Line l : lines_) {
					l.setVisible(true);
				}
			} else {
				for (Line l : lines_) {
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
		} else
			super.drop(dragAction_);
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
		ArrayList<Line> ll = new ArrayList<>(lines_);
		for (Line l : ll) {
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
		for (NodeGroup g : innerNodes_) {
			OutNode o = g.getOutNode();
			InNode i = g.getInNode();
			o.setLocation(new Point(i.getLocation().x + 10, i.getLocation().y));
		}

		for (NodeGroup g : outterNodes_) {
			OutNode o = g.getOutNode();
			InNode i = g.getInNode();
			i.setLocation(new Point(o.getLocation().x - 10, o.getLocation().y));
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
		for (Line l : lines_) {
			if (!l.equals(source) && l.contains(p))
				l.click(new Point(p.x - l.getX(), p.y - l.getY()), source);
		}
	}

	@Override
	public boolean compatible(OVComponent c) {
		if (c instanceof OVProceduralNode)
			return false;
		return true;
	}

}
