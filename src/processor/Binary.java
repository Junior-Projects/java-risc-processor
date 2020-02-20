package processor;

public class Binary {
    private String x, y;

    public Binary() {
        x = y = "";
    }

    public String extend(String x) { //used to extend to 16 bits
        int l = 16 - x.length();
        while (l-- > 0)
            x = 0 + "" + x;
        return x;
    }

    public String extend(String x, int ext) { //used to extend to "ext" (which is 32 in this project)
    	int l = ext - x.length();
        while (l-- > 0)
            x = 0 + "" + x;
        return x;
    	}
    public String shiftLeft(String x, int shamt) {
        for (int i = 0; i < shamt; i++)
            x += "0" + "";
        return x;
    }
//	public String add(String x,String y) {
//	int carry=0;
//	ArrayList<String> ls = new ArrayList<>();
//	
//	if(x.length()!=y.length()) {x=extend(x); y=extend(y);}
//	
//	for(int i =x.length()-1;i>=0;i--)
//		{
//		if(ls.size()<16) {
//			if(Character.getNumericValue(x.charAt(i))+Character.getNumericValue(y.charAt(i)) + carry==0)
//				{ls.add(0,"0"); carry = 0;}
//			else if (Character.getNumericValue(x.charAt(i))+Character.getNumericValue(y.charAt(i)) + carry==1)
//				{ls.add(0,"1"); carry=0;}
//			
//			else if (Character.getNumericValue(x.charAt(i))+Character.getNumericValue(y.charAt(i)) + carry>=2)
//				{ls.add(0,"0"); carry =1;}
//		if(i==0 & carry==1)
//			ls.add(0,"1");
//			}
//		}
//		return ls.toString().replaceAll("\\[","").replaceAll("]", "").replaceAll(", ", "");	
//}
    
    public String add(String x, String y) {
        return extend(Integer.toBinaryString(toInteger(x) + toInteger(y)), 32);
    }
    
//	public String sub(String x,String y) {
//	ArrayList<String> ls = new ArrayList<>();
//	
//	if(x.length()!=y.length()) {x=extend(x); y=extend(y);}
//	
//	for(int i = x.length()-1;i>=0;i--)
//		{
//		if(ls.size()<16) {
//			if(Character.getNumericValue(x.charAt(i))-Character.getNumericValue(y.charAt(i))<='0')
//				ls.add(0,"0");
//			else if (Character.getNumericValue(x.charAt(i))-Character.getNumericValue(y.charAt(i))=='1')
//				ls.add(0,"1");
//			}
//		}
//	return ls.toString().replaceAll("\\[","").replaceAll("]", "").replaceAll(", ", "");	
//}

    public String sub(String x, String y) {
        if (x.length() != y.length()) {
            extend(x, 32);
            extend(y, 32);
        }
        int result = toInteger(x) - toInteger(y);
        return extend(Integer.toBinaryString(result), 32);
    }

    public String functionInBinary(String x) {
        //Jump
        if (x.equalsIgnoreCase("jmp"))
            return "111";
            //Arithmetic
        else if (x.equalsIgnoreCase("add"))
            return "000";
        else if (x.equalsIgnoreCase("sub"))
            return "001";
            //Memory
        else if (x.equalsIgnoreCase("set"))
            return "110";
        else if (x.equalsIgnoreCase("get"))
            return "100";
        else if (x.equalsIgnoreCase("gti"))
            return "101";
            //Branch
        else if (x.equalsIgnoreCase("bgt"))
            return "010";
        else if (x.equalsIgnoreCase("beq"))
            return "011";
        else
            return "000";
    }

    public String flip(String x, int ind) { //used in two's complement
        String result = "";
        for (int i = 0; i < ind; i++) {
            if (x.charAt(i) == '0')
                result += "1";
            else
                result += "0";
        }
        return result += x.substring(ind);
    }

    public String twosComplement(String x) {
        int lastind = x.lastIndexOf('1');
        return flip(x, lastind);
    }

    public int toInteger(String x) {
        int result = 0;
        boolean negative = false;
        if (x.length() == 32 && x.charAt(0) == '1') {
            x = twosComplement(x);
            negative = true;
        }
        for (int i = 0; i < x.length(); i++)
            result += Math.pow(2, x.length() - 1 - i) * Character.getNumericValue(x.charAt(i));
        if (negative == true)
            return result * -1;
        return result;
    }

    public void fill(String bin, String[] mem, int start) { //to save in memory
        int x = 0;
        for (int i = start; i < start + 32; i++)
            mem[i] = bin.charAt(x++) + "";
    }

    public String load(String[] mem, int start) {
        String x = "";
        for (int i = start; i < start + 32; i++)
            x += mem[i];
        return x;
    }

    public String toBinary(String line) {
        String result = "";
        String x[] = line.split("\\s+");
        String funct = x[0];
        if (funct.equalsIgnoreCase("stop;"))
            return "1111111111111111";
        funct = functionInBinary(x[0]);
        result += funct;
        int indexofsemi = 0;

        if (funct.equals("000") || funct.equals("001")) //add sub
        {
            result += extend(Integer.toBinaryString(Character.getNumericValue(x[1].charAt(1))), 3) + //adds register 1 in binary
                    extend(Integer.toBinaryString(Character.getNumericValue(x[3].charAt(1))), 3) + //adds register 2 in binary
                    extend(Integer.toBinaryString(Character.getNumericValue(x[5].charAt(1))), 3) + "0000";
        } //adds register 3 in binary

        else if (funct.equals("100") || funct.equals("101")) //get
            result += extend(Integer.toBinaryString(Character.getNumericValue(x[3].charAt(1))), 3)
                    + extend(Integer.toBinaryString(Integer.parseInt(x[1])), 10); //adds address + register
        else if (funct.equals("110")) {//set
            indexofsemi = x[3].indexOf(';');
            result += extend(Integer.toBinaryString(Character.getNumericValue(x[1].charAt(1))), 3) + extend(Integer.toBinaryString(Integer.parseInt(x[3].substring(0, indexofsemi))), 10); //adds register + address
        } else if (funct.equals("010") || funct.equals("011"))//beq bgt
        {
            indexofsemi = x[5].indexOf(';');
            result += extend(Integer.toBinaryString(Character.getNumericValue(x[1].charAt(1))), 3) + //adds register 1 in binary
                    extend(Integer.toBinaryString(Character.getNumericValue(x[3].charAt(1))), 3) +//adds register 2 in binary
                    extend(Integer.toBinaryString(Integer.parseInt(x[5].substring(0, indexofsemi))), 7);
        } else if (funct.equals("111")) {
            indexofsemi = x[2].indexOf(';');
            result += extend(Integer.toBinaryString(Integer.parseInt(x[2].substring(0, indexofsemi))), 13);
        }
        return result;
    }
}
