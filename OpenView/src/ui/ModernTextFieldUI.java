/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTextFieldUI;

/**
 *
 * @author martino
 */
public class ModernTextFieldUI extends BasicTextFieldUI{

    @Override
    protected void paintBackground(Graphics grphcs) {
        JComponent jc=getComponent();
        grphcs.setColor(jc.getBackground().brighter());
        grphcs.fillRoundRect(0, 0, jc.getWidth()-1, jc.getHeight()-1,8,8);
        grphcs.setColor(jc.getBackground().darker());
        grphcs.drawRoundRect(0, 0, jc.getWidth()-1, jc.getHeight()-1,8,8);
    }

    @Override
    public void installUI(JComponent jc) {
        super.installUI(jc); //To change body of generated methods, choose Tools | Templates.
        jc.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 2));

    }

    
}
