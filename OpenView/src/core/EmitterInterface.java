package core;

public interface EmitterInterface {
	public boolean connect(SlotInterface s);
	public boolean deconnect(SlotInterface s);
	
	public ValueType getType();
	public String getLabel();
	public String getDescription();
	
	public void trigger(Value v);
	public void setValue(Value v);
	public Value readValue();
	
	public boolean isFree();
	public boolean isPolyvalent();
}
