package nut.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nut.base.ShiftReader;
import nut.data.QueryProcessor;
import nut.json.EmployeeInfoJsonModel;
import nut.json.MontlyWageReportJsonModel;

@Path("/api")

public class ApiService {
	@GET
	@Path("/monthWage/{year}/{month}/{start}/{count}")
	@Produces(MediaType.APPLICATION_JSON)
	public MontlyWageReportJsonModel monthWageReceived(
		@PathParam("month") int month, 
		@PathParam("year") int year,
		@PathParam("start") int start,
		@PathParam("count") int count){		 
		ShiftReader.allowReload();
		ShiftReader.readShift(WebHelper.getInstance().getCsvPath());
		return QueryProcessor.getMonthlyWage(month, year, start, count);
	}
	
	@GET
	@Path("/employeeInfo/{year}/{month}/{employeeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public EmployeeInfoJsonModel getEmployeeInfo(@PathParam("month") int month, 
			@PathParam("year") int year, @PathParam("employeeId") int employeeId){
		ShiftReader.readShift(WebHelper.getInstance().getCsvPath());
		return QueryProcessor.getEmployeeInfo(month, year, employeeId);
	}
}
