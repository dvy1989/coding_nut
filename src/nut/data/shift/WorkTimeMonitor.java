package nut.data.shift;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;

import nut.util.TimeUtils;

public class WorkTimeMonitor {
	private static WorkTimeMonitor instance = new WorkTimeMonitor();
	
	private HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, WorkDay>>>> calendar;
	
	public static WorkTimeMonitor getInstance(){
		return instance;
	}
	
	private WorkTimeMonitor(){
		calendar = new HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, WorkDay>>>>();
	}
	
	private WorkDay addOrGet(int id, LocalDateTime date){
		if (!calendar.containsKey(id)){
			calendar.put(id, new HashMap<Integer, HashMap<Integer, HashMap<Integer, WorkDay>>>());
		}
		HashMap<Integer, HashMap<Integer, HashMap<Integer, WorkDay>>> employee = calendar.get(id);
		
		if (!employee.containsKey(date.getYear())){
			employee.put(date.getYear(), new HashMap<Integer, HashMap<Integer, WorkDay>>());
		}
		HashMap<Integer, HashMap<Integer, WorkDay>> year = employee.get(date.getYear());
		
		if (!year.containsKey(date.getMonthValue())){
			year.put(date.getMonthValue(), new HashMap<Integer, WorkDay>());
		}
		HashMap<Integer, WorkDay> month = year.get(date.getMonthValue());
		
		if (!month.containsKey(date.getDayOfMonth())){
			month.put(date.getDayOfMonth(), new WorkDay(id, date));
		}
		
		return month.get(date.getDayOfMonth());
	}	  
	
	public void addShift(int id, int[] date, int[] start, int[] finish) {
		LocalDateTime dateTime = TimeUtils.makeDate(date[2], date[1], date[0], 0, 0);
		LocalDateTime shiftStart = TimeUtils.makeDate(date[2], date[1], date[0], start[0], start[1]);
		LocalDateTime shiftFinish = TimeUtils.makeDate(date[2], date[1], date[0], finish[0], finish[1]);
		WorkDay workDay = addOrGet(id, dateTime);
		if (shiftStart.isAfter(shiftFinish)){
			shiftFinish = shiftFinish.plusDays(1);
		}
		workDay.addShift(shiftStart, shiftFinish);
	}
	
	public HashMap<Integer, Collection<WorkDay>> getDaysOfAllEmployees(int month, int year){
		HashMap<Integer, Collection<WorkDay>> workDays = new HashMap<Integer, Collection<WorkDay>>();
		for (Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, WorkDay>>>> employee : calendar.entrySet()){
			workDays.put(employee.getKey(), getDaysOfEmployee(month, year, employee.getKey()));
		}
		return workDays;
	}

	public Collection<WorkDay> getDaysOfEmployee(int month, int year, int employeeId) {
		if (!calendar.containsKey(employeeId)){
			return null;
		}
		
		if (!calendar.get(employeeId).containsKey(year)){
			return null;
		}
		
		if (!calendar.get(employeeId).get(year).containsKey(month)){
			return null;
		}
		
		Collection<WorkDay> workDays = calendar.get(employeeId).get(year).get(month).values();
		workDays.forEach(workDay -> workDay.calculateOvertimes());
		return workDays;
	}
	
	 

	public WorkDay getDayOfEmployee(int month, int year, int day, int employeeId) {
		if (!calendar.containsKey(employeeId)){
			return null;
		}
		
		if (!calendar.get(employeeId).containsKey(year)){
			return null;
		}
		
		if (!calendar.get(employeeId).get(year).containsKey(month)){
			return null;
		}
		
		if (!calendar.get(employeeId).get(year).get(month).containsKey(day)){
			return null;
		}
		
		WorkDay workDay = calendar.get(employeeId).get(year).get(month).get(day);
		workDay.calculateOvertimes();
		return workDay;
	}
}
