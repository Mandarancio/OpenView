package run.window;

import gui.enums.EditorMode;

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

import org.w3c.dom.Document;

import run.window.support.XMLBuilder;
import ui.icons.IconsLibrary;

/***
 * Main panel of OpenView, it contains everything
 * 
 * @author martino
 * 
 */
public class MainPanel extends JPanel {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -8438576029794021570L;
	/***
	 * Right setting panel
	 */
	private RightPanel rightPanel_;
	/***
	 * Editor panel
	 */
	private EditorPanel editor_;

	/***
	 * Initialize all the objects and GUI components
	 */
	public MainPanel() {
		this.setLayout(new BorderLayout());

		initTools();
		editor_ = new EditorPanel(rightPanel_);
		editor_.setGridVisible(true);
		editor_.setGridEnabled(true);

		this.add(editor_, BorderLayout.CENTER);
	}

	public MainPanel(boolean runMode) {
		this.setLayout(new BorderLayout());

		if (!runMode) {
			initTools();
			editor_ = new EditorPanel(rightPanel_);
			editor_.setGridVisible(true);
			editor_.setGridEnabled(true);

			this.add(editor_, BorderLayout.CENTER);
		} else {
			editor_ = new EditorPanel(null);
			editor_.setGridVisible(false);
			editor_.setGridEnabled(false);
			this.add(editor_, BorderLayout.CENTER);
		}
	}

	/***
	 * Direct access to the Editor panel
	 * 
	 * @return Editor panel
	 */
	public EditorPanel getEditor() {
		return editor_;
	}

	private void initTools() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setPreferredSize(new Dimension(30, 28));
		toolBar.add(new JLabel("Mode    "));
		final JComboBox<EditorMode> modeBox = new JComboBox<EditorMode>();
		modeBox.addItem(EditorMode.GUI);
		modeBox.addItem(EditorMode.NODE);
		toolBar.add(modeBox);
		toolBar.addSeparator();

		final JToggleButton debugButton = new JToggleButton(
				IconsLibrary.getIcon(IconsLibrary.RunDebug, 22));
		debugButton.setToolTipText("Debug");
		final JToggleButton runButton = new JToggleButton(IconsLibrary.getIcon(
				IconsLibrary.Run, 22));
		runButton.setToolTipText("Run");
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				editor_.setMode((editor_.getMode() == EditorMode.RUN ? (EditorMode) modeBox
						.getSelectedItem() : EditorMode.RUN));
				rightPanel_.setVisible(editor_.getMode() == EditorMode.RUN ? false
						: true);

				modeBox.setEnabled(editor_.getMode() != EditorMode.RUN);

				debugButton.setEnabled(modeBox.isEnabled());
				if (modeBox.isEnabled()) {
					runButton.setIcon(IconsLibrary
							.getIcon(IconsLibrary.Run, 22));
				} else {
					runButton.setIcon(IconsLibrary.getIcon(IconsLibrary.Stop,
							22));
				}
				revalidate();

			}
		});

		toolBar.add(runButton);
		toolBar.add(debugButton);

		toolBar.addSeparator(new Dimension(30, 10));
		JButton saveButton = new JButton(IconsLibrary.getIcon(
				IconsLibrary.FileSave, 22));
		saveButton.setToolTipText("Save");

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

		JButton openButton = new JButton(IconsLibrary.getIcon(
				IconsLibrary.FileOpen, 22));
		openButton.setToolTipText("Open");
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
				if (modeBox.isEnabled()) {
					debugButton.setIcon(IconsLibrary.getIcon(
							IconsLibrary.RunDebug, 22));
				} else {
					debugButton.setIcon(IconsLibrary.getIcon(IconsLibrary.Stop,
							22));
				}
			}
		});

		modeBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				editor_.setMode((EditorMode) modeBox.getSelectedItem());
			}
		});
		this.add(toolBar, BorderLayout.NORTH);

		rightPanel_ = new RightPanel();
		rightPanel_.setSize(new Dimension(300, 100));
		rightPanel_.setPreferredSize(new Dimension(300, 100));
		this.add(rightPanel_, BorderLayout.LINE_END);

	}

}
