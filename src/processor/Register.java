package processor;

public class Register {

    protected Instruction[] InsMem;
    private String value;
    private Binary b = new Binary();
    private int id;

    public Register() {
        id = 0;
        value = "";
    }
    public Register(int id, String val) {
        this.id = id;
        value = b.extend(val, 32);
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = b.extend(value, 32);
    }
    public void setInsMem(Instruction[] insMem) {
        InsMem = insMem;
    }
    public String toString() {
        return "\nReg " + id + ": " + value + "\t" + b.toInteger(value);
    }
}

class PC extends Register {
    private int value;
    private Binary b = new Binary();

    public PC(int val) {
        super();
        value = val;
        setValue(Integer.toBinaryString(value));
    }
    public int getPCValue() {
        return this.value;
    }
    public void setPCValue(int value) {
        this.value = value;
        setValue(b.extend(Integer.toBinaryString(value), 32));
    }
    public String getValue() {
        return b.extend(Integer.toBinaryString(getPCValue()), 32);
    }
    public Instruction fetch() {
        return InsMem[value];
    }
    public void increment() {
        value += 2;
    }
}

class MBR extends Register {
    private String value;
    private Binary b = new Binary();

    public MBR() {
        super();
    }
    public MBR(String buff) {
        super();
        value = b.extend(buff, 32);
    }
    public void setValue(String buff) {
        value = b.extend(buff, 32);
    }
    
    public void clear() {
    	setValue("0");
    }
    public String getMBRValue() {
        return value;
    }
}