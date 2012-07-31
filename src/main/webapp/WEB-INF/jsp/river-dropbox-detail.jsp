<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<div id="river-dropbox-detail" class="container-fluid" style="display: none;">
    <div class="row-fluid">
        <div class="span10">
            <h3>Home Documents</h3>
        </div>
        <div class="btn-toolbar span2">
            <div class="pull-right">
                <button id="btnDropBoxRiverStart" class="btn btn-success" data-loading-text="Starting..."><i class="icon-white icon-play"></i> Start</button>
                <button id="btnDropBoxRiverStop" class="btn btn-danger" data-loading-text="Stopping..."><i class="icon-white icon-stop"></i> Stop</button>
            </div>
        </div>
    </div>
    <div class="row-fluid">
        <form id="river-dropbox-form" class="form form-horizontal well span12">
            <%-- General --%>
            <fieldset class="span6">
                <legend>General</legend>
                <input id="river-dropbox-id" type="hidden" value=""/>
                <div class="control-group">
                    <label class="control-label" for="river-dropbox-name">Name</label>
                    <div class="controls">
                        <input class="input" id="river-dropbox-name" type="text" placeholder="Name" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-dropbox-path">Path</label>
                    <div class="controls">
                        <input class="input" id="river-dropbox-path" type="text" placeholder="Path" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-dropbox-rates">Update Rates</label>
                    <div class="controls">
                        <div class="input-append">
                            <%-- <input class="input" id="river-dropbox-rates" type="range" value="60" min="1" max="600" required/> --%>

                            <input class="input" id="river-dropbox-rates" type="text" value="60" required/>
                            <span class="add-on">min.</span>
                        </div>
                        <div id="river-dropbox-rates-slider"></div>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-dropbox-token">Token</label>
                    <div class="controls">
                        <input class="input" id="river-dropbox-token" type="text" placeholder="eg. XvxggXVHhGDHJ"/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-dropbox-secret">Secret</label>
                    <div class="controls">
                        <input class="input" id="river-dropbox-secret" type="text" placeholder="eg. dhsghdsgsdhjgj"/>
                        <span class="help-inline"></span>
                    </div>
                </div>
            </fieldset>
            <%-- Advanced --%>
            <fieldset class="span6">
                <legend>Advanced</legend>
                <div class="control-group">
                    <label class="control-label" for="river-dropbox-index">Index</label>
                    <div class="controls">
                        <input class="input" id="river-dropbox-index" type="text" placeholder="Index" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-dropbox-type">Type</label>
                    <div class="controls">
                        <input class="input" id="river-dropbox-type" type="text" placeholder="Type" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-dropbox-analyser">Analyser</label>
                    <div class="controls">
                        <select id="river-dropbox-analyser" required>
                            <option value="standard">Standard</option>
                            <option value="french">French</option>
                        </select>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-dropbox-includes">Includes</label>
                    <div class="controls">
                        <input class="input" id="river-dropbox-includes" type="text" placeholder="eg. *.doc, *.pdf"/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-dropbox-excludes">Excludes</label>
                    <div class="controls">
                        <input class="input" id="river-dropbox-excludes" type="text" placeholder="eg. *.doc, *.pdf"/>
                        <span class="help-inline"></span>
                    </div>
                </div>
            </fieldset>
            <%-- Actions --%>
            <fieldset class="span12">
                <div class="form-actions">
                    <input id="btnDropBoxRiverValidator" type="submit" style="display:none" name="submitButton">
                    <button id="btnDropBoxRiverCreate" type="submit" class="btn btn-success"><i class="icon-white icon-plus"></i> Create</button>
                    <button id="btnDropBoxRiverDelete" class="btn btn-danger"><i class="icon-white icon-trash"></i> Delete</button>
                    <button id="btnDropBoxRiverUpdate" type="submit" class="btn btn-primary"><i class="icon-white icon-edit"></i> Update</button>
                </div>
            </fieldset>
        </form>
    </div>
</div>