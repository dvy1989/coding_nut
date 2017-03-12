package nut.data.shift;

import java.time.LocalDateTime;
import nut.util.*;

public class WorkShift {
	private final LocalDateTime start;
	private final LocalDateTime finish;
	private WorkShiftType shiftType;
	
	public WorkShift(LocalDateTime start, LocalDateTime finish, WorkShiftType shiftType){
		this.start = start;
		this.finish = finish;
		this.shiftType = shiftType;
	}
	
	public boolean isOverlaped(LocalDateTime start, LocalDateTime finish){
		return ((TimeUtils.isAfterOrEqual(start, this.start) && start.isBefore(this.finish))
				|| (finish.isAfter(this.start) && TimeUtils.isBeforeOrEqual(finish, this.finish)));
	}
	
	public double getHours(){
		return TimeUtils.getHoursBetween(start, finish);
	}
}
