/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package proceduralScript;

import core.Value;

/**
 *
 * @author martino
 */
public class IFBlock extends AbstractBlock{

    public IFBlock() {
        super("IF");
    }

    @Override
    public Value run() {
        //nothing to do here
        return new Value(Void.TYPE);
    }    
}
