package processor;

public class Datapath {
	private Register[] reg;
	private Register src1, src2, dst;
	private PC pc;
	private Instruction[] instrs;
	private MBR mbr;
	private ALU alu;
	private Multiplexer mux1, mux2, mux3, mux4;
	private Memory mem;
	private Binary b = new Binary();

	public Datapath() {
		instrs = new Instruction[0];
		reg = new Register[8];
		pc = new PC(0);
		mbr = new MBR("");
		alu = new ALU();
		mux1 = mux2 = mux3 = mux4 = new Multiplexer();
		mem = new Memory();
	}

	public void setReg(Register[] reg) {
		this.reg = reg;
	}

	public void setPc(PC pc) {
		this.pc = pc;
	}

	public void setInstr(Instruction[] instr) {
		this.instrs = instr;
	}

	public MBR getMbr() {
		return mbr;
	}

	public ALU getAlu() {
		return alu;
	}

	public void setMem(Memory mem) {
		this.mem = mem;
	}

	public void construct(int m2, int m3, int m4, int ALUOp) {
		Instruction instr = instrs[pc.getPCValue()];

		pc.increment();

		mux4 = new Multiplexer(pc.getPCValue() + "", (Integer.parseInt(instr.getBadd())) + pc.getPCValue()+ "",b.toInteger(instr.getAddress()) + "");

		pc.setPCValue(Integer.parseInt(mux4.select(m4)));

		src1 = reg[b.toInteger(instr.getSrc1())];

		src2 = reg[b.toInteger(instr.getSrc2())];

		if (dst != null) {
			mux1 = new Multiplexer(instr.getSrc2(), instr.getDst());
			int select = (dst.equals(src2)) ? 0 : 1;
			dst = reg[b.toInteger(mux1.select(select))];
		}
		mux2 = new Multiplexer(src2.getValue(), b.extend(instr.getAddress(), 32));
		
		alu = new ALU(src1.getValue(), mux2.select(m2), ALUOp);
		
		mux3 = new Multiplexer(src2.getValue(), mem.loadFromMemory(instr.getMadd()), instr.getAddress()); // 1 for set 2
																											// for get 3
																											// for gti
		mbr = new MBR(mux3.select(m3));
	}

}
