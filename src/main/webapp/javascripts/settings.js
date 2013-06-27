// JQuery - onReady
$(function() {
	// AJAX configuration
	$.ajaxSetup({
		cache: false,
		error: onAjaxError
	});
	
	// Upload
	initUpload();

	// River FS
	initRiverFS();

	// River DropBox
	initRiverDropBox();
	
	// River Google Drive
	initRiverDrive();

	// Create Rivers button
	$(".dropdown-toggle").dropdown();
	$("#btnAddFSRiver").click(addFSRiver);
	$("#btnAddDropBoxRiver").click(addDropBoxRiver);
	$("#btnAddDriveRiver").click(addDriveRiver);
});

