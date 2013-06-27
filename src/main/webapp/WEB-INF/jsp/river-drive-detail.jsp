<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<div id="river-drive-detail" class="container-fluid" style="display: none;">
    <div class="row-fluid">
        <div class="span10">
            <h3>Home Documents</h3>
        </div>
        <div class="btn-toolbar span2">
            <div class="pull-right">
                <button id="btnDriveRiverStart" class="btn btn-success" data-loading-text="Starting..."><i class="icon-white icon-play"></i> Start</button>
                <button id="btnDriveRiverStop" class="btn btn-danger" data-loading-text="Stopping..."><i class="icon-white icon-stop"></i> Stop</button>
            </div>
        </div>
    </div>
    <div class="row-fluid">
        <form id="river-drive-form" class="form form-horizontal well span12">
            <%-- General --%>
            <fieldset class="span6">
                <legend>General</legend>
                <input id="river-drive-id" type="hidden" value=""/>
                <div class="control-group">
                    <label class="control-label" for="river-drive-name">Name</label>
                    <div class="controls">
                        <input class="input" id="river-drive-name" type="text" placeholder="Name" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-drive-folder">Folder</label>
                    <div class="controls">
                        <input class="input" id="river-drive-folder" type="text" placeholder="Folder"/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-drive-rates">Update Rates</label>
                    <div class="controls">
                        <div class="input-append">
                            <input class="input" id="river-drive-rates" type="text" value="60" required/>
                            <span class="add-on">min.</span>
                        </div>
                        <div id="river-drive-rates-slider"></div>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-drive-clientId">Client Id</label>
                    <div class="controls">
                        <input class="input" id="river-drive-clientId" type="text" placeholder="eg. XvxggXVHhGDHJ" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-drive-clientSecret">Client Secret</label>
                    <div class="controls">
                        <input class="input" id="river-drive-clientSecret" type="text" placeholder="eg. dhsghdsgsdhjgj" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-drive-refreshToken">Refresh Token</label>
                    <div class="controls">
                        <input class="input" id="river-drive-refreshToken" type="text" placeholder="eg. dhsghdsgsdhjgj" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
            </fieldset>
            <%-- Advanced --%>
            <fieldset class="span6">
                <legend>Advanced</legend>
                <div class="control-group">
                    <label class="control-label" for="river-drive-index">Index</label>
                    <div class="controls">
                        <input class="input" id="river-drive-index" type="text" placeholder="Index" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-drive-type">Type</label>
                    <div class="controls">
                        <input class="input" id="river-drive-type" type="text" placeholder="Type" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-drive-analyser">Analyser</label>
                    <div class="controls">
                        <select id="river-drive-analyser" required>
                            <option value="standard">Standard</option>
                            <option value="french">French</option>
                        </select>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-drive-includes">Includes</label>
                    <div class="controls">
                        <input class="input" id="river-drive-includes" type="text" placeholder="eg. *.doc, *.pdf"/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-drive-excludes">Excludes</label>
                    <div class="controls">
                        <input class="input" id="river-drive-excludes" type="text" placeholder="eg. *.doc, *.pdf"/>
                        <span class="help-inline"></span>
                    </div>
                </div>
            </fieldset>
            <%-- Actions --%>
            <fieldset class="span12">
                <div class="form-actions">
                    <input id="btnDriveRiverValidator" type="submit" style="display:none" name="submitButton">
                    <button id="btnDriveRiverCreate" type="submit" class="btn btn-success"><i class="icon-white icon-plus"></i> Create</button>
                    <button id="btnDriveRiverDelete" class="btn btn-danger"><i class="icon-white icon-trash"></i> Delete</button>
                    <button id="btnDriveRiverUpdate" type="submit" class="btn btn-primary"><i class="icon-white icon-edit"></i> Update</button>
                </div>
            </fieldset>
        </form>
    </div>
</div>