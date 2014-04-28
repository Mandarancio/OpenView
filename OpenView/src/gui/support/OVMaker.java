package gui.support;

import gui.components.OVComponent;
import gui.components.OVComponentContainer;
import gui.components.ovgui.OVButton;
import gui.components.ovgui.OVCheckBox;
import gui.components.ovgui.OVLabel;
import gui.components.ovgui.OVOperatorComponent;
import gui.components.ovgui.OVTextArea;
import gui.components.ovgui.OVTextField;
import gui.components.ovnode.OVIFTriggerNode;
import gui.components.ovnode.OVNodeBlock;
import gui.components.ovnode.OVPullNode;
import gui.components.ovnode.OVTimerTriggerNode;
import gui.components.ovnode.OVVariableNode;
import gui.components.ovprocedural.OVPAssign;
import gui.components.ovprocedural.OVPConst;
import gui.components.ovprocedural.OVPVar;
import gui.components.ovprocedural.OVProceduralBlock;
import gui.interfaces.OVContainer;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class OVMaker extends JPopupMenu implements ActionListener {

	public enum OVMakerMode {

		GUI, NODE, PROCEDURAL, NODEONLY
	}

	/**
     *
     */
	private static final long serialVersionUID = -6272300889162031511L;
	private static final String Timer = "Timer", Pull = "Pull",
			Variable = "Variable";
	private static final String Label = "Label", Button = "Button",
			TextArea = "Text Area", TextField = "Text Field",
			Container = "Container", Operator = "Operator",
			NodeBlock = "Block", IFTrigger = "IF trigger";
	private static final String ProceduralBlock = "Procedural block",
			Var = "Var",Const="Const",Assign="Assign";
	private static final String Check="Check box";
	private OVContainer father_;
	private Point point_;

	public OVMaker(Point p, OVMakerMode mode, OVContainer father) {
		father_ = father;
		point_ = p;
		this.initMenu(mode);
		this.show((JComponent) father, p.x, p.y);
	}

	private void initMenu(OVMakerMode mode) {
		if (mode == OVMakerMode.GUI) {
			initGUI();
		} else if (mode == OVMakerMode.NODE) {
			initGUI();
			initNode();
		} else if (mode == OVMakerMode.NODEONLY) {
			initNode();
		} else if (mode == OVMakerMode.PROCEDURAL) {
			initProcedural();
		}
	}

	private void initNode() {
		JMenu menu = new JMenu("Basic Node");

		JMenuItem i = new JMenuItem(Variable);
		i.setActionCommand(i.getText());
		i.addActionListener(this);
		menu.add(i);
		i = new JMenuItem(Timer);
		i.setActionCommand(i.getText());
		i.addActionListener(this);
		menu.add(i);
		i = new JMenuItem(Pull);
		i.setActionCommand(i.getText());
		i.addActionListener(this);
		menu.add(i);
		i = new JMenuItem(Operator);
		i.setActionCommand(i.getText());
		i.addActionListener(this);
		menu.add(i);
		i = new JMenuItem(IFTrigger);
		i.setActionCommand(i.getText());
		i.addActionListener(this);
		menu.add(i);
		i = new JMenuItem(NodeBlock);
		i.setActionCommand(i.getText());
		i.addActionListener(this);
		menu.add(i);
		i = new JMenuItem(ProceduralBlock);
		i.setActionCommand(i.getText());
		i.addActionListener(this);
		menu.add(i);
		add(menu);
	}

	private void initGUI() {
		JMenu menu = new JMenu("Basic GUI");
		JMenuItem i = new JMenuItem(Label);
		i.setActionCommand(i.getText());
		i.addActionListener(this);
		menu.add(i);
		i = new JMenuItem(Button);
		i.setActionCommand(i.getText());
		i.addActionListener(this);
		menu.add(i);
		i = new JMenuItem(TextField);
		i.setActionCommand(i.getText());
		i.addActionListener(this);
		menu.add(i);
		i = new JMenuItem(TextArea);
		i.setActionCommand(i.getText());
		i.addActionListener(this);
		menu.add(i);
		i = new JMenuItem(Check);
		i.setActionCommand(i.getText());
		i.addActionListener(this);
		menu.add(i);
		i = new JMenuItem(Container);
		i.setActionCommand(i.getText());
		i.addActionListener(this);
		menu.add(i);
		add(menu);

	}

	private void initProcedural() {
		JMenu menu = new JMenu("Procedural");
		JMenuItem i = new JMenuItem(Var);
		i.setActionCommand(i.getText());
		i.addActionListener(this);
		menu.add(i);
		i = new JMenuItem(Const);
		i.setActionCommand(i.getText());
		i.addActionListener(this);
		menu.add(i);
		i = new JMenuItem(Assign);
		i.setActionCommand(i.getText());
		i.addActionListener(this);
		menu.add(i);
		add(menu);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(Button)) {
			create(new OVButton(father_));
		} else if (cmd.equals(Label)) {
			create(new OVLabel(father_));
		} else if (cmd.equals(TextField)) {
			create(new OVTextField(father_));
		} else if (cmd.equals(Variable)) {
			create(new OVVariableNode(father_));
		} else if (cmd.equals(Timer)) {
			create(new OVTimerTriggerNode(father_));
		} else if (cmd.equals(Pull)) {
			create(new OVPullNode(father_));
		} else if (cmd.equals(Container)) {
			create(new OVComponentContainer(father_));
		} else if (cmd.equals(Operator)) {
			create(new OVOperatorComponent(father_));
		} else if (cmd.equals(TextArea)) {
			create(new OVTextArea(father_));
		} else if (cmd.equals(NodeBlock)) {
			create(new OVNodeBlock(father_));
		} else if (cmd.equals(IFTrigger)) {
			create(new OVIFTriggerNode(father_));
		} else if (cmd.equals(ProceduralBlock)) {
			create(new OVProceduralBlock(father_));
		} else if (cmd.equals(Var)) {
			create(new OVPVar(father_));
		}else if (cmd.equals(Const)){
			create(new OVPConst(father_));
		}else if (cmd.equals(Assign)){
			create(new OVPAssign(father_));
		}else if (cmd.equals(Check)){
			create(new OVCheckBox(father_));
		}
	}

	private void create(OVComponent c) {
		Point p = father_.validate(point_);
		c.moveTo(p.x, p.y);
		father_.addComponent(c);

		this.setVisible(false);
		father_ = null;
	}

}
