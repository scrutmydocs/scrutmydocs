<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<script src="javascripts/dropzone.js"></script>
<link rel="stylesheet" href="css/dropzone.css">
<div class="modal fade" id="diaUpload">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h3>Upload your documents</h3>
    </div>
    <div class="modal-body">
        <form action="upload/servlet" class="dropzone" id="dropzone-a">
            <div class="row-fluid">
                <div class="span12">
                    <input type="file" name="file" >
                </div>
            </div>
        </form>
    </div>
</div>  
