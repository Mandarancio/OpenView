package ovscript;

import javax.swing.JOptionPane;

import core.Value;

public class Alert extends AbstractBlock{


	private Block body_;

	public Alert(Block b) {
		super("alert");
		setBody(b);
	}

	@Override
	public Value run(CodeBlock i) throws InterpreterException {
		Value v=body_.run( i);
		JOptionPane.showMessageDialog(null,v.getString());
		return v;
	}

	public Block getBody() {
		return body_;
	}

	public void setBody(Block body_) {
		this.body_ = body_;
	}
	

}
