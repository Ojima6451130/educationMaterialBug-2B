package jp.co.kikin.dto;

public class SalaryInfoDto {

	/** 役職 */
	private String postName;
	/** 基本給 */
	private int basicSalary;
	/** 夜間 */
	private int nightPrice;
	/** 残業 */
	private int overtimePrice;
	/** 特別手当 */
	private int otherPrice;
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public int getBasicSalary() {
		return basicSalary;
	}
	public void setBasicSalary(int basicSalary) {
		this.basicSalary = basicSalary;
	}
	public int getNightPrice() {
		return nightPrice;
	}
	public void setNightPrice(int nightPrice) {
		this.nightPrice = nightPrice;
	}
	public int getOvertimePrice() {
		return overtimePrice;
	}
	public void setOvertimePrice(int overtimePrice) {
		this.overtimePrice = overtimePrice;
	}
	public int getOtherPrice() {
		return otherPrice;
	}
	public void setOtherPrice(int otherPrice) {
		this.otherPrice = otherPrice;
	}

}