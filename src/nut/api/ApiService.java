package nut.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import nut.base.ShiftReader;
import nut.data.QueryProcessor;

/**
 * RESTFul API class  
 * All methods are GET as only return value
 */
@Path("/api")

public class ApiService {
	/**
	 * Serializes object to json string
	 * It's required as AWS encountered some problems
	 * with returning JSON of other, than String, objects
	 * @param obj
	 * @return JSON
	 */
	private String serialize(Object obj){
		Gson gson = new Gson();
		return gson.toJson(obj);
	}
	
	@GET
	@Path("/dayInfo/{year}/{month}/{day}/{employeeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDayInfo(
			@PathParam("month") int month, 
			@PathParam("year") int year,
			@PathParam("day") int day,
			@PathParam("employeeId") int employeeId,
			@Context HttpServletRequest httpRequest){ 
		try{
			ShiftReader.allowReload();
			ShiftReader.readShift(FileLookup.lookupFile(httpRequest, FileLookup.csvFileName));
			return Response.ok(serialize(QueryProcessor.getWorkDayInfo(month, year, day, employeeId))).build();
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
			return Response.ok(serialize(QueryProcessor.getEmployeeInfo(month, year, employeeId))).build();
		}
		catch (Exception exp){
			return Response.serverError().entity(exp.getMessage()).build();
		}
	}
	
	@GET
	@Path("/monthWage/{year}/{month}/{start}/{count}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMonthlyWage(
		@PathParam("month") int month, 
		@PathParam("year") int year,
		@PathParam("start") int start,
		@PathParam("count") int count,
		@Context HttpServletRequest httpRequest){
		try{
			ShiftReader.allowReload();
			ShiftReader.readShift(FileLookup.lookupFile(httpRequest, FileLookup.csvFileName));
			return Response.ok(serialize(QueryProcessor.getMonthlyWage(month, year, start, count))).build();			 
		}
		catch (Exception exp){			 
			return Response.serverError().entity(exp.getMessage()).build();
		}
	}
}
