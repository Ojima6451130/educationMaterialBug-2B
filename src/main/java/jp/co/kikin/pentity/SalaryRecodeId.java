package jp.co.kikin.pentity;

import java.io.Serializable;

import lombok.Data;

@Data
public class SalaryRecodeId implements Serializable{
	private String employeeId;
	private String yearMonth;

}
