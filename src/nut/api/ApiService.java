package nut.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nut.base.ShiftReader;
import nut.data.QueryProcessor;

@Path("/api")

public class ApiService {
	@GET
	@Path("/monthWage/{year}/{month}/{start}/{count}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response monthWageReceived(
		@PathParam("month") int month, 
		@PathParam("year") int year,
		@PathParam("start") int start,
		@PathParam("count") int count,
		@Context HttpServletRequest httpRequest){
		try{
			ShiftReader.allowReload();
			ShiftReader.readShift(FileLookup.lookupFile(httpRequest, FileLookup.csvFileName));
			return Response.ok(QueryProcessor.getMonthlyWage(month, year, start, count)).build();
		}
		catch (Exception exp){
			return Response.serverError().entity(exp.getMessage()).build();
		}
	}
	
	@GET
	@Path("/employeeInfo/{year}/{month}/{employeeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEmployeeInfo(
			@PathParam("month") int month, 
			@PathParam("year") int year,
			@PathParam("employeeId") int employeeId,
			@Context HttpServletRequest httpRequest){ 
		try{
			ShiftReader.allowReload();
			ShiftReader.readShift(FileLookup.lookupFile(httpRequest, FileLookup.csvFileName));
			return Response.ok(QueryProcessor.getEmployeeInfo(month, year, employeeId)).build();
		}
		catch (Exception exp){
			return Response.serverError().entity(exp.getMessage()).build();
		}
	}
}
