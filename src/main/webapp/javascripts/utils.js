//////////////////////////////////////////////////////
// Upload File
var filesList = new Array();
var initUpload = function() {
// Initialize the jQuery File Upload widget:
    $("#fileupload").fileupload( {
        url: "upload/",
        add: function (e, data) {
            $.each(data.files, function (index, file) {
                filesList.push(file);
                $("#uploadInfo ul").append('<li>Uploading  '+file.name+'</li>');
            });
        },
        done: function (e, data) {
            $("#uploadInfo").html('<ul></ul>');
            $("#progress .bar").css("width","0%");
            filesList = new Array();
            // Close
            $("#diaUpload").modal("hide");
        },
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $("#progress .bar").css("width",progress + "%");
        }
    });

    $("#diaUpload .start").click(function(e) {
        $("#fileupload").fileupload("send", {files: filesList});

        e.preventDefault();
        return false;
    });
};
//////////////////////////////////////////////////////

// Post JSON
$.postJSON = function(url, payload, callback) {
    $.ajax({
        url: url,
        type: "POST",
        data: JSON.stringify(payload),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: callback
    });
};


// Normalize String
String.prototype.NormaliseUrl = function () {
    var url = this;
    var preserveNormalForm = /[,_`;\':-]+/gi
    url = url.replace(preserveNormalForm, ' ');

    // strip accents
    url = stripVowelAccent(url);

    //remove all special chars
    url = url.replace(/[^a-z|^0-9|^-|\s]/gi, '').trim();

    //replace spaces with a -
    url = url.replace(/\s+/gi, '-');
    return url;
};

// Trim char
String.prototype.trim = function () {
    return this.replace(/^\s+|\s+$/g, "");
};
// Strip Accent
function stripVowelAccent(str) {
    var rExps = [{ re: /[\xC0-\xC6]/g, ch: 'A' },
    { re: /[\xE0-\xE6]/g, ch: 'a' },
    { re: /[\xC8-\xCB]/g, ch: 'E' },
    { re: /[\xE8-\xEB]/g, ch: 'e' },
    { re: /[\xCC-\xCF]/g, ch: 'I' },
    { re: /[\xEC-\xEF]/g, ch: 'i' },
    { re: /[\xD2-\xD6]/g, ch: 'O' },
    { re: /[\xF2-\xF6]/g, ch: 'o' },
    { re: /[\xD9-\xDC]/g, ch: 'U' },
    { re: /[\xF9-\xFC]/g, ch: 'u' },
    { re: /[\xD1]/g, ch: 'N' },
    { re: /[\xF1]/g, ch: 'n'}];

    for (var i = 0, len = rExps.length; i < len; i++)
        str = str.replace(rExps[i].re, rExps[i].ch);

    return str;
};