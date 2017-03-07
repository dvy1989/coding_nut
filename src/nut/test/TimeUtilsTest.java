package nut.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import nut.util.TimeUtils;

public class TimeUtilsTest {

	@Test
	public void getMillisecondsTest() {
		assertEquals(0, TimeUtils.getMilliseconds(LocalDateTime.of(1970, 1, 1, 0, 0, 0)));
	}
	
	@Test
	public void getHoursBetweenTest(){
		LocalDateTime date1 = LocalDateTime.of(2015, 1, 1, 0, 0, 0);
		LocalDateTime date2 = LocalDateTime.of(2015, 1, 1, 1, 0, 0);	 
		assertEquals(1, TimeUtils.getHoursBetween(date1, date2), 0);
		
		date1 = LocalDateTime.of(2015, 1, 1, 22, 0, 0);
		date2 = LocalDateTime.of(2015, 1, 2, 3, 0, 0);
		assertEquals(5, TimeUtils.getHoursBetween(date1, date2), 0);
	}

}
