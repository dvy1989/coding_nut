package nut.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import nut.data.shift.WorkDay;

public class WorkDayTest {

	@Test
	public void testAddShift() {
		WorkDay workDay = new WorkDay(0, LocalDateTime.of(2015, 1, 1, 0, 0));
		workDay.addShift(LocalDateTime.of(2015, 1, 1, 3, 0), LocalDateTime.of(2015, 1, 1, 2, 0).plusDays(1));
	}

}
