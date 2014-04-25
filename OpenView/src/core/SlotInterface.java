package core;

import java.util.ArrayList;

public interface SlotInterface {
	public boolean connect(EmitterInterface e);
	public boolean deconnect(EmitterInterface e);
	public void deconnectAll();
	ArrayList<EmitterInterface> getConnections();
	
	public ValueType getType();
	public void setType(ValueType t);
	public void trigger(Value v);
	
	public String getLabel();
	public String getDescription();
	public boolean isFree();
	public boolean isCompatible(EmitterInterface e);
	
	public void addListener(SlotListener l);
	public void removeListener(SlotListener l);
	
	public void emitterUpdated(EmitterInterface e);
	
	public Value pullValue();
}
