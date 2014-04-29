/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ovscript;

import core.Value;

/**
 *
 * @author martino
 */
class ENDBlock extends AbstractBlock{

    public ENDBlock() {
        super("end6");
    }

    @Override
    public Value run(Interpreter i) {
        return new Value(Void.TYPE);
    }
    
}
