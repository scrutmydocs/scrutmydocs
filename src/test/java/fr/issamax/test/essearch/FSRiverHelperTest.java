/*
 * Licensed to David Pilato and Malloum Laya (the "Authors") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Authors licenses this
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

package fr.issamax.test.essearch;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.junit.Assert;
import org.junit.Test;

import fr.issamax.essearch.admin.river.data.FSRiver;
import fr.issamax.essearch.admin.river.data.FSRiverHelper;
import fr.issamax.essearch.constant.ESSearchProperties;

public class FSRiverHelperTest {

	/**
	 * We want to check the parsing of <pre>
{
  "index" : {
	  "index" : "docs",
	  "type" : "doc",
	  "analyzer" : "standard"
  },
  "fs" : {
      "update_rate" : 30000,
      "name" : "tmp",
      "url" : "/tmp_es"
      "includes" : "*.doc,*.pdf"
      "excludes" : "resume.*"
  },
  "type" : "fs"
}
</pre>
	 * @throws IOException 
	 */
	@Test public void test_tofsriver() throws IOException {
		FSRiver model = new FSRiver("tmp", ESSearchProperties.INDEX_NAME, 
				ESSearchProperties.INDEX_TYPE_DOC, "tmp", "/tmp_es", 30L, "*.doc,*.pdf", "resume.*", "standard", false);
		
		XContentBuilder xb = FSRiverHelper.toXContent(model);		
		String jsonContent = xb.string();

		Map<String, Object> map = XContentHelper.convertToMap(jsonContent.getBytes(), 0, jsonContent.length(), false).v2();
		
		FSRiver fsriver = FSRiverHelper.toRiver(map);
		
		Assert.assertEquals(model.getId(), fsriver.getId());
		Assert.assertEquals(model.getType(), fsriver.getType());
		Assert.assertEquals(model.getUrl(), fsriver.getUrl());
		Assert.assertEquals(model.getUpdateRate(), fsriver.getUpdateRate());

		Assert.assertEquals(model.getIncludes(), fsriver.getIncludes());
		Assert.assertEquals(model.getExcludes(), fsriver.getExcludes());

		Assert.assertEquals(model.getName(), fsriver.getName());
		Assert.assertEquals(model.getTypename(), fsriver.getTypename());
		Assert.assertEquals(model.getAnalyzer(), fsriver.getAnalyzer());
	}
}
