package gui;

import gui.components.OVComponent;
import gui.enums.EditorMode;
import gui.interfaces.SettingListener;
import gui.interfaces.SettingManager;
import gui.layers.LayerSelector;
import gui.settings.CategoryPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import core.Setting;
import core.Value;
import core.ValueType;
import core.support.MutableBoolean;
import gui.support.JColorButton;

import javax.swing.JColorChooser;

public class NodeSettingPanel extends JPanel implements SettingManager {

	/**
     *
     */
	private static final long serialVersionUID = -5297687376220230306L;
	private HashMap<Setting, SettingListener> listeners_ = new HashMap<>();
	private EditorMode mode_;
	private LayerSelector layerSelector_ = new LayerSelector();

	public NodeSettingPanel() {
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

	}

	@Override
	public void select(OVComponent c) {
		if (c == null) {
			return;
		}
		initComponents(c);
		this.add(new JPanel());
		this.add(layerSelector_);
		revalidate();

		layerSelector_.select(c);
	}

	@Override
	public void deselect() {
		this.removeAll();
		for (Setting s : listeners_.keySet()) {
			s.removeListener(listeners_.get(s));
		}
		layerSelector_.deselect();
		listeners_.clear();
		repaint();
	}

	private void initComponents(OVComponent c) {
		for (String category : c.getNodeSettingCategories()) {
			CategoryPanel cp = new CategoryPanel(category);
			ArrayList<Setting> settings = c.getNodeSettingCategory(category);
			cp.setNormalSize(new Dimension(getWidth(),
					25 * (settings.size() + 1)));
			for (Setting s : settings) {
				addComponent(s, cp);

			}
			add(cp);
			cp.setVisible(true);
		}
	}

	private void addComponent(final Setting s, CategoryPanel cp) {
		JPanel p = new JPanel();
		p.setMaximumSize(new Dimension(getWidth(), 25));
		p.setLayout(null);

		JLabel l = new JLabel(s.getName());
		l.setBounds(0, 0, 80, 25);
		l.setForeground(Color.gray);
		p.add(l);
		JComponent c = null;

		if (s.isConstant()) {
			l = new JLabel(s.getValue().getString());
			l.setHorizontalAlignment(JLabel.RIGHT);
			c = l;

		} else {
			if (s.getValue().getDescriptor().hasPossbilities()) {
				final JComboBox<String> cb = new JComboBox<>();
				for (Value v : s.getValue().getDescriptor().getPossibilities()) {
					cb.addItem(v.getString());
				}
				cb.setSelectedItem(s.getValue().getString());
				SettingListener listener = new SettingListener() {

					@Override
					public void valueUpdated(Setting setting, Value v) {
						if (!cb.getSelectedItem().equals(v.getString())) {
							cb.setSelectedItem(v.getString());
						}
					}
				};

				cb.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent ae) {
						if (!s.getValue().getString()
								.equals(cb.getSelectedItem())) {
							s.setValue(s.getValue().getDescriptor()
									.getPossibilities()
									.get(cb.getSelectedIndex()).getData());
						}
					}
				});
				s.addListener(listener);
				listeners_.put(s, listener);

				c = cb;

			} else if (s.getType() == ValueType.STRING) {
				final JTextField tf = new JTextField(s.getValue().getString());
				tf.setHorizontalAlignment(JLabel.RIGHT);

				final MutableBoolean loopFlag = new MutableBoolean(false);

				SettingListener listener = new SettingListener() {

					@Override
					public void valueUpdated(Setting setting, Value v) {
						if (!loopFlag.value) {
							if (!v.getString().equals(tf.getText())) {
								loopFlag.value = true;
								tf.setText(v.getString());
							}
						} else {
							loopFlag.value = false;
						}
					}
				};

				tf.getDocument().addDocumentListener(new DocumentListener() {

					@Override
					public void removeUpdate(DocumentEvent e) {
						trigger();
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						trigger();
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						trigger();
					}

					private void trigger() {
						if (!loopFlag.value) {
							if (!s.getValue().getString().equals(tf.getText())) {
								loopFlag.value = true;
								s.setValue(tf.getText());
							}
						} else {
							loopFlag.value = false;
						}

					}
				});
				s.addListener(listener);
				listeners_.put(s, listener);
				c = tf;
			} else if (s.getType() == ValueType.BOOLEAN) {

				final JCheckBox cb = new JCheckBox();
				try {
					cb.setSelected(s.getValue().getBoolean());
				} catch (Exception e) {
					e.printStackTrace();
				}
				cb.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							if (s.getValue().getBoolean() != cb.isSelected()) {
								s.setValue(new Boolean(cb.isSelected()));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

				SettingListener listener = new SettingListener() {

					@Override
					public void valueUpdated(Setting setting, Value v) {
						try {
							boolean e = v.getBoolean();
							if (e != cb.isSelected()) {
								cb.setSelected(e);
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				};
				s.addListener(listener);
				listeners_.put(s, listener);
				c = cb;

			} else if (s.getType() == ValueType.INTEGER) {
				try {
					final JSpinner sp = new JSpinner(new SpinnerNumberModel(s
							.getValue().getInt(), s.getMin().getInt(), s
							.getMax().getInt(), 1));
					SettingListener listener = new SettingListener() {

						@Override
						public void valueUpdated(Setting setting, Value v) {
							try {
								sp.setValue(v.getInt());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};

					sp.addChangeListener(new ChangeListener() {

						@Override
						public void stateChanged(ChangeEvent e) {
							s.setValue(sp.getValue());
						}
					});
					s.addListener(listener);
					listeners_.put(s, listener);
					c = sp;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (s.getType() == ValueType.DOUBLE) {
				try {
					final JSpinner sp = new JSpinner(new SpinnerNumberModel(s
							.getValue().getDouble(), s.getMin().getDouble(), s
							.getMax().getDouble(), 0.01));

					SettingListener listener = new SettingListener() {

						@Override
						public void valueUpdated(Setting setting, Value v) {
							try {
								sp.setValue(v.getDouble());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};

					sp.addChangeListener(new ChangeListener() {

						@Override
						public void stateChanged(ChangeEvent e) {
							s.setValue(sp.getValue());
						}
					});
					s.addListener(listener);
					listeners_.put(s, listener);
					c = sp;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (s.getType() == ValueType.COLOR) {
				final JColorButton b = new JColorButton();
				try {
					b.setColor(s.getValue().getColor());
				} catch (Exception e) {
					e.printStackTrace();
				}
				b.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						Color newColor = JColorChooser.showDialog(
								NodeSettingPanel.this, "Choose Color",
								b.getColor());
						if (newColor != null && !newColor.equals(b.getColor())) {
							b.setColor(newColor);
							s.setValue(newColor);
						}
					}
				});

				SettingListener list = new SettingListener() {

					@Override
					public void valueUpdated(Setting setting, Value v) {
						try {
							if (v.getColor().equals(b.getColor())) {
								b.setColor(v.getColor());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				s.addListener(list);
				listeners_.put(s, list);
				c = b;
			} else if (s.getType() == ValueType.ENUM) {
				final JComboBox<String> box = new JComboBox<>();

				try {
					@SuppressWarnings("unchecked")
					EnumSet<?> set = EnumSet.allOf(s.getValue().getEnum()
							.getClass());
					for (Object obj : set.toArray()) {
						box.addItem(obj.toString());
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				box.setSelectedItem(s.getValue().getString());
				SettingListener listener = new SettingListener() {

					@Override
					public void valueUpdated(Setting setting, Value v) {
						box.setSelectedItem(v.getString());
					}
				};

				box.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

						try {
							@SuppressWarnings("unchecked")
							Enum<?> t = Enum.valueOf(s.getValue().getEnum()
									.getClass(), box.getSelectedItem()
									.toString());
							s.setValue(t);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				s.addListener(listener);
				listeners_.put(s, listener);
				c = box;
			} else if (s.getType() == ValueType.FILE) {
				JButton b = new JButton("Open");
				b.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						// Create a file chooser
						final JFileChooser fc = new JFileChooser();

						// In response to a button click:
						int returnVal = fc.showOpenDialog(null);

						if (returnVal == JFileChooser.APPROVE_OPTION) {
							try {
								File f = fc.getSelectedFile();
								s.setValue(f);
							} catch (Exception e) {
								JOptionPane.showMessageDialog(null,
										e.getMessage());
								e.printStackTrace();
							}
						}
					}
				});
				c = b;
			}
		}

		if (c != null) {
			c.setBounds(80, 0, getWidth() - 118, 25);
			p.add(c);
			c.setVisible(true);
			if (mode_.isExec()) {
				c.setEnabled(false);
			}
		}
		cp.add(p);
	}

	@Override
	public void setMode(EditorMode mode) {
		mode_ = mode;
	}

}
