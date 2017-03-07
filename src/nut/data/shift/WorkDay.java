package nut.data.shift;

import java.time.LocalDateTime;
import java.util.ArrayList;

import nut.util.TimeUtils;

public class WorkDay {
	private final int id;	 
	private static final int normalFinishHour = 18;
	private static final int normalStartHour = 6;
	private static final double normalWorkingDay = 8;
	private final LocalDateTime normalFinish;
	private final LocalDateTime normalStart;

	private final ArrayList<WorkShift> shifts;
	private final ArrayList<WorkShift> shiftsWithErrors;
	
	private double normalWorkingHours = 0;
	private double eveningWorkingHours = 0;
	
	public WorkDay(int id, LocalDateTime date) {
		this.id = id;		 
		this.normalStart = date.withHour(normalStartHour).withMinute(0);
		this.normalFinish = date.withHour(normalFinishHour).withMinute(0);
		shifts = new ArrayList<WorkShift>();
		shiftsWithErrors = new ArrayList<WorkShift>();
	}

	public void addShift(LocalDateTime shiftStart, LocalDateTime shiftFinish) {
		for (WorkShift shift : shifts){
			if (shift.isOverlaped(shiftStart, shiftFinish)){
				shiftsWithErrors.add(new WorkShift(shiftStart, shiftFinish));
				return;
			}
		}
		shifts.add(new WorkShift(shiftStart, shiftFinish));
		addHours(shiftStart, shiftFinish);
	}

	private void addHours(LocalDateTime shiftStart, LocalDateTime shiftFinish) {
		if (shiftStart.isBefore(normalStart)){
			if (shiftFinish.isAfter(normalStart)){
				eveningWorkingHours += TimeUtils.getHoursBetween(shiftStart, normalStart);
				shiftStart = normalStart;
			}
			else{
				eveningWorkingHours += TimeUtils.getHoursBetween(shiftStart, shiftFinish);			 
			}		 
		}
		
		if (TimeUtils.isAfterOrEqual(shiftStart, normalStart) && shiftStart.isBefore(normalFinish)){
			if (shiftFinish.isAfter(normalFinish)){
				normalWorkingHours += TimeUtils.getHoursBetween(shiftStart, normalFinish);
				shiftStart = normalFinish;
			}
			else{
				normalWorkingHours += TimeUtils.getHoursBetween(shiftStart, shiftFinish);				 
			}			 
		}
		
		if (TimeUtils.isAfterOrEqual(shiftStart, normalFinish) && shiftStart.isBefore(shiftFinish)){
			eveningWorkingHours += TimeUtils.getHoursBetween(shiftStart, shiftFinish);
		}		
	}
	
	public double getNormalHours(){
		return normalWorkingHours;
	}
	
	public double getEveningHours(){
		return eveningWorkingHours;
	}

	public int getEmployeeId() {		 
		return id;
	}

	public double getOvertimeHours() {		 
		return normalWorkingHours + eveningWorkingHours > normalWorkingDay ? 
				(normalWorkingHours + eveningWorkingHours) - normalWorkingDay : 0;
	}

	public LocalDateTime getDate() {
		return normalStart;
	}
}
