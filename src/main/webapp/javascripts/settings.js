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
	
	// River S3
	initRiverS3();

	// Create Rivers button
	$(".dropdown-toggle").dropdown();
	$("#btnAddFSRiver").click(addFSRiver);
	$("#btnAddDropBoxRiver").click(addDropBoxRiver);
	$("#btnAddS3River").click(addS3River);
});

