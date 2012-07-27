<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<div class="modal fade" id="diaUpload">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h3>Upload your documents</h3>
    </div>
    <div class="modal-body">
        <form method="POST" enctype="multipart/form-data" class="well">
            <div class="row-fluid">
                <div class="span12">
                    <span class="btn btn-success fileinput-button">
                        <i class="icon-plus icon-white"></i>
                        <span>Add files...</span>
                        <input id="fileupload" type="file" name="files[]" multiple>
                    </span>
                </div>
            </div>
            <div class="row-fluid">
                <div id="uploadInfo" class="span12">
                    <ul>
                    </ul>
                </div>
                <div id="progress" class="span12 progress progress-info progress-striped active">
                    <div class="bar" style="width: 0%;"></div>
                </div>
            </div>
        </form>
    </div>
    <div class="modal-footer fileupload-buttonbar">
        <a href="#" class="btn btn-warning cancel" data-dismiss="modal"><i class="icon-ban-circle icon-white"></i> Cancel</a>
        <a href="#" class="btn btn-primary start"><i class="icon-white icon-upload"></i> Upload</a>
    </div>
</div>  
