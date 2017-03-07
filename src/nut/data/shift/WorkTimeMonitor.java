package nut.data.shift;

import java.time.LocalDateTime;
import java.util.*;
import nut.util.TimeUtils;

public class WorkTimeMonitor {
	private static WorkTimeMonitor instance = new WorkTimeMonitor();
	
	private HashMap<Integer, HashMap<Integer, HashMap<Integer, WorkDay>>> calendar;
	
	public static WorkTimeMonitor getInstance(){
		return instance;
	}
	
	private WorkTimeMonitor(){
		calendar = new HashMap<Integer, HashMap<Integer, HashMap<Integer, WorkDay>>>();
	}
	
	private WorkDay addOrGet(int id, LocalDateTime date){
		addYearData(date.getYear());
		
		addMonthData(date.getMonthValue(), date.getYear());		 
		
		HashMap<Integer, WorkDay> monthData = getMonthData(date.getMonthValue(), date.getYear());
		if (!monthData.containsKey(date.getDayOfMonth())){
			monthData.put(date.getDayOfMonth(), new WorkDay(id, date));
		}
		return monthData.get(date.getDayOfMonth());
	}
	
	private HashMap<Integer, HashMap<Integer, WorkDay>> getYearData(int year){
		return calendar.get(year);
	}
	
	private HashMap<Integer, WorkDay> getMonthData(int month, int year){
		return calendar.get(year).get(month);
	}
	
	private void addYearData(int year){
		if (getYearData(year) == null){
			calendar.put(year, new HashMap<Integer, HashMap<Integer, WorkDay>>());
		}
	}
	
	private void addMonthData(int month, int year){
		if (getMonthData(month, year) == null){
			getYearData(year).put(month, new HashMap<Integer, WorkDay>());
		}
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
	
	public Collection<WorkDay> lookupDays(int month, int year){		
		HashMap<Integer, HashMap<Integer, WorkDay>> yearData = getYearData(year);
		if (yearData != null){			 
			HashMap<Integer, WorkDay> monthData = yearData.get(month);
			if (monthData != null){				
				return monthData.values();
			}
		}		
		return null;
	}

	public Collection<WorkDay> lookupDays(int month, int year, int employeeId) {
		HashMap<Integer, HashMap<Integer, WorkDay>> yearData = getYearData(year);
		if (yearData != null){			 
			HashMap<Integer, WorkDay> monthData = yearData.get(month);
			if (monthData != null){				
				ArrayList<WorkDay> result = new ArrayList<WorkDay>();
				for (WorkDay workDay : monthData.values()){
					if (workDay.getEmployeeId() == employeeId){
						result.add(workDay);
					}
				}
				return result;
			}
		}		
		return null;
	}
}
