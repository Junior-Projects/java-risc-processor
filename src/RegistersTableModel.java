import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RegistersTableModel implements Comparable<RegistersTableModel> {
    private SimpleStringProperty register;
    private SimpleStringProperty value;
    private SimpleIntegerProperty integerProperty;

    public RegistersTableModel(String register, String value) {
        this.register = new SimpleStringProperty(register);
        this.value = new SimpleStringProperty(value);
        integerProperty = new SimpleIntegerProperty(0);
    }

    
    public String getRegister() {
        return register.get();
    }

    public SimpleStringProperty registerProperty() {
        return register;
    }

    public void setRegister(String register) {
        this.register.set(register);
    }

    public String getValue() {
        return value.get();
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    public int getIntegerProperty() {
        return integerProperty.get();
    }

    public SimpleIntegerProperty integerPropertyProperty() {
        return integerProperty;
    }

    public void setIntegerProperty(int integerProperty) {
        this.integerProperty.set(integerProperty);
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    //used to sort
    @Override
    public int compareTo(RegistersTableModel model) {
        return getRegister().compareTo(model.getRegister());
    }

    //used to check if objects are equal
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RegistersTableModel) {
            if (compareTo((RegistersTableModel) obj) == 0)
                return true;
            return false;
        }
        return false;
    }
}
