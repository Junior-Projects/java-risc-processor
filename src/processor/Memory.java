package processor;

public class Memory {
    private String[] mem;
    private Binary b = new Binary();

    public Memory() {
        mem = new String[4096];
        initializeMemory();
    }

    public Memory(String[] Mem) {
        mem = Mem;
    }

    public Memory(int x) {
        mem = new String[x];
    }

    public String loadFromMemory(String address) {
        int add = b.toInteger(address);
        return b.load(mem, add);
    }

    public void saveToMemory(String value, String address) {
        b.fill(value, mem, b.toInteger(address));
    }

    public void initializeMemory() {
        for (int i = 0; i < mem.length; i++)
            mem[i] = "0";
    }
}