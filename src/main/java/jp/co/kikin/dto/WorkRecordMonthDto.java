package jp.co.kikin.dto;

public class WorkRecordMonthDto {
	/** 年月 */
    private String yearMonth;
    /** 社員ID */
    private String employeeId;
	
    /** 勤労日数*/
    private Integer totalWorkingDays;
    /** 総就業時間*/
    private Integer totalWorkMinutes;
    /** 総残業時間*/
    private Integer totalOverMinutes;
    /** 夜間時間*/
    private Integer totalNightMinutes;
    
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public Integer getTotalWorkingDays() {
		return totalWorkingDays;
	}
	public void setTotalWorkingDays(Integer totalWorkingDays) {
		this.totalWorkingDays = totalWorkingDays;
	}
	public Integer getTotalWorkMinutes() {
		return totalWorkMinutes;
	}
	public void setTotalWorkMinutes(Integer totalWorkMinutes) {
		this.totalWorkMinutes = totalWorkMinutes;
	}
	public Integer getTotalOverMinutes() {
		return totalOverMinutes;
	}
	public void setTotalOverMinutes(Integer totalOverMinutes) {
		this.totalOverMinutes = totalOverMinutes;
	}
	public Integer getTotalNightMinutes() {
		return totalNightMinutes;
	}
	public void setTotalNightMinutes(Integer totalNightMinutes) {
		this.totalNightMinutes = totalNightMinutes;
	}
}
