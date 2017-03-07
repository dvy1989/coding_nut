package nut.data.shift;

import java.time.LocalDateTime;
import nut.util.*;

public class WorkShift {
	private final LocalDateTime start;
	private final LocalDateTime finish;
	
	public WorkShift(LocalDateTime start, LocalDateTime finish){
		this.start = start;
		this.finish = finish;
	}
	
	public boolean isOverlaped(LocalDateTime start, LocalDateTime finish){
		return ((TimeUtils.isAfterOrEqual(start, this.start) && start.isBefore(this.finish))
				|| (finish.isAfter(this.start) && TimeUtils.isBeforeOrEqual(finish, this.finish)));
	}
}
