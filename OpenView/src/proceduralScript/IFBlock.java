/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proceduralScript;

import core.Value;
import core.ValueType;

/**
 *
 * @author martino
 */
public class IFBlock extends AbstractBlock {

    private Block condition_;
    private Block else_;
    private Block body_;

    public IFBlock() {
        super("IF");
    }

    @Override
    public Value run() {
        //nothing to do here
        return new Value(Void.TYPE);
    }

    public void setCondition(Block b) {
        condition_ = b;
    }

    public void setElse(Block b) {
        else_ = b;
    }

    @Override
    public Block next() {
        System.out.println(condition_.name());
        
        Value v = condition_.run();
        System.out.println(v);
        boolean b = false;
        try {
            b = v.getBoolean();
        } catch (Exception e) {
        }
        if (b) {
            System.out.println("IF CONDITION VALID");
            System.out.println(body_);
            return body_; //To change body of generated methods, choose Tools | Templates.
        } else {
            if (else_ != null) {
                return else_.next();
            } else {
                return super.next();
            }
        }
    }

    void setBody(Block first) {
        body_ = first;
    }

}
