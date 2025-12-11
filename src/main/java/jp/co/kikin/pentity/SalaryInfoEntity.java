package jp.co.kikin.pentity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="m_salary_Info")
@Data
public class SalaryInfoEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_ID")
	private String post_ID;
	
	@Column(name = "basic_salary")
	private int basic_salary;
	
	@Column(name = "night_price")
	private int night_price;
	
	@Column(name = "overtime_price")
	private int overtime_price;
	
	@Column(name = "other_price")
	private int other_price;
}
