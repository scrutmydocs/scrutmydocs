
// DropBox River
var addDropBoxRiver = function() {
	$("#leftMenu ul.nav-list li").removeClass("active");
	$("#river-detail").children().each(function(index, elt) {
		$(elt).hide();
	});

	// Buttons
	$("#btnDropBoxRiverCreate").show();
	$("#btnDropBoxRiverDelete").hide();
	$("#btnDropBoxRiverUpdate").hide();

	// Initialize default values
	showDropBoxRiver({
		name: "DropBox river",
		updateRate: 60,
		indexname: "docs",
		typename: "doc",
		analyzer: "standard",
        token: "",
        secret: ""
	});

	$("#river-dropbox-form input").blur(function(event) {
	    event.target.checkValidity();
	}).bind('invalid', function(event) {
	    setTimeout(function() { $(event.target).focus();}, 50);
	});

	$("#river-dropbox-form").submit(function(e) {
		e.preventDefault();
		return false;
	});

	// Display
	$("#river-dropbox-detail").show();
	$("#river-dropbox-name").focus();
};
var initRiverDropBox = function(){
	// Name
	$("#river-dropbox-name").change(function(e) {
		$("#river-dropbox-detail h3").text($(this).val());
	});
	// Rates
	$("#river-dropbox-rates").change(function(e) {
		var value = $(this).val()*1;
		$("#river-dropbox-rates-slider").slider( "option", "value", value );
	});
	$("#river-dropbox-rates-slider").slider({
		range: "min",
		min: 1,
		max: 60*24,
		value: 60,
		slide: function( event, ui ) {
			$("#river-dropbox-rates").val(ui.value);
		}
	});

	// Actions
	$("#btnDropBoxRiverStart").click(doStartDropBoxRiver);
	$("#btnDropBoxRiverStop").click(doStopDropBoxRiver);
	$("#btnDropBoxRiverCreate").click(doCreateDropBoxRiver);
	$("#btnDropBoxRiverDelete").click(doDeleteDropBoxRiver);
	$("#btnDropBoxRiverUpdate").click(doUpdateDropBoxRiver);

	// Load rivers
	$.getJSON("api/1/settings/rivers/dropbox",function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}
		// Update rivers-dropbox Menu
		$.each(json.object, function(index, dropboxriver) {
			insertDropBoxRiver(dropboxriver);
		});
	});
};
// Menu
var insertDropBoxRiver = function(dropboxriver) {
	var status = '';
	if (dropboxriver.start===true) {
		status = '&nbsp;<span class="badge badge-success"></span>';
	}
	$('<li id="river-dropbox-'+dropboxriver.id+'"><a href="#"><i class="icon-folder-open"></i> '+dropboxriver.name+status+'</a></li>')
		.insertAfter("#rivers-dropbox")
		.click(function() {
			var id = $(this).attr("id");
			openDropBoxRiver(id);
		});
}
var updateDropBoxRiverMenu = function(dropboxriver) {
	var status = '';
	if (dropboxriver.start===true) {
		status = '&nbsp;<span class="badge badge-success"></span>';
	}
	$("#river-dropbox-"+dropboxriver.id).empty().append('<a href="#"><i class="icon-folder-open"></i> '+dropboxriver.name+status+'</a>');
}

// Show the DropBox River
var openDropBoxRiver = function(id) {
	$("#leftMenu ul.nav-list li").removeClass("active");
	$("#"+id).addClass("active");

	var riverId = id.substring("river-dropbox-".length);
	$.getJSON("api/1/settings/rivers/dropbox/" + riverId, function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}

		$("#river-detail").children().each(function(index, elt) {
			$(elt).hide();
		});

		// Buttons
		$("#btnDropBoxRiverCreate").hide();
		$("#btnDropBoxRiverDelete").show();
		$("#btnDropBoxRiverUpdate").show();

		showDropBoxRiver(json.object);

		// Display
		$("#river-dropbox-detail").show();
		$("#river-dropbox-name").focus();
	});
};
var showDropBoxRiver = function(dropboxriver) {
	// Buttons Status
	$("#btnDropBoxRiverStart").button("reset");
	$("#btnDropBoxRiverStop").button("reset");
	if (dropboxriver.start===true) {
		$("#btnDropBoxRiverStart").hide();
		$("#btnDropBoxRiverStop").show();
	} else if (dropboxriver.start===false) {
		$("#btnDropBoxRiverStart").show();
		$("#btnDropBoxRiverStop").hide();
	} else {
		$("#btnDropBoxRiverStart").hide();
		$("#btnDropBoxRiverStop").hide();
	}

	// Fields
	$("#river-dropbox-detail h3").text(dropboxriver.name);
	$("#river-dropbox-id").val(dropboxriver.id);
	$("#river-dropbox-name").val(dropboxriver.name);
	$("#river-dropbox-path").val(dropboxriver.url);
	$("#river-dropbox-rates").val(dropboxriver.updateRate);
    $("#river-dropbox-token").val(dropboxriver.token);
    $("#river-dropbox-secret").val(dropboxriver.secret);

	$("#river-dropbox-index").val(dropboxriver.indexname);
	$("#river-dropbox-type").val(dropboxriver.typename);
	$("#river-dropbox-analyser").val(dropboxriver.analyzer);
	$("#river-dropbox-includes").val(dropboxriver.includes);
	$("#river-dropbox-excludes").val(dropboxriver.excludes);
};

// Load Current DropBox River
var getDropBoxRiver = function() {
	var id = $("#river-dropbox-id").val();
	if (!id) {
		id = $("#river-dropbox-name").val().NormaliseUrl();
	}

	return {
		id : id,
		name: $("#river-dropbox-name").val(),
		url: $("#river-dropbox-path").val(),
		updateRate: $("#river-dropbox-rates").val(),
        token: $("#river-dropbox-token").val(),
        secret: $("#river-dropbox-secret").val(),
		indexname: $("#river-dropbox-index").val(),
		typename: $("#river-dropbox-type").val(),
		analyzer: $("#river-dropbox-analyser").val(),
		includes: $("#river-dropbox-includes").val(),
		excludes: $("#river-dropbox-excludes").val()
	};
};

// DropBox River Creation
var doCreateDropBoxRiver = function(e) {
	// Validation
	$("#btnDropBoxRiverValidator").click();
	if ($("#river-dropbox-form input:invalid").length>0) {
		e.preventDefault();
		return false;		
	}

	var data = getDropBoxRiver();
	data.start = false;
	$.postJSON("api/1/settings/rivers/dropbox/", data, function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}
		insertDropBoxRiver(data);
		$("#river-dropbox-"+data.id).addClass("active");
		showDropBoxRiver(data);
		
		// Buttons
		$("#btnDropBoxRiverCreate").hide();
		$("#btnDropBoxRiverDelete").show();
		$("#btnDropBoxRiverUpdate").show();

		showNotices([{
			type: "alert-success",
			title: data.name + " created",
			message : "The dropbox river '"+data.name+"' have been created."
		}]);
	});

	// Stop Event
	e.preventDefault();
	return false;
};
// DropBox River Deletion
var doDeleteDropBoxRiver = function(e) {
	var data = getDropBoxRiver();
	$.ajax({
		url: "api/1/settings/rivers/dropbox/" + data.id,
		type: "DELETE",
		success: function(json) {
			// Handle errors
			if (!json.ok) {
				showRestError(json);
				return;
			}
			$("#river-dropbox-"+data.id).remove();
			$("#river-dropbox-detail").hide();

			showNotices([{
				type: "alert-danger",
				title: data.name + " deleted",
				message : "The dropbox river '"+data.name+"' have been deleted."
			}]);
		}
	});

	// Stop Event
	e.preventDefault();
	return false;
};
// DropBox River Update
var doUpdateDropBoxRiver = function(e) {
	// Validation
	$("#btnDropBoxRiverValidator").click();
	if ($("#river-dropbox-form input:invalid").length>0) {
		e.preventDefault();
		return false;		
	}
	
	var data = getDropBoxRiver();
	$.postJSON("api/1/settings/rivers/dropbox/", data, function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}

		updateDropBoxRiverMenu(data);

		showNotices([{
			type: "alert-info",
			title: data.name + " updated",
			message : "The dropbox river '"+data.name+"' have been updated."
		}]);
	});

	// Stop Event
	e.preventDefault();
	return false;
};

// DropBox River Start
var doStartDropBoxRiver = function(e) {
	$("#btnDropBoxRiverStart").button("loading");
	var data = getDropBoxRiver();
	data.start = true;
	$.getJSON("api/1/settings/rivers/dropbox/" + data.id + "/start", null, function (json){
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}

		showDropBoxRiver(data);
		updateDropBoxRiverMenu(data);

		showNotices([{
			type: "alert-success",
			title: data.name + " started",
			message : "The dropbox river '"+data.name+"' have been started."
		}]);
	});
	
	// Stop Event
	e.preventDefault();
	return false;	
}

// DropBox River Stop
var doStopDropBoxRiver = function(e) {
	$("#btnDropBoxRiverStop").button("loading");
	var data = getDropBoxRiver();
	data.start = false;
	$.getJSON("api/1/settings/rivers/dropbox/" + data.id + "/stop", null, function (json){
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}
	
		showDropBoxRiver(data);
		updateDropBoxRiverMenu(data);

		showNotices([{
			type: "alert-danger",
			title: data.name + " stopped",
			message : "The dropbox river '"+data.name+"' have been stopped."
		}]);
	});
	
	// Stop Event
	e.preventDefault();
	return false;	
}