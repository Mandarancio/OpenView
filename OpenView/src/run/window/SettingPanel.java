package run.window;

import gui.components.OVComponent;
import gui.enums.EditorMode;
import gui.interfaces.SettingListener;
import gui.interfaces.SettingManager;
import gui.settings.CategoryPanel;
import gui.support.Setting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import run.window.support.JColorButton;
import core.Value;
import core.ValueType;
import core.support.MutableBoolean;

public class SettingPanel extends JPanel implements SettingManager {

    /**
     *
     */
    private static final long serialVersionUID = -5297687376220230306L;
    private HashMap<Setting, SettingListener> listeners_ = new HashMap<>();
    private OVComponent component_;
    private EditorMode mode_ = EditorMode.GUI;

    public SettingPanel() {
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    @Override
    public void select(OVComponent c) {
        if (c == null) {
            return;
        }
        component_ = c;
        initComponents(c);
        revalidate();
    }

    @Override
    public void deselect() {
        component_ = null;
        this.removeAll();
        for (Setting s : listeners_.keySet()) {
            s.removeListener(listeners_.get(s));
        }
        listeners_.clear();
        repaint();
    }

    private void initComponents(OVComponent c) {
        for (String category : c.getSettingCategories()) {
            CategoryPanel cp = new CategoryPanel(category);
            ArrayList<Setting> settings = c.getSettingCategory(category);
            cp.setNormalSize(new Dimension(getWidth(),
                    25 * (settings.size() + 1)));
            boolean flag = false;
            int i = 0;
            for (Setting s : settings) {
                if (((mode_ == EditorMode.GUI || mode_.isExec()) && s
                        .isGuiMode())
                        || (mode_ == EditorMode.NODE && s.isNodeMode())) {
                    addComponent(s, cp);
                    flag = true;
                    i++;
                }
            }
            if (flag) {
                cp.setNormalSize(new Dimension(getWidth(), 25 * (i + 1)));
                this.add(cp);
            }
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
        if (mode_ == EditorMode.GUI || mode_ == EditorMode.DEBUG) {
            if (s.isConstant()) {
                l = new JLabel(s.getValue().getString());
                l.setHorizontalAlignment(JLabel.RIGHT);
                c = l;

            } else {
                if (s.getValue().getDescriptor().hasPossbilities()) {
                    final JComboBox<String> cb = new JComboBox<>();
                    for (Value v : s.getValue().getDescriptor()
                            .getPossibilities()) {
                        cb.addItem(v.getString());
                    }

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
                    final JTextField tf = new JTextField(s.getValue()
                            .getString());
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

                    tf.getDocument().addDocumentListener(
                            new DocumentListener() {

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
                                        if (!s.getValue().getString()
                                        .equals(tf.getText())) {
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
                                if (s.getValue().getBoolean() != cb
                                        .isSelected()) {
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
                        final JSpinner sp = new JSpinner(
                                new SpinnerNumberModel(s.getValue().getInt(), s
                                        .getMin().getInt(),
                                        s.getMax().getInt(), 1));
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
                        e.printStackTrace();
                    }
                } else if (s.getType() == ValueType.DOUBLE) {
                    try {
                        final JSpinner sp = new JSpinner(
                                new SpinnerNumberModel(s.getValue().getDouble(), s
                                        .getMin().getDouble(),
                                        s.getMax().getDouble(), 0.01));
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
                                e.printStackTrace();
                            }
                        }
                    });
                    s.addListener(listener);
                    listeners_.put(s, listener);
                    c = box;
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
                                    SettingPanel.this, "Choose Color",
                                    b.getColor());
                            if (newColor != null
                                    && !newColor.equals(b.getColor())) {
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
                }

            }
            if (c != null) {
                c.setBounds(80, 0, getWidth() - 118, 25);
                if (mode_.isExec()) {
                    c.setEnabled(false);
                }
                p.add(c);
            }
        } else if (mode_ == EditorMode.NODE) {
            if (!s.isConstant()) {
                if (s.isInput()) {
                    final JCheckBox inbox = new JCheckBox("in");
                    if (s.getInputNode() != null) {
                        inbox.setSelected(true);
                    }
                    inbox.setHorizontalAlignment(JLabel.RIGHT);
                    inbox.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            component_.refreshInputs(s, inbox.isSelected());
                        }
                    });

                    inbox.setBounds(80, 0, (getWidth() - 110) / 2, 25);
                    p.add(inbox);
                }
                if (s.isOutput()) {
                    final JCheckBox outbox = new JCheckBox("out");
                    if (s.getOutputNode() != null) {
                        outbox.setSelected(true);
                    }
                    outbox.setHorizontalAlignment(JLabel.RIGHT);
                    outbox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            component_.refreshOutputs(s, outbox.isSelected());
                        }
                    });

                    outbox.setBounds(80 + (getWidth() - 110) / 2, 0,
                            (getWidth() - 110) / 2, 25);
                    p.add(outbox);
                }
            }
        }

        cp.add(p);
    }

    @Override
    public void setMode(EditorMode mode) {
        if (mode != mode_) {
            mode_ = mode;
            OVComponent c = component_;
            deselect();
            select(c);
        }

    }

}
