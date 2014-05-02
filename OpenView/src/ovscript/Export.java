package ovscript;

import core.Emitter;
import core.Value;
import core.ValueType;

public class Export extends AbstractBlock {
	private Block expression_;
	private Emitter emitter_;
	private ValueType type_ = ValueType.VOID;

	public Export(Block arg) {
		super("export");
		expression_ = arg;
	}

	public Export(Block arg, String type) {
		this(arg);
		if (type.length()!=0){
			try{
				setType((ValueType.valueOf(type)));
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public Value run(CodeBlock i) {
		Value v=expression_.run(i);
		if (emitter_==null)
			emitter_=i.getEmitter();
		if (emitter_!=null)
			emitter_.trigger(v);
		return v;
	}

	public Emitter getEmitter() {
		return emitter_;
	}

	public void setEmitter(Emitter emitter_) {
		this.emitter_ = emitter_;
	}

	public ValueType getType() {
		return type_;
	}

	public void setType(ValueType type_) {
		this.type_ = type_;
	}

}
