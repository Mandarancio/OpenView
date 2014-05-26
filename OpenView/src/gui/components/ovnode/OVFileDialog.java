package gui.components.ovnode;

import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.constants.ComponentSettings;
import gui.interfaces.OVContainer;
import gui.support.Setting;

import java.io.File;

import javax.swing.JFileChooser;

import org.w3c.dom.Element;

import core.SlotInterface;
import core.SlotListener;
import core.Value;
import core.ValueType;

public class OVFileDialog extends OVNodeComponent implements SlotListener {

	public enum FileDialogMode {
		OPEN, SAVE
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6668437211870321890L;
	private static final String _trigger = "Trigger";
	private static final String _output = "File";
	private static final String _mode = "Mode";

	private OutNode output_;

	public OVFileDialog(OVContainer father) {
		super(father);
		InNode n = addInput(_trigger, ValueType.VOID);
		n.addListener(this);
		output_ = addOutput(_output, ValueType.FILE);
		Setting s = new Setting(_mode, FileDialogMode.OPEN, this);
		addBothSetting(ComponentSettings.SpecificCategory, s);
		getSetting(ComponentSettings.Name).setValue("F.Dialog");
	}

	public OVFileDialog(Element e, OVContainer father) {
		super(e, father);
		for (InNode i : inputs_) {
			if (i.getLabel().equals(_trigger)) {
				i.addListener(this);
			}
		}
		for (OutNode o : outputs_) {
			if (o.getLabel().equals(_output)) {
				output_ = o;
			}
		}
	}

	@Override
	public void valueRecived(SlotInterface s, Value v) {
		if (s.getLabel().equals(_trigger)) {
			try {
				FileDialogMode mode = (FileDialogMode) getNodeSetting(_mode)
						.getValue().getEnum();
				JFileChooser fc = new JFileChooser();

				// In response to a button click:
				int returnVal = JFileChooser.CANCEL_OPTION;
				if (mode == FileDialogMode.OPEN) {
					returnVal = fc.showOpenDialog(OVFileDialog.this);
				} else {
					returnVal = fc.showSaveDialog(OVFileDialog.this);
				}
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					if (f != null) {
						output_.trigger(new Value(f));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
