package gui.adapters;

import gui.interfaces.OVContainer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

public class ContainerMouseAdapter extends MouseAdapter{
	private OVContainer container_;
	
	public ContainerMouseAdapter(OVContainer cont){
		container_=cont;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		((JComponent)e.getSource()).requestFocus();
		container_.deselectAll();
		if (e.getButton()==MouseEvent.BUTTON3)
			container_.showMenu(e.getPoint());
	}
}
