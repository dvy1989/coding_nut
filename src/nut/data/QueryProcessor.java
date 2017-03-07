package nut.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import nut.json.EmployeeInfoJsonModel;
import nut.calc.WageCalculator;
import nut.calc.data.WageInfo;
import nut.data.shift.WorkDay;
import nut.data.shift.WorkTimeMonitor;
import nut.json.BasicWageInfoJsonModel;
import nut.json.MontlyWageReportJsonModel;
import nut.util.TimeUtils;

public class QueryProcessor {
	public static MontlyWageReportJsonModel getMonthlyWage(int month, int year, int start, int count){		
		HashMap<Integer, BasicWageInfoJsonModel> response = new HashMap<Integer, BasicWageInfoJsonModel>();
		Collection<WorkDay> workDays = WorkTimeMonitor.getInstance().lookupDays(month, year);
		if (workDays == null){
			return new MontlyWageReportJsonModel();
		}
		for (WorkDay workDay : workDays){			 
			Employee employee = EmployeeList.getInstance().lookupEmployee(workDay.getEmployeeId());
			if (employee != null) {
				if (!response.containsKey(employee.getId())){
					BasicWageInfoJsonModel json = new BasicWageInfoJsonModel();
					json.Id = employee.getId();
					json.FirstName = employee.getFirstName();
					json.SecondName = employee.getSecondName();				
					json.Wage = 0; 
					response.put(json.Id, json);
				}
				WageInfo wageInfo = WageCalculator.calculateWage(workDay);
				response.get(employee.getId()).Wage += wageInfo.getTotalWage();
			}
		}
		return prepareResponse(response.values(), start, count);		 
	}
	
	private static MontlyWageReportJsonModel prepareResponse(Collection<BasicWageInfoJsonModel> result, int start, int count){
		int toIndex = 0;	
		MontlyWageReportJsonModel response = new MontlyWageReportJsonModel();
		if (start >= result.size()){
			return response;
		}
		ArrayList<BasicWageInfoJsonModel> tmp = new ArrayList<BasicWageInfoJsonModel>(result);
		Collections.sort(tmp, new Comparator<BasicWageInfoJsonModel>(){
			@Override
			public int compare(BasicWageInfoJsonModel item1, BasicWageInfoJsonModel item2){
				return item1.Id - item2.Id;
			}
		});
		if (tmp.size() >= start + count){
			toIndex = start + count;
		}
		else{			
			toIndex = tmp.size();			
		}		 
		response.WageInfo = tmp.subList(start, toIndex);
		response.RecordsAfter = tmp.size() - toIndex;
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
		Collection<WorkDay> workDays = WorkTimeMonitor.getInstance().lookupDays(month, year, employeeId);
		response.WorkDays = new ArrayList<String>();
		for (WorkDay workDay : workDays){
			response.WorkDays.add(TimeUtils.formatDate(workDay.getDate(), "d"));
		}
		return response;
	}
}
