var restFolder = "rest";
var apiFolder = "api";
var start = 0;
var count = 1;
var labelStore = null;

function getLabel(){
	var date = new Date();
	return {
		month: date.getMonth(),
		year: date.getFullYear()
	};
}

function failCallback(jsonResponse){
	$("#getButton").attr("disabled", false);
}

function sendAjaxRequest(url, data, successCallback){
	$.ajax({
		type: "GET", //We have only read operations
		url: url,
		//data: data,
		success: successCallback,
		fail: failCallback
	});
}

function createHeaderElement(headerRow, text){
	var th = $("<th></th>");
	headerRow.append(th);
	th.text(text);
}

function createRow(table){
	var row = $("<tr></tr>");
	table.append(row);
	return row;
}

function createTableHeader(table){
	var row = createRow(table);
	createHeaderElement(row, "Id");
	createHeaderElement(row, "Name");
	createHeaderElement(row, "Wage");
}

function createTable(div){
	var table = $("<table></table>");
	div.append(table);
	createTableHeader(table);
	return table;
}

function createCellWithData(row, json, fieldName, separator = " "){
	var cell = $("<td></td>");
	row.append(cell);
	if (!Array.isArray(fieldName)){
		cell.text(json[fieldName]);
	}
	else{
		var tmpArr = [];
		for (var j = 0; j < fieldName.length; j++){
			tmpArr.push(json[fieldName[j]]);
		}
		cell.text(tmpArr.join(separator));
	}
	return cell;
}

function fillTableWithData(table, jsonData){
	var row;
	for (var i = 0; i < jsonData.length; i++){
		row = createRow(table);
		createCellWithData(row, jsonData[i], "Id");
		createCellWithData(row, jsonData[i], ["FirstName", "SecondName"]);
		appendLink(createCellWithData(row, jsonData[i], "Wage"), jsonData[i].Id);
	}
}

function addPaging(div, after, before){
	var pagingDiv=$("<div></div>");
	if (before){	 
		var button=$("<input type=\"button\" value=\"Previous\" onclick=\"toPreviousClicked()\" />");
		pagingDiv.append(button);
	}
	if (after){	 
		var button=$("<input type=\"button\" value=\"Next\" onclick=\"toNextClicked()\" />");
		pagingDiv.append(button);
	}
	div.append(pagingDiv);
}

function toPreviousClicked(){
	start -= count;
	var data = {
		month: labelStore.month,
		year: labelStore.year
	};	 
	loadData(data);
}

function toNextClicked(){
	start += count;
	var data = {
			month: labelStore.month,
			year: labelStore.year
	};	 
	loadData(data);
}

function monthWageReceived(jsonResponse){
	$("#getButton").attr("disabled", false);
	$("#detailedcontainer").empty();
	if (jsonResponse.WageInfo && Array.isArray(jsonResponse.WageInfo) && jsonResponse.WageInfo.length > 0){
		var div = $("#wagedata");
		div.empty();
		var table = createTable(div);
		fillTableWithData(table, jsonResponse.WageInfo);
		if (jsonResponse.RecordsAfter || jsonResponse.RecordsBefore){
			addPaging(div, jsonResponse.RecordsAfter, jsonResponse.RecordsBefore);
		}
	}
	else{
		$("#wagedata").empty();
		$("#wagedata").text("No data found for this month")
	}
}

function loadMonthes(data){
	var month;
	for (var i = 0; i < 12; i++){
		month = i + 1;		 
		$("#monthSelect").append("<option value=\"" + month + "\"" + ((month === data.month) ? " selected" : "") + ">"
				 + getMonthName(month) + "</option>");		 
	}
	$("#yearSelect").val(data.year);
}

function getMonthName(month){
	var date = new Date();
	date.setMonth(month - 1);
	return date.toLocaleString("en-US", {month : "long"});
}

function loadData(data){
	$("#getButton").attr("disabled", true);
	var url = window.location.href + restFolder + 
	"/" + apiFolder + 
	"/" + "monthWage" + 
	"/" + data.year + 
	"/" + data.month +
	"/" + start +
	"/" + count;
	 
	sendAjaxRequest(url, data, monthWageReceived);
}

function getWageClicked(){
	start = 0;
	var data = {
		month: $("#monthSelect").val(),
		year: $("#yearSelect").val()
	};	
	labelStore = data;
	loadData(data);
}

$(document).ready(function(){	 
	var data = getLabel();
	loadMonthes(data);
	loadData(data);
});