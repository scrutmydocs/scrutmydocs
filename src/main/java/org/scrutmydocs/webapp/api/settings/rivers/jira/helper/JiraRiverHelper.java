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

package org.scrutmydocs.webapp.api.settings.rivers.jira.helper;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.scrutmydocs.webapp.api.settings.rivers.AbstractRiverHelper;
import org.scrutmydocs.webapp.api.settings.rivers.jira.data.JiraRiver;

/**
 * Manage JIRA River metadata parsing
 * @author Johann NG SING KWONG
 *
 */
public class JiraRiverHelper extends AbstractRiverHelper<JiraRiver> {
	/**
	 * We build "jira" rivers.
	 */
	@Override
	public String type() {
		return "jira";
	}

    /**
     * @param id The unique id of this river
     * @param indexname The ES index where we store jira objects
     * @param typename The ES type we use to store jira issues
     * @param name The human readable name for this river
     * @param urlBase base URL of JIRA instance
     * @param username JIRA login credentials to access jira issues
     * @param pwd JIRA login credentials to access jira issues
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
        
	/**
	 * We manage :
	 * <ul>
	 * <li>urlBase
	 * <li>username
	 * <li>pwd
	 * <li>jqlTimeZone
	 * <li>timeout
     * <li>maxIssuesPerRequest
     * <li>projectKeysIndexed
     * <li>indexUpdatePeriod
     * <li>indexFullUpdatePeriod
     * <li>maxIndexingThreads
	 * </ul>
	 */
	@Override
    public XContentBuilder addMeta(XContentBuilder xcb, JiraRiver river) throws IOException {

        xcb.field("id", river.getId())
                .field("name", river.getName())
                .field("urlBase", river.getUrlBase())
                .field("username", river.getUsername())
                .field("pwd", river.getPwd())
                .field("jqlTimeZone", river.getJqlTimeZone())
                .field("timeout", river.getTimeout())
                .field("maxIssuesPerRequest", river.getMaxIssuesPerRequest())
                .field("projectKeysIndexed", river.getProjectKeysIndexed())
                .field("indexUpdatePeriod", river.getIndexUpdatePeriod())
                .field("indexUpdatePeriod", river.getIndexUpdatePeriod())
                .field("indexFullUpdatePeriod", river.getIndexFullUpdatePeriod())
                .field("maxIndexingThreads", river.getMaxIndexingThreads());
        return xcb;
    }

	/**
	 * We add specific activity_log metadata
	 * @param xcb
	 * @param river
	 * @return
	 * @throws IOException
	 */
	
	public XContentBuilder addJiraActivityLogMeta(XContentBuilder xcb, JiraRiver river)
			throws IOException {
		return xcb;
	}
	
	/**
	 * We manage :
	 * <ul>
	 * <li>urlBase
	 * <li>username
	 * <li>pwd
	 * <li>jqlTimeZone
	 * <li>timeout
     * <li>maxIssuesPerRequest
     * <li>projectKeysIndexed
     * <li>indexUpdatePeriod
     * <li>indexFullUpdatePeriod
     * <li>maxIndexingThreads
	 * </ul>
	 */
	@Override
	public JiraRiver parseMeta(JiraRiver river, Map<String, Object> content) {
		// We parse specific Jira metadata depending on the Jira river type
		river.setId(getSingleStringValue(type() + ".id", content));
		river.setName(getSingleStringValue(type() + ".name", content));
		river.setUrlBase(getSingleStringValue(type() + ".urlBase", content));
		river.setUsername(getSingleStringValue(type() + ".username", content));
		river.setPwd(getSingleStringValue(type() + ".pwd", content));
        river.setJqlTimeZone(getSingleStringValue(type() + ".jqlTimeZone",content));
        river.setTimeout(getSingleStringValue(type() + ".timeout",content));
        river.setMaxIssuesPerRequest(getSingleLongValue(type() + ".maxIssuesPerRequest",content));
        river.setProjectKeysIndexed(getSingleStringValue(type() + ".projectKeysIndexed",content));
		river.setIndexUpdatePeriod(getSingleStringValue(type()	+ ".indexUpdatePeriod", content));
		river.setIndexFullUpdatePeriod(getSingleStringValue(type()	+ ".indexFullUpdatePeriod", content));
        river.setMaxIndexingThreads(getSingleLongValue(type() + ".maxIndexingThreads",content));

     	return river;
	}	
	

	/**
	 * Build a Jira river definition
	 * 
	 * @param river
	 *            The river definition
	 * @return An ES xcontent
	 * JSON definiton is like:
	 * 
	 * <pre>
	 * {
	 * 	"type" : "jira",
	 * 	"jira" : {
	 *         "urlBase" : "http://localhost:8080",
	 *         "username" : "admin",
	 *         "pwd" : "admin",
	 *         "jqlTimeZone" : "Europe/Paris",
	 *         "timeout" : "5s",
	 *         "maxIssuesPerRequest" : 50,
	 *         "projectKeysIndexed" : "DEMO",
	 *         "indexUpdatePeriod" : "1m",
	 *         "indexFullUpdatedPeriod" : "1h",
	 *         "maxIndexingThreads" : 2
	 *     },
	 *     "index" : {
	 *         "index" : "my_jira_index",
	 *         "type" : "jira_issue"
	 *     },
	 *     "activity_log": {
	 *         "index" : "jira_river_activity",
	 *         "type" : "jira_river_indexupdate"
	 *     }
	 * }
	 * </pre>
	 */
	@Override
	public XContentBuilder toXContent(JiraRiver river) {
		XContentBuilder xb = null;
		try {
			xb = jsonBuilder()
					.startObject()
						.field("type", river.getType())
						.startObject(river.getType());

			// We add specific metadata here
			xb = addMeta(xb, river);
			
			xb
						.endObject()
						.startObject("index")
							.field("index", river.getIndexname())
							.field("type", river.getTypename())
						.endObject()
						.startObject("activity_log")
							.field("index", river.getJiraRiverActivityIndexName())
							.field("type", river.getJiraRiverUpdateType())
						.endObject()
					.endObject();
		} catch (IOException e) {
			// TODO Log when error
		}		
		return xb;
	}
	
	/**
	 * Build a river from a JSON definiton content such as :
	 * 
	 * <pre>
	 * {
	 *  	"type" : "jira",
	 *  	"jira" : {
	 *          "urlBase" : "http://localhost:8080",
	 *          "username" : "admin",
	 *          "pwd" : "admin",
	 *          "jqlTimeZone" : "Europe/Paris",
	 *          "timeout" : "5s",
	 *          "maxIssuesPerRequest" : 50,
	 *          "projectKeysIndexed" : "DEMO",
	 *          "indexUpdatePeriod" : "1m",
	 *          "indexFullUpdatedPeriod" : "1h",
	 *          "maxIndexingThreads" : 2
	 *      },
	 *      "index" : {
	 *          "index" : "my_jira_index",
	 *          "type" : "jira_issue"
	 *      },
	 *      "activity_log": {
	 *          "index" : "jira_river_activity",
	 *          "type" : "jira_river_indexupdate"
	 *      }
	 *  }
	 * </pre>
	 * 
	 * @param content
	 *            The JSON form
	 * @return A River
	 */
	@Override
	public JiraRiver toRiver(JiraRiver river, Map<String, Object> content) {
		try {
			// First we check that it's a type()
			if (!content.containsKey("type")) 
				throw new RuntimeException("Your River object should be a river and contain \"type\":\"rivertype\"");
			
			String type = XContentMapValues.nodeStringValue(content.get("type"), "");
			
			// Then we dig into type()
			if (!content.containsKey(type)) 
				throw new RuntimeException("A River must contain \""+type+"\":{...}");
			if (type.equals(this.type())) {
				river.setId(getSingleStringValue(type + ".id", content));
				river.setName(getSingleStringValue(type + ".name", content));
			}
		// We parse specific metadata depending on the river type
			river = parseMeta(river, content);
			
			// Then we dig into index
			if (content.containsKey("index")) {
				river.setIndexname(getSingleStringValue("index.index", content));
				river.setTypename(getSingleStringValue("index.type", content));
				// TODO Add support for fancy river name ???
			}
		} catch (Exception e) {
			// TODO Log when error
		}		
		return river;
	}
	
	public static XContentBuilder toJiraContentIndexMapping(String type, String analyzer) {
	XContentBuilder xb = null;
	try {
		xb = jsonBuilder()
				.startObject()
					.startObject(type)
						.startObject("_timestamp")
							.field("enabled", "true")
						.endObject()
						.startObject("properties")
							.startObject("project_key")
								.field("type", "string")
								.field("analyzer", analyzer)
							.endObject()
							.startObject("source")
								.field("type", "string")
								.field("analyzer", analyzer)
							.endObject()
						.endObject()
					.endObject()
				.endObject();
	    } catch (IOException e) {
		// TODO Log when error
	    }
	   return xb;
    }


    public static XContentBuilder toJiraRiverActivityMapping(String type, String analyzer) {
        XContentBuilder xb = null;
        try {
            xb = jsonBuilder()
                    .startObject()
                        .startObject(type)
                            .startObject("properties")
                                .startObject("project_key")
                                    .field("type", "string")
                                    .field("analyzer", analyzer)
                                .endObject()
                                .startObject("update_type")
                                    .field("type", "string")
                                    .field("analyzer", analyzer)
                                .endObject()
                                .startObject("result")
                                    .field("type", "string")
                                    .field("analyzer", analyzer)
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject();
        } catch (IOException e) {
            // TODO Log when error
        }
        return xb;
    }

}


