package nut.data.shift;

import java.time.LocalDateTime;
import nut.util.*;

/**
 * Work shift
 * @author Владимир
 *
 */
public class WorkShift {
	private final LocalDateTime finish;
	/**
	 * TYpe of shift: normal, evening, overtime etc.
	 */
	private WorkShiftType shiftType;
	private final LocalDateTime start;
	
	public WorkShift(LocalDateTime start, LocalDateTime finish, WorkShiftType shiftType){
		this.start = start;
		this.finish = finish;
		this.shiftType = shiftType;
	}
	
	public double duration(){
		return TimeUtils.getHoursBetween(start, finish);
	}
	
	public LocalDateTime getFinish() {		 
		return finish;
	}

	public LocalDateTime getStart() {	 
		return start;
	}

	public WorkShiftType getType() {
		return shiftType;
	}

	/**
	 * Returns type, if this shift is overlapped
	 * @param start
	 * @param finish
	 * @return
	 */
	public boolean isOverlaped(LocalDateTime start, LocalDateTime finish){
		return ((TimeUtils.isAfterOrEqual(start, this.start) && start.isBefore(this.finish))
				|| (finish.isAfter(this.start) && TimeUtils.isBeforeOrEqual(finish, this.finish)));
	}
	
	/**
	 * Used, if it's required to change type of the shift
	 * @param shiftType
	 */
	public void setType(WorkShiftType shiftType) {
		this.shiftType = shiftType;
	}
}
