/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.maker;

import gui.components.OVComponent;
import gui.interfaces.OVContainer;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

/**
 *
 * @author martino
 */
public class OVBaseMaker extends JPopupMenu implements ActionListener {

    private OVContainer father_;
    private final Point point_;

    public OVBaseMaker(Point p, OVContainer father, JMenu... menus) {
        father_ = father;
        point_ = p;
        this.initMenu(menus);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String key = ae.getActionCommand();
        if (OVClassFactory.hasClass(key)) {
            create(OVClassFactory.getInstance(key, father_));
        }
    }

    protected void shuowPopup() {
        this.show((JComponent) father_, point_.x, point_.y);
    }

    protected void initMenu(JMenu... menus) {
        for (JMenu m : menus) {
            this.add(m);
        }
    }

    protected void create(OVComponent c) {
        if (c != null) {
            Point p = father_.validate(point_);
            c.moveTo(p.x, p.y);
            father_.addComponent(c);

            this.setVisible(false);
            father_ = null;
        }
    }

}
