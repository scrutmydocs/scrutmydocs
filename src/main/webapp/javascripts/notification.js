/**
 * Notices
 * Contains 
 *	type: CSS class for bootstrap alerts: {<none>, alert-success, alert-info, alert-error}
 *	title: notification title
 * 	message: detail message
 */
// Show notices (inline)
var showNotices = function(notices) {
	$("#notices").empty();
	$.each(notices, function(index, notice) {
		showNotice(notice);
	});
}
var showNotice = function(notice) {
	var type = '';
	if (notice.type) {
		type = ' '+notice.type;
	}

	var title = '';
	if (notice.title) {
		title = '<strong>'+notice.title+'</strong><br />';
	}
	var message = notice.message;
	
	var html = '<div class="alert'+type+'"><a class="close" data-dismiss="alert" href="#">×</a>'+title + message +'</div>';
	$("#notices").append(html);
	$("#notices").show().delay(5000).hide('slow'); // Hide after 5s
}

/**
 * Notifications
 * Contains 
 *	icon: relative path to an image
 *	title: notification title
 * 	message: detail message
 */

// Show Notification (popup)
var showNotifications = function(notifications) {
	$.each(notifications, function(index,notification) {
		showNotification(notification);
	});
}
var showNotification = function(notification) {
	var icon = notification.icon;
	var title = notification.title;
	var message = notification.message;

	// Check webkitNotifications
	if (window.webkitNotifications) {
			// HTML 5
		if (window.webkitNotifications.checkPermission() == 0) {
			// Already allowed
			window.webkitNotifications.createNotification(icon, title, message);
		} else {
			// Request permission
			window.webkitNotifications.requestPermission(function () {
				window.webkitNotifications.createNotification(icon, title, message);
			});
			if (window.webkitNotifications.checkPermission() == 0) {
				// OK
				window.webkitNotifications.createNotification(icon, title, message);
			} else {
				// :(
				showNotificationDialog(icon, title, message);		
			}
		}
	} else {
		// Dialog
		showNotificationDialog(icon, title, message);
    }
}

// A notification dialog
var showNotificationDialog = function(icon, title, message) {
	$("#diaNotification h3").empty().append(title);
	$("#diaNotification .modal-body img").attr("src",icon);
	$("#diaNotification .modal-body p").empty().append(message);
	$("#diaNotification").modal("show");
}


/**
 * REST errors
 */
// Handle AJAX errors
var onAjaxError = function(jqXHR, textStatus, errorThrown) {
	showNotification({
		icon: "img/error.png",
		title: "AJAX Fail",
		message: "Status: "+textStatus + "\nError: "+errorThrown
	});
};

// Show rest error
var showRestError = function(json) {
	var msg;
	if ($.isArray(json.errors)) {
		msg = json.errors.join('<br>');
	} else {
		msg = errors;
	}
	showNotification({
		icon: "img/error.png",
		title: "Error",
		message: msg
	});
};
