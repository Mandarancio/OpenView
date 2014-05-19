package run.init;

import gui.components.ovgui.OVButton;
import gui.components.ovgui.OVCheckBox;
import gui.components.ovgui.OVGauge;
import gui.components.ovgui.OVLabel;
import gui.components.ovgui.OVPlotComponent;
import gui.components.ovgui.OVProgressBar;
import gui.components.ovgui.OVSpinner;
import gui.components.ovgui.OVSwitcher;
import gui.components.ovgui.OVTextArea;
import gui.components.ovgui.OVTextField;
import gui.components.ovgui.plot.OVPlot;
import gui.components.ovnode.OVCSVFile;
import gui.components.ovnode.OVComment;
import gui.components.ovnode.OVForTrigger;
import gui.components.ovnode.OVFunctionNode;
import gui.components.ovnode.OVIFTriggerNode;
import gui.components.ovnode.OVNodeBlock;
import gui.components.ovnode.OVOperatorNode;
import gui.components.ovnode.OVPullNode;
import gui.components.ovnode.OVRandomNode;
import gui.components.ovnode.OVTextFile;
import gui.components.ovnode.OVTimerTriggerNode;
import gui.components.ovnode.OVVariableNode;
import gui.components.ovprocedural.OVProceduralBlock;
import gui.interfaces.OVContainer;

import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import core.maker.OVClassFactory;
import core.maker.OVClassManager;
import core.maker.OVMenuManager;
import core.module.BaseModule;
import core.support.ClassKey;

public class Init {

	public static void init() {
		Init.initClasses();
		Init.initMenus();
		SettingsUtils.load();
		Init.initModules();
	}

	public static void initClasses() {
		OVClassManager m = OVClassFactory.getManager();
		m.addClass(ClassKey.Button, OVButton.class);
		m.addClass(ClassKey.Check, OVCheckBox.class);
		m.addClass(ClassKey.Progress, OVProgressBar.class);
		m.addClass(ClassKey.Label, OVLabel.class);
		m.addClass(ClassKey.Gauge, OVGauge.class);
		m.addClass(ClassKey.TextArea, OVTextArea.class);
		m.addClass(ClassKey.TextField, OVTextField.class);
		m.addClass(ClassKey.Switch, OVSwitcher.class);
		m.addClass(ClassKey.Spinner, OVSpinner.class);

		m.addClass(ClassKey.Plot, OVPlotComponent.class);
		m.addClass(ClassKey.PlotL, OVPlot.class);

		m.addClass(ClassKey.Container, OVContainer.class);

		m.addClass(ClassKey.Variable, OVVariableNode.class);
		m.addClass(ClassKey.Operator, OVOperatorNode.class);
		m.addClass(ClassKey.Function, OVFunctionNode.class);
		m.addClass(ClassKey.For, OVForTrigger.class);
		m.addClass(ClassKey.Timer, OVTimerTriggerNode.class);
		m.addClass(ClassKey.IFTrigger, OVIFTriggerNode.class);
		m.addClass(ClassKey.Comment, OVComment.class);
		m.addClass(ClassKey.Random, OVRandomNode.class);
		m.addClass(ClassKey.Pull, OVPullNode.class);
		m.addClass(ClassKey.TextFile, OVTextFile.class);
		m.addClass(ClassKey.CSVFile, OVCSVFile.class);
		m.addClass(ClassKey.ProceduralBlock, OVProceduralBlock.class);

		m.addClass(ClassKey.NodeBlock, OVNodeBlock.class);
	}

	public static void initMenus() {
		OVMenuManager.addGUIMenu(initGUI());
		OVMenuManager.addNodeMenu(initNode());
	}

	private static JMenu initNode() {
		JMenu menu = new JMenu("Basic Node");

		JMenuItem i = new JMenuItem(ClassKey.Variable);
		i.setActionCommand(i.getText());
		menu.add(i);
		JMenu submenu = new JMenu("File");

		i = new JMenuItem(ClassKey.TextFile);
		i.setActionCommand(i.getText());
		submenu.add(i);

		i = new JMenuItem(ClassKey.CSVFile);
		i.setActionCommand(i.getText());
		submenu.add(i);

		menu.add(submenu);

		submenu = new JMenu("Triggers");

		i = new JMenuItem(ClassKey.IFTrigger);
		i.setActionCommand(i.getText());
		submenu.add(i);

		i = new JMenuItem(ClassKey.For);
		i.setActionCommand(i.getText());
		submenu.add(i);

		i = new JMenuItem(ClassKey.Timer);
		i.setActionCommand(i.getText());
		submenu.add(i);

		menu.add(submenu);

		i = new JMenuItem(ClassKey.Pull);
		i.setActionCommand(i.getText());
		menu.add(i);

		i = new JMenuItem(ClassKey.Operator);
		i.setActionCommand(i.getText());
		menu.add(i);

		i = new JMenuItem(ClassKey.Function);
		i.setActionCommand(i.getText());
		menu.add(i);

		i = new JMenuItem(ClassKey.Random);
		i.setActionCommand(i.getText());
		menu.add(i);

		i = new JMenuItem(ClassKey.NodeBlock);
		i.setActionCommand(i.getText());
		menu.add(i);
		//
		i = new JMenuItem(ClassKey.ProceduralBlock);
		i.setActionCommand(i.getText());
		menu.add(i);

		i = new JMenuItem(ClassKey.Comment);
		i.setActionCommand(i.getText());
		menu.add(i);

		i = new JMenuItem(ClassKey.Arduino);
		i.setActionCommand(i.getText());
		menu.add(i);

		//
		return (menu);
	}

	private static JMenu initGUI() {
		JMenu menu = new JMenu("Basic GUI");

		JMenuItem i = new JMenuItem(ClassKey.Label);
		i.setActionCommand(i.getText());
		menu.add(i);

		i = new JMenuItem(ClassKey.Button);
		i.setActionCommand(i.getText());
		menu.add(i);

		i = new JMenuItem(ClassKey.TextField);
		i.setActionCommand(i.getText());
		menu.add(i);

		i = new JMenuItem(ClassKey.TextArea);
		i.setActionCommand(i.getText());
		menu.add(i);

		i = new JMenuItem(ClassKey.Check);
		i.setActionCommand(i.getText());
		menu.add(i);

		i = new JMenuItem(ClassKey.Spinner);
		i.setActionCommand(i.getText());
		menu.add(i);

		i = new JMenuItem(ClassKey.Switch);
		i.setActionCommand(i.getText());
		menu.add(i);

		i = new JMenuItem(ClassKey.Progress);
		i.setActionCommand(i.getText());
		menu.add(i);

		i = new JMenuItem(ClassKey.Container);
		i.setActionCommand(i.getText());
		menu.add(i);

		i = new JMenuItem(ClassKey.Plot);
		i.setActionCommand(i.getText());
		menu.add(i);

		i = new JMenuItem(ClassKey.Gauge);
		i.setActionCommand(i.getText());
		menu.add(i);

		return menu;
	}

	public static void initModules() {
		Settings setting = SettingsUtils.getSettings();
		for (File f : ModuleUtil.getModuleList()) {
			BaseModule m = ModuleUtil.loadModule(f);
			if (setting.hasModule(m.getModuleName())) {
				if (setting.isEnable(m.getModuleName())) {
					ModuleUtil.importModule(m);
				}
			} else {
				ModuleUtil.importModule(m);
				setting.addModule(m.getModuleName(), true);
			}
		}

	}
}
