<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<link rel="stylesheet" href="css/dropzone.css">
<div class="modal fade" id="diaUpload">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h3>Upload your documents</h3>
    </div>
    <div class="modal-body">
        <form action="upload/servlet" class="dropzone" id="uploadDropzone">
            <div class="fallback">
                <input name="file" type="file" multiple />
            </div>
        </form>
    </div>
</div>  
