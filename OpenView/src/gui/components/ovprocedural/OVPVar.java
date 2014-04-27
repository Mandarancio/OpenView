/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.components.ovprocedural;

import gui.interfaces.OVContainer;
import proceduralScript.Block;
import proceduralScript.Var;

/**
 *
 * @author martino
 */
public class OVPVar extends OVProceduralNode{
    private final Var var_;

    public OVPVar(OVContainer father) {
        super(father, new Var("var"));
        var_=(Var)block_;
        
    }


    
}
