package gui.components.ovnode;

import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.constants.ComponentSettings;
import gui.enums.EditorMode;
import gui.interfaces.OVContainer;
import gui.support.Setting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.w3c.dom.Element;

import core.SlotInterface;
import core.SlotListener;
import core.Value;
import core.ValueDescriptor;
import core.ValueType;

public class OVTextFile extends OVNodeComponent implements SlotListener {

	public enum TextFileMode {

		LINE, ALL
	}

	/**
     *
     */
	private static final long serialVersionUID = -3461901041165688465L;
	private static final String Input = "Input";
	private static final String Trigger = "Trigger";
	private static final String File = "File";
	private static final String Output = "Output";
	private boolean status_ = false;
	private java.io.File file_;
	private BufferedWriter writer_;
	private BufferedReader reader_;
	private OutNode output_;
	private TextFileMode mode_ = TextFileMode.ALL;

	public OVTextFile(OVContainer father) {
		super(father);
		Setting s = new Setting(File, new ValueDescriptor(ValueType.FILE), this);
		this.addBothSetting(ComponentSettings.SpecificCategory, s);

		InNode input = addInput(Input, ValueType.STRING);
		input.addListener(this);
		InNode trigger = addInput(Trigger, ValueType.VOID);
		trigger.addListener(this);

		output_ = addOutput(Output, ValueType.STRING);

		getSetting(ComponentSettings.Name).setValue("Text");

	}

	public OVTextFile(Element e, OVContainer father) {
		super(e, father);
		for (InNode n : inputs_) {
			if (n.getLabel().equals(Trigger)) {
				n.addListener(this);
			} else if (n.getLabel().equals(Input)) {
				n.addListener(this);
			}
		}

		for (OutNode n : outputs_) {
			if (n.getLabel().equals(Output)) {
				output_ = n;
			}
		}
	}

	@Override
	public void setMode(EditorMode mode_) {
		if (mode_ != getMode()) {
			if (mode_.isExec()) {
				openFile();
			} else if (status_) {
				closeFile();
			}
		}
		super.setMode(mode_);
	}

	private void closeFile() {
		if (status_) {
			try {
				if (writer_ != null) {
					writer_.close();
					writer_ = null;
				}
				if (reader_ != null) {
					reader_.close();
					reader_ = null;
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
			status_ = false;

		}

	}

	private void openFile() {
		try {
			file_ = getNodeSetting(File).getValue().getFile();
			if (file_ != null && file_.exists()) {
				status_ = true;
			} else {
				status_ = false;
			}

		} catch (Exception e) {
			status_ = false;
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	@Override
	public void valueRecived(SlotInterface s, Value v) {
		if (!status_) {
			openFile();
		}
		if (status_) {
			if (s.getLabel().equals(Trigger)) {
				try {
					if (mode_ == TextFileMode.LINE) {
						if (reader_ == null) {
							if (writer_ != null) {
								writer_.close();
								writer_ = null;
							}
							reader_ = new BufferedReader(new FileReader(file_));
						}
						String line = reader_.readLine();
						if (line != null) {
							output_.trigger(new Value(line, ValueType.STRING));
						}
					} else {
						if (reader_ == null) {
							if (writer_ != null) {
								writer_.close();
								writer_ = null;
							}
							reader_ = new BufferedReader(new FileReader(file_));
						}
						String line = reader_.readLine();
						String text = "";
						while (line != null) {
							text += line + "\n";
							line = reader_.readLine();
						}
						reader_.close();
						reader_ = null;
						output_.trigger(new Value(text, ValueType.STRING));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (s.getLabel().equals(Input)) {
				try {
					if (writer_ == null) {
						if (reader_ != null) {
							reader_.close();
							reader_ = null;
						}
						writer_ = new BufferedWriter(new FileWriter(file_));
					}
					writer_.write(v.getString() + "\n");
					writer_.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
