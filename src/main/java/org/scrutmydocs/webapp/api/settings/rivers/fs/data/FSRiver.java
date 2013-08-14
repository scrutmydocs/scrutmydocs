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

package org.scrutmydocs.webapp.api.settings.rivers.fs.data;

import fr.pilato.elasticsearch.river.fs.river.FsRiver;
import org.scrutmydocs.webapp.api.settings.rivers.abstractfs.data.AbstractFSRiver;
import org.scrutmydocs.webapp.util.StringTools;


/**
 * Manage Filesystem Rivers metadata
 * @author PILATO
 *
 */
public class FSRiver extends AbstractFSRiver {
	private static final long serialVersionUID = 1L;

    private String protocol;
    private String server;
    private String username;
    private String password;

    /**
	 * We implement here a "fs" river
	 */
	@Override
	public String getType() {
		return "fs";
	}

	public FSRiver() {
		this("tmp", "/tmp", 60L);
	}
	
	/**
	 * @param id The unique id of this river
	 * @param url URL where to fetch content
	 * @param updateRate Update Rate (in seconds)
	 */
	public FSRiver(String id, String url, Long updateRate) {
		super(id, url, updateRate);
        protocol = FsRiver.PROTOCOL.LOCAL;
        username = null;
        password = null;
    }

	/**
	 * @param id The unique id of this river
	 * @param indexname The ES index where we store our docs
	 * @param typename The ES type we use to store docs
	 * @param name The human readable name for this river
     * @param protocol local or ssh
     * @param server server for ssh
     * @param username username for ssh
     * @param password password for ssh
	 * @param url URL where to fetch content
	 * @param updateRate Update Rate (in seconds)
	 * @param includes Include list (comma separator)
	 * @param excludes Exclude list (comma separator)
	 * @param analyzer Analyzer to use
	 * @param started Is the river already started ?
	 */
	public FSRiver(String id, String indexname, String typename, String name,
			String server, String protocol, String username, String password,
            String url, Long updateRate, String includes, String excludes, String analyzer, boolean started) {
		super(id, indexname, typename, name, url, updateRate, includes, excludes, analyzer, started);
        this.protocol = protocol;
        this.server = server;
        this.username = username;
        this.password = password;
	}

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
	public String toString() {
		return StringTools.toString(this);
	}

}
