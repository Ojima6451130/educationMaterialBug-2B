package jp.co.kikin.dto;

public class ActualWorkBaseDto {
	private String startTime;
	private String endTime;
	private String shiftStartTime;
	private String shiftEndTime;
	private String breakTime;
	private boolean noShift;
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getShiftStartTime() {
		return shiftStartTime;
	}
	public void setShiftStartTime(String shiftStartTime) {
		this.shiftStartTime = shiftStartTime;
	}
	public String getShiftEndTime() {
		return shiftEndTime;
	}
	public void setShiftEndTime(String shiftEndTime) {
		this.shiftEndTime = shiftEndTime;
	}
	public String getBreakTime() {
		return breakTime;
	}
	public void setBreakTime(String breakTime) {
		this.breakTime = breakTime;
	}
	public boolean isNoShift() {
		return noShift;
	}
	public void setNoShift(boolean noShift) {
		this.noShift = noShift;
	}

}
