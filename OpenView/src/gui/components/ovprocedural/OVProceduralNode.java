package gui.components.ovprocedural;

import proceduralScript.Block;
import gui.components.ovnode.OVNodeComponent;
import gui.interfaces.OVContainer;
import proceduralScript.AbstractBlock;

public class OVProceduralNode extends OVNodeComponent {

    /**
     *
     */
    private static final long serialVersionUID = -2159211622124885366L;
    protected AbstractBlock block_;

    public OVProceduralNode(OVContainer father, AbstractBlock block) {
        super(father);
        block_ = block;
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
        super.setName(string); //To change body of generated methods, choose Tools | Templates.
        block_.setName(string);
    }

}
