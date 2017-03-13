package nut.data.shift;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import nut.util.HourAndMinute;

public class WorkDay {
	private static final int normalFinishHour = 18;	 
	private static final int normalStartHour = 6;
	private static final double normalWorkingDayHours = 8;
	private final ArrayList<WorkShift> errorShifts;
	private double eveningWorkingHours = 0;
	/**
	 * ID of employee, for whom the given day
	 * is defined
	 */
	private final int id;

	private final LocalDateTime normalFinish;
	private final LocalDateTime normalStart;
	
	private double normalWorkingHours = 0;
	private final ArrayList<WorkShift> shifts;
	
	public WorkDay(int id, LocalDateTime date) {
		this.id = id;		 
		this.normalStart = date.withHour(normalStartHour).withMinute(0);
		this.normalFinish = date.withHour(normalFinishHour).withMinute(0);
		shifts = new ArrayList<WorkShift>();	
		errorShifts = new ArrayList<WorkShift>();
	}

	private void addEveningShift(LocalDateTime shiftStart, LocalDateTime shiftFinish){
		WorkShift workShift = new WorkShift(shiftStart, shiftFinish, WorkShiftType.Evening);
		shifts.add(workShift);
		eveningWorkingHours += workShift.duration();
	} 
	
	private void addNormalShift(LocalDateTime shiftStart, LocalDateTime shiftFinish) {
		WorkShift workShift = new WorkShift(shiftStart, shiftFinish, WorkShiftType.Normal);
		shifts.add(workShift);
		normalWorkingHours += workShift.duration();
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

	private int findAndSplit(int index, double hoursToSplit, WorkShiftType shiftType){		 
		double hoursSinceStart = 0, duration = 0;		 
		while (index < shifts.size()){
			duration = shifts.get(index).duration();
			if (hoursSinceStart + duration > hoursToSplit){
				splitShift(index, hoursToSplit, hoursSinceStart, shiftType);
				return index + 1;				 
			}
			else{
				hoursSinceStart += duration;
				index++;
			}
		}
		return index;
	}

	private void sortShifts(){
		shifts.sort(new Comparator<WorkShift>(){
			@Override
			public int compare(WorkShift item1, WorkShift item2){
				if (item1.getStart().isAfter(item2.getStart())){
					return 1;
				}
				if (item1.getStart().isBefore(item2.getStart())){
					return -1;
				}
				return 0;
			}
		});
	}
	
	private void splitShift(int index, double splitHours, double hoursSinceStart, WorkShiftType shiftType){
		double duration = shifts.get(index).duration();
		LocalDateTime start = shifts.get(index).getStart();
		LocalDateTime finish = shifts.get(index).getFinish();
		HourAndMinute shift = HourAndMinute.getHourAndMinute(duration - ((hoursSinceStart + duration) - splitHours));
		LocalDateTime tmp = start.plusHours(shift.Hours).plusMinutes(shift.Minutes);
		shifts.set(index, new WorkShift(start, tmp, shifts.get(index).getType()));
		shifts.add(index + 1, new WorkShift(tmp, finish, shiftType));
	}
	
	public void addShift(LocalDateTime shiftStart, LocalDateTime shiftFinish) {
		for (WorkShift shift : shifts){
			if (shift.isOverlaped(shiftStart, shiftFinish)){
				errorShifts.add(new WorkShift(shiftStart, shiftFinish, WorkShiftType.Error));
				return;
			}
		}
		addShiftInternal(shiftStart, shiftFinish);		 
	}

	/**
	 * Calculates overtimes
	 */
	//TODO Move to own class
	public void calculateOvertimes(){
		sortShifts();		
		int index = findAndSplit(0, normalWorkingDayHours, WorkShiftType.Overtime1);
		index = findAndSplit(index, 2, WorkShiftType.Overtime2);
		index = findAndSplit(index, 2, WorkShiftType.Overtime3);
		index = shifts.size() - 1;
		
		while (index > 1){
			if (shifts.get(index).getType().compareTo(shifts.get(index - 1).getType()) < 0){
				shifts.get(index).setType(shifts.get(index - 1).getType());
			}
			index--;
		}
		
		for (int i = 0; i < shifts.size(); i++){
			if (shifts.get(i).getStart().isEqual(shifts.get(i).getFinish())){
				shifts.remove(i);
				i--;
			}
		}
	}

	public LocalDateTime getDate() {
		return normalStart;
	}

	public int getEmployeeId() {		 
		return id;
	}
	
	public double getEveningHours(){
		return eveningWorkingHours;
	}
	
	public double getNormalHours(){
		return normalWorkingHours;
	}
	
	public double getOvertimeHours() {		 
		return normalWorkingHours + eveningWorkingHours > normalWorkingDayHours ? 
				(normalWorkingHours + eveningWorkingHours) - normalWorkingDayHours : 0;
	}

	public Collection<WorkShift> getShifts() {
		return shifts;
	}
}
