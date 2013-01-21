<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<div id="river-jira-detail" class="container-fluid" style="display: none;">
    <div class="row-fluid">
        <div class="span10">
            <h3>Jira Issues</h3>
        </div>
        <div class="btn-toolbar span2">
            <div class="pull-right">
                <button id="btnJiraRiverStart" class="btn btn-success" data-loading-text="Starting..."><i class="icon-white icon-play"></i> Start</button>
                <button id="btnJiraRiverStop" class="btn btn-danger" data-loading-text="Stopping..."><i class="icon-white icon-stop"></i> Stop</button>
            </div>
        </div>
    </div>
    <div class="row-fluid">
        <form id="river-jira-form" class="form form-horizontal well span12">
            <%-- General --%>
            <fieldset class="span6">
                <legend>General</legend>
                <input id="river-jira-id" type="hidden" value=""/>
                <div class="control-group">
                    <label class="control-label" for="river-jira-name">Name</label>
                    <div class="controls">
                        <input class="input" id="river-jira-name" type="text" placeholder="Name"  required />
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-jira-urlBase">URL</label>
                    <div class="controls">
                        <input class="input" id="river-jira-urlBase" type="text" placeholder="URL" required />
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-jira-index">JIRA Components Index</label>
                    <div class="controls">
                        <input class="input" id="river-jira-index" type="text" placeholder="my_jira_index" readonly="true" />
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-jira-issue-type">Main Indexed JIRA Issue Type</label>
                    <div class="controls">
                        <input class="input" id="river-jira-issue-type" type="text" placeholder="jira_issue" readonly="true" />
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-jira-comment-type">Indexed JIRA Issue Comment Type</label>
                    <div class="controls">
                        <input class="input" id="river-jira-comment-type" type="text" placeholder="jira_issue_comment" readonly="true" />
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-jira-username">Username</label>
                    <div class="controls">
                        <input class="input" id="river-jira-username" type="text" required />
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-jira-pwd">Password</label>
                    <div class="controls">
                        <input class="input" id="river-jira-pwd" type="text" required />
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-jira-river-activity-indexname">River Activity Index Name</label>
                    <div class="controls">
                        <input class="input" id="river-jira-river-activity-indexname" type="text" placeholder="jira_river_activity" readonly="true" />
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-jira-river-activity-indexupdate">River Activity Index Update Type</label>
                    <div class="controls">
                        <input class="input" id="river-jira-river-activity-indexupdate" type="text" placeholder="jira_river_indexupdate" readonly="true" />
                        <span class="help-inline"></span>
                    </div>
                </div>

            </fieldset>
            <%-- Advanced --%>
            <fieldset class="span6">
                <legend>Optional</legend>
                
                 
                
                <div class="control-group">
                    <label class="control-label" for="river-jira-jqltimezone">TimeZone</label>
                    <div class="controls">
                        <input class="input" id="river-jira-jqltimezone" type="text" />
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-jira-timeout">Timeout</label>
                    <div class="controls">
                        <input class="input" id="river-jira-timeout" type="text" />
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-jira-max-issues-per-request">Max Issues Per Request</label>
                    <div class="controls">
                        <input class="input" id="river-jira-max-issues-per-request" type="text" />
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-jira-project-keys-indexed">Project Keys Indexed</label>
                    <div class="controls">
                        <input class="input" id="river-jira-project-keys-indexed" type="text" />
                        <span class="help-inline"></span>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label" for="river-jira-index-update-period">Index Update Period</label>
                    <div class="controls">
                        <input class="input" id="river-jira-index-update-period" type="text" />
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-jira-index-full-update-period">Index Full Update Period</label>
                    <div class="controls">
                        <input class="input" id="river-jira-index-full-update-period" type="text" />
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="river-jira-max-indexing-threads">Max Indexing Threads</label>
                    <div class="controls">
                        <input class="input" id="river-jira-max-indexing-threads" type="text" />
                        <span class="help-inline"></span>
                    </div>
                </div>
            </fieldset>
            <%-- Actions --%>
            <fieldset class="span12">
                <div class="form-actions">
                    <input id="btnJiraRiverValidator" type="submit" style="display:none" name="submitButton">
                    <button id="btnJiraRiverCreate" type="submit" class="btn btn-success"><i class="icon-white icon-plus"></i> Create</button>
                    <button id="btnJiraRiverDelete" class="btn btn-danger"><i class="icon-white icon-trash"></i> Delete</button>
                    <button id="btnJiraRiverUpdate" type="submit" class="btn btn-primary"><i class="icon-white icon-edit"></i> Update</button>
                </div>
            </fieldset>
        </form>
    </div>
</div>