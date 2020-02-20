import javafx.beans.property.SimpleStringProperty;

public class TableViewInstructions {
    private SimpleStringProperty instruction; //to bind String instruction to instruction column
    private SimpleStringProperty parameters;

    public TableViewInstructions(){
        instruction = new SimpleStringProperty("");
        parameters = new SimpleStringProperty("");
    }

    public TableViewInstructions(String instruction, String parameters) {
        this.instruction = new SimpleStringProperty(instruction);
        this.parameters = new SimpleStringProperty(parameters);
    }

    public String getInstruction() {
        return instruction.get();
    }

    public void setInstruction(String instruction) {
        this.instruction.set(instruction);
    }

    public String getParameters() {
        return parameters.get();
    }

    public void setParameters(String parameters) {
        this.parameters.set(parameters);
    }

    @Override
    public String toString() {
        return instruction.get()+" " + parameters.get();
    }
}
