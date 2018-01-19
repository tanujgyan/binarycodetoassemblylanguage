import java.util.ArrayList;

//This is the entity class for MIPS instruction
public class MIPSInstruction {
	private String opcode;
	private String rs;
	private String rt;
	private String destination;
	private String offset;
	private String shamt;
	private String fucnt;
	private String addressForJType;
	private String addressForIType;
	private String targetForJType;
	
	//nir
	static ArrayList<String> mipsCode= new ArrayList<String>();
	//
	public String getOpcode() {
		return opcode;
	}
	public void setOpcode(
			String opcode) {
		this.opcode = opcode;
	}
	public String getRS() {
		return rs;
	}
	public void setRS(
			String source) {
		this.rs = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(
			String destination) {
		this.destination = destination;
	}
	public String getOffset() {
		return offset;
	}
	public void setOffset(
			String offset) {
		this.offset = offset;
	}
	public String getShamt() {
		return shamt;
	}
	public void setShamt(
			String shamt) {
		this.shamt = shamt;
	}
	public String getRt() {
		return rt;
	}
	public void setRt(
			String rt) {
		this.rt = rt;
	}
	public String getFucnt() {
		return fucnt;
	}
	public void setFucnt(
			String fucnt) {
		this.fucnt = fucnt;
	}
	public String getAddressForJType() {
		return addressForJType;
	}
	public void setAddressForJType(
			String addressForJType) {
		this.addressForJType = addressForJType;
	}
	public String getAddressForIType() {
		return addressForIType;
	}
	public void setAddressForIType(
			String addressForIType) {
		this.addressForIType = addressForIType;
	}
	public String getTargetForJType() {
		return targetForJType;
	}
	public void setTargetForJType(String targetForJType) {
		this.targetForJType = targetForJType;
	}
}
