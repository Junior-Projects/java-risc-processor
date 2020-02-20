package processor;

public class Instruction {
    private Binary b = new Binary();

    private String instr;
    private String funct;
    private String src1 = "000", src2 = "000", dst = "000";
    private String address = "0";
    private String badd = "0"; //branch address
    private String madd = "0"; //move address
    private String shamt = "0000";
    private String type;
    private String full;

    public Instruction() {
        src1 = src2 = dst = "";
        instr = address;
    }

    public Instruction(String line) {
        full = line;
        funct = line.substring(0, 3);

        if (funct.equals("000") || funct.equals("001")) //add || sub
        {
            src1 = line.substring(3, 6);
            src2 = line.substring(6, 9);
            dst = line.substring(9, 12);
            shamt = line.substring(12);
            type = "A";
            address = "0";
        } else if (funct.equals("010") || funct.equals("011")) //beq || bgt
        {
            src1 = line.substring(3, 6);
            src2 = line.substring(6, 9);
            address = line.substring(9);
            badd = b.toInteger(b.shiftLeft(address, 1))+"";
            type = "B";
        } else if (funct.equals("100") || funct.equals("101") || funct.equals("110")) //move instructions(get || gti || set) // byte addressed
        {
            src2 = line.substring(3, 6);
            address = line.substring(6);
            madd = Integer.toString(b.toInteger(b.shiftLeft(address, 2)));
            type = "M";
        } else if (funct.equals("111")) { // jmp
            address = line.substring(3);
            type = "J";
        }
    }

    public String getFunct() {
        return funct;
    }

    public String getSrc1() {
        return src1;
    }

    public String getSrc2() {
        return src2;
    }

    public String getDst() {
        return dst;
    }

    public String getAddress() {
        return address;
    }

    public String getBadd() {
        return badd;
    }

    public String getMadd() {
        return madd;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return full;
    }

    public void initializeInstructionMemory(Instruction[] x) {
        for (int i = 0; i < x.length; i++)
            x[i] = new Instruction();
    }
}