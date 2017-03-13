/**
 * 
 */
var employeeId = 0;

function drawCalendar(infoDiv, data){	 
	var div = $("<div id=\"wagedatatable\" class=\"wage-data\"></div>");
	var table = $("<table class=\"calendar-table\"></table>");
	var dayOfMonth = 1;
	var date = new Date();
	date.setDate(dayOfMonth);
	date.setMonth(labelStore.month - 1);
	date.setYear(labelStore.year);
	var row = $("<tr></tr>");
	var cell;
	var dayNames = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
	for (var i = 0; i < 7; i++){
		cell = $("<th></th>");
		cell.text(dayNames[i]);
		row.append(cell);
	}
	table.append(row);
	row = $("<tr></tr>");
	for (var i = 0; i < date.getDay(); i++){
		cell = $("<td></td>");
		row.append(cell);
	}
	while (labelStore.month > date.getMonth()){
		var num = date.getDate();
		if (data.WorkDays.includes("" + num)){
			cell = $("<td class=\"work-date\"></td>");
		}
		else{
			cell = $("<td></td>");
		} 
		cell.text(num);
		row.append(cell);		 
		dayOfMonth++;
		date.setDate(dayOfMonth);
		if (date.getDay() === 0){
			table.append(row);
			row = $("<tr></tr>");
		}
	}	
	table.append(row);
	div.append(table);
	infoDiv.append(div);
	$(".work-date").click(function(){
		showDayDetailedView(employeeId, $(this).text());
	});
}

function dayInfoLoaded(jsonResponse){
	console.log(jsonResponse);
	var infoDiv = $(".detailed-info-div");
	drawDayView(infoDiv, jsonResponse);
}

function drawDayView(infoDiv, data){
	$("#wagedatatable").css("display", "none");
	var div = $("<div id=\"daydetails\" class=\"wage-data\"></div>");
	var dateDiv = $("<div></div>");
	dateDiv.text("Date: " + ("00" + data.Day).slice(-2) + " " + getMonthName(labelStore.month) + " " + labelStore.year);
	var backDiv = $("<div id=\"backtocalendar\" class=\"back-to-calendar\"></div>");
	backDiv.text("Back to calendar");
	div.append(dateDiv);	 
	for (var i = 0; i < data.WorkShifts.length; i++){
		var shift = data.WorkShifts[i];
		var periodDiv = $("<div class=\"shift-info " + data.WorkShifts[i].ShiftType + "\"></div>");
		periodDiv.text(shift.Start + " - " 
				+ shift.Finish
				+ "; "
				+ (shift.Hours > 0 ? shift.Hours + " hours " : "")
				+ (shift.Minutes > 0 ? shift.Minutes + " minutes; " : "")
				+ "Wage: " + shift.Wage);
		div.append(periodDiv);
	}
	var totalDiv = $("<div></div>");
	totalDiv.text("Total wage for this day: " + data.DayWage);
	div.append(totalDiv);
	div.append($("<div class=\"shift-info\">Legend:</div>"));
	div.append($("<div class=\"shift-info Normal\">Normal working hours with wage $3.75</div>"));
	div.append($("<div class=\"shift-info Evening\">Evening hours with compensation $1.15</div>"));
	div.append($("<div class=\"shift-info Overtime1\">Overtime hours with compensation 125%</div>"));
	div.append($("<div class=\"shift-info Overtime2\">Overtime hours with compensation 150%</div>"));
	div.append($("<div class=\"shift-info Overtime3\">Overtime hours with compensation 200%</div>"));
	div.append(backDiv);
	infoDiv.append(div);
	$("#backtocalendar").click(function(){
		$("#daydetails").remove();
		$("#wagedatatable").css("display", "block");
	});
}

function createTextDiv(text){
	var div = $("<div></div>");
	div.text(text);
	return div;
}

function createPersonalDiv(infoDiv, data){	 
	var personalDiv = $("<div class=\"personal-info-div\"></div>");	 
	personalDiv.append(createTextDiv("First name: " + data.FirstName));
	personalDiv.append(createTextDiv("Surname: " + data.SecondName));
	personalDiv.append(createTextDiv("Wage details for: " + getMonthName(labelStore.month) + ", " + labelStore.year));
	personalDiv.append($("<div style=\"background-color: #8Aff8A; padding: 3px;\">This color " +
			"refers to days, when employee actually worked. Click a day box of this color to" +
			"see details</div><br>"));
	infoDiv.append(personalDiv);	 
}

function createInfoDiv(div, jsonResponse){
	var infoDiv = $("<div class=\"detailed-info-div\"></div>");
	div.append(infoDiv);
	createPersonalDiv(infoDiv, jsonResponse);
	drawCalendar(infoDiv, jsonResponse);
}

function detailedDataLoaded(jsonResponse){
	var div = $("#detailedcontainer");
	div.empty();
	createInfoDiv(div, jsonResponse);
}

function showDetailedView(id){
	if (labelStore){
		employeeId = id;
		var url = window.location.href + restFolder + 
		"/" + apiFolder + 
		"/" + "employeeInfo" + 
		"/" + labelStore.year + 
		"/" + labelStore.month +
		"/" + id;
		 
		sendAjaxRequest(url, labelStore, detailedDataLoaded);
	}
}

function showDayDetailedView(id, day){
	if (labelStore){
		var url = window.location.href + restFolder + 
		"/" + apiFolder + 
		"/" + "dayInfo" + 
		"/" + labelStore.year + 
		"/" + labelStore.month +
		"/" + day +
		"/" + id;
		 
		sendAjaxRequest(url, labelStore, dayInfoLoaded);
	}
}

function appendLink(td, id){
	var link = $("<span class=\"details-link\" onclick=\"showDetailedView(" + id + ")\">See details</span>");
	td.append(link);
}