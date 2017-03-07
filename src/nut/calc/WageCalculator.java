package nut.calc;

import nut.calc.data.WageInfo;
import nut.data.shift.WorkDay;

public class WageCalculator {
	private static final double hourWage = 3.75;
	private static final double eveningCompensation = 1.15;	 
	
	public static WageInfo calculateWage(WorkDay workDay){
		double normalWage = getNormalWage(workDay);
		double eveningWage = getNormalWage(workDay);
		double overtimeWage = getNormalWage(workDay);
		return new WageInfo(normalWage, eveningWage, overtimeWage);
	}
	
	public static double round(double value){
		value = value * 100;
		value = Math.round(value);
		return value / 100;
	}
	
	public static double getNormalWage(WorkDay day){		 
		return round(day.getNormalHours() * hourWage);
	}
	
	public static double getEveningWage(WorkDay day){
		return round(day.getEveningHours() * (hourWage + eveningCompensation));
	}
	
	public static double getOvertimeWage(WorkDay day){
		double wage = 0;
		double hours = day.getOvertimeHours();
		if (hours > 4){
			wage += (hours - 4) * (hourWage + hourWage);
			hours = 4;
		}
		if (hours > 2){
			wage += (hours - 2) * (hourWage + 0.5 * hourWage);
			hours = 2;
		}
		if (hours <= 2){
			wage += hours * (hourWage + 0.25 * hourWage);
		}
		return round(wage);
	}
}
