package gui.components.ovprocedural;

import gui.components.ovnode.OVNodeComponent;
import gui.constants.ComponentSettings;
import gui.interfaces.OVContainer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import ovscript.AbstractBlock;
import ovscript.Block;
import core.Value;

public class OVProceduralNode extends OVNodeComponent {

	/**
     *
     */
	private static final long serialVersionUID = -2159211622124885366L;
	protected AbstractBlock block_;

	public OVProceduralNode(OVContainer father, AbstractBlock block) {
		super(father);
		block_ = block;
		getSetting(ComponentSettings.SizeW).setMin(new Value(15));
		getSetting(ComponentSettings.SizeW).setValue(30);

		getSetting(ComponentSettings.SizeH).setMin(new Value(15));
		getSetting(ComponentSettings.SizeH).setValue(30);
		setBackground(new Color(50,0,0,180));
		setSize(30, 30);
	}

	public Block getBlock() {
		return block_;
	}

	protected void setBlock(AbstractBlock block) {
		this.block_ = block;
	}

	public void setNext(Block block) {
		// set block
		block_.setNext(block);
	}

	@Override
	public void setName(String string) {
		super.setName(string); // To change body of generated methods, choose
								// Tools | Templates.
		block_.setName(string);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setColor(isSelected() ? getBackground().brighter()
				: getBackground());
		g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);

		g2d.setColor(isSelected() ? Color.lightGray : Color.lightGray.darker());
		g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1,4, 4);

		paintOVComponent(g2d);

	}

	public void setLeft(OVProceduralNode node){}
	public void setRight(OVProceduralNode node){}
	public void setNext(OVProceduralNode node){}
	
}
