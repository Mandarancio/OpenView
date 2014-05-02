package ovscript;

import core.Slot;
import core.Value;
import core.ValueType;

public class Import extends AbstractBlock{
	private Slot slot_;
	private ValueType type_;

	public Import() {
		super("import");
		setType(ValueType.VOID);
	}
	
	public Import(String arg){
		this();
		if (arg.length()!=0){
			try{
				setType(ValueType.valueOf(arg));
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public Value run(CodeBlock i) {
		if (slot_==null)
			slot_=i.getSlot();
		if (slot_!=null){
			return slot_.pullValue();
		}
		return new Value();
	}

	public ValueType getType() {
		return type_;
	}

	public void setType(ValueType type_) {
		this.type_ = type_;
	}

	public Slot getSlot() {
		return slot_;
	}

	public void setSlot(Slot slot_) {
		this.slot_ = slot_;
	}

}