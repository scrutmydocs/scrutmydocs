/*
 * Licensed to scrutmydocs.org (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.scrutmydocs.webapp.api.settings.rivers.jira.data;

import org.scrutmydocs.webapp.api.settings.rivers.basic.data.BasicRiver;
import org.scrutmydocs.webapp.util.StringTools;
import static org.scrutmydocs.webapp.constant.SMDSearchProperties.*;


/**
 * Manage Jira Rivers metadata
 *
 * @author Johann NG SING KWONG
 *
 */
public class JiraRiver extends BasicRiver {

    private static final long serialVersionUID = 1L;
    private String urlBase;
    private String username;
    private String pwd;
    private String jqlTimeZone;
    private String timeout;
    private Long maxIssuesPerRequest;
    private String projectKeysIndexed;
    private String indexUpdatePeriod;
    private String indexFullUpdatePeriod;
    private Long maxIndexingThreads;
    private String analyzer;


    //Managing index dependencies
    private String jiraIssueCommentType; //jira_issue_comment
    private String jiraRiverActivityIndexName; //jira_river_activity
    private String jiraRiverUpdateType; //jira_river_indexupdate

        /*Default Constructor*/
    public JiraRiver(){
        this("jirariver",JIRA_COMPONENT_INDEX,JIRA_ISSUE_TYPE,JIRA_ISSUE_COMMENT_TYPE,"Jira River",
        		"http://localhost:5150","admin","admin",JIRA_RIVER_ACTIVITY_INDEX,JIRA_RIVER_INDEX_UPDATE_TYPE,"3m","1h",2L,false,"keyword");
    }

    public JiraRiver(String id, String indexname, String typename, String jiraIssueCommentType, String name, String urlBase,String username,
    		String pwd,  String jiraRiverActivityIndexName, String jiraRiverUpdateType, String indexUpdatePeriod, String indexFullUpdatePeriod, Long maxIndexingThreads, boolean start, String analyzer){
    	super(id,indexname,typename,name,start);
    	this.urlBase = urlBase;
        this.username = username;
        this.pwd = pwd;
        this.jiraIssueCommentType = jiraIssueCommentType;
        this.jiraRiverActivityIndexName = jiraRiverActivityIndexName;
        this.jiraRiverUpdateType = jiraRiverUpdateType;
        this.indexUpdatePeriod = indexUpdatePeriod;
        this.indexFullUpdatePeriod = indexFullUpdatePeriod;
        this.maxIndexingThreads = maxIndexingThreads;
        this.analyzer = analyzer;


   }

    /**
     * @param id The unique id of this river
     * @param indexname The ES index where we store our docs
     * @param typename The ES type we use to store docs
     * @param jiraIssueCommentType JIRA Issue Index type
     * @param name The human readable name for this river
     * @param urlBase base URL of JIRA instance
     * @param username JIRA login credentials to access jira issues
     * @param pwd JIRA login credentials to access jira issues
     * @param jiraRiverActivityIndexName River Activity Index name
     * @param jqlTimeZone identifier of timezone used to format time values into
     * JQL
     * @param timeout timeout for http/s REST request to the JIRA
     * @param maxIssuesPerRequest maximal number of updated issues requested
     * from JIRA by one REST request
     * @param projectKeysIndexed comma separated list of JIRA project keys to be
     * indexed
     * @param indexUpdatePeriod time value, defines how ofter is search index
     * updated from JIRA instance
     * @param indexFullUpdatePeriod time value, defines how ofter is search
     * index updated from JIRA instance in full update mode
     * @param maxIndexingThreads maximal number of parallel indexing threads
     * running for this river
     * @param started Is the river already started ?
     */
    public JiraRiver(String id, String indexname, String typename, String jiraIssueCommentType, String name,
            String urlBase, String username, String pwd, String jiraRiverActivityIndexName, String jiraRiverUpdateType, String jqlTimeZone,
            String timeout, Long maxIssuesPerRequest, String projectKeysIndexed,
            String indexUpdatePeriod, String indexFullUpdatePeriod, Long maxIndexingThreads,
            boolean started, String analyzer) {
        this(id, indexname, typename, jiraIssueCommentType, name, urlBase, username,
              pwd, jiraRiverActivityIndexName, jiraRiverUpdateType, indexUpdatePeriod, indexFullUpdatePeriod, maxIndexingThreads, started, analyzer);
    	this.jqlTimeZone = jqlTimeZone;
        this.timeout = timeout;
        this.maxIssuesPerRequest = maxIssuesPerRequest;
        this.projectKeysIndexed = projectKeysIndexed;

    }
 
    /**
     * We implement here a "jira" river
     */
    @Override
    public String getType() {
        return "jira";
    }
   
    /**
     * @return base URL of JIRA instance
     */
    public String getUrlBase() {
        return urlBase;
    }

    /**
     * @param urlBase base URL of JIRA instance
     */
    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getJqlTimeZone() {
        return jqlTimeZone;
    }

    public void setJqlTimeZone(String jqlTimeZone) {
        this.jqlTimeZone = jqlTimeZone;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public Long getMaxIssuesPerRequest() {
        return maxIssuesPerRequest;
    }

    public void setMaxIssuesPerRequest(Long maxIssuesPerRequest) {
        this.maxIssuesPerRequest = maxIssuesPerRequest;
    }

    public String getProjectKeysIndexed() {
        return projectKeysIndexed;
    }

    public void setProjectKeysIndexed(String projectKeysIndexed) {
        this.projectKeysIndexed = projectKeysIndexed;
    }

    public String getIndexUpdatePeriod() {
        return indexUpdatePeriod;
    }

    public void setIndexUpdatePeriod(String indexUpdatePeriod) {
        this.indexUpdatePeriod = indexUpdatePeriod;
    }

    public String getIndexFullUpdatePeriod() {
        return indexFullUpdatePeriod;
    }

    public void setIndexFullUpdatePeriod(String indexFullUpdatePeriod) {
        this.indexFullUpdatePeriod = indexFullUpdatePeriod;
    }

    public Long getMaxIndexingThreads() {
        return maxIndexingThreads;
    }

    public void setMaxIndexingThreads(Long maxIndexingThreads) {
        this.maxIndexingThreads = maxIndexingThreads;
    }

    public String getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }

    public String getJiraIssueCommentType() {
        return jiraIssueCommentType;
    }

    public void setJiraIssueCommentType(String jira_issue_comment) {
        this.jiraIssueCommentType = jira_issue_comment;
    }

    public String getJiraRiverActivityIndexName() {
        return jiraRiverActivityIndexName;
    }

    public void setJiraRiverActivityIndexName(String jiraRiverActivityIndex) {
        this.jiraRiverActivityIndexName = jiraRiverActivityIndex;
    }

    public String getJiraRiverUpdateType() {
        return jiraRiverUpdateType;
    }

    public void setJiraRiverUpdateType(String jiraRiverUpdate) {
        this.jiraRiverUpdateType = jiraRiverUpdate;
    }

    @Override
    public String toString() {
        return StringTools.toString(this);
    }
}
