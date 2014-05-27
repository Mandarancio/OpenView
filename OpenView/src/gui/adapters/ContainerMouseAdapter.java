package gui.adapters;

import gui.interfaces.OVContainer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

/***
 * Simple container mouse adapter. It work together with the {@link OVContainer} interface
 * 
 * @author martino
 * 
 */
public class ContainerMouseAdapter extends MouseAdapter {
	/***
	 * assigned container
	 */
	private OVContainer container_;

	/***
	 * initialize the mouse adapter
	 * 
	 * @param cont
	 *            target {@link OVContainer}
	 */
	public ContainerMouseAdapter(OVContainer cont) {
		container_ = cont;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		((JComponent) e.getSource()).requestFocus();
		container_.deselectAll();
		if (e.getButton() == MouseEvent.BUTTON3)
			container_.showMenu(e.getPoint());
	}
}
