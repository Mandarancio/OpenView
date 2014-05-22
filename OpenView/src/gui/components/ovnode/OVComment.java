package gui.components.ovnode;

import gui.constants.ComponentSettings;
import gui.interfaces.OVContainer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.w3c.dom.Element;

import core.Setting;
import core.Value;
import core.support.OrientationEnum;

public class OVComment extends OVNodeComponent {

    /**
     *
     */
    private static final long serialVersionUID = -4875249844443085942L;
    private static final String Text = "Text";

    public OVComment(OVContainer father) {
        super(father);
        Setting s = new Setting(Text, new Value("double click to edit"),this);
        s.setNodeMode(false);
        addSetting(ComponentSettings.SpecificCategory, s);
        this.setBackground(new Color(255, 238, 170, 200));
        this.setForeground(new Color(0, 0, 0));
        resizable_ = true;
        getSetting(ComponentSettings.SizeW).setValue(180);
        getSetting(ComponentSettings.SizeH).setValue(30);
    }

    public OVComment(Element e, OVContainer father) {
        super(e, father);
        this.setBackground(new Color(255, 238, 170, 180));
        this.setForeground(new Color(0, 0, 0));
        resizable_ = true;
    }

    @Override
    protected void paintOVNode(Graphics2D g2d) {
        String text = getSetting(Text).getValue().getString();
        String[] lines = text.split("\n");
        int y = 5;
        Rectangle r = new Rectangle(5, y, getWidth() - 10, 20);
        g2d.setColor(getForeground());
        for (int i = 0; i < lines.length; i++) {
            paintText(lines[i], g2d, r, OrientationEnum.LEFT);
            r = new Rectangle(r.x, r.y + 24, r.width, r.height);
        }

    }

    @Override
    public void valueUpdated(Setting s, Value v) {
        if (s.getName().equals(Text)) {
            repaint();
        }
        super.valueUpdated(s, v);
    }

    @Override
    public void doubleClick(Point point) {
        super.doubleClick(point);
        // create a JTextArea
        JTextArea textArea = new JTextArea(6, 25);
        textArea.setText(getSetting(Text).getValue().getString());
        textArea.setEditable(true);

        // wrap a scrollpane around it
        JScrollPane scrollPane = new JScrollPane(textArea);

        // display them in a message dialog
        JOptionPane.showMessageDialog(this, scrollPane);
        getSetting(Text).setValue(textArea.getText());

    }

    @Override
    public void rightClick(Point point) {
        super.rightClick(point); //To change body of generated methods, choose Tools | Templates.
        getFather().showMenu(getFather().getAbsoluteLocation(this, point));
    }

    
}
