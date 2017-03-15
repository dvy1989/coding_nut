var rest = {
	restFolder: "rest",
	apiFolder: "api",
	getRestUrl(method, args){		 
		var restUrl = window.location.href + this.restFolder + "/" + this.apiFolder + "/" + method;
		for (var i = 1; i < args.length; i++){
			restUrl += "/" + args[i];
		}
		return restUrl;
	},
	invokeMethod(method, successCallback, args){
		var me = this;
		$.ajax({
			type: "GET", //We have only read operations
			url: me.getRestUrl(method, Array.from(arguments).slice(1)),			 
			success: successCallback,
			fail: failCallback
		});
	}
}

var currentDateStore = {
	yearKey: "year",
	monthKey: "month",
	initialSet: function(month, year){		
		this.setIfNotExist(this.yearKey, year);
		this.setIfNotExist(this.monthKey, month);
	},
	setIfNotExist: function(key, value){		 
		if (!window.localStorage.getItem(key)){			
			window.localStorage.setItem(key, value);
		}
	},
	setYear: function(year){
		window.localStorage.setItem(this.yearKey, year);
	},
	setMonth: function(month){
		window.localStorage.setItem(this.monthKey, month);
	},
	getYear: function(){
		return parseInt(window.localStorage.getItem(this.yearKey));
	},
	getMonth: function(){
		return parseInt(window.localStorage.getItem(this.monthKey));
	}
}

var paging = {
	start: 0,
	count: 1,
	forward: function(){
		this.start += this.count;
	},
	back: function(){
		this.start -= this.count;
		if (this.start < 0){
			this.start = 0;
		}
	},
	reset: function(){
		this.start = 0;
	}
}

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
	paging.back();
	loadMonthlyWage();
}

function toNextClicked(){
	paging.forward();
	loadMonthlyWage();
}

function monthWageReceived(jsonResponse){
	$("#getButton").attr("disabled", false);
	$("#detailedcontainer").empty();
	if (jsonResponse.WageInfo 
			&& Array.isArray(jsonResponse.WageInfo) 
			&& jsonResponse.WageInfo.length > 0){
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

function loadMonthes(){
	var month;
	for (var i = 0; i < 12; i++){		 
		$("#monthSelect").append("<option value=\"" + i + "\""
				 + ((i === currentDateStore.getMonth()) ? " selected" : "") + ">"
				 + getMonthName(i) + "</option>");		 
	}
	$("#yearSelect").val(currentDateStore.getYear());
}

function getMonthName(month){
	var date = new Date();
	date.setMonth(month);
	return date.toLocaleString("en-US", {month : "long"});
}

function loadMonthlyWage(){
	$("#getButton").attr("disabled", true);
	rest.invokeMethod("monthlyWage", 
			monthWageReceived, 
			currentDateStore.getYear(),
			currentDateStore.getMonth() + 1,
			paging.start,
			paging.count);
}

function getWageClicked(){	 	
	currentDateStore.setMonth($("#monthSelect").val());
	currentDateStore.setYear($("#yearSelect").val());
	
	loadMonthlyWage();
}

$(document).ready(function(){
	var date = new Date();
	currentDateStore.initialSet(date.getMonth(), date.getFullYear());
	paging.reset();
	loadMonthes();
	loadMonthlyWage();
});