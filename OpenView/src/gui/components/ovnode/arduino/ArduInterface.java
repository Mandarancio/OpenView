/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.components.ovnode.arduino;

/**
 *
 * @author martino
 */
public interface ArduInterface {
    public void gpioWrite(int port,boolean value);
}
