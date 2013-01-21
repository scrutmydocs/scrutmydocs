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

package org.scrutmydocs.webapp.test.rivers;

import java.util.Collection;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.scrutmydocs.webapp.api.settings.rivers.jira.data.JiraRiver;
import org.scrutmydocs.webapp.api.settings.rivers.jira.helper.JiraRiverHelper;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.scrutmydocs.webapp.service.settings.rivers.jira.AdminJiraRiverService;
import org.scrutmydocs.webapp.test.AbstractConfigurationTest;
import org.springframework.beans.factory.annotation.Autowired;


public class AdminJiraRiverServiceTest extends AbstractConfigurationTest {

	@Autowired AdminJiraRiverService adminService;
	
	@Test public void test_add_river() throws InterruptedException {
		Assert.assertNotNull(adminService);

		XContentBuilder xb = new JiraRiverHelper().toXContent(new JiraRiver());
		//new JiraRiver("myjiratestriver", "jira", "jira_issue", "Jira River", "http://localhost:4450","admin","admin",null,null,null,null,null,null,null,true));
				client.prepareIndex(SMDSearchProperties.ES_META_INDEX, SMDSearchProperties.ES_META_RIVERS, "jirariver_test").setSource(xb)
				.execute().actionGet();
		
		// We have to wait for 1s
		Thread.sleep(1000);
		
		Collection<JiraRiver> rivers = adminService.get();
		//Assert.assertEquals("Rivers should not be empty", 1, rivers.size());
		Assert.assertEquals("Rivers should not be empty", 3, rivers.size());
	}
	
	@Test public void test_get_one_river() throws InterruptedException {
		Assert.assertNotNull(adminService);

		XContentBuilder xb = new JiraRiverHelper().toXContent(new JiraRiver());
		//new JiraRiver("myjiratestriver", "jira", "jira_issue", "Jira River", "http://localhost:4450","admin","admin",null,null,null,null,null,null,null,true));
			
		
		client.prepareIndex(SMDSearchProperties.ES_META_INDEX, SMDSearchProperties.ES_META_RIVERS, "myjiratestriver").setSource(xb)
				.execute().actionGet();
		
		// We have to wait for 1s
		Thread.sleep(1000);
		
		JiraRiver jirariver = adminService.get("myjiratestriver");
		Assert.assertNotNull("Rivers should exist", jirariver);
	}

	@Test public void test_get_nonexisting_river() throws InterruptedException {
		Assert.assertNotNull(adminService);

		JiraRiver jirariver = adminService.get("iamariverthatdoesntexist");
		Assert.assertNull("Rivers should not exist", jirariver);
	}

}
