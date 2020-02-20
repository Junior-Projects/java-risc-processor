import javafx.beans.property.SimpleStringProperty;

public class MemorySlotTableModel {
    private SimpleStringProperty instruction;
    private SimpleStringProperty memorySlot;

    public MemorySlotTableModel(String instruction, String memorySlot) {
        this.instruction = new SimpleStringProperty(instruction);
        this.memorySlot = new SimpleStringProperty(memorySlot);
    }

    public String getInstruction() {
        return instruction.get();
    }

    public SimpleStringProperty instructionProperty() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction.set(instruction);
    }

    public String getMemorySlot() {
        return memorySlot.get();
    }

    public SimpleStringProperty memorySlotProperty() {
        return memorySlot;
    }

    public void setMemorySlot(String memorySlot) {
        this.memorySlot.set(memorySlot);
    }
}