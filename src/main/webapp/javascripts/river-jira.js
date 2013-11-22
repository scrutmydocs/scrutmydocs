
// Jira River
var addJiraRiver = function() {
	$("#leftMenu ul.nav-list li").removeClass("active");
	$("#river-detail").children().each(function(index, elt) {
		$(elt).hide();
	});

	// Buttons
	$("#btnJiraRiverCreate").show();
	$("#btnJiraRiverDelete").hide();
	$("#btnJiraRiverUpdate").hide();

	// Initialize default values
	showJiraRiver({
		indexname: "my_jira_index",
		typename: "jira_issue",
        jiraIssueCommentType: "jira_issue_comment",
        jiraRiverActivityIndexName: "jira_river_activity",
        jiraRiverUpdateType: "jira_river_indexupdate",
        jqlTimeZone: "Europe/Paris",
        timeout: "5s",
        maxIssuesPerRequest: "50",
        indexUpdatePeriod: "5m",
		indexFullUpdatePeriod: "1h",
		maxIndexingThreads: "2"

	});

	$("#river-jira-form input").blur(function(event) {
	    event.target.checkValidity();
	}).bind('invalid', function(event) {
	    setTimeout(function() { $(event.target).focus();}, 50);
	});

	$("#river-jira-form").submit(function(e) {
		e.preventDefault();
		return false;
	});

	// Display
    $("#river-jira-detail h3").text("Create a new JIRA River");
	$("#river-jira-detail").show();
	$("#river-jira-name").focus();
};
var initRiverJira = function(){
	// Name
	$("#river-jira-name").change(function(e) {
		$("#river-jira-detail h3").text($(this).val());
	});

	// Actions
	$("#btnJiraRiverStart").click(doStartJiraRiver);
	$("#btnJiraRiverStop").click(doStopJiraRiver);
	$("#btnJiraRiverCreate").click(doCreateJiraRiver);
	$("#btnJiraRiverDelete").click(doDeleteJiraRiver);
	$("#btnJiraRiverUpdate").click(doUpdateJiraRiver);

	// Load rivers
	$.getJSON("api/1/settings/rivers/jira",function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}
		// Update rivers-jira Menu
		$.each(json.object, function(index, jirariver) {
			insertJiraRiver(jirariver);
		});
	});
};
// Menu
var insertJiraRiver = function(jirariver) {
	var status = '';
	if (jirariver.start===true) {
		status = '&nbsp;<span class="badge badge-success"></span>';
	}
	$('<li id="river-jira-'+jirariver.id+'"><a href="#"><i class="icon-folder-open"></i> '+jirariver.name+status+'</a></li>')
		.insertAfter("#rivers-jira")
		.click(function() {
			var id = $(this).attr("id");
			openJiraRiver(id);
		});
}
var updateJiraRiverMenu = function(jirariver) {
	var status = '';
	if (jirariver.start===true) {
		status = '&nbsp;<span class="badge badge-success"></span>';
	}
	$("#river-jira-"+jirariver.id).empty().append('<a href="#"><i class="icon-folder-open"></i> '+jirariver.name+status+'</a>');
}

// Show the JIRA River
var openJiraRiver = function(id) {
	$("#leftMenu ul.nav-list li").removeClass("active");
	$("#"+id).addClass("active");

	var riverId = id.substring("river-jira-".length);
	$.getJSON("api/1/settings/rivers/jira/" + riverId, function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}

		$("#river-detail").children().each(function(index, elt) {
			$(elt).hide();
		});

		// Buttons
		$("#btnJiraRiverCreate").hide();
		$("#btnJiraRiverDelete").show();
		$("#btnJiraRiverUpdate").show();

		showJiraRiver(json.object);

		// Display
		$("#river-jira-detail").show();
		$("#river-jira-name").focus();
	});
};
var showJiraRiver = function(jirariver) {
	// Buttons Status
	$("#btnJiraRiverStart").button("reset");
	$("#btnJiraRiverStop").button("reset");
	if (jirariver.start===true) {
		$("#btnJiraRiverStart").hide();
		$("#btnJiraRiverStop").show();
	} else if (jirariver.start===false) {
		$("#btnJiraRiverStart").show();
		$("#btnJiraRiverStop").hide();
	} else {
		$("#btnJiraRiverStart").hide();
		$("#btnJiraRiverStop").hide();
	}

	// Fields
	$("#river-jira-detail h3").text(jirariver.name);
	$("#river-jira-id").val(jirariver.id);
	$("#river-jira-name").val(jirariver.name);
	$("#river-jira-urlBase").val(jirariver.urlBase);
	$("#river-jira-index").val(jirariver.indexname);
	$("#river-jira-issue-type").val(jirariver.typename);
    $("#river-jira-comment-type").val(jirariver.jiraIssueCommentType);
	$("#river-jira-username").val(jirariver.username);
	$("#river-jira-pwd").val(jirariver.pwd);
    $("#river-jira-river-activity-indexname").val(jirariver.jiraRiverActivityIndexName);
    $("#river-jira-river-activity-indexupdate").val(jirariver.jiraRiverUpdateType);
	$("#river-jira-jqltimezone").val(jirariver.jqlTimeZone);
	$("#river-jira-timeout").val(jirariver.timeout);
	$("#river-jira-max-issues-per-request").val(jirariver.maxIssuesPerRequest);
	$("#river-jira-project-keys-indexed").val(jirariver.projectKeysIndexed);
	$("#river-jira-index-update-period").val(jirariver.indexUpdatePeriod);
	$("#river-jira-index-full-update-period").val(jirariver.indexFullUpdatePeriod);
	$("#river-jira-max-indexing-threads").val(jirariver.maxIndexingThreads);
};

// Load Current jira River
var getJiraRiver = function() {
	var id = $("#river-jira-id").val();
	if (!id) {
		id = "id"+$("#river-jira-name").val().trim();
	}

	return {

		id : id,
		name: $("#river-jira-name").val().NormaliseUrl(),
		urlBase: $("#river-jira-urlBase").val(),
		indexname: $("#river-jira-index").val(),
		typename: $("#river-jira-issue-type").val(),
        jiraIssueCommentType: $("#river-jira-comment-type").val(),
		username: $("#river-jira-username").val(),
		pwd: $("#river-jira-pwd").val(),
        jiraRiverActivityIndexName: $("#river-jira-river-activity-indexname").val(),
        jiraRiverUpdateType: $("#river-jira-river-activity-indexupdate").val(),
		jqlTimeZone: $("#river-jira-jqltimezone").val(),
		timeout: $("#river-jira-timeout").val(),
		maxIssuesPerRequest: $("#river-jira-max-issues-per-request").val(),
		projectKeysIndexed: $("#river-jira-project-keys-indexed").val(),
		indexUpdatePeriod: $("#river-jira-index-update-period").val(),
		indexFullUpdatePeriod: $("#river-jira-index-full-update-period").val(),
		maxIndexingThreads: $("#river-jira-max-indexing-threads").val()
	};
};

// Jira River Creation
var doCreateJiraRiver = function(e) {
	// Validation
	$("#btnJiraRiverValidator").click();
	if ($("#river-jira-form input:invalid").length>0) {
		e.preventDefault();
		return false;		
	}

	var data = getJiraRiver();
	data.start = false;
	$.postJSON("api/1/settings/rivers/jira/", data, function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}
		insertJiraRiver(data);
		$("#river-jira-"+data.id).addClass("active");
		showJiraRiver(data);
		
		// Buttons
		$("#btnJiraRiverCreate").hide();
		$("#btnJiraRiverDelete").show();
		$("#btnJiraRiverUpdate").show();

		showNotices([{
			type: "alert-success",
			title: data.name + " created",
			message : "The jira river '"+data.name+"' have been created."
		}]);
	});

	// Stop Event
	e.preventDefault();
	return false;
};
// Jira River Deletion
var doDeleteJiraRiver = function(e) {
	var data = getJiraRiver();
	$.ajax({
		url: "api/1/settings/rivers/jira/" + data.id,
		type: "DELETE",
		success: function(json) {
			// Handle errors
			if (!json.ok) {
				showRestError(json);
				return;
			}
			$("#river-jira-"+data.id).remove();
			$("#river-jira-detail").hide();

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
// Jira River Update
var doUpdateJiraRiver = function(e) {
	// Validation
	$("#btnJiraRiverValidator").click();
	if ($("#river-jira-form input:invalid").length>0) {
		e.preventDefault();
		return false;		
	}
	
	var data = getJiraRiver();
	$.postJSON("api/1/settings/rivers/jira/", data, function(json) {
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}

		updateJiraRiverMenu(data);

		showNotices([{
			type: "alert-success",
			title: data.name + " updated",
			message : "The jira river '"+data.name+"' have been updated."
		}]);
	});

	// Stop Event
	e.preventDefault();
	return false;
};

// Jira River Start
var doStartJiraRiver = function(e) {
	$("#btnJiraRiverStart").button("loading");
	var data = getJiraRiver();
	data.start = true;
	$.getJSON("api/1/settings/rivers/jira/" + data.id + "/start", null, function (json){
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}

		showJiraRiver(data);
		updateJiraRiverMenu(data);

		showNotices([{
			type: "alert-success",
			title: data.name + " started",
			message : "The Jira river '"+data.name+"' have been started."
		}]);
	});
	
	// Stop Event
	e.preventDefault();
	return false;	
}

// Jira River Stop
var doStopJiraRiver = function(e) {
	$("#btnJiraRiverStop").button("loading");
	var data = getJiraRiver();
	data.start = false;
	$.getJSON("api/1/settings/rivers/jira/" + data.id + "/stop", null, function (json){
		// Handle errors
		if (!json.ok) {
			showRestError(json);
			return;
		}
	
		showJiraRiver(data);
		updateJiraRiverMenu(data);

		showNotices([{
			type: "alert-success",
			title: data.name + " stopped",
			message : "The Jira river '"+data.name+"' have been stopped."
		}]);
	});
	
	// Stop Event
	e.preventDefault();
	return false;	
}