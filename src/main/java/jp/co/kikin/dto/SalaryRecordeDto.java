package jp.co.kikin.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jp.co.kikin.pentity.SalaryRecodeEntity;

public class SalaryRecordeDto {
	
	//社員ID
	private SalaryRecodeEntity salaryRecodeEntity;
	
	//その他
	private int otherPrice;

	public SalaryRecodeEntity getSalaryRecodeEntity() {
		return salaryRecodeEntity;
	}

	public void setSalaryRecodeEntity(SalaryRecodeEntity salaryRecodeEntity) {
		this.salaryRecodeEntity = salaryRecodeEntity;
	}

	public int getOtherPrice() {
		return otherPrice;
	}

	public void setOtherPrice(int otherPrice) {
		this.otherPrice = otherPrice;
	}
	
	

}
