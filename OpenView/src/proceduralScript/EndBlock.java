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
class EndBlock extends AbstractBlock{

    public EndBlock() {
        super("end6");
    }

    @Override
    public Value run() {
        return new Value(Void.TYPE);
    }
    
}
