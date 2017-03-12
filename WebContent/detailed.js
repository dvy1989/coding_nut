/**
 * 
 */
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
		 drawDayView(infoDiv, $(this).text());
	});
}

function drawDayView(infoDiv, day){
	$("#wagedatatable").css("display", "none");
	var div = $("<div id=\"daydetails\" class=\"wage-data\"></div>");
	var dateDiv = $("<div></div>");
	dateDiv.text("Date: " + ("00" + day).slice(-2) + " " + getMonthName(labelStore.month) + " " + labelStore.year);
	var backDiv = $("<div id=\"backtocalendar\" class=\"back-to-calendar\"></div>");
	backDiv.text("Back to calendar");
	div.append(dateDiv);
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
		var url = window.location.href + restFolder + 
		"/" + apiFolder + 
		"/" + "employeeInfo" + 
		"/" + labelStore.year + 
		"/" + labelStore.month +
		"/" + id;
		 
		sendAjaxRequest(url, labelStore, detailedDataLoaded);
	}
}

function appendLink(td, id){
	var link = $("<span class=\"details-link\" onclick=\"showDetailedView(" + id + ")\">See details</span>");
	td.append(link);
}