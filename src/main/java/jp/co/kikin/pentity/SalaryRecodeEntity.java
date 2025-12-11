package jp.co.kikin.pentity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="t_monthly_salary")
@IdClass(SalaryRecodeId.class)
@Data
public class SalaryRecodeEntity {

	@Id
	@Column(name = "employee_id")
	private String employeeId;
	
	@Id
	@Column(name = "year_month")
	private String yearMonth;
	
	@Column(name = "working_day")
	private int workingday;
	
	@Column(name = "total_work_hours")
	private int totalworkhours;
	
	@Column(name = "night_hours")
	private int nighthours;
	
	@Column(name = "overtime_hours")
	private int overtimehours;
	
	@Column(name = "other")
	private int other;

}