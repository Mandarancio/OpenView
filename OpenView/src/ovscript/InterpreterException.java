/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ovscript;

/**
 *
 * @author martino
 */
public class InterpreterException extends  Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3083112810263700197L;
	private int line=-1;
    public InterpreterException(String msg, int line){
        super(msg);
        this.line=line;
    }
    
    public int getLine(){
        return line;
    }
}
