package nut.data.shift;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class WorkDay {
	private final int id;	 
	private static final int normalFinishHour = 18;
	private static final int normalStartHour = 6;
	private static final double normalWorkingDayHours = 8;
	private final LocalDateTime normalFinish;
	private final LocalDateTime normalStart;

	private final ArrayList<WorkShift> shifts;
	
	private double normalWorkingHours = 0;
	private double eveningWorkingHours = 0;
	
	public WorkDay(int id, LocalDateTime date) {
		this.id = id;		 
		this.normalStart = date.withHour(normalStartHour).withMinute(0);
		this.normalFinish = date.withHour(normalFinishHour).withMinute(0);
		shifts = new ArrayList<WorkShift>();		 
	}

	public void addShift(LocalDateTime shiftStart, LocalDateTime shiftFinish) {
		for (WorkShift shift : shifts){
			if (shift.isOverlaped(shiftStart, shiftFinish)){
				shifts.add(new WorkShift(shiftStart, shiftFinish, WorkShiftType.Error));
				return;
			}
		}
		addShiftInternal(shiftStart, shiftFinish);
	}
	
	private void addShiftInternal(LocalDateTime shiftStart, LocalDateTime shiftFinish) {
		if (shiftStart.isBefore(normalStart)){
			if (shiftFinish.isAfter(normalStart)){
				addShiftInternal(shiftStart, normalStart);				 
			}
			else{
				addEveningShift(shiftStart, shiftFinish);
			}
			shiftStart = normalStart;
		}
		else{
			if (shiftStart.isBefore(normalFinish)) {
				if (shiftFinish.isAfter(normalFinish)){
					addShiftInternal(shiftStart, normalFinish);					 
				}
				else{
					addNormalShift(shiftStart, shiftFinish);					 
				}
				shiftStart = normalFinish;
			}
			else{
				addEveningShift(shiftStart, shiftFinish);
				shiftStart = shiftFinish;
			}			 
		}
		if (shiftStart.isBefore(shiftFinish)){
			addShiftInternal(shiftStart, shiftFinish);
		}
	}

	private void addNormalShift(LocalDateTime shiftStart, LocalDateTime shiftFinish) {
		WorkShift workShift = new WorkShift(shiftStart, shiftFinish, WorkShiftType.Normal);
		shifts.add(workShift);
		normalWorkingHours += workShift.getHours();
	}

	private void addEveningShift(LocalDateTime shiftStart, LocalDateTime shiftFinish){
		WorkShift workShift = new WorkShift(shiftStart, shiftFinish, WorkShiftType.Evening);
		shifts.add(workShift);
		eveningWorkingHours += workShift.getHours();
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
		return normalWorkingHours + eveningWorkingHours > normalWorkingDayHours ? 
				(normalWorkingHours + eveningWorkingHours) - normalWorkingDayHours : 0;
	}

	public LocalDateTime getDate() {
		return normalStart;
	}
}
