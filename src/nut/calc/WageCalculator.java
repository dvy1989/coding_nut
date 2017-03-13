package nut.calc;

import nut.data.shift.WorkDay;
import nut.data.shift.WorkShift;

/**
 * Methods for wage calculation
 * Class defines only size of wage for different types of
 * working shifts
 * @author Владимир
 *
 */
public class WageCalculator {
	private static final double eveningCompensation = 1.15;
	private static final double hourWage = 3.75;	 
	
	/**
	 * Calculate wage for the whole day
	 * @param day Work day
	 * @return Wage
	 */
	public static double calculateDayWage(WorkDay day){
		double wage = 0;
		for (WorkShift shift : day.getShifts()){
			wage += calculateShiftWage(shift);
		}
		return wage;
	}
	
	/**
	 * Calculate wage for work shift
	 * @param workShift Work shift
	 * @return Wage
	 */
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
