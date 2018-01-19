import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Translator {
	public static ArrayList<MIPSInstruction> mips =new  ArrayList<MIPSInstruction>();
	Map<String, Integer>  pos =new HashMap<String, Integer>();

	public int labelCount=0;
	String mipsCodeLine="";
	public ArrayList<String> assemblyLanguageCode = null;
	public String exitLabel = "exit:";
	public int exitLabelPosition =0;
	public void TranslateBinaryToAssembly(ArrayList<String> binaryMIPSCodeList) {
		assemblyLanguageCode = new ArrayList<String>();
		for(String s: binaryMIPSCodeList)
		{
			String opcode = s.substring(0,6);
			//R Type Instruction
			switch(opcode)
			{
				case "000000":
					TranslateBinaryToAssemblyForRType(s);
					mipsCodeLine ="";
					break;
					//aadi
				case "001000":
					//addiu
				case"001001":
					//andi
				case "001100":
					//beq
				case "000100":
					//bgez	
				case "000001":
					//bgtz
				case "000111":
					//blez
				case "000110":
					//bltz
					//check this case
					//case "000001":
					//
				case "000101":
					//lb
				case "100000":
					//lbu
				case "100100":
					//lh
				case "100001":
					//lhu
				case "100101":
					//lui
				case "001111":
					//lw
				case "100011":
					//lwcl
				case "110001":
					//ori
				case "001101":
					//sb
				case "101000":
					//slti
				case "001010":
					//sh
				case "001011":
					//sw
				case "101011":
					//swcl
				case "111001":
					//xori
				case "001110":
					TranslateBinaryToAssemblyForIType(s);
					mipsCodeLine ="";
					break;
					//j 
				case "000010":
					//jal
				case "000011":
					TranslateBinaryToAssemblyForJType(s);
					mipsCodeLine ="";
					break;
				default:
					//change it later
					mipsCodeLine = "Invalid instruction";
					assemblyLanguageCode.add(mipsCodeLine);
					
					//System.out.println("Invalid instruction");
					break;
			}

			

		}
		InsertLabels();
		CreateAssemblyLanguageFile(assemblyLanguageCode);
		_System.fileCount++;
		/*for(String s1: assemblyLanguageCode)
		{
			
			//System.out.println(s1);
			//CreateAssemblyLanguageFile(s1);
			
		}*/
	}
	
	public void CreateAssemblyLanguageFile(ArrayList<String> s)
	{
		String s1="bin"+_System.fileCount+".txt";
		Path file=Paths.get(s1);
		try {
			Files.write(file, s, Charset.forName("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void TranslateBinaryToAssemblyForIType(String binaryMIPSCode)
	{

		MIPSInstruction m =new MIPSInstruction();
		//Read the binary string and extract opcode, source, destination, offset

		m.setOpcode(binaryMIPSCode.substring(0, 6));
		m.setRS(binaryMIPSCode.substring(6,11));
		m.setDestination(binaryMIPSCode.substring(11,16));
		m.setOffset((binaryMIPSCode.substring(16,32)));
		switch(m.getOpcode())
		{
			case "001000":

				mipsCodeLine = mipsCodeLine.concat(" addi ");
				break;
			case"001001":

				mipsCodeLine = mipsCodeLine.concat(" addiu ");
				break;
			case "001100":

				mipsCodeLine = mipsCodeLine.concat(" andi ");
				break;
			case "000100":

				mipsCodeLine = mipsCodeLine.concat(" beq ");
				//GetTheLabel("beq", BinaryToDecimalConvertor(m.getOffset()));
				break;
			case "000001":
				if(m.getRS().equals("00001"))
				{
					mipsCodeLine = mipsCodeLine.concat(" bgez ");
					break;
				}
				else if(m.getRS().equals("00000"))
				{
					mipsCodeLine = mipsCodeLine.concat(" bltz ");
					break;
				}
			case "000111":
				if(m.getRS().equals("00000"))
				{
					mipsCodeLine = mipsCodeLine = mipsCodeLine.concat(" bgtz ");
					break;
				}
			case "000110":
				if(m.getRS().equals("00000"))
				{
					mipsCodeLine = mipsCodeLine.concat(" blez ");
					break;
				}





			case "000101":


				/*String label = Long.toString(BinaryToDecimalConvertor(m.getOffset()));
				int lineNumber = Integer.parseInt(label);
				int len  = assemblyLanguageCode.size();
				//assemblyLanguageCode.add(len+1+lineNumber, "exit:");
				exitLabelPosition = len+1+lineNumber;*/
				mipsCodeLine = mipsCodeLine.concat(" bne ");
				break;
			case "100000":

				mipsCodeLine = mipsCodeLine.concat(" lb ");
				break;
			case "100100":

				mipsCodeLine = mipsCodeLine.concat(" lbu ");
				break;
			case "100001":

				mipsCodeLine = mipsCodeLine.concat(" lh ");
				break;
			case "100101":

				mipsCodeLine = mipsCodeLine.concat(" lhu ");
				break;
			case "001111":

				mipsCodeLine = mipsCodeLine.concat(" lui ");
				break;
			case "100011":

				mipsCodeLine = mipsCodeLine.concat(" lw ");
				break;
			case "110001":

				mipsCodeLine = mipsCodeLine.concat(" lwc1 ");
				break;
			case "001101":

				mipsCodeLine = mipsCodeLine.concat(" ori ");
				break;
			case "101000":

				mipsCodeLine = mipsCodeLine.concat(" sb ");
				break;
			case "001010":

				mipsCodeLine = mipsCodeLine.concat(" slti ");
				break;
			case "001011":

				mipsCodeLine = mipsCodeLine.concat(" sltiu ");
				break;
			case "101001":

				mipsCodeLine = mipsCodeLine.concat(" sh ");
				break;
			case "101011":

				mipsCodeLine = mipsCodeLine.concat(" sw ");
				break;
			case "111001":

				mipsCodeLine = mipsCodeLine.concat(" swc1 ");
				break;
			case "001110":
				mipsCodeLine = mipsCodeLine.concat(" xori ");
				break;
		}

		mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
		mipsCodeLine = mipsCodeLine.concat(", ");
		//lw and sw
		if(m.getOpcode().equals("101011")||m.getOpcode().equals("100011"))
		{
			mipsCodeLine = mipsCodeLine.concat(Long.toString(BinaryToDecimalConvertor(m.getOffset(),"")));
			mipsCodeLine = mipsCodeLine.concat("(");
			mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
			mipsCodeLine = mipsCodeLine.concat(")");
		}
		//lui
		else if(m.getOpcode().equals("001111"))
		{
			mipsCodeLine = mipsCodeLine.concat(Long.toString(BinaryToDecimalConvertor(m.getOffset(),"TranslateBinaryToAssemblyForIType")));
		}
		else
		{
			mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
			mipsCodeLine = mipsCodeLine.concat(", ");
			int offset = (int) BinaryToDecimalConvertor(m.getOffset(),"TranslateBinaryToAssemblyForIType");
			if(!(m.getOpcode().equals("000101")|| m.getOpcode().equals("000100")||m.getOpcode().equals("000110")))
			{
				if(m.getOpcode().equals("101011")||m.getOpcode().equals("100011"))
				{

				}
				else
				{
					mipsCodeLine = mipsCodeLine.concat(Long.toString(BinaryToDecimalConvertor(m.getOffset(),"TranslateBinaryToAssemblyForIType")));
				}
			}
			else if(m.getOpcode().equals("000101")|| m.getOpcode().equals("000100")|| m.getOpcode().equals("000110"))
			{
				mipsCodeLine = mipsCodeLine.concat(GetTheLabel(m.getOpcode(), offset));
			}
		}
		//add to assembly language program
		assemblyLanguageCode.add(mipsCodeLine);

	}

	public void TranslateBinaryToAssemblyForRType(String binaryMIPSCode)
	{
		MIPSInstruction m =new MIPSInstruction();
		//Read the binary string and extract opcode, source, destination, offset
		m.setFucnt(binaryMIPSCode.substring(26,32));
		m.setShamt(binaryMIPSCode.substring(21,26));
		m.setDestination(binaryMIPSCode.substring(16,21));
		m.setRt(binaryMIPSCode.substring(11,16));
		m.setRS(binaryMIPSCode.substring(6,11));
		m.setOpcode(binaryMIPSCode.substring(0,6));


		switch(m.getFucnt())
		{
			case "100000": 

				mipsCodeLine = mipsCodeLine.concat(" add ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				break;

			case "100001": 

				mipsCodeLine = mipsCodeLine.concat(" addu ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				break;

			case "100100": 

				mipsCodeLine = mipsCodeLine.concat(" and ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				break;
			case "011010": 
				mipsCodeLine = mipsCodeLine.concat(" div ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				break;

			case "011011": 
				mipsCodeLine = mipsCodeLine.concat(" divu ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				break;
			case "011000": 

				mipsCodeLine = mipsCodeLine.concat(" mult ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				break;
			case "011001": 

				mipsCodeLine = mipsCodeLine.concat(" multu ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				break;

			case "000000": 

				mipsCodeLine = mipsCodeLine.concat(" sll ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(Long.toString(BinaryToDecimalConvertor(m.getShamt(),"TranslateBinaryToAssemblyForIType")));
				break;

			case "100111":	

				mipsCodeLine = mipsCodeLine.concat(" nor ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				break;
			case "100101":	

				mipsCodeLine = mipsCodeLine.concat(" or ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				break;
			case "100110":	

				mipsCodeLine = mipsCodeLine.concat(" xor ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				break;
			case "100010": 

				mipsCodeLine = mipsCodeLine.concat(" sub ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				break;	
			case "100011": 

				mipsCodeLine = mipsCodeLine.concat(" subu ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				break;
			case "001000":
				mipsCodeLine = mipsCodeLine.concat(" jr ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				break;
			case "010000":
				mipsCodeLine = mipsCodeLine.concat(" mfhi ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				break;	
			case "001001":
				mipsCodeLine = mipsCodeLine.concat(" jalr ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				break;
			case "010010":
				mipsCodeLine = mipsCodeLine.concat(" mflo ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				break;
			case "010001":
				mipsCodeLine = mipsCodeLine.concat(" mthi ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				break;	
			case "010011":
				mipsCodeLine = mipsCodeLine.concat(" mtlo ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				break;	
			case "000100": 

				mipsCodeLine = mipsCodeLine.concat(" sllv ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				break;
			case "101010": 

				mipsCodeLine = mipsCodeLine.concat(" slt ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				break;
			case "101011": 

				mipsCodeLine = mipsCodeLine.concat(" sltu ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				break;
			case "000011": 

				mipsCodeLine = mipsCodeLine.concat(" sra ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(Long.toString(BinaryToDecimalConvertor(m.getShamt(),"TranslateBinaryToAssemblyForIType")));
				break;
			case "000111": 

				mipsCodeLine = mipsCodeLine.concat(" srav ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				break;
			case "000010": 

				mipsCodeLine = mipsCodeLine.concat(" srl ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(Long.toString(BinaryToDecimalConvertor(m.getShamt(),"TranslateBinaryToAssemblyForIType")));
				break;

			case "000110": 

				mipsCodeLine = mipsCodeLine.concat(" srlv ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getDestination()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRt()));
				mipsCodeLine = mipsCodeLine.concat(", ");
				mipsCodeLine = mipsCodeLine.concat(getRegisterName(m.getRS()));
				break;
			case "001100": 

				mipsCodeLine = mipsCodeLine.concat(" syscall ");
				break;

			default:
				mipsCodeLine = mipsCodeLine.concat(" error ");	
				break;
		}
		//add to assembly language program
		assemblyLanguageCode.add(mipsCodeLine);


	}

	public void TranslateBinaryToAssemblyForJType(String binaryMIPSCode)
	{
		MIPSInstruction m =new MIPSInstruction();
		//Read the binary string and extract opcode, source, destination, offset
		m.setTargetForJType(binaryMIPSCode.substring(6,32));
		m.setOpcode(binaryMIPSCode.substring(0,6));
		switch(m.getOpcode())
		{
			case "000010":
				String label = GetTheLabel("j", m.getTargetForJType());
				mipsCodeLine = mipsCodeLine.concat(" j " + label);
			case "000011":
				String label1 = GetTheLabel("jal", m.getTargetForJType());
				mipsCodeLine = mipsCodeLine.concat(" jal " + label1);

		}
		//add to assembly language program
		assemblyLanguageCode.add(mipsCodeLine);


	}
	private long BinaryToDecimalConvertor(String binarynum, String callingFunction)
	{
		int decimal = Integer.parseInt(binarynum,2);
		//if the immediate value goes beyond range. range is 2^15 -1
		//this is implemented to handle negative immediate values
		if(callingFunction.equals("TranslateBinaryToAssemblyForIType"))
		{
			if(decimal>32767)
			{
				while(true)
				{
					int temp = decimal - 32768;
					if(temp>32767)
					{
						continue;
					}
					else
					{
						decimal = -(32768 - temp);
						break;
					}
				}
			}
		}
		
		return decimal;
	}
	private String getRegisterName(String binaryText)
	{
		String mipsCodeLine ="";
		//get the register name from binary address
		switch(binaryText)
		{
			case "00000":
				mipsCodeLine = mipsCodeLine.concat(" $0 ");
				break;
			case "00001":
				mipsCodeLine = mipsCodeLine.concat(" $at ");
				break;
			case "00010":
				mipsCodeLine = mipsCodeLine.concat(" $v0 ");
				break;
			case "00011":
				mipsCodeLine = mipsCodeLine.concat(" $v1 ");
				break;
			case "00100":
				mipsCodeLine = mipsCodeLine.concat(" $a0 ");
				break;
			case "00101":
				mipsCodeLine = mipsCodeLine.concat(" $a1 ");
				break;
			case "00110":
				mipsCodeLine = mipsCodeLine.concat(" $a2 ");
				break;
			case "00111":
				mipsCodeLine = mipsCodeLine.concat(" $a3 ");
				break;
			case "01000":
				mipsCodeLine = mipsCodeLine.concat(" $t0 ");
				break;
			case "01001":
				mipsCodeLine = mipsCodeLine.concat(" $t1 ");
				break;
			case "01010":
				mipsCodeLine = mipsCodeLine.concat(" $t2 ");
				break;
			case "01011":
				mipsCodeLine = mipsCodeLine.concat(" $t3 ");
				break;
			case "01100":
				mipsCodeLine = mipsCodeLine.concat(" $t4 ");
				break;
			case "01101":
				mipsCodeLine = mipsCodeLine.concat(" $t5 ");
				break;
			case "01110":
				mipsCodeLine = mipsCodeLine.concat(" $t6 ");
				break;
			case "01111":
				mipsCodeLine = mipsCodeLine.concat(" $t7 ");
				break;
			case "10000":
				mipsCodeLine = mipsCodeLine.concat(" $s0 ");
				break;
			case "10001":
				mipsCodeLine = mipsCodeLine.concat(" $s1 ");
				break;
			case "10010":
				mipsCodeLine = mipsCodeLine.concat(" $s2 ");
				break;
			case "10011":
				mipsCodeLine = mipsCodeLine.concat(" $s3 ");
				break;
			case "10100":
				mipsCodeLine = mipsCodeLine.concat(" $s4 ");
				break;
			case "10101":
				mipsCodeLine = mipsCodeLine.concat(" $s5 ");
				break;
			case "10110":
				mipsCodeLine = mipsCodeLine.concat(" $s6 ");
				break;
			case "10111":
				mipsCodeLine = mipsCodeLine.concat(" $s7 ");
				break;
			case "11000":
				mipsCodeLine = mipsCodeLine.concat(" $t8 ");
				break;
			case "11001":
				mipsCodeLine = mipsCodeLine.concat(" $t9 ");
				break;
			case "11010":
				mipsCodeLine = mipsCodeLine.concat(" $k0 ");
				break;
			case "11011":
				mipsCodeLine = mipsCodeLine.concat(" $k1 ");
				break;
			case "11100":
				mipsCodeLine = mipsCodeLine.concat(" $gp ");
				break;
			case "11101":
				mipsCodeLine = mipsCodeLine.concat(" $sp ");
				break;
			case "11110":
				mipsCodeLine = mipsCodeLine.concat(" $fp ");
				break;
			case "11111":
				mipsCodeLine = mipsCodeLine.concat(" $ra ");
				break;
			default:
				mipsCodeLine = mipsCodeLine.concat(" error ");
				break;

		}
		return mipsCodeLine;

	}
	//when address is passed
	public String GetTheLabel(String callingFunction, String val)
	{
		String loop = "loop" + Math.random();
		int baseAddress = 1048577;
		int targetAddress = (int) BinaryToDecimalConvertor(val,"TranslateBinaryToAssemblyForJType");
		int difference = targetAddress-baseAddress;
		//add label
		//assemblyLanguageCode.add(difference+1, "loop:");
		//increment label count
		labelCount++;
		pos.put(loop, difference+1);
		return loop;
	}
	//when offset is passed
	public String GetTheLabel(String callingFunction, long val)
	{
		String label ="label"+Math.random();
		labelCount++;
		int currentCountOfLines = assemblyLanguageCode.size();
		pos.put(label, (int) (currentCountOfLines+val+1));
		return label;

	}
	public void InsertLabels()
	{
		pos = sortByValue(pos);
		int count =0;
		try
		{
		Iterator it = pos.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			// System.out.println(pair.getKey() + " = " + pair.getValue());
			assemblyLanguageCode.add((int)pair.getValue()+count,(String)pair.getKey()+":");
			it.remove(); // avoids a ConcurrentModificationException
			count++;
		}
		}
		catch(Exception e)
		{
			assemblyLanguageCode.add("");
		}
	}
	//sort by value
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		return map.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(/*Collections.reverseOrder()*/))
				.collect(Collectors.toMap(
						Map.Entry::getKey, 
						Map.Entry::getValue, 
						(e1, e2) -> e1, 
						LinkedHashMap::new
						));
	}
}