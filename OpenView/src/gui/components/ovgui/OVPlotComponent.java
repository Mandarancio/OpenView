package gui.components.ovgui;

import gui.components.OVComponent;
import gui.components.OVComponentContainer;
import gui.components.nodes.InNode;
import gui.components.nodes.Line;
import gui.components.ovnode.OVNodeBlock;
import gui.components.ovnode.OVNodeComponent;
import gui.constants.ComponentSettings;
import gui.enums.EditorMode;
import gui.interfaces.NodeListener;
import gui.interfaces.OVContainer;
import gui.interfaces.OVNode;
import gui.support.OVMaker;

import java.awt.BorderLayout;
import java.awt.Point;
import java.util.ArrayList;

import org.math.plot.Plot2DPanel;
import org.math.plot.plots.Plot;
import org.w3c.dom.Element;

import core.SlotInterface;
import core.SlotListener;
import core.Value;
import core.ValueType;

public class OVPlotComponent extends OVComponentContainer implements SlotListener,
		NodeListener {

	/**
     *
     */
	private static final long serialVersionUID = 1L;
	private static final String Clean = "clean";
	private Plot2DPanel plot_;

	private ArrayList<InNode> plotLines_ = new ArrayList<>();

	public OVPlotComponent(OVContainer father) {
		super(father);
		this.setLayout(new BorderLayout());
		getSetting(ComponentSettings.SizeW).setValue(300);
		getSetting(ComponentSettings.SizeH).setValue(300);
		getSetting(ComponentSettings.Name).setValue("Plot");

		initPlot();

		InNode input = addInput(Clean, ValueType.NONE);
		input.addListener(this);

		input = addInput("plot 0", ValueType.NONE);
		input.addListener(this);
		input.addNodeListener(this);
		plotLines_.add(input);
	}

	public OVPlotComponent(Element e, OVContainer father) {
		super(e, father);
		this.setLayout(new BorderLayout());

		initPlot();

		for (InNode i : inputs_) {
			if (i.getLabel().equals(Clean)) {
				i.addListener(this);
			} else if (i.getLabel().startsWith("plot ")) {
				i.addListener(this);
				i.addNodeListener(this);
				plotLines_.add(i);
			}
		}
	}

	private void initPlot() {
		plot_ = new Plot2DPanel();
		this.add(plot_, BorderLayout.CENTER);
	}

	private void addData(Value v, int plot) throws Exception {
		if (v.getType().isNumeric()) {
			if (plot_.getPlots().size() > plot) {
				Plot p = plot_.getPlot(plot);

				if (p != null) {
					double data[][] = p.getData();
					double ndata[][] = new double[data.length + 1][2];
					double val = v.getDouble();
					for (int i = 0; i < data.length; i++) {
						ndata[i][1] = data[i][1];
						ndata[i][0] = i;
					}
					ndata[data.length][0] = data.length;
					ndata[data.length][1] = val;
					p.setData(ndata);
				}
			} else {
				double y[] = new double[1];
				y[0] = v.getDouble();
				plot_.addLinePlot("plot " + plot, y);
			}
		} else if (v.getType().isArray()) {
			if (v.getArray().size() == 2) {
				if (plot_.getPlots().size() > plot) {
					Plot p = plot_.getPlot(plot);

					if (p != null) {
						double data[][] = p.getData();
						double ndata[][] = new double[data.length + 1][2];
						for (int i = 0; i < data.length; i++) {
							ndata[i][1] = data[i][1];
							ndata[i][0] = data[i][0];
						}
						ndata[data.length][0] = v.getArray().get(0).getDouble();
						ndata[data.length][1] = v.getArray().get(1).getDouble();
						p.setData(ndata);
					}
				} else {
					double xy[][] = new double[1][2];
					xy[0][0] = v.getArray().get(0).getDouble();
					xy[0][1] = v.getArray().get(1).getDouble();
					plot_.addLinePlot("plot " + plot, xy);
				}
			} else {
				double data[][];

				data = new double[v.getArray().size()][2];
				Value vls[] = v.getValues();
				for (int i = 0; i < vls.length; i++) {
					if (vls[i].getType().isArray()) {
						data[i][0] = vls[i].getArray().get(0).getDouble();
						data[i][1] = vls[i].getArray().get(1).getDouble();
					} else {
						data[i][1] = vls[i].getDouble();
						data[i][0] = i;
					}
				}
				if (plot_.getPlots().size() > plot
						&& plot_.getPlots().size() > 0) {

					Plot p = plot_.getPlot(plot);

					if (p != null) {
						p.setData(data);
					}
				} else {
					plot_.addLinePlot("plot " + plot, data);
				}
			}
		}
		plot_.resetMapData();
	}

	@Override
	public void valueRecived(SlotInterface s, Value v) {
		if (getMode().isExec()) {
			if (plotLines_.contains(s)) {
				try {
					int i = plotLines_.indexOf(s);
					addData(v, i);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (s.getLabel().equals(Clean)) {
				plot_.removeAllPlots();
			}
		}
	}

	@Override
	public void setMode(EditorMode mode) {
		
		if (mode != getMode()) {
			if (mode==EditorMode.GUI || mode.isExec()){
				hideAll();
				this.setLayout(new BorderLayout());
				plot_.setBounds(5, 5, getWidth()-20, getHeight()-20);
				this.add(plot_, BorderLayout.CENTER);
				if (getMode().isExec()) {
					plot_.removeAllPlots();
				}
			}else {
				this.remove(plot_);
				this.setLayout(null);
				showAll();
			}
			super.setMode(mode);
		}
	}

	private void showAll() {
		for (Line l:  lines_){
			this.add(l);
		}
		for (OVComponent c: components_){
			this.add(c);
		}
	}

	private void hideAll() {
		for (Line l:  lines_){
			this.remove(l);
		}
		for (OVComponent c: components_){
			this.remove(c);
		}
	}
	
	

	private void updateDynNodes() {
		ArrayList<InNode> free = new ArrayList<>();
		for (InNode in : plotLines_) {
			if (in.isFree()) {
				free.add(in);
			}
		}

		if (free.size() == 0) {
			InNode n = addInput("plot " + plotLines_.size(), ValueType.NONE);
			n.addListener(this);
			n.addNodeListener(this);
			plotLines_.add(n);
		} else if (free.size() > 1) {
			for (int i = 1; i < free.size(); i++) {
				removeInput(free.get(i));
				plotLines_.remove(free.get(i));
			}
		}
		free.clear();
	}

	@Override
	public void connected(OVNode n) {
		updateDynNodes();
	}

	@Override
	public void deconneced(OVNode n) {
		updateDynNodes();
	}
	

    @Override
    public void showMenu(Point point) {
    	if (getMode()==EditorMode.NODE)
    		super.showMenu(point, OVMaker.OVMakerMode.NODEONLY); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean compatible(OVComponent c) {
        if (c instanceof OVNodeBlock || c instanceof OVNodeComponent) {
            return true;
        }
        return false;
    }
}
