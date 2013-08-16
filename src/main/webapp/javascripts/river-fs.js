
// FS River
var addFSRiver = function() {
	$("#leftMenu ul.nav-list li").removeClass("active");
	$("#river-detail").children().each(function(index, elt) {
		$(elt).hide();
	});

	// Buttons
	$("#btnFSRiverCreate").show();
	$("#btnFSRiverDelete").hide();
	$("#btnFSRiverUpdate").hide();
    $("#river-fs-ssh").hide();

	// Initialize default values
	showFSRiver({
		name: "FS river",
        protocol: "local",
		updateRate: 60,
		indexname: "docs",
		typename: "doc",
		analyzer: "standard"
	});

	$("#river-fs-form input").blur(function(event) {
	    event.target.checkValidity();
	}).bind('invalid', function(event) {
	    setTimeout(function() { $(event.target).focus();}, 50);
	});

	$("#river-fs-form").submit(function(e) {
		e.preventDefault();
		return false;
	});

	// Display
	$("#river-fs-detail").show();
	$("#river-fs-name").focus();
};
var initRiverFS = function(){
	// Name
	$("#river-fs-name").change(function(e) {
		$("#river-fs-detail h3").text($(this).val());
	});
	// Rates
	$("#river-fs-rates").change(function(e) {
		var value = $(this).val()*1;
		$("#river-fs-rates-slider").slider( "option", "value", value );
	});
	$("#river-fs-rates-slider").slider({
		range: "min",
		min: 1,
		max: 60*24,
		value: 60,
		slide: function( event, ui ) {
			$("#river-fs-rates").val(ui.value);
		}
	});
    // Rates
    $("#river-fs-protocol").change(function(e) {
        var value = $(this).val();
        if (value == "ssh") {
            $("#river-fs-ssh").show();
        } else {
            $("#river-fs-ssh").hide();
        }
    });
	// Actions
	$("#btnFSRiverStart").click(doStartFSRiver);
	$("#btnFSRiverStop").click(doStopFSRiver);
	$("#btnFSRiverCreate").click(doCreateFSRiver);
	$("#btnFSRiverDelete").click(doDeleteFSRiver);
	$("#btnFSRiverUpdate").click(doUpdateFSRiver);

	// Load rivers
	$.getJSON("api/1/settings/rivers/fs",function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}
		// Update rivers-fs Menu
		$.each(json.object, function(index, fsriver) {
			insertFSRiver(fsriver);
		});
	});
};
// Menu
var insertFSRiver = function(fsriver) {
	var status = '';
	if (fsriver.start===true) {
		status = '&nbsp;<span class="badge badge-success"></span>';
	}
	$('<li id="river-fs-'+fsriver.id+'"><a href="#"><i class="icon-folder-open"></i> '+fsriver.name+status+'</a></li>')
		.insertAfter("#rivers-fs")
		.click(function() {
			var id = $(this).attr("id");
			openFSRiver(id);
		});
}
var updateFSRiverMenu = function(fsriver) {
	var status = '';
	if (fsriver.start===true) {
		status = '&nbsp;<span class="badge badge-success"></span>';
	}
	$("#river-fs-"+fsriver.id).empty().append('<a href="#"><i class="icon-folder-open"></i> '+fsriver.name+status+'</a>');
}

// Show the FS River
var openFSRiver = function(id) {
	$("#leftMenu ul.nav-list li").removeClass("active");
	$("#"+id).addClass("active");

	var riverId = id.substring("river-fs-".length);
	$.getJSON("api/1/settings/rivers/fs/" + riverId, function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}

		$("#river-detail").children().each(function(index, elt) {
			$(elt).hide();
		});

		// Buttons
		$("#btnFSRiverCreate").hide();
		$("#btnFSRiverDelete").show();
		$("#btnFSRiverUpdate").show();

		showFSRiver(json.object);

		// Display
		$("#river-fs-detail").show();
		$("#river-fs-name").focus();
	});
};
var showFSRiver = function(fsriver) {
	// Buttons Status
	$("#btnFSRiverStart").button("reset");
	$("#btnFSRiverStop").button("reset");
	if (fsriver.start===true) {
		$("#btnFSRiverStart").hide();
		$("#btnFSRiverStop").show();
	} else if (fsriver.start===false) {
		$("#btnFSRiverStart").show();
		$("#btnFSRiverStop").hide();
	} else {
		$("#btnFSRiverStart").hide();
		$("#btnFSRiverStop").hide();
	}

	// Fields
	$("#river-fs-detail h3").text(fsriver.name);
	$("#river-fs-id").val(fsriver.id);
	$("#river-fs-name").val(fsriver.name);
    $("#river-fs-protocol").val(fsriver.protocol);
    $("#river-fs-server").val(fsriver.server);
    $("#river-fs-username").val(fsriver.username);
    $("#river-fs-password").val(fsriver.password);
    if (fsriver.protocol == "ssh") {
        $("#river-fs-ssh").show();
    } else {
        $("#river-fs-ssh").hide();
    }
	$("#river-fs-path").val(fsriver.url);
	$("#river-fs-rates").val(fsriver.updateRate);

	$("#river-fs-index").val(fsriver.indexname);
	$("#river-fs-type").val(fsriver.typename);
	$("#river-fs-analyser").val(fsriver.analyzer);
	$("#river-fs-includes").val(fsriver.includes);
	$("#river-fs-excludes").val(fsriver.excludes);
};

// Load Current FS River
var getFSRiver = function() {
	var id = $("#river-fs-id").val();
	if (!id) {
		id = $("#river-fs-name").val().NormaliseUrl();
	}

	return {
		id : id,
		name: $("#river-fs-name").val(),
        protocol: $("#river-fs-protocol").val(),
        server: $("#river-fs-server").val(),
        username: $("#river-fs-username").val(),
        password: $("#river-fs-password").val(),
		url: $("#river-fs-path").val(),
		updateRate: $("#river-fs-rates").val(),
		indexname: $("#river-fs-index").val(),
		typename: $("#river-fs-type").val(),
		analyzer: $("#river-fs-analyser").val(),
		includes: $("#river-fs-includes").val(),
		excludes: $("#river-fs-excludes").val()
	};
};

// FS River Creation
var doCreateFSRiver = function(e) {
	// Validation
	$("#btnFSRiverValidator").click();
	if ($("#river-fs-form input:invalid").length>0) {
		e.preventDefault();
		return false;		
	}

	var data = getFSRiver();
	data.start = false;
	$.postJSON("api/1/settings/rivers/fs/", data, function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}
		insertFSRiver(data);
		$("#river-fs-"+data.id).addClass("active");
		showFSRiver(data);
		
		// Buttons
		$("#btnFSRiverCreate").hide();
		$("#btnFSRiverDelete").show();
		$("#btnFSRiverUpdate").show();

		showNotices([{
			type: "alert-success",
			title: data.name + " created",
			message : "The file system river '"+data.name+"' have been created."
		}]);
	});

	// Stop Event
	e.preventDefault();
	return false;
};
// FS River Deletion
var doDeleteFSRiver = function(e) {
	var data = getFSRiver();
	$.ajax({
		url: "api/1/settings/rivers/fs/" + data.id,
		type: "DELETE",
		success: function(json) {
			// Handle errors
			if (!json.ok) {
				showRestError(json);
				return;
			}
			$("#river-fs-"+data.id).remove();
			$("#river-fs-detail").hide();

			showNotices([{
				type: "alert-success",
				title: data.name + " deleted",
				message : "The file system river '"+data.name+"' have been deleted."
			}]);
		}
	});

	// Stop Event
	e.preventDefault();
	return false;
};
// FS River Update
var doUpdateFSRiver = function(e) {
	// Validation
	$("#btnFSRiverValidator").click();
	if ($("#river-fs-form input:invalid").length>0) {
		e.preventDefault();
		return false;		
	}
	
	var data = getFSRiver();
	$.postJSON("api/1/settings/rivers/fs/", data, function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}

		updateFSRiverMenu(data);

		showNotices([{
			type: "alert-success",
			title: data.name + " updated",
			message : "The file system river '"+data.name+"' have been updated."
		}]);
	});

	// Stop Event
	e.preventDefault();
	return false;
};

// FS River Start
var doStartFSRiver = function(e) {
	$("#btnFSRiverStart").button("loading");
	var data = getFSRiver();
	data.start = true;
	$.getJSON("api/1/settings/rivers/fs/" + data.id + "/start", null, function (json){
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}

		showFSRiver(data);
		updateFSRiverMenu(data);

		showNotices([{
			type: "alert-success",
			title: data.name + " started",
			message : "The file system river '"+data.name+"' have been started."
		}]);
	});
	
	// Stop Event
	e.preventDefault();
	return false;	
}

// FS River Stop
var doStopFSRiver = function(e) {
	$("#btnFSRiverStop").button("loading");
	var data = getFSRiver();
	data.start = false;
	$.getJSON("api/1/settings/rivers/fs/" + data.id + "/stop", null, function (json){
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}
	
		showFSRiver(data);
		updateFSRiverMenu(data);

		showNotices([{
			type: "alert-success",
			title: data.name + " stopped",
			message : "The file system river '"+data.name+"' have been stopped."
		}]);
	});
	
	// Stop Event
	e.preventDefault();
	return false;	
}
