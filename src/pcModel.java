import javafx.beans.property.SimpleStringProperty;

public class pcModel {
	SimpleStringProperty intValue= new SimpleStringProperty();
	SimpleStringProperty instruction= new SimpleStringProperty("");
	
	public pcModel() {
		this.instruction.set("");
		this.intValue.set("");
	}
	
	public pcModel(String intValue,String instruction) {
		setIntValue(intValue); setInstruction(instruction);
	}
	
	public String getIntValue() {
		return this.intValue.get();
	}
	
	public void setIntValue(String intValue) {
		this.intValue.set(intValue);
	}
	
	public String getInstruction() {
		return this.instruction.get();
	}
	
	public void setInstruction(String instruction) {
		this.instruction.set(instruction);
	}
}
