// JQuery - onReady
$(function() {
	// AJAX configuration
	$.ajaxSetup({
		cache: false,
		error: onAjaxError
	});

	// River FS
	initRiverFS();

    // River Jira
	initRiverJira();

	// River DropBox
	initRiverDropBox();
	
	// River Google Drive
	initRiverDrive();
	
	// River S3
	initRiverS3();

	// Create Rivers button
	$(".dropdown-toggle").dropdown();
	$("#btnAddFSRiver").click(addFSRiver);
	$("#btnAddJiraRiver").click(addJiraRiver);	
	$("#btnAddDropBoxRiver").click(addDropBoxRiver);
	$("#btnAddDriveRiver").click(addDriveRiver);
	$("#btnAddS3River").click(addS3River);
});

