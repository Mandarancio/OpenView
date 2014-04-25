package gui.adapters;

import gui.enums.DragAction;
import gui.interfaces.DragComponent;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

public class DragMouseAdapter extends MouseAdapter {

	private DragComponent component_;
	private DragAction dragAction_=DragAction.NOTHING;
	private Point dragPoint_;
	
	
	public DragMouseAdapter(DragComponent comp) {
		component_ = comp;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		((JComponent)e.getSource()).requestFocus();
		if (e.getButton()==MouseEvent.BUTTON1 ){
			if (e.getClickCount()==1)
				component_.leftClick(e.getPoint());
			else
				component_.doubleClick(e.getPoint());
		}
		else if (e.getButton()==MouseEvent.BUTTON3)
			component_.rightClick(e.getPoint());
	}

	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton()==MouseEvent.BUTTON1){
			dragAction_=component_.drag(e.getPoint());
			if (dragAction_==DragAction.DRAG){
				dragPoint_=e.getPoint();
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton()==MouseEvent.BUTTON1){
			component_.drop(dragAction_);
			dragAction_=DragAction.NOTHING;
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (dragAction_==DragAction.DRAG){
			component_.moveOf(e.getPoint().x-dragPoint_.x, e.getPoint().y-dragPoint_.y);
		}
		else if (dragAction_==DragAction.RESIZE){
			component_.resizeTo(e.getPoint().x,e.getPoint().y);
			
		}else if (dragAction_==DragAction.LINE){
			component_.moveLineTo(e.getX(), e.getY());
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		component_.mouseExited();
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		component_.mouseMoved(e.getPoint());
	}
	
}
