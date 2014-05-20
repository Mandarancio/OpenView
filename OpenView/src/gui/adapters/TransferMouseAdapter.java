package gui.adapters;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TransferMouseAdapter extends MouseAdapter {
	private MouseAdapter target_;

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

	private MouseEvent convert(MouseEvent e) {
		return new MouseEvent((Component) e.getSource(), e.getID(),
				e.getWhen(), e.getModifiers(), e.getX() + 5, e.getY() + 5,
				e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(),
				e.isPopupTrigger(), e.getButton());

	}
}
