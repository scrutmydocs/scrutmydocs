// JQuery - onReady
$(function() {
	// AJAX configuration
	$.ajaxSetup({
		cache: false,
		error: onAjaxError
	});

	// Bind Search
	$("form.form-search").submit(doSearch);

	// Upload with dropzone
    Dropzone.options.uploadDropzone = {
        init: function() {
            uploadDropzone = this;
            $("#diaUpload").on('hidden', function () {
                // We clean old entries each time we exit dialog box
                uploadDropzone.removeAllFiles();
            });
        }
    };

	// Focus on Search
	$("input.search-query").focus();
});


// Searching
var searchSize=10;
var doSearch = function(e) {
	var query = $("input[name=q]").val();
	doSearchPage(query,0);

	// stop the event
	e.preventDefault();
	return false;
};
var lastData;
var doSearchPage = function(query, from) {
	// Parameters
	var data ={
		first: from,
		pageSize: searchSize,
		search: query
	};
	// Rest
	lastData = data;
	$.postJSON("api/1/search", data, handleSearchResults);
    $("#loading img").show();
};

// Handle Search Results
var handleSearchResults = function(data) {
    $("#loading img").hide();
    // Handle errors
	if (!data.ok) {
		showRestError(data);
		return;
	}
	// OK
	var json = data.object;

	// Layout
	$("#intro").removeClass("hero-unit").addClass("well");
	$("#intro-1").remove();
	$("#intro-2 .invisible").removeClass("invisible");
	$("#base").show();

	// Display search info
	var total =json.totalHits;
	$("#result").empty();
	if(total > 1) {
		$("#result").append('<span class="badge">'+ total +'</span> documents found in <span class="badge">'+json.took+'</span> milliseconds');
        $("#results-row").show();
        if (total > 10) {
            $("#results-pagination").show();
        } else {
            $("#results-pagination").hide();
        }
    } else if (total==1){
		$("#result").append('<span class="badge">One</span> document found in <span class="badge">'+json.took+'</span> milliseconds');
        $("#results-row").show();
        $("#results-pagination").hide();
    } else {
		$("#result").append('Can not find any document matching <span class="badge">'+$("input[name=q]").val()+'</span>...');
        $("#results-row").hide();
        $("#results-pagination").hide();
    }
	
	// Display hits
	$("#results").empty();
	if (json.hits) {
		var link;
		var icon;
		var title;
		var contentType;
		$.each(json.hits,function(index, hit) {
			// TODO evaluate a Javascript Templating solution
			// handle content type for icons
            if (hit.contentType != null) {
                if (hit.contentType.indexOf(";",0) > 0) {
                    contentType = hit.contentType.substr(0,hit.contentType.indexOf(";",0));
                } else {
                    contentType = hit.contentType;
                }

            } else {
                contentType = "";
            }
			icon = "";
			// TODO to refactor
			if (contentType==="application/vnd.oasis.opendocument.text") {
				icon = '<img alt="writer" src="img/docicons/writer.png"/> ';
			} else if (contentType==="application/pdf") {
				icon = '<img alt="pdf" src="img/docicons/pdf.png"/> ';
			} else if (contentType==="image/png") {
				icon = '<img alt="image" src="img/docicons/image.png"/> ';
			} else if (contentType==="image/gif") {
				icon = '<img alt="image" src="img/docicons/image.png"/> ';
            } else if (contentType==="text/plain") {
                icon = '<img alt="text" src="img/docicons/txt.png"/> ';
            }
            /* TODO Missing icons, ...
			} else if (contentType==="application/octet-stream") {
				icon = '<img alt="binary" src="img/docicons/binary.png"/> ';
			} else if (contentType==="application/zip") {
				icon = '<img alt="zip" src="img/docicons/zip.png"/> ';
			} else if (contentType==="audio/mpeg") {
				icon = '<img alt="audio" src="img/docicons/audio.png"/> ';
			*/

			// Create links
			if (hit.title) {
				title = hit.title;
			} else {
				title = hit.id;
			}
			link = '<a target="_blank" href="download?id='+hit.id+'&index='+hit.index+'&content_type=' +
                contentType +'">' +icon+ title+'</a>';
			if (hit.highlights) {
				// add highlight
				link += '<blockquote>' +hit.highlights.join('<br>')+ '</blockquote>';
			}
			$("#results").append('<li>'+link+'</li>');
		});
	}

	// Paging
	var from = lastData.first;
	var to = lastData.first + lastData.pageSize;
	var q = lastData.search;

	var pages = $(".pagination ul").empty();
	if (from > 0) {
		pages.append('<li><a href="#">Prev</a></li>');
	} else {
		pages.append('<li class="disabled"><a href="#">Prev</a></li>');
	}
	// nbPage
	var nbPage = Math.ceil(total/searchSize);
	for (var i=1; i<=nbPage; i++) {
		if(((i-1)*searchSize)==from) {
			pages.append('<li class="disabled"><a href="#">'+i+'</a></li>');
		} else {
			pages.append('<li><a href="#">'+i+'</a></li>');
		}
	}
	if (to>=total) {
		pages.append('<li class="disabled"><a href="#">Next</a></li>');
	} else {
		pages.append('<li><a href="#">Next</a></li>');
	}
	$(".pagination ul li").click(changePage);
};

// Change search result page
var changePage = function(e) {
	var parent = $(this);
	if (!parent.hasClass("active") && !parent.hasClass("disabled")) {
		var txt = $(this).children("a").text();
		if (txt == "Next") {
			doSearchPage(lastData.search, lastData.first + searchSize);
		} else if (txt == "Prev"){
			doSearchPage(lastData.search, lastData.first - searchSize);
		} else {
			doSearchPage(lastData.search, searchSize * (parseInt(txt)-1) );
		}
	}

	// stop the event
	e.preventDefault();
	return false;
};
