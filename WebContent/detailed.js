/**
 * 
 */
function createInfoDiv(div){
	var infoDiv = $("<div class=\"detailed-info-div\"></div>");
	div.append(infoDiv);
	
}

function detailedDataLoaded(jsonResponse){
	var div = $("#detailedcontainer");
	div.empty();
	createInfoDiv(div);
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