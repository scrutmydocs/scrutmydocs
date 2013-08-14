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

package org.scrutmydocs.webapp.api.settings.rivers.fs.helper;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.scrutmydocs.webapp.api.settings.rivers.abstractfs.helper.AbstractFSRiverHelper;
import org.scrutmydocs.webapp.api.settings.rivers.fs.data.FSRiver;

import java.io.IOException;
import java.util.Map;

public class FSRiverHelper extends AbstractFSRiverHelper<FSRiver> {
	/**
	 * We build "fs" rivers.
	 */
	@Override
	public String type() {
		return "fs";
	}

	/**
     * We manage :
     * <ul>
     * <li>mode
     * <li>username
     * <li>password
     * </ul>
	 */
	@Override
	public XContentBuilder addFSMeta(XContentBuilder xcb, FSRiver river)
			throws IOException {
        xcb
                .field("protocol", river.getProtocol())
                .field("server", river.getServer())
                .field("username", river.getUsername())
                .field("password", river.getPassword());
		return xcb;
	}

	/**
	 * We manage :
	 * <ul>
     * <li>mode
     * <li>username
     * <li>password
	 * </ul>
	 * JSON definiton :<pre>
{
  "type" : "fs",
  "fs" : {
	  "update_rate" : 30000,
	  "name" : "tmp",
	  "protocol" : "local",
      "server" : "localhost",
	  "username" : "login",
      "password" : "password",
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
	public FSRiver parseFSMeta(FSRiver river, Map<String, Object> content) {
        river.setProtocol(getSingleStringValue("fs.protocol", content));
        river.setServer(getSingleStringValue("fs.server", content));
        river.setUsername(getSingleStringValue("fs.username", content));
        river.setPassword(getSingleStringValue("fs.password", content));
        return river;
	}
}
