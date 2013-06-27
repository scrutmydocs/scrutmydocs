// Initialize the Drive river management screen
var initRiverDrive = function(){
	// Name
	$("#river-drive-name").change(function(e) {
		$("#river-drive-detail h3").text($(this).val());
	});
	// Rates
	$("#river-drive-rates").change(function(e) {
		var value = $(this).val()*1;
		$("#river-drive-rates-slider").slider( "option", "value", value );
	});
	$("#river-drive-rates-slider").slider({
		range: "min",
		min: 1,
		max: 60*24,
		value: 60,
		slide: function( event, ui ) {
			$("#river-drive-rates").val(ui.value);
		}
	});
	
	// Actions
	$("#btnDriveRiverStart").click(doStartDriveRiver);
	$("#btnDriveRiverStop").click(doStopDriveRiver);
	$("#btnDriveRiverCreate").click(doCreateDriveRiver);
	$("#btnDriveRiverDelete").click(doDeleteDriveRiver);
	$("#btnDriveRiverUpdate").click(doUpdateDriveRiver);

	// Load rivers
	$.getJSON("api/1/settings/rivers/drive",function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}
		// Update rivers-drive Menu
		$.each(json.object, function(index, driveriver) {
			insertDriveRiver(driveriver);
		});
	});
};
// Adding a new Drive River
var addDriveRiver = function() {
	$("#leftMenu ul.nav-list li").removeClass("active");
	$("#river-detail").children().each(function(index, elt) {
		$(elt).hide();
	});

	// Buttons
	$("#btnDriveRiverCreate").show();
	$("#btnDriveRiverDelete").hide();
	$("#btnDriveRiverUpdate").hide();

	// Initialize default values
	showDriveRiver({
		name: "Google Drive river",
		updateRate: 60,
		indexname: "docs",
		typename: "doc",
		analyzer: "standard",
        token: "",
        secret: ""
	});

	$("#river-drive-form input").blur(function(event) {
	    event.target.checkValidity();
	}).bind('invalid', function(event) {
	    setTimeout(function() { $(event.target).focus();}, 50);
	});

	$("#river-drive-form").submit(function(e) {
		e.preventDefault();
		return false;
	});

	// Display
	$("#river-drive-detail").show();
	$("#river-drive-name").focus();
};

// Menu
var insertDriveRiver = function(driveriver) {
	var status = '';
	if (driveriver.start===true) {
		status = '&nbsp;<span class="badge badge-success"></span>';
	}
	$('<li id="river-drive-'+driveriver.id+'"><a href="#"><i class="icon-folder-open"></i> '+driveriver.name+status+'</a></li>')
		.insertAfter("#rivers-drive")
		.click(function() {
			var id = $(this).attr("id");
			openDriveRiver(id);
		});
};
var updateDriveRiverMenu = function(driveriver) {
	var status = '';
	if (driveriver.start===true) {
		status = '&nbsp;<span class="badge badge-success"></span>';
	}
	$("#river-drive-"+driveriver.id).empty().append('<a href="#"><i class="icon-folder-open"></i> '+driveriver.name+status+'</a>');
};

// Show the Drive River
var openDriveRiver = function(id) {
	$("#leftMenu ul.nav-list li").removeClass("active");
	$("#"+id).addClass("active");

	var riverId = id.substring("river-drive-".length);
	$.getJSON("api/1/settings/rivers/drive/" + riverId, function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}

		$("#river-detail").children().each(function(index, elt) {
			$(elt).hide();
		});

		// Buttons
		$("#btnDriveRiverCreate").hide();
		$("#btnDriveRiverDelete").show();
		$("#btnDriveRiverUpdate").show();

		showDriveRiver(json.object);

		// Display
		$("#river-drive-detail").show();
		$("#river-drive-name").focus();
	});
};
var showDriveRiver = function(driveriver) {
	// Buttons Status
	$("#btnDriveRiverStart").button("reset");
	$("#btnDriveRiverStop").button("reset");
	if (driveriver.start===true) {
		$("#btnDriveRiverStart").hide();
		$("#btnDriveRiverStop").show();
	} else if (driveriver.start===false) {
		$("#btnDriveRiverStart").show();
		$("#btnDriveRiverStop").hide();
	} else {
		$("#btnDriveRiverStart").hide();
		$("#btnDriveRiverStop").hide();
	}

	// Fields
	$("#river-drive-detail h3").text(driveriver.name);
	$("#river-drive-id").val(driveriver.id);
	$("#river-drive-name").val(driveriver.name);
	$("#river-drive-folder").val(driveriver.url);
	$("#river-drive-rates").val(driveriver.updateRate);
    $("#river-drive-clientId").val(driveriver.clientId);
    $("#river-drive-clientSecret").val(driveriver.clientSecret);
    $("#river-drive-refreshToken").val(driveriver.refreshToken);

	$("#river-drive-index").val(driveriver.indexname);
	$("#river-drive-type").val(driveriver.typename);
	$("#river-drive-analyser").val(driveriver.analyzer);
	$("#river-drive-includes").val(driveriver.includes);
	$("#river-drive-excludes").val(driveriver.excludes);
};

// Load current Google Drive River
var getDriveRiver = function() {
	var id = $("#river-drive-id").val();
	if (!id) {
		id = $("#river-drive-name").val().NormaliseUrl();
	}

	return {
		id : id,
		name: $("#river-drive-name").val(),
		url: $("#river-drive-folder").val(),
		//folder: $("#river-drive-folder").val(),
		updateRate: $("#river-drive-rates").val(),
        clientId: $("#river-drive-clientId").val(),
        clientSecret: $("#river-drive-clientSecret").val(),
        refreshToken: $("#river-drive-refreshToken").val(),
		indexname: $("#river-drive-index").val(),
		typename: $("#river-drive-type").val(),
		analyzer: $("#river-drive-analyser").val(),
		includes: $("#river-drive-includes").val(),
		excludes: $("#river-drive-excludes").val()
	};
};

// Drive River Creation
var doCreateDriveRiver = function(e) {
	// Validation
	$("#btnDriveRiverValidator").click();
	if ($("#river-drive-form input:invalid").length>0) {
		e.preventDefault();
		return false;		
	}

	var data = getDriveRiver();
	data.start = false;
	$.postJSON("api/1/settings/rivers/drive/", data, function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}
		insertDriveRiver(data);
		$("#river-drive-"+data.id).addClass("active");
		showDriveRiver(data);
		
		// Buttons
		$("#btnDriveRiverCreate").hide();
		$("#btnDriveRiverDelete").show();
		$("#btnDriveRiverUpdate").show();

		showNotices([{
			type: "alert-success",
			title: data.name + " created",
			message : "The drive river '"+data.name+"' have been created."
		}]);
	});

	// Stop Event
	e.preventDefault();
	return false;
};
// Drive River Deletion
var doDeleteDriveRiver = function(e) {
	var data = getDriveRiver();
	$.ajax({
		url: "api/1/settings/rivers/drive/" + data.id,
		type: "DELETE",
		success: function(json) {
			// Handle errors
			if (!json.ok) {
				showRestError(json);
				return;
			}
			$("#river-drive-"+data.id).remove();
			$("#river-drive-detail").hide();

			showNotices([{
				type: "alert-danger",
				title: data.name + " deleted",
				message : "The drive river '"+data.name+"' have been deleted."
			}]);
		}
	});

	// Stop Event
	e.preventDefault();
	return false;
};
// Drive River Update
var doUpdateDriveRiver = function(e) {
	// Validation
	$("#btnDriveRiverValidator").click();
	if ($("#river-drive-form input:invalid").length>0) {
		e.preventDefault();
		return false;		
	}
	
	var data = getDriveRiver();
	$.postJSON("api/1/settings/rivers/drive/", data, function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}

		updateDriveRiverMenu(data);

		showNotices([{
			type: "alert-info",
			title: data.name + " updated",
			message : "The drive river '"+data.name+"' have been updated."
		}]);
	});

	// Stop Event
	e.preventDefault();
	return false;
};

// Drive River Start
var doStartDriveRiver = function(e) {
	$("#btnDriveRiverStart").button("loading");
	var data = getDriveRiver();
	data.start = true;
	$.getJSON("api/1/settings/rivers/drive/" + data.id + "/start", null, function (json){
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}

		showDriveRiver(data);
		updateDriveRiverMenu(data);

		showNotices([{
			type: "alert-success",
			title: data.name + " started",
			message : "The drive river '"+data.name+"' have been started."
		}]);
	});
	
	// Stop Event
	e.preventDefault();
	return false;	
};
// Drive River Stop
var doStopDriveRiver = function(e) {
	$("#btnDriveRiverStop").button("loading");
	var data = getDriveRiver();
	data.start = false;
	$.getJSON("api/1/settings/rivers/drive/" + data.id + "/stop", null, function (json){
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}
	
		showDriveRiver(data);
		updateDriveRiverMenu(data);

		showNotices([{
			type: "alert-danger",
			title: data.name + " stopped",
			message : "The drive river '"+data.name+"' have been stopped."
		}]);
	});
	
	// Stop Event
	e.preventDefault();
	return false;	
};