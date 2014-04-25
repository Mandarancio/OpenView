package gui.components.ovprocedural;

import proceduralScript.Block;
import gui.components.ovnode.OVNodeComponent;
import gui.interfaces.OVContainer;

public class OVProceduralNode extends OVNodeComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2159211622124885366L;
	private Block block_;

	public OVProceduralNode(OVContainer father, Block block) {
		super(father);
		block_ = block;
	}

	public Block getBlock() {
		return block_;
	}

	protected void setBlock(Block block) {
		this.block_ = block;
	}

	public void setNext(Block block) {
		// set block
		block_.setNext(block);
	}
}
