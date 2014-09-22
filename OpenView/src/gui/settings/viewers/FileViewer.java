package gui.settings.viewers;

import gui.support.Setting;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import core.ValueType;
import core.support.Rule;

public class FileViewer extends Viewer implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2136954296240311357L;

	public FileViewer(Setting s) {
		super(s);
		JButton b = new JButton("Open");
		b.addActionListener(this);
		this.addMainComponent(b);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Create a file chooser
		JFileChooser fc = new JFileChooser();

		// In response to a button click:
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				File f = fc.getSelectedFile();
				setting_.setValue(f);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	@Override
	public Viewer copy(Setting s) throws Exception {
		return new FileViewer(s);
	}

	public static Rule rule() {
		return new Rule() {

			@Override
			public boolean check(Object... args) {
				if (args.length == 1 && args[0] instanceof Setting) {
					Setting s = (Setting) args[0];
					return s.getType() == ValueType.FILE;
				}
				return false;
			}
			@Override
			public int orderValue() {
				return 4;
			}
		};
	}
	
}
