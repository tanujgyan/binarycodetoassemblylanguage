import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class _System {
	private static String FILENAME=null;
	public static ArrayList<String> rawDataFromInputFile= new ArrayList<String>();
	public static ArrayList<String> rawDataFromInputFileRandom;
	private static Translator translator =new Translator();
	private _System s=new _System();
	public static int fileCount=0;
	public static void main (String[] args) 
	{
		//_System s=new _System();
		FILENAME="F:\\OSU Resources\\Architecture\\mips codes\\mips code and binary\\sample 4-simple array bin";
		ReadBinaryTextFile();
	}
	public static void ReadBinaryTextFile()
	{

		BufferedReader br = null;
		FileReader fr = null;

		try {

			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(FILENAME));

			//Read all jobs from binary text file to a variable 
			while ((sCurrentLine = br.readLine()) != null) {

				rawDataFromInputFile.add(sCurrentLine);
			}
			rawDataFromInputFileRandom=new ArrayList<String>(rawDataFromInputFile);
			translator.TranslateBinaryToAssembly(rawDataFromInputFile);
			for(int i=0;i<10;i++)
			{

				ArrayList<String> s = InjectRandomErrors();
				translator.TranslateBinaryToAssembly(s);
			}
			InjectInsecurities();
			InjectTwoInsecurities();
		} 
		catch (IOException e) {


			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}
	/**
	 * This method is used to inject random errors
	 */
	public static ArrayList<String> InjectRandomErrors()
	{
		ArrayList<String> s = new ArrayList<String>(rawDataFromInputFile);
		int count = s.size();
		int totalBits = count*32;
		//percentage of error to be injected
		int numberOfBitsToBeFlipped = (int) (0.1*totalBits);
		s = FlipBits(numberOfBitsToBeFlipped,s);
		return s;

	}

	/**
	 * To inject insecurity. this will target one opcode
	 */

	public static void InjectInsecurities()
	{
		ArrayList<String> str = new ArrayList<String>(rawDataFromInputFile);
		ArrayList<String> str1 = new ArrayList<String>(rawDataFromInputFile);
		ArrayList<String> str2 = new ArrayList<String>(rawDataFromInputFile);
		for(int i=0;i<str.size();i++)
		{
			String opcode = str.get(i).substring(26,32);
			//change add to div
			if(opcode.equals("100000"))
			{
				String s = str.get(i).substring(0,26)+ "011010";
				str.remove(i);
				str.add(i, s);
				//break;
			}
			
		}
		translator.TranslateBinaryToAssembly(str);	
		for(int i=0;i<str1.size();i++)
		{
			String opcode = str1.get(i).substring(26,32);
			//change sub to add
			if(opcode.equals("100010"))
			{
				String s = str1.get(i).substring(0,26)+ "100000";
				str1.remove(i);
				str1.add(i, s);
				//break;
			}
			
		}
		translator.TranslateBinaryToAssembly(str1);
		//replace ori with xori
		//for simple calculator program the result is not changing! This is a good news will help in drawing the graph
		for(int i=0;i<str2.size();i++)
		{
			String opcode = str2.get(i).substring(0,6);
			if(opcode.equals("001101"))
			{
				String s =  "001110"+str2.get(i).substring(6);
				str2.remove(i);
				str2.add(i, s);
				//break;
			}
			
		}
		translator.TranslateBinaryToAssembly(str2);
		
	}
	/**
	 * This method will inject multiple security
	 */
	public static void InjectTwoInsecurities()
	{
		ArrayList<String> str = new ArrayList<String>(rawDataFromInputFile);
		for(int i=0;i<str.size();i++)
		{
			String opcode = str.get(i).substring(0,6);
			//change addiu to addi
			if(opcode.equals("001001"))
			{
				String s =  "001000"+str.get(i).substring(6);
				str.remove(i);
				str.add(i, s);
				//break;
			}
			String opcode1 = str.get(i).substring(0,6);
			if(opcode1.equals("001101"))
			{
				String s =  "001110"+str.get(i).substring(6);
				str.remove(i);
				str.add(i, s);
				//break;
			}
			
		}
		translator.TranslateBinaryToAssembly(str);			
	}

	
	/**
	 * This method will flip the bits
	 * @param number
	 */
	public static ArrayList<String> FlipBits(int number,ArrayList<String> str)
	{

		for(int i=0;i<number;i++)
		{
			//generate a number between 1 and 50
			//int random = (int )(Math.random() * 50 + 1);
			int random = (int )(Math.random() * (rawDataFromInputFile.size()*32) );
			int row =0, column =0;
			int j=0;
			for(;j<rawDataFromInputFile.size()*32;j++)
			{
				row = (random/32);
				column = random%32;
				if(row<(rawDataFromInputFile.size()/32) && column<32)
				{
					break;
				}
			}
			String s="";
			try
			{
				s = rawDataFromInputFile.get(row);
			}
			//debug purpose only
			catch(Exception ex)
			{
				System.out.println(random);
				System.out.println(row);
				System.out.println(rawDataFromInputFile.size()*32);
				System.out.println(j);
			}
			char c='2';
			if(column!=0)
				c= s.charAt(column-1);
			else if(column==0)
			{
				c= s.charAt(column);
			}
			char replacedChar;
			if(c=='0')
			{
				replacedChar ='1';
			}
			else 
			{
				replacedChar ='0';
			}
			String s1="";
			if(column!=0)
				s1= s.substring(0, column-1)+replacedChar+s.substring(column);
			else
				s1 = replacedChar+s.substring(1);
			str.remove(row);
			str.add(row, s1);
		}
		System.out.println("------------------------------------------------------------------------------------------------");
		for(String s:rawDataFromInputFile)
		{
			System.out.println(s);
		}
		return str;
	}
}


