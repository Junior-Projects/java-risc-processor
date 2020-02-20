package processor;

public class ControlUnit {

	private Register[] reg;
	Binary b = new Binary();
	private PC pc;
	private Instruction[] instrs;
	private Memory mem;
	private Datapath path;
	private ALU alu;

	public ControlUnit() {
		reg = new Register[8];
		pc = new PC(0);
		instrs = new Instruction[0];
		path = new Datapath();
		mem = new Memory();
	}

	public Datapath getPath() {
		return path;
	}

	public void clearMBR() {
		path.getMbr().clear();
	}

	public void setReg(Register[] reg) {
		this.reg = reg;
		path.setReg(reg);
	}

	public void setPc(PC pc) {
		this.pc = pc;
		path.setPc(pc);
	}

	public void setInstrs(Instruction[] instrs) {
		this.instrs = instrs;
		path.setInstr(instrs);
	}

	public void setMem(Memory mem) {
		this.mem = mem;
		path.setMem(mem);
	}

	public void execute(Instruction ins) {
		if (ins.toString().equals("1111111111111111"))
			return;
		if (ins.getType().equalsIgnoreCase("A")) {
			int AluOp = 0;
			if (ins.getFunct().equals("001"))// sub
				AluOp = 1;
			path.construct(0, 1, 0, AluOp);
			if (ins.getDst().equals("000"))
				return;
			else
				reg[b.toInteger(ins.getDst())].setValue(path.getAlu().calculate());// sets value of destination register
		} 
		else if (ins.getType().equals("M")) {
			if (ins.getFunct().equals("100"))// get
			{
				path.construct(0, 1, 0, 0);
				reg[b.toInteger(ins.getSrc2())].setValue(path.getMbr().getMBRValue());// loads value from memory into
																						// destination register
			} else if (ins.getFunct().equals("101")) {
				path.construct(0, 2, 0, 0);
				reg[b.toInteger(ins.getSrc2())].setValue(path.getMbr().getMBRValue()); // loads constant into register
			} else {
				path.construct(0, 0, 0, 0);
				mem.saveToMemory(path.getMbr().getMBRValue(), ins.getMadd());
			}
		} 
		else if (ins.getType().equals("B")) {
			alu = new ALU(reg[b.toInteger(ins.getSrc1())].getValue(), reg[b.toInteger(ins.getSrc2())].getValue(), 1);
			if (ins.getFunct().equals("011")) {
				if (b.toInteger(alu.calculate()) == 0)
					path.construct(0, 0, 1, 1);
				else
					path.construct(0, 0, 0, 1);
			} else {
				if (b.toInteger(alu.calculate()) > 0)
					path.construct(0, 0, 1, 1);
				else
					path.construct(0, 0, 0, 1);
			}

		} 
		else if (ins.getType().equals("J"))
			path.construct(0, 0, 2, 0);
	}

}
