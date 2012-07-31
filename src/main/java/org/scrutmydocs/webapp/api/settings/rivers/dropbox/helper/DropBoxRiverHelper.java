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

package org.scrutmydocs.webapp.api.settings.rivers.dropbox.helper;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.scrutmydocs.webapp.api.settings.rivers.abstractfs.helper.AbstractFSRiverHelper;
import org.scrutmydocs.webapp.api.settings.rivers.dropbox.data.DropBoxRiver;

import java.io.IOException;
import java.util.Map;

public class DropBoxRiverHelper extends AbstractFSRiverHelper<DropBoxRiver> {
    private final String appkey;
    private final String appsecret;

    public DropBoxRiverHelper(String appkey, String appsecret) {
        this.appkey = appkey;
        this.appsecret = appsecret;
    }

	/**
	 * We build "dropbox" rivers.
	 */
	@Override
	public String type() {
		return "dropbox";
	}


	/**
	 * We manage :
	 * <ul>
	 * <li>token
	 * <li>secret
	 * </ul>
	 */
	@Override
	public XContentBuilder addFSMeta(XContentBuilder xcb, DropBoxRiver river)
			throws IOException {
		xcb
            .field("appkey", appkey)
            .field("appsecret", appsecret)
			.field("token", river.getToken())
			.field("secret", river.getSecret());

		return xcb;
	}


	/**
	 * We manage :
	 * <ul>
	 * <li>token
	 * <li>secret
	 * </ul>
	 * JSON definiton :<pre>
{
  "type" : "dropbox",
  "dropbox" : {
	  "update_rate" : 30000,
	  "name" : "tmp",
	  "token" : "XXXXXXXXXXXXXX",
	  "secret" : "ZZZZZZZZZZZZZZ",
	  "url" : "/tmp_es",
	  "includes" : "*.doc,*.pdf",
	  "excludes" : "resume.*",
  },
  "index" : {
	  "index" : "docs",
	  "type" : "doc",
	  "analyzer" : "standard"
  }
}
</pre>
	 */
	@Override
	public DropBoxRiver parseFSMeta(DropBoxRiver river,
			Map<String, Object> content) {
		river.setToken(getSingleStringValue("dropbox.token", content));
		river.setSecret(getSingleStringValue("dropbox.secret", content));
		return river;
	}
}
