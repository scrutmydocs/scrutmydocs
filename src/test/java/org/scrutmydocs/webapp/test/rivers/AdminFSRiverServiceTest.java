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

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.scrutmydocs.webapp.api.settings.rivers.fs.data.FSRiver;
import org.scrutmydocs.webapp.api.settings.rivers.fs.helper.FSRiverHelper;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.scrutmydocs.webapp.service.settings.rivers.fs.AdminFSRiverService;
import org.scrutmydocs.webapp.test.AbstractConfigurationTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;


public class AdminFSRiverServiceTest extends AbstractConfigurationTest {

	@Autowired AdminFSRiverService adminService;
	
	@Test public void test_add_river() throws InterruptedException {
		Assert.assertNotNull(adminService);

		XContentBuilder xb = new FSRiverHelper().toXContent(
				new FSRiver("mytestriver", SMDSearchProperties.INDEX_NAME, 
						SMDSearchProperties.INDEX_TYPE_DOC, "tmp", "/tmp_es", 30L, "standard", false));		
		
		client.prepareIndex(SMDSearchProperties.ES_META_INDEX, SMDSearchProperties.ES_META_RIVERS, "mytestriver").setSource(xb)
				.execute().actionGet();
		
		// We have to refresh docs
        client.admin().indices().prepareRefresh(SMDSearchProperties.ES_META_INDEX).execute().actionGet();
		
		Collection<FSRiver> rivers = adminService.get();
		Assert.assertEquals("Rivers should not be empty", 1, rivers.size());
	}
	
	@Test public void test_get_one_river() throws InterruptedException {
		Assert.assertNotNull(adminService);

		XContentBuilder xb = new FSRiverHelper().toXContent(
				new FSRiver("mytestriver", SMDSearchProperties.INDEX_NAME, 
						SMDSearchProperties.INDEX_TYPE_DOC, "tmp", "/tmp_es", 30L, "standard", false));		
		
		client.prepareIndex(SMDSearchProperties.ES_META_INDEX, SMDSearchProperties.ES_META_RIVERS, "mytestriver").setSource(xb)
				.execute().actionGet();

        // We have to refresh docs
        client.admin().indices().prepareRefresh(SMDSearchProperties.ES_META_INDEX).execute().actionGet();
		
		FSRiver fsriver = adminService.get("mytestriver");
		Assert.assertNotNull("Rivers should exist", fsriver);
	}

	@Test public void test_get_nonexisting_river() throws InterruptedException {
		Assert.assertNotNull(adminService);

		FSRiver fsriver = adminService.get("iamariverthatdoesntexist");
		Assert.assertNull("Rivers should not exist", fsriver);
	}

}
