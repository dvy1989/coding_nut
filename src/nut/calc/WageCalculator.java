package nut.calc;

import nut.data.shift.WorkDay;
import nut.data.shift.WorkShift;

public class WageCalculator {
	private static final double hourWage = 3.75;
	private static final double eveningCompensation = 1.15;	 
	
	public static double calculateDayWage(WorkDay day){
		double wage = 0;
		for (WorkShift shift : day.getShifts()){
			wage += calculateShiftWage(shift);
		}
		return wage;
	}
	
	public static double calculateShiftWage(WorkShift workShift){
		switch (workShift.getType()){
		case Error:
			return 0;
		case Evening:
			return workShift.duration() * (hourWage + eveningCompensation);
		case Normal:
			return workShift.duration() * hourWage;
		case Overtime1:
			return workShift.duration() * (hourWage + 0.25 * hourWage);
		case Overtime2:
			return workShift.duration() * (hourWage + 0.5 * hourWage);
		case Overtime3:
			return workShift.duration() * (hourWage + hourWage);
		default:
			return 0;			
		}
	}
}
