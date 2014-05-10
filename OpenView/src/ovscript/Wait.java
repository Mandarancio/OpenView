/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ovscript;

import core.Value;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martino
 */
public class Wait extends AbstractBlock {

    private Block body_;

    public Wait(Block b) {
        super("wait");
        body_ = b;
    }

    @Override
    public Value run(CodeBlock i) throws InterpreterException {
        try {
            int mills = body_.run(i).getInt();
            Thread.sleep(mills);
        } catch (InterruptedException ex) {
            Logger.getLogger(Wait.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Wait.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Value();

    }

    public Block getBody() {
        return body_;
    }

    public void setBody(Block body) {
        this.body_ = body;
    }
}
