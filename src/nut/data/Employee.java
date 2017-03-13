package nut.data;

/**
 * Employee's info
 * @author Владимир
 *
 */
public class Employee {
	 private final String firstName;
	 private final int id;
	 /**
	  * Used as surname
	  */
	 private final String secondName;

	public Employee(int id, String firstName, String secondName) {
		 this.firstName = firstName;
		 this.secondName = secondName;
		 this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getFirstName() {		 
		return firstName;
	}

	public String getSecondName() {		 
		return secondName;
	}

}
