package nut.data;

import java.util.HashMap;

/**
 * Storage of employees information
 * @author Владимир
 *
 */
public class EmployeeList  {
	private static EmployeeList instance = new EmployeeList();
	private HashMap<Integer, Employee> employees;
	
	public static EmployeeList getInstance(){
		return instance;
	}
	
	private EmployeeList(){
		employees = new HashMap<Integer, Employee>();
	}
	
	public void add(int id, String firstName, String secondName){
		if (!employees.containsKey(id)){
			employees.put(id, new Employee(id, firstName, secondName));			
		}		 
	}

	public Employee lookupEmployee(int employeeId) {
		return employees.get(employeeId);
	}
}
