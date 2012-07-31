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

package org.scrutmydocs.webapp.api.settings.rivers.dropbox.data;

import org.scrutmydocs.webapp.api.settings.rivers.abstractfs.data.AbstractFSRiver;
import org.scrutmydocs.webapp.util.StringTools;


/**
 * Manage DropBoxRiver Rivers metadata
 * @author PILATO
 *
 */
public class DropBoxRiver extends AbstractFSRiver {
	private static final long serialVersionUID = 1L;
	
	private String token;
	private String secret;

	/**
	 * We implement here a "dropbox" river
	 */
	@Override
	public String getType() {
		return "dropbox";
	}

	public DropBoxRiver() {
		this(null, null, "tmp", "/tmp", 60L);
	}
	
	/**
	 * @param id The unique id of this river
	 * @param token Dropbox Token
	 * @param secret Dropbox Secret
	 * @param url URL where to fetch content
	 * @param updateRate Update Rate (in seconds)
	 */
	public DropBoxRiver(String id, String token, String secret, String url, Long updateRate) {
		super(id, url, updateRate);
		this.token = token;
		this.secret = secret;
	}


	/**
	 * @param id The unique id of this river
	 * @param indexname The ES index where we store our docs
	 * @param typename The ES type we use to store docs
	 * @param name The human readable name for this river
	 * @param token Dropbox Token
	 * @param secret Dropbox Secret
	 * @param url URL where to fetch content
	 * @param updateRate Update Rate (in seconds)
	 * @param analyzer Analyzer to use
	 * @param started Is the river already started ?
	 */
	public DropBoxRiver(String id, String indexname, String typename, String name,
			String token, String secret, String url, Long updateRate, String analyzer, boolean started) {
		this(id, indexname, typename, name, token, secret, url, updateRate, null, null, analyzer, started);
	}

	/**
	 * @param id The unique id of this river
	 * @param indexname The ES index where we store our docs
	 * @param typename The ES type we use to store docs
	 * @param name The human readable name for this river
	 * @param token Dropbox Token
	 * @param secret Dropbox Secret
	 * @param url URL where to fetch content
	 * @param updateRate Update Rate (in seconds)
	 * @param includes Include list (comma separator)
	 * @param excludes Exclude list (comma separator)
	 * @param analyzer Analyzer to use
	 * @param started Is the river already started ?
	 */
	public DropBoxRiver(String id, String indexname, String typename, String name,
			String token, String secret, String url, Long updateRate, String includes, String excludes, String analyzer, boolean started) {
		super(id, indexname, typename, name, url, updateRate, includes, excludes, analyzer, started);
		this.token = token;
		this.secret = secret;
	}

	/**
	 * @return DropBox Token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token DropBox Token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return DropBox Secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @param secret DropBox Secret
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public String toString() {
		return StringTools.toString(this);
	}

}
