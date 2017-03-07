package nut.calc.data;

public class WageInfo {	
	private final double normalWage;
	private final double eveningWage;
	private final double overtimeWage;

	public WageInfo(double normalWage, double eveningWage, double overtimeWage) {
		this.normalWage = normalWage;
		this.eveningWage = eveningWage;
		this.overtimeWage = overtimeWage;
	}

	public double getTotalWage() {		 
		return normalWage + eveningWage + overtimeWage;
	} 
}
