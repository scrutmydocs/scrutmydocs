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
import org.elasticsearch.common.xcontent.XContentHelper;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.scrutmydocs.webapp.api.settings.rivers.s3.data.S3River;
import org.scrutmydocs.webapp.api.settings.rivers.s3.helper.S3RiverHelper;

import java.io.IOException;
import java.util.Map;

/**
 * TODO: fix tests.
 */
@Ignore
public class S3RiverHelperTest {

	/**
	 * We want to check the parsing of <pre>
{
  "index" : {
	  "index" : "docs",
	  "type" : "doc",
	  "analyzer" : "standard"
  },
  "s3" : {
      "update_rate" : 30000,
      "name" : "tmp",
	  "token" : "XXXXXXXXXXXXXX",
	  "secret" : "ZZZZZZZZZZZZZZ",
      "url" : "/tmp_es"
      "includes" : "*.doc,*.pdf"
      "excludes" : "resume.*"
  },
  "type" : "s3"
}
</pre>
	 * @throws java.io.IOException
	 */
	@Test public void test_tos3river() throws IOException {
		S3River model = new S3River("tmp", "mytoken", "mysecret", "bucket", "/tmp_es", 30L);
		
		XContentBuilder xb = new S3RiverHelper().toXContent(model);
		String jsonContent = xb.string();

		Map<String, Object> map = XContentHelper.convertToMap(jsonContent.getBytes(), 0, jsonContent.length(), false).v2();
		
		S3River river = new S3RiverHelper().toRiver(new S3River(), map);
		
		Assert.assertEquals(model.getId(), river.getId());
		Assert.assertEquals(model.getType(), river.getType());
		Assert.assertEquals(model.getAccessKey(), river.getAccessKey());
		Assert.assertEquals(model.getSecretKey(), river.getSecretKey());
        Assert.assertEquals(model.getBucket(), river.getBucket());
		Assert.assertEquals(model.getUrl(), river.getUrl());
		Assert.assertEquals(model.getUpdateRate(), river.getUpdateRate());

		Assert.assertEquals(model.getIncludes(), river.getIncludes());
		Assert.assertEquals(model.getExcludes(), river.getExcludes());

		Assert.assertEquals(model.getName(), river.getName());
		Assert.assertEquals(model.getTypename(), river.getTypename());
		Assert.assertEquals(model.getAnalyzer(), river.getAnalyzer());
	}
}
