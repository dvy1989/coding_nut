package nut.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Test;

import nut.data.shift.WorkDay;
import nut.data.shift.WorkShift;
import nut.data.shift.WorkShiftType;

public class WorkDayTest {

	@Test
	public void testAddShift() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {		
		WorkDay workDay = new WorkDay(0, LocalDateTime.of(2015, 1, 1, 0, 0));
		workDay.addShift(LocalDateTime.of(2015, 1, 1, 3, 0), LocalDateTime.of(2015, 1, 1, 2, 0).plusDays(1));
		workDay.calculateOvertimes();
		
		Field shiftsField = WorkDay.class.getDeclaredField("shifts");
		shiftsField.setAccessible(true);
		ArrayList<WorkShift> shifts = (ArrayList<WorkShift>)shiftsField.get(workDay);
		assertEquals(6, shifts.size());
		
		assertEquals(LocalDateTime.of(2015, 1, 1, 3, 0), shifts.get(0).getStart());
		assertEquals(LocalDateTime.of(2015, 1, 1, 6, 0), shifts.get(0).getFinish());
		assertEquals(3, shifts.get(0).duration(), 0);
		assertEquals(WorkShiftType.Evening, shifts.get(0).getType());
		
		assertEquals(LocalDateTime.of(2015, 1, 1, 6, 0), shifts.get(1).getStart());
		assertEquals(LocalDateTime.of(2015, 1, 1, 11, 0), shifts.get(1).getFinish());
		assertEquals(5, shifts.get(1).duration(), 0);
		assertEquals(WorkShiftType.Normal, shifts.get(1).getType());
		
		assertEquals(LocalDateTime.of(2015, 1, 1, 11, 0), shifts.get(2).getStart());
		assertEquals(LocalDateTime.of(2015, 1, 1, 13, 0), shifts.get(2).getFinish());
		assertEquals(2, shifts.get(2).duration(), 0);
		assertEquals(WorkShiftType.Overtime1, shifts.get(2).getType());
		
		assertEquals(LocalDateTime.of(2015, 1, 1, 13, 0), shifts.get(3).getStart());
		assertEquals(LocalDateTime.of(2015, 1, 1, 15, 0), shifts.get(3).getFinish());
		assertEquals(2, shifts.get(3).duration(), 0);
		assertEquals(WorkShiftType.Overtime2, shifts.get(3).getType());
		
		assertEquals(LocalDateTime.of(2015, 1, 1, 15, 0), shifts.get(4).getStart());
		assertEquals(LocalDateTime.of(2015, 1, 1, 18, 0), shifts.get(4).getFinish());
		assertEquals(3, shifts.get(4).duration(), 0);
		assertEquals(WorkShiftType.Overtime3, shifts.get(4).getType());
		
		assertEquals(LocalDateTime.of(2015, 1, 1, 18, 0), shifts.get(5).getStart());
		assertEquals(LocalDateTime.of(2015, 1, 2, 2, 0), shifts.get(5).getFinish());
		assertEquals(8, shifts.get(5).duration(), 0);
		assertEquals(WorkShiftType.Overtime3, shifts.get(5).getType());	
		
	}	 
	
	@Test
	public void testShiftNoOvertime() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		WorkDay workDay = new WorkDay(0, LocalDateTime.of(2015, 1, 1, 0, 0));
		workDay.addShift(LocalDateTime.of(2015, 1, 1, 3, 0), LocalDateTime.of(2015, 1, 1, 8, 0));
		workDay.addShift(LocalDateTime.of(2015, 1, 1, 10, 0), LocalDateTime.of(2015, 1, 1, 11, 0));
		workDay.calculateOvertimes();
		
		Field shiftsField = WorkDay.class.getDeclaredField("shifts");
		shiftsField.setAccessible(true);
		ArrayList<WorkShift> shifts = (ArrayList<WorkShift>)shiftsField.get(workDay);
		assertEquals(3, shifts.size());
		
		assertEquals(LocalDateTime.of(2015, 1, 1, 3, 0), shifts.get(0).getStart());
		assertEquals(LocalDateTime.of(2015, 1, 1, 6, 0), shifts.get(0).getFinish());
		assertEquals(3, shifts.get(0).duration(), 0);
		assertEquals(WorkShiftType.Evening, shifts.get(0).getType());		
		
		assertEquals(LocalDateTime.of(2015, 1, 1, 6, 0), shifts.get(1).getStart());
		assertEquals(LocalDateTime.of(2015, 1, 1, 8, 0), shifts.get(1).getFinish());
		assertEquals(2, shifts.get(1).duration(), 0);
		assertEquals(WorkShiftType.Normal, shifts.get(1).getType());
		
		assertEquals(LocalDateTime.of(2015, 1, 1, 10, 0), shifts.get(2).getStart());
		assertEquals(LocalDateTime.of(2015, 1, 1, 11, 0), shifts.get(2).getFinish());
		assertEquals(1, shifts.get(2).duration(), 0);
		assertEquals(WorkShiftType.Normal, shifts.get(2).getType());
	}

}
