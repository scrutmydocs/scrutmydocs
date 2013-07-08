var initRiverS3 = function(){
	// Name
	$("#river-s3-name").change(function(e) {
		$("#river-s3-detail h3").text($(this).val());
	});
	// Rates
	$("#river-s3-rates").change(function(e) {
		var value = $(this).val()*1;
		$("#river-s3-rates-slider").slider( "option", "value", value );
	});
	$("#river-s3-rates-slider").slider({
		range: "min",
		min: 1,
		max: 60*24,
		value: 60,
		slide: function( event, ui ) {
			$("#river-s3-rates").val(ui.value);
		}
	});

	// Actions
	$("#btnS3RiverStart").click(doStartS3River);
	$("#btnS3RiverStop").click(doStopS3River);
	$("#btnS3RiverCreate").click(doCreateS3River);
	$("#btnS3RiverDelete").click(doDeleteS3River);
	$("#btnS3RiverUpdate").click(doUpdateS3River);

	// Load rivers
	$.getJSON("api/1/settings/rivers/s3",function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}
		// Update rivers-S3 Menu
		$.each(json.object, function(index, s3river) {
			insertS3River(s3river);
		});
	});
};

// S3 River
var addS3River = function() {
	$("#leftMenu ul.nav-list li").removeClass("active");
	$("#river-detail").children().each(function(index, elt) {
		$(elt).hide();
	});

	// Buttons
	$("#btnS3RiverCreate").show();
	$("#btnS3RiverDelete").hide();
	$("#btnS3RiverUpdate").hide();

	// Initialize default values
	showS3River({
		name: "S3 river",
		updateRate: 60,
		indexname: "docs",
		typename: "doc",
		analyzer: "standard",
        token: "",
        secret: ""
	});

	$("#river-s3-form input").blur(function(event) {
	    event.target.checkValidity();
	}).bind('invalid', function(event) {
	    setTimeout(function() { $(event.target).focus();}, 50);
	});

	$("#river-s3-form").submit(function(e) {
		e.preventDefault();
		return false;
	});

	// Display
	$("#river-s3-detail").show();
	$("#river-s3-name").focus();
};

// Menu
var insertS3River = function(s3river) {
	var status = '';
	if (s3river.start===true) {
		status = '&nbsp;<span class="badge badge-success"></span>';
	}
	$('<li id="river-s3-'+s3river.id+'"><a href="#"><i class="icon-folder-open"></i> '+s3river.name+status+'</a></li>')
		.insertAfter("#rivers-s3")
		.click(function() {
			var id = $(this).attr("id");
			openS3River(id);
		});
};
var updateS3RiverMenu = function(s3river) {
	var status = '';
	if (s3river.start===true) {
		status = '&nbsp;<span class="badge badge-success"></span>';
	}
	$("#river-s3-"+s3river.id).empty().append('<a href="#"><i class="icon-folder-open"></i> '+s3river.name+status+'</a>');
};

// Show the S3 River
var openS3River = function(id) {
	$("#leftMenu ul.nav-list li").removeClass("active");
	$("#"+id).addClass("active");

	var riverId = id.substring("river-s3-".length);
	$.getJSON("api/1/settings/rivers/s3/" + riverId, function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}

		$("#river-detail").children().each(function(index, elt) {
			$(elt).hide();
		});

		// Buttons
		$("#btnS3RiverCreate").hide();
		$("#btnS3RiverDelete").show();
		$("#btnS3RiverUpdate").show();

		showS3River(json.object);

		// Display
		$("#river-s3-detail").show();
		$("#river-s3-name").focus();
	});
};
var showS3River = function(s3river) {
	// Buttons Status
	$("#btnS3RiverStart").button("reset");
	$("#btnS3RiverStop").button("reset");
	if (s3river.start===true) {
		$("#btnS3RiverStart").hide();
		$("#btnS3RiverStop").show();
	} else if (s3river.start===false) {
		$("#btnS3RiverStart").show();
		$("#btnS3RiverStop").hide();
	} else {
		$("#btnS3RiverStart").hide();
		$("#btnS3RiverStop").hide();
	}

	// Fields
	$("#river-s3-detail h3").text(s3river.name);
	$("#river-s3-id").val(s3river.id);
	$("#river-s3-name").val(s3river.name);
	$("#river-s3-bucket").val(s3river.bucket);
	$("#river-s3-path").val(s3river.url);
	$("#river-s3-rates").val(s3river.updateRate);
    $("#river-s3-accessKey").val(s3river.accessKey);
    $("#river-s3-secretKey").val(s3river.secretKey);

	$("#river-s3-index").val(s3river.indexname);
	$("#river-s3-type").val(s3river.typename);
	$("#river-s3-analyser").val(s3river.analyzer);
	$("#river-s3-includes").val(s3river.includes);
	$("#river-s3-excludes").val(s3river.excludes);
};

// Load Current S3 River
var getS3River = function() {
	var id = $("#river-s3-id").val();
	if (!id) {
		id = $("#river-s3-name").val().NormaliseUrl();
	}

	return {
		id : id,
		name: $("#river-s3-name").val(),
		bucket: $("#river-s3-bucket").val(),
		url: $("#river-s3-path").val(),
		updateRate: $("#river-s3-rates").val(),
        accessKey: $("#river-s3-accessKey").val(),
        secretKey: $("#river-s3-secretKey").val(),
		indexname: $("#river-s3-index").val(),
		typename: $("#river-s3-type").val(),
		analyzer: $("#river-s3-analyser").val(),
		includes: $("#river-s3-includes").val(),
		excludes: $("#river-s3-excludes").val()
	};
};

// S3 River Creation
var doCreateS3River = function(e) {
	// Validation
	$("#btnS3RiverValidator").click();
	if ($("#river-s3-form input:invalid").length>0) {
		e.preventDefault();
		return false;		
	}

	var data = getS3River();
	data.start = false;
	$.postJSON("api/1/settings/rivers/s3/", data, function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}
		insertS3River(data);
		$("#river-s3-"+data.id).addClass("active");
		showS3River(data);
		
		// Buttons
		$("#btnS3RiverCreate").hide();
		$("#btnS3RiverDelete").show();
		$("#btnS3RiverUpdate").show();

		showNotices([{
			type: "alert-success",
			title: data.name + " created",
			message : "The S3 river '"+data.name+"' have been created."
		}]);
	});

	// Stop Event
	e.preventDefault();
	return false;
};
// S3 River Deletion
var doDeleteS3River = function(e) {
	var data = getS3River();
	$.ajax({
		url: "api/1/settings/rivers/s3/" + data.id,
		type: "DELETE",
		success: function(json) {
			// Handle errors
			if (!json.ok) {
				showRestError(json);
				return;
			}
			$("#river-s3-"+data.id).remove();
			$("#river-s3-detail").hide();

			showNotices([{
				type: "alert-danger",
				title: data.name + " deleted",
				message : "The S3 river '"+data.name+"' have been deleted."
			}]);
		}
	});

	// Stop Event
	e.preventDefault();
	return false;
};
// S3 River Update
var doUpdateS3River = function(e) {
	// Validation
	$("#btnS3RiverValidator").click();
	if ($("#river-s3-form input:invalid").length>0) {
		e.preventDefault();
		return false;		
	}
	
	var data = getS3River();
	$.postJSON("api/1/settings/rivers/s3/", data, function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}

		updateS3RiverMenu(data);

		showNotices([{
			type: "alert-info",
			title: data.name + " updated",
			message : "The S3 river '"+data.name+"' have been updated."
		}]);
	});

	// Stop Event
	e.preventDefault();
	return false;
};

// S3 River Start
var doStartS3River = function(e) {
	$("#btnS3RiverStart").button("loading");
	var data = getS3River();
	data.start = true;
	$.getJSON("api/1/settings/rivers/s3/" + data.id + "/start", null, function (json){
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}

		showS3River(data);
		updateS3RiverMenu(data);

		showNotices([{
			type: "alert-success",
			title: data.name + " started",
			message : "The S3 river '"+data.name+"' have been started."
		}]);
	});
	
	// Stop Event
	e.preventDefault();
	return false;	
};

// S3 River Stop
var doStopS3River = function(e) {
	$("#btnS3RiverStop").button("loading");
	var data = getS3River();
	data.start = false;
	$.getJSON("api/1/settings/rivers/s3/" + data.id + "/stop", null, function (json){
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}
	
		showS3River(data);
		updateS3RiverMenu(data);

		showNotices([{
			type: "alert-danger",
			title: data.name + " stopped",
			message : "The S3 river '"+data.name+"' have been stopped."
		}]);
	});
	
	// Stop Event
	e.preventDefault();
	return false;	
};