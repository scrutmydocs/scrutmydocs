<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<div id="river-s3-detail" class="container-fluid" style="display: none;">
    <div class="row-fluid">
        <div class="span10">
            <h3>Home Documents</h3>
        </div>
        <div class="btn-toolbar span2">
            <div class="pull-right">
                <button id="btnS3RiverStart" class="btn btn-success" data-loading-text="Starting..."><i class="icon-white icon-play"></i> Start</button>
                <button id="btnS3RiverStop" class="btn btn-danger" data-loading-text="Stopping..."><i class="icon-white icon-stop"></i> Stop</button>
            </div>
        </div>
    </div>
    <div class="row-fluid">
        <form id="river-s3-form" class="form form-horizontal well span12">
            <%-- General --%>
            <fieldset class="span6">
                <legend>General</legend>
                <input id="river-s3-id" type="hidden" value=""/>
                <div class="control-group">
                    <label class="control-label" for="river-s3-name">Name</label>
                    <div class="controls">
                        <input class="input" id="river-s3-name" type="text" placeholder="Name" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-s3-bucket">Bucket</label>
                    <div class="controls">
                        <input class="input" id="river-s3-bucket" type="text" placeholder="Bucket" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-s3-path">Path prefix</label>
                    <div class="controls">
                        <input class="input" id="river-s3-path" type="text" placeholder="Path/"/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-s3-rates">Update Rates</label>
                    <div class="controls">
                        <div class="input-append">
                            <input class="input" id="river-s3-rates" type="text" value="60" required/>
                            <span class="add-on">min.</span>
                        </div>
                        <div id="river-s3-rates-slider"></div>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-s3-token">Access Key</label>
                    <div class="controls">
                        <input class="input" id="river-s3-accessKey" type="text" placeholder="eg. AAAAAAAAAAAAAAAA"/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-s3-secret">Secret Key</label>
                    <div class="controls">
                        <input class="input" id="river-s3-secretKey" type="text" placeholder="eg. BBBBBBBBBBBBBBBB"/>
                        <span class="help-inline"></span>
                    </div>
                </div>
            </fieldset>
            <%-- Advanced --%>
            <fieldset class="span6">
                <legend>Advanced</legend>
                <div class="control-group">
                    <label class="control-label" for="river-s3-index">Index</label>
                    <div class="controls">
                        <input class="input" id="river-s3-index" type="text" placeholder="Index" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-s3-type">Type</label>
                    <div class="controls">
                        <input class="input" id="river-s3-type" type="text" placeholder="Type" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-s3-analyser">Analyser</label>
                    <div class="controls">
                        <select id="river-s3-analyser" required>
                            <option value="standard">Standard</option>
                            <option value="french">French</option>
                        </select>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-s3-includes">Includes</label>
                    <div class="controls">
                        <input class="input" id="river-s3-includes" type="text" placeholder="eg. *.doc, *.pdf"/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-s3-excludes">Excludes</label>
                    <div class="controls">
                        <input class="input" id="river-s3-excludes" type="text" placeholder="eg. *.doc, *.pdf"/>
                        <span class="help-inline"></span>
                    </div>
                </div>
            </fieldset>
            <%-- Actions --%>
            <fieldset class="span12">
                <div class="form-actions">
                    <input id="btnS3RiverValidator" type="submit" style="display:none" name="submitButton">
                    <button id="btnS3RiverCreate" type="submit" class="btn btn-success"><i class="icon-white icon-plus"></i> Create</button>
                    <button id="btnS3RiverDelete" class="btn btn-danger"><i class="icon-white icon-trash"></i> Delete</button>
                    <button id="btnS3RiverUpdate" type="submit" class="btn btn-primary"><i class="icon-white icon-edit"></i> Update</button>
                </div>
            </fieldset>
        </form>
    </div>
</div>