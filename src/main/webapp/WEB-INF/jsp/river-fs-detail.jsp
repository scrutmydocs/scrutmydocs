<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<div id="river-fs-detail" class="container-fluid" style="display: none;">
    <div class="row-fluid">
        <div class="span10">
            <h3>Home Documents</h3>
        </div>
        <div class="btn-toolbar span2">
            <div class="pull-right">
                <button id="btnFSRiverStart" class="btn btn-success" data-loading-text="Starting..."><i class="icon-white icon-play"></i> Start</button>
                <button id="btnFSRiverStop" class="btn btn-danger" data-loading-text="Stopping..."><i class="icon-white icon-stop"></i> Stop</button>
            </div>
        </div>
    </div>
    <div class="row-fluid">
        <form id="river-fs-form" class="form form-horizontal well span12">
            <%-- General --%>
            <fieldset class="span6">
                <legend>General</legend>
                <input id="river-fs-id" type="hidden" value=""/>
                <div class="control-group">
                    <label class="control-label" for="river-fs-name">Name</label>
                    <div class="controls">
                        <input class="input" id="river-fs-name" type="text" placeholder="Name" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-fs-path">Path</label>
                    <div class="controls">
                        <input class="input" id="river-fs-path" type="text" placeholder="Path" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-fs-rates">Update Rates</label>
                    <div class="controls">
                        <div class="input-append">
                            <%-- <input class="input" id="river-fs-rates" type="range" value="60" min="1" max="600" required/> --%>

                            <input class="input" id="river-fs-rates" type="text" value="60" required/>
                            <span class="add-on">min.</span>
                        </div>
                        <div id="river-fs-rates-slider"></div>
                        <span class="help-inline"></span>
                    </div>
                </div>
            </fieldset>
            <%-- Advanced --%>
            <fieldset class="span6">
                <legend>Advanced</legend>
                <div class="control-group">
                    <label class="control-label" for="river-fs-index">Index</label>
                    <div class="controls">
                        <input class="input" id="river-fs-index" type="text" placeholder="Index" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-fs-type">Type</label>
                    <div class="controls">
                        <input class="input" id="river-fs-type" type="text" placeholder="Type" required/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-fs-analyser">Analyser</label>
                    <div class="controls">
                        <select id="river-fs-analyser" required>
                            <option value="standard">Standard</option>
                            <option value="french">French</option>
                        </select>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-fs-includes">Includes</label>
                    <div class="controls">
                        <input class="input" id="river-fs-includes" type="text" placeholder="eg. *.doc, *.pdf"/>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-fs-excludes">Excludes</label>
                    <div class="controls">
                        <input class="input" id="river-fs-excludes" type="text" placeholder="eg. *.doc, *.pdf"/>
                        <span class="help-inline"></span>
                    </div>
                </div>
            </fieldset>
            <%-- Actions --%>
            <fieldset class="span12">
                <div class="form-actions">
                    <input id="btnFSRiverValidator" type="submit" style="display:none" name="submitButton">
                    <button id="btnFSRiverCreate" type="submit" class="btn btn-success"><i class="icon-white icon-plus"></i> Create</button>
                    <button id="btnFSRiverDelete" class="btn btn-danger"><i class="icon-white icon-trash"></i> Delete</button>
                    <button id="btnFSRiverUpdate" type="submit" class="btn btn-primary"><i class="icon-white icon-edit"></i> Update</button>
                </div>
            </fieldset>
        </form>
    </div>
</div>