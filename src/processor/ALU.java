package processor;

public class ALU {
	private int cont;
	private String op1,op2;
	Binary b = new Binary();
	
	public ALU() {cont = 0; op1=op2="00000000000000000000000000000000";}
	public ALU(String x, String y) {
		op1 = b.extend(x,32);
		op2 = b.extend(y,32);

	}
	public ALU(String x, String y,int c) {
		cont =c;
		op1 = b.extend(x,32);
		op2 = b.extend(y,32);

	}

	public int getControl() {return cont;}
	public void setControl(int cont) {this.cont = cont;}
	public String getOp1() {return op1;}
	public void setOp1(String op1) {this.op1 = op1;}
	public String getOp2() {return op2;}
	public void setOp2(String op2) {this.op2 = op2;}
	
	public String resultToBinaryString() {return calculate();}
	public int resultToInteger() {return b.toInteger(calculate());}

	public String calculate() {
		if(cont==0)
			return b.add(op1, op2);
		else
			return b.sub(op1, op2);
	}
	

}



