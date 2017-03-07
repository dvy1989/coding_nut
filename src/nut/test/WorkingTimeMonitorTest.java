package nut.test;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import nut.data.shift.WorkDay;
import nut.data.shift.WorkTimeMonitor;

public class WorkingTimeMonitorTest {

	@Test
	public void TestAddDay() {
		int id = 1;
		int[] date = {1, 5, 2015};
		int[] start = {10,30};
		int[] finish = {11,20};
		WorkTimeMonitor.getInstance().addShift(id, date, start, finish);
		
		Collection<WorkDay> workDays = WorkTimeMonitor.getInstance().lookupDays(5, 2015);
		assertNotEquals(null, workDays);
	}

}
