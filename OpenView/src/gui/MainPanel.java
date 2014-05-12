package gui;

import gui.enums.EditorMode;
import gui.interfaces.OVContainer;
import gui.support.XMLBuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import org.w3c.dom.Document;

public class MainPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8438576029794021570L;
	private RightPanel rightPanel_;
	private EditorPanel editor_;

	public MainPanel() {

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setPreferredSize(new Dimension(30, 30));
		toolBar.add(new JLabel("Mode    "));
		final JComboBox<EditorMode> modeBox = new JComboBox<EditorMode>();
		modeBox.addItem(EditorMode.GUI);
		modeBox.addItem(EditorMode.NODE);
		toolBar.add(modeBox);
		toolBar.addSeparator();

		final JToggleButton debugButton = new JToggleButton("  Debug  ");
		final JToggleButton runButton = (new JToggleButton("  Run  "));
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				editor_.setMode((editor_.getMode() == EditorMode.RUN ? (EditorMode) modeBox
						.getSelectedItem() : EditorMode.RUN));
				rightPanel_.setVisible(editor_.getMode() == EditorMode.RUN ? false
						: true);
				modeBox.setEnabled(editor_.getMode() != EditorMode.RUN);
				debugButton.setEnabled(modeBox.isEnabled());
				revalidate();

			}
		});

		toolBar.add(runButton);
		toolBar.add(debugButton);

		toolBar.addSeparator(new Dimension(30, 10));
		JButton saveButton = new JButton(UIManager.getIcon("Tree.saveIcon"));
		saveButton.setText("Save");

		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Create a file chooser
				final JFileChooser fc = new JFileChooser();

				// In response to a button click:
				int returnVal = fc.showSaveDialog(MainPanel.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						File f = fc.getSelectedFile();
						Document doc = XMLBuilder.makeDoc();
						doc.appendChild(editor_.getXML(doc));
						XMLBuilder.saveDoc(doc, f);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(MainPanel.this,
								e.getMessage());
						e.printStackTrace();
					}
				}

			}
		});

		toolBar.add(saveButton);

		JButton openButton = new JButton(UIManager.getIcon("Tree.openIcon"));
		openButton.setText("Open");
		openButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Create a file chooser
				final JFileChooser fc = new JFileChooser();

				// In response to a button click:
				int returnVal = fc.showOpenDialog(MainPanel.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						File f = fc.getSelectedFile();
						Document doc = XMLBuilder.loadDoc(f);
						editor_.loadXML(doc.getDocumentElement());
					} catch (Exception e) {
						JOptionPane.showMessageDialog(MainPanel.this,
								e.getMessage());
						e.printStackTrace();
					}
				}
			}
		});
		toolBar.add(openButton);

		debugButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				editor_.setMode((editor_.getMode() == EditorMode.DEBUG ? (EditorMode) modeBox
						.getSelectedItem() : EditorMode.DEBUG));
				modeBox.setEnabled(editor_.getMode() != EditorMode.DEBUG);
				runButton.setEnabled(modeBox.isEnabled());
				revalidate();

			}
		});

		modeBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				editor_.setMode((EditorMode) modeBox.getSelectedItem());
			}
		});

		this.setLayout(new BorderLayout());
		rightPanel_ = new RightPanel();
		editor_ = new EditorPanel(rightPanel_);
		editor_.setGridVisible(true);
		editor_.setGridEnabled(true);

		rightPanel_.setSize(new Dimension(300, 100));

		rightPanel_.setPreferredSize(new Dimension(300, 100));
		this.add(toolBar, BorderLayout.NORTH);
		this.add(editor_, BorderLayout.CENTER);
		this.add(rightPanel_, BorderLayout.LINE_END);
	}

	public OVContainer getEditor() {
		return editor_;
	}

}
