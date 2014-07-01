package gui.settings.viewers;

import java.awt.BorderLayout;

import gui.interfaces.SettingListener;
import gui.support.Setting;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import core.Value;


public class Viewer extends JPanel implements SettingListener{
	
	private static final long serialVersionUID = -2653260515004891031L;
	private JLabel nameLabel_;
	protected Setting setting_;
	private BorderLayout layout_;
	
	public Viewer(Setting s){
		setting_=s;
		nameLabel_=new JLabel(s.getName());
		layout_=new BorderLayout(0,0);
		this.setLayout(layout_);
		this.add(nameLabel_,BorderLayout.WEST);
	}
	
	protected void addMainComponent(JComponent c){
		this.add(c,BorderLayout.EAST);
	}

	@Override
	public void valueUpdated(Setting setting, Value v) {
		// TODO Auto-generated method stub
		
	}
}
