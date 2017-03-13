package nut.data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import nut.calc.WageCalculator;
import nut.data.shift.WorkDay;
import nut.data.shift.WorkShift;
import nut.data.shift.WorkTimeMonitor;
import nut.json.BasicWageInfoJsonModel;
import nut.json.EmployeeInfoJsonModel;
import nut.json.MontlyWageReportJsonModel;
import nut.json.WorkDayInfoJsonModel;
import nut.json.WorkShiftJsonModel;
import nut.util.HourAndMinute;
import nut.util.TimeUtils;

public class QueryProcessor {
	private static final DecimalFormat format = new DecimalFormat("$#.##");
	
	public static MontlyWageReportJsonModel getMonthlyWage(int month, int year, int start, int count){		
		 		
		MontlyWageReportJsonModel response = new MontlyWageReportJsonModel();
		
		HashMap<Integer, Collection<WorkDay>> employeesWorkDaysInfo = WorkTimeMonitor.getInstance().getDaysOfAllEmployees(month, year);
		
		ArrayList<Integer> employeeIdList = new ArrayList<Integer>(employeesWorkDaysInfo.keySet());
		Collections.sort(employeeIdList);
		
		int index = start;
		int employeeId = 0;
		
		ArrayList<BasicWageInfoJsonModel> wageInfo = new ArrayList<BasicWageInfoJsonModel>();
		while (index < employeeIdList.size() && index < (start + count)) {
			employeeId = employeeIdList.get(index);
			Employee employee = EmployeeList.getInstance().lookupEmployee(employeeId);			 
			if (employee != null){
				double wage = 0;
				BasicWageInfoJsonModel employeeWageInfo = new BasicWageInfoJsonModel();
				employeeWageInfo.FirstName = employee.getFirstName();
				employeeWageInfo.Id = employee.getId();
				employeeWageInfo.SecondName = employee.getSecondName();
				Collection<WorkDay> workDays = employeesWorkDaysInfo.get(employeeId);
				if (workDays != null){
					for (WorkDay workDay : workDays){
						wage += WageCalculator.calculateDayWage(workDay);
					}
				}
				employeeWageInfo.Wage = format.format(wage);
				wageInfo.add(employeeWageInfo);
			}
			index++;			 
		}
		
		response.WageInfo = wageInfo;
		
		if (employeesWorkDaysInfo.size() >= (start + count)){
			response.RecordsAfter = employeesWorkDaysInfo.size() - (start + count);
		}
		else{
			response.RecordsAfter = 0;
		}
		response.RecordsBefore = start;		 
		
		return response;
	}	 

	public static EmployeeInfoJsonModel getEmployeeInfo(int month, int year, int employeeId) {
		EmployeeInfoJsonModel response = new EmployeeInfoJsonModel();
		Employee employee = EmployeeList.getInstance().lookupEmployee(employeeId);
		if (employee != null){
			response.Id = employee.getId();
			response.FirstName = employee.getFirstName();
			response.SecondName = employee.getSecondName();
		}
		else{
			return response;
		}		
		Collection<WorkDay> workDays = WorkTimeMonitor.getInstance().getDaysOfEmployee(month, year, employeeId);
		if (workDays == null){
			return response;
		}
		response.WorkDays = new ArrayList<String>();
		for (WorkDay workDay : workDays){
			response.WorkDays.add(TimeUtils.formatDate(workDay.getDate(), "d"));
		}
		return response;		 
	}
	
	public static WorkDayInfoJsonModel getWorkDayInfo(int month, int year, int day, int employeeId){
		WorkDay workDay = WorkTimeMonitor.getInstance().getDayOfEmployee(month, year, day, employeeId);
		WorkDayInfoJsonModel response = new WorkDayInfoJsonModel();
		response.WorkShifts = new ArrayList<WorkShiftJsonModel>();
		response.Day = day;
		double dayWage = 0;
		for (WorkShift shift : workDay.getShifts()){
			double shiftWage = WageCalculator.calculateShiftWage(shift);
			WorkShiftJsonModel workShiftJson = new WorkShiftJsonModel();
			workShiftJson.Wage = format.format(shiftWage);
			workShiftJson.Start = TimeUtils.formatDate(shift.getStart(), "d.MM.Y HH:mm");
			workShiftJson.Finish = TimeUtils.formatDate(shift.getFinish(), "d.MM.Y HH:mm");
			HourAndMinute hourAndMinute = HourAndMinute.getHourAndMinute(shift.duration());
			workShiftJson.Hours = hourAndMinute.Hours;
			workShiftJson.Minutes = hourAndMinute.Minutes;
			workShiftJson.ShiftType = shift.getType();
			response.WorkShifts.add(workShiftJson);
			dayWage += shiftWage;
		}
		response.DayWage = format.format(dayWage);
		return response;
	}
}
