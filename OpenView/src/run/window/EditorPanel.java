package run.window;

import gui.adapters.ContainerMouseAdapter;
import gui.components.OVComponent;
import gui.components.nodes.OVLine;
import gui.components.ovnode.OVComment;
import gui.constants.ComponentSettings;
import gui.enums.DragAction;
import gui.enums.EditorMode;
import gui.interfaces.OVContainer;
import gui.interfaces.OVNode;
import gui.layers.NodeLayer;
import gui.support.OVMaker;
import gui.support.OVMaker.OVMakerMode;
import gui.support.OVToolTip;
import gui.support.XMLParser;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import run.window.support.XMLBuilder;
import core.Value;
import core.support.OrientationEnum;

/***
 * Editor panel, is here all the graphics magic happen
 * 
 * @author martino
 * 
 */
public class EditorPanel extends OVComponent implements OVContainer,
		KeyListener {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 1075006905710700282L;
	/***
	 * Grid size in pixel
	 */
	private static final int GridStep = 15;
	/***
	 * Background tile size
	 */
	private static final int TileSize = 80 * 15;
	/***
	 * Index of the comments layer
	 */
	public static final Integer commentsLayer = new Integer(1);
	/***
	 * Index of the lines layer
	 */
	public static final Integer linesLayer = new Integer(2);
	/***
	 * Index of the ov components layer
	 */
	public static final Integer componentsLayer = new Integer(3);
	/***
	 * Index of the tool-tips layer
	 */
	public static final Integer toolTipLayer = new Integer(4);

	/**
	 * Background gird texture
	 */
	private BufferedImage bgTile_;

	/***
	 * List of components
	 */
	private ArrayList<OVComponent> components_ = new ArrayList<>();
	/***
	 * List of lines
	 */
	private ArrayList<OVLine> lines_ = new ArrayList<>();

	/***
	 * Right setting panels
	 */
	private RightPanel rightPanel_;
	/***
	 * Mouse adapter
	 */
	private ContainerMouseAdapter mouseAdapter_;

	/***
	 * Grid visible flag
	 */
	private boolean gridVisible_ = false;
	/***
	 * Grid enabled flag
	 */
	private boolean gridEnable_ = false;

	/***
	 * Object tree reference
	 */
	private ObjectTree objectTree_;
	/***
	 * Editor mode
	 */
	private EditorMode mode_ = EditorMode.GUI;
	/***
	 * Clip-board of element to copy
	 */
	private ArrayList<Element> clipboard_;
	/***
	 * current Node layer
	 */
	private NodeLayer currentLayer_;
	/***
	 * List of node layers
	 */
	private ArrayList<NodeLayer> nodeLayers_ = new ArrayList<>();

	/***
	 * Lock flag
	 */
	private boolean __lock = false;

	/***
	 * initialize the editor panel
	 * 
	 * @param panel
	 */
	public EditorPanel(RightPanel panel) {
		super(null);
		if (panel != null) {
			this.objectTree_ = panel.getObjectTree();
			panel.getLayerManager().setMainContainer(this);
		}
		this.setLayout(null);
		this.setBackground(new Color(69, 70, 64));
		this.setForeground(new Color(200, 200, 200));
		this.rightPanel_ = panel;
		this.mouseAdapter_ = new ContainerMouseAdapter(this);
		this.addMouseListener(mouseAdapter_);
		this.addKeyListener(this);
		this.getSetting(ComponentSettings.Name).setValue("Root");
		this.getSetting(ComponentSettings.Name).setConstant(true);
		this.getSetting(ComponentSettings.Enable).setConstant(true);
		this.getSetting(ComponentSettings.PosX).setConstant(true);
		this.getSetting(ComponentSettings.PosY).setConstant(true);
		getSetting(ComponentSettings.SizeH)
				.setMax(new Value(Integer.MAX_VALUE));
		getSetting(ComponentSettings.SizeW)
				.setMax(new Value(Integer.MAX_VALUE));

		initBG();
	}

	/***
	 * Initialize the background tiles
	 */
	private void initBG() {
		// Fixed size of the texture
		int w = TileSize, h = TileSize;
		// Create a new buffered image and get the graphics2d object
		bgTile_ = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) bgTile_.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Fill the background
		g.setColor(getBackground());
		g.fillRoundRect(0, 0, w, h, 0, 0);

		// Set the grid color
		g.setColor(getBackground().brighter());

		// Draw the grid
		for (int i = 0; i < w / GridStep + 1; i++) {
			g.drawLine(i * GridStep, 0, i * GridStep, h);
		}
		for (int i = 0; i < h / GridStep + 1; i++) {
			g.drawLine(0, i * GridStep, w, i * GridStep);
		}
	}

	/***
	 * Set key listener to component
	 * 
	 * @param c
	 * 
	 */
	private void setKeyListener(OVComponent c) {
		KeyListener[] list = c.getKeyListeners();
		if (list != null && list.length > 0) {
			for (KeyListener l : list) {
				if (l.equals(this)) {
					return;
				}
			}
		}
		c.addKeyListener(this);
	}

	@Override
	public void addComponent(OVComponent c) {
		boolean addFlag = false;
		initLayers(c);
		c.setVisible(true);
		for (OVComponent ovc : components_) {
			if (!ovc.equals(c) && ovc.isContainer()) {
				if (((OVContainer) ovc).contains(c)
						&& ((OVContainer) ovc).compatible(c)) {
					((OVContainer) ovc).addComponent(c);
					addFlag = true;
					break;
				}
			}
		}
		if (this.compatible(c) || addFlag) {
			ObjectManager.addComponent(c);
			if (objectTree_ != null)
				objectTree_.addComponent(c);
			setKeyListener(c);
			if (!addFlag) {
				components_.add(c);
				if (c instanceof OVComment) {
					this.add(c, commentsLayer, 0);
				} else {
					this.add(c, componentsLayer, 0);
				}
				select(c);
				c.setMode(mode_);
			}
		}
	}

	@Override
	public void removeComponent(OVComponent c) {
		ObjectManager.removeComponent(c);
		if (objectTree_ != null)
			objectTree_.removeComponent(c);
		if (components_.contains(c)) {
			if (rightPanel_ != null)
				rightPanel_.deselect();
			c.delete();
			components_.remove(c);
			this.remove(c);
		}
	}

	@Override
	public void select(OVComponent c) {
		deselectAll();
		c.setSelected(true);
		if (c.getParent().equals(this)) {
			this.moveToFront(c);
		}
		if (rightPanel_ != null) {
			rightPanel_.deselect();
			rightPanel_.select(c);
			rightPanel_.repaint();
		}
	}

	@Override
	public void deselect(OVComponent c) {
		c.setSelected(false);
		if (rightPanel_ != null)
			rightPanel_.deselect();
	}

	@Override
	public void deselectAll() {
		for (OVComponent c : components_) {
			if (c.isSelected()) {
				c.setSelected(false);
				c.repaint();
			}
			if (c.isContainer()) {
				((OVContainer) c).deselectAll();
			}
		}
		for (OVLine l : lines_) {
			if (l.isSelected()) {
				l.setSelected(false);
				l.repaint();
			}
		}
		if (rightPanel_ != null) {
			rightPanel_.deselect();
			rightPanel_.select(this);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(getBackground().brighter());
		if (isGridVisible() && mode_ != EditorMode.RUN) {
			if (bgTile_ != null) {
				int nw = getWidth() / TileSize;
				int nh = getHeight() / TileSize;
				// Repeat the background texture
				for (int i = 0; i <= nw; i++) {
					for (int j = 0; j <= nh; j++) {
						g.drawImage(bgTile_, i * TileSize, j * TileSize, this);
					}
				}
			} else {
				for (int x = GridStep; x < getWidth(); x += GridStep) {
					g.drawLine(x, 0, x, getHeight());
				}
				for (int y = GridStep; y < getHeight(); y += GridStep) {
					g.drawLine(0, y, getWidth(), y);
				}
			}
		}
	}

	/***
	 * Check if grid is visible
	 * 
	 * @return
	 */
	public boolean isGridVisible() {
		return gridVisible_;
	}

	/***
	 * Set grid visible flag
	 * 
	 * @param gridVisible
	 *            flag
	 */
	public void setGridVisible(boolean gridVisible) {
		this.gridVisible_ = gridVisible;
	}

	/***
	 * check if grid is enabled
	 * 
	 * @return
	 */
	public boolean isGridEnabled() {
		return gridEnable_;
	}

	/***
	 * set grid enabled flag
	 * 
	 * @param gridEnable
	 *            flag
	 */
	public void setGridEnabled(boolean gridEnable) {
		this.gridEnable_ = gridEnable;
	}

	@Override
	public Point validate(Point p) {
		if (isGridEnabled()) {
			int dx = p.x % GridStep;
			if (dx >= GridStep / 2.0) {
				dx = (-GridStep + dx);
			}
			int dy = p.y % GridStep;
			if (dy >= GridStep / 2.0) {
				dy = (-GridStep + dy);
			}
			return new Point(p.x - dx, p.y - dy);
		}
		return p;
	}

	@Override
	public Dimension validate(Dimension d) {
		if (isGridEnabled()) {
			int dx = d.width % GridStep;
			if (dx >= GridStep / 2.0) {
				dx = (-GridStep + dx);
			}
			int dy = d.height % GridStep;
			if (dy >= GridStep / 2.0) {
				dy = (-GridStep + dy);
			}
			return new Dimension(d.width - dx, d.height - dy);
		}
		return d;
	}

	/***
	 * get the editor mode
	 */
	public EditorMode getMode() {
		return mode_;
	}

	/**
	 * set current editor mode
	 */
	public void setMode(EditorMode mode) {
		if (this.mode_ != mode) {
			if (currentLayer_ != null) {
				setSelectedLayer(null);
				if (rightPanel_ != null)
					rightPanel_.getLayerManager().setCurrentLayer(null);
			}
			this.mode_ = mode;
			deselectAll();
			if (rightPanel_ != null)
				rightPanel_.setMode(mode);
			if (mode_ == EditorMode.NODE || mode_ == EditorMode.DEBUG) {
				for (OVLine l : lines_) {
					l.setVisible(true);
				}
			} else {
				for (OVLine l : lines_) {
					l.setVisible(false);
				}
			}
			for (OVComponent component : components_) {
				component.setMode(mode);
			}

			clearToolTips();
			repaint();
			requestFocus();
			if (rightPanel_ != null)
				rightPanel_.repaint();

		}
	}

	@Override
	public OVToolTip showToolTip(String tooltip, Point p,
			OrientationEnum orientation) {
		OVToolTip toolTip = new OVToolTip(tooltip, p, orientation, getFont());
		this.add(toolTip, toolTipLayer, 0);
		this.moveToFront(toolTip);
		return toolTip;
	}

	@Override
	public void hideToolTip(OVToolTip toolTip) {
		if (toolTip != null) {
			this.remove(toolTip);
			this.repaint(toolTip.getBounds());

		}
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
		return l;
	}

	@Override
	public void confirmLine(OVLine l) {
		for (OVComponent c : components_) {
			c.showNodes();
		}
		if (l == null) {
			return;
		}

		Point p = l.p2;

		if (l.a.getLocation().x + l.ca.getX() == p.x) {
			p = l.p1;
		}

		for (OVComponent c : components_) {
			if (c.isVisible() && c.getBounds().contains(p)) {
				OVNode n = c.getNode(SwingUtilities.convertPoint(this, p, c));
				if (n != null && n.compatible(l.a) && l.a.compatible(n)) {
					l.connect(n, c);
					l.setSelected(false);
					l.addKeyListener(this);
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

	@Override
	public void removeLine(OVLine line) {
		lines_.remove(line);
		this.remove(line);
		this.repaint(line.getBounds());
	}

	@Override
	public void keyPressed(KeyEvent arg0) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (!mode_.isExec()) {
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {

				removeSelected();
			} else if (e.getKeyCode() == KeyEvent.VK_C
					&& e.getModifiers() == KeyEvent.CTRL_MASK) {
				copy();
			} else if (e.getKeyCode() == KeyEvent.VK_V
					&& e.getModifiers() == KeyEvent.CTRL_MASK) {
				paste();
			} else if (e.getKeyCode() == KeyEvent.VK_Z
					&& e.getModifiers() == KeyEvent.CTRL_MASK) {
				// undo
			} else if (e.getKeyCode() == KeyEvent.VK_Y
					&& e.getModifiers() == KeyEvent.CTRL_MASK) {
				// redo
			}

		}
	}

	/***
	 * paste clip-board (if any)
	 */
	private void paste() {
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p, this);
		p = validate(p);
		for (Element e : clipboard_) {
			OVComponent c = XMLParser.parseElement(e, this);
			if (c != null) {
				c.setFather(this);
				c.resetUUID();
				c.setLocation(p.x, p.y);

				addComponent(c);
			}
		}
	}

	/***
	 * Copy selected elements in the clip-board
	 */
	private void copy() {
		ArrayList<Element> clipboard = new ArrayList<>();
		Document doc;
		try {
			doc = XMLBuilder.makeDoc();
			for (OVComponent c : components_) {
				if (c.isSelected()) {
					clipboard.add(c.getXML(doc));
				}
			}
			if (clipboard.size() > 0) {
				clipboard_ = clipboard;
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	/***
	 * Remove all selected elements
	 */
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
			c.getFather().removeComponent(c);
		}
		ArrayList<OVLine> ll = new ArrayList<>(lines_);
		for (OVLine l : ll) {
			if (l.isSelected()) {
				l.getFather().removeLine(l);
				l.deconnect();
				l.removeKeyListener(this);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void addNotify() {
		super.addNotify();
		requestFocus();
	}

	@Override
	public void showMenu(Point point) {
		if (!mode_.isExec()) {
			showMenu(point, (mode_ == EditorMode.GUI ? OVMakerMode.GUI
					: OVMakerMode.NODE));
		}
	}

	@Override
	public void showMenu(Point p, OVMakerMode mode) {
		if (!mode_.isExec()) {
			new OVMaker(p, mode, this);
		}
	}

	@Override
	public void showCustomMenu(Point p, JMenu m) {
		if (!mode_.isExec()) {
			new OVMaker(p, m, this);
		}
	}

	@Override
	public OVContainer parent() {
		return null;
	}

	@Override
	public OVContainer superParent() {
		return this;
	}

	@Override
	public boolean contains(OVComponent c) {
		return true;
	}

	@Override
	public void clickEvent(Point p, Object source) {
		for (OVLine l : lines_) {
			if (!l.equals(source) && l.getBounds().contains(p)) {
				l.click(new Point(p.x - l.getX(), p.y - l.getY()), source);
			}
		}

	}

	/***
	 * Remove all tool-tips
	 */
	private void clearToolTips() {
		Component[] cc = getComponents();
		for (Component c : cc) {
			if (c instanceof OVToolTip) {
				hideToolTip((OVToolTip) c);
			}
		}
	}

	@Override
	public boolean compatible(OVComponent c) {

		return true;
	}

	@Override
	public Element getXML(Document doc) {
		Element e = doc.createElement(EditorPanel.class.getSimpleName());
		for (OVComponent c : components_) {
			e.appendChild(c.getXML(doc));
		}
		for (OVLine l : lines_) {
			e.appendChild(l.getXML(doc));
		}
		for (NodeLayer l : nodeLayers_) {
			e.appendChild(l.getXML(doc));
		}
		return e;
	}

	/***
	 * Load XML element
	 * 
	 * @param el
	 *            XML element to load
	 */
	public void loadXML(Element el) {
		clearAll();
		NodeList nl = el.getElementsByTagName(NodeLayer.class.getSimpleName());
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n != null && n instanceof Element) {
				Element e = (Element) n;
				if (e.getParentNode().equals(el)) {
					NodeLayer l = new NodeLayer(e);
					if (rightPanel_ != null)
						rightPanel_.getLayerManager().addLayer(l);
					nodeLayers_.add(l);
				}
			}
		}

		nl = el.getChildNodes();
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
						this.add(l, linesLayer);
						this.moveToBack(l);
						l.setSelected(false);
						l.addKeyListener(this);
						if (mode_ == EditorMode.RUN || mode_ == EditorMode.GUI) {
							l.setVisible(false);
						}
					}
				}
			}
		}

		if (objectTree_ != null) {
			objectTree_.refresh();
		}

	}

	/***
	 * remove all components and lines
	 */
	public void clearAll() {
		ArrayList<OVLine> ls = new ArrayList<>(lines_);
		for (OVLine l : ls) {
			l.delete();
			remove(l);
		}
		ArrayList<OVComponent> cs = new ArrayList<>(components_);
		for (OVComponent c : cs) {
			c.delete();
			remove(c);
		}
		ls.clear();
		cs.clear();
		components_.clear();
		lines_.clear();
		removeAll();
		repaint();
	}

	@Override
	public OVNode getNode(String parent, String uuid) {
		for (OVComponent c : components_) {
			if (c.getUUID().toString().equals(parent)) {
				return c.getNode(uuid);
			}
		}
		return null;
	}

	@Override
	public ObjectTree getObjectTree() {
		return objectTree_;
	}

	@Override
	public DragAction drag(Point point) {
		return DragAction.NOTHING;
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {

		super.setBounds(x, y, width, height);
		if (this.getSetting(ComponentSettings.SizeW) != null && !__lock) {
			__lock = true;
			try {
				if (width != getSetting(ComponentSettings.SizeW).getValue()
						.getInt()) {
					this.getSetting(ComponentSettings.SizeW).setValue(width);
				}
				if (height != getSetting(ComponentSettings.SizeH).getValue()
						.getInt()) {
					this.getSetting(ComponentSettings.SizeH).setValue(height);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			__lock = false;
		}
	}

	/***
	 * set current node layer
	 * 
	 * @param n
	 *            node layer
	 */
	public void setSelectedLayer(NodeLayer n) {
		if (n != null && !nodeLayers_.contains(n)) {
			nodeLayers_.add(n);
		}
		if (mode_ == EditorMode.DEBUG || mode_ == EditorMode.NODE) {
			currentLayer_ = n;
			for (OVComponent c : components_) {
				c.setNodeLayer(n);
			}
			if (rightPanel_ != null)
				rightPanel_.repaint();
		}
	}

	/***
	 * remove a node layer
	 * 
	 * @param n
	 *            node layer
	 */
	public void removeLayer(NodeLayer n) {
		if (n != null) {
			nodeLayers_.remove(n);
		}
		if (mode_ == EditorMode.DEBUG || mode_ == EditorMode.NODE) {
			currentLayer_ = null;
			for (OVComponent c : components_) {
				c.removeNodeLayer(n);
			}
		}
	}

	/***
	 * Copy layers to a component
	 * 
	 * @param c
	 *            component
	 */
	private void initLayers(OVComponent c) {
		for (NodeLayer l : nodeLayers_) {
			if (!l.equals(currentLayer_)) {
				c.setNodeLayer(l);
			}
		}
		c.setNodeLayer(currentLayer_);
	}

	@Override
	public void leftClick(Point point) {
		this.select(this);
	}
}
