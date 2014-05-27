package gui.adapters;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/***
 * Mouse adapter used to pass the mouse events to the container object mouse
 * adapter
 * 
 * @author martino
 * 
 */
public class TransferMouseAdapter extends MouseAdapter {

	/***
	 * this is the target adapter
	 */
	private MouseAdapter target_;

	/***
	 * initilaize the transfer adapter
	 * 
	 * @param adapter
	 *            target mouse adapter
	 */
	public TransferMouseAdapter(MouseAdapter adapter) {
		target_ = adapter;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		target_.mouseClicked(convert(e));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		target_.mousePressed(convert(e));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		target_.mouseReleased(convert(e));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		target_.mouseDragged(convert(e));
	}

	/***
	 * convert a mouse event for the target mouse event (changing the local
	 * coordinate)
	 * 
	 * @param e
	 *            original mouse event
	 * @return converted mouse event
	 */
	private MouseEvent convert(MouseEvent e) {
		return new MouseEvent((Component) e.getSource(), e.getID(),
				e.getWhen(), e.getModifiers(), e.getX() + 5, e.getY() + 5,
				e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(),
				e.isPopupTrigger(), e.getButton());

	}
}
