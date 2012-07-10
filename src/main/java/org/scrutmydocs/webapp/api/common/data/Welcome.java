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

package org.scrutmydocs.webapp.api.common.data;

import java.io.Serializable;

public class Welcome implements Serializable {
	private static final long serialVersionUID = 1L;

	private String version = null;
	private String message = null;
	private Api[] apis = null;
	
	/**
	 * Default constructor :
	 */
	public Welcome() {
		version = "0.0.1-SNAPSHOT";
		apis = new Api[4];
		apis[0] = new Api("/", "GET", "This API");
		apis[1] = new Api("_help", "GET", "Should give you help on each APIs.");
		apis[2] = new Api("/doc", "PUT/GET/DELETE", "Manage documents.");
		apis[3] = new Api("/index", "POST/DELETE", "Manage Indices");
	}
	
	/**
	 * @param message Message to display to user
	 */
	public Welcome(String message) {
		this();
		this.message = message;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the apis
	 */
	public Api[] getApis() {
		return apis;
	}

	/**
	 * @param apis the apis to set
	 */
	public void setApis(Api[] apis) {
		this.apis = apis;
	}


	
}
