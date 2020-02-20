package processor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Microprocessor {

    public Binary b = new Binary();
    // declaring registers
    final Register r0 = new Register(0, b.extend("0", 32));
    private Register r1, r2, r3, r4, r5, r6, r7;

    private Register[] reg = { r0, r1, r2, r3, r4, r5, r6, r7 };

    private Memory memory;
    private Instruction[] instructions;
    private ControlUnit controlUnit;
    private PC pc;

    private int ind = 0;
    private String[] instructionSyntax = new String[256]; // original instructions array before binarization

    public Microprocessor(File file) throws IOException {

        // create the registers
        r1 = new Register(1, b.extend("0", 32));
        r2 = new Register(2, b.extend("0", 32));
        r3 = new Register(3, b.extend("0", 32));
        r4 = new Register(4, b.extend("0", 32));
        r5 = new Register(5, b.extend("0", 32));
        r6 = new Register(6, b.extend("0", 32));
        r7 = new Register(7, b.extend("0", 32));

        memory = new Memory(4096);
        instructions = new Instruction[256];
        controlUnit = new ControlUnit();
        pc = new PC(0);

        initialize(file);
    }

    private void initialize(File file) throws IOException {
        ind = 0;
        instructions[0] = new Instruction();
        instructions[0].initializeInstructionMemory(instructions);
        memory.initializeMemory();
        controlUnit.setPc(pc);
        controlUnit.setReg(reg);
        controlUnit.setInstrs(instructions);
        controlUnit.setMem(memory);
        pc.setInsMem(instructions);

        BufferedReader bri = new BufferedReader(new FileReader(file));
        BufferedWriter bwi = new BufferedWriter(new FileWriter("BinaryInput.txt"));

        String line = bri.readLine();
        while (line != null) {
            bwi.append(b.toBinary(line) + "\n");
            instructionSyntax[ind] = line;
            ind += 2;

            line = bri.readLine();
        }

        bri.close();
        bwi.close();

        BufferedReader br = new BufferedReader(new FileReader("BinaryInput.txt"));

        line = br.readLine();
        ind = 0;// fills instruction array

        while (line != null) {
            Instruction ins = new Instruction(line);
            instructions[ind] = ins;
            line = br.readLine();
            ind += 2;
        }
        br.close();
    }

    public String getMBRValue() {
        return controlUnit.getPath().getMbr().getMBRValue();
    }

    public void clearMBR() {
        controlUnit.clearMBR();
    }

    public String getPCValue() {
        return pc.getPCValue() + "";
    }

    public PC getPc() {
        return pc;
    }

    public void setPc(PC pc) {
        this.pc = pc;
    }

    public Instruction getInstruction() {
        return pc.fetch();
    }

    public Memory getMemory() {
        return memory;
    }

    public Binary getBinary() {
        return b;
    }

    public String getMemorySlot(String currentInstruction) {
        Instruction instruction = new Instruction(b.toBinary(currentInstruction));

        if (currentInstruction.contains("get") || currentInstruction.contains("set")) {
            String address = instruction.getAddress();
            int memoryAddress = b.toInteger(address) * 4;
            return memoryAddress + "";
        }
        return 0 + "";
    }

    public void process() {
        Instruction ins = pc.fetch();
        if (ins != null && !ins.toString().equals("1111111111111111") && pc.getPCValue() < ind) {
            controlUnit.execute(ins);
        } else if (ins != null || ins.toString().equals("1111111111111111") || pc.getPCValue() > ind)
            return;
    }

    public int index() {
        return ind - 2;
    }

    public Register getRegisterByIndex(int index) {
        return reg[index];
    }

    public int getIntRegisterValue(int index) {
        Register register = reg[index];
        return b.toInteger(register.getValue());
    }

    public int convertBinaryToInt(String value) {
        return b.toInteger(value);
    }

    public String[] getInstructionSyntax() {
        return instructionSyntax;
    }

    public void setInstructionSyntax(String[] instructionSyntax) {
        this.instructionSyntax = instructionSyntax;
    }

}
