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

package fr.issamax.essearch.data.admin.river;

import java.io.Serializable;

import fr.issamax.essearch.constant.ESSearchProperties;

/**
 * Manage Abstract Rivers metadata
 * @author PILATO
 *
 */
public abstract class AbstractRiver implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String indexname;
	private String typename;
	private boolean start;
	
	/**
	 * @return The river implementation type, for example: fs, dropbox, rss
	 */
	public abstract String getType();
	
	/**
	 * Default constructor using a dummy name and defaults to
	 * index/type : docs/doc
	 */
	public AbstractRiver() {
		this("dummy");
	}
	
	/**
	 * Default constructor using a dummy name and defaults to
	 * index/type : docs/doc
	 */
	public AbstractRiver(String id) {
		this(id, ESSearchProperties.INDEX_NAME, ESSearchProperties.INDEX_TYPE_DOC, "My Dummy River", false);
	}
	
	/**
	 * @param id The unique id of this river
	 * @param indexname The ES index where we store our docs
	 * @param typename The ES type we use to store docs
	 * @param name The human readable name for this river
	 * @param start Started ?
	 */
	public AbstractRiver(String id, String indexname, String typename, String name, boolean start) {
		this.id = id;
		this.indexname = indexname;
		this.typename = typename;
		this.name = name;
		this.start = start;
	}

	/**
	 * @return The human readable name for this river
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The human readable name for this river
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The ES index where we store our docs
	 */
	public String getIndexname() {
		return indexname;
	}

	/**
	 * @param indexname The ES index where we store our docs
	 */
	public void setIndexname(String indexname) {
		this.indexname = indexname;
	}

	/**
	 * @return The ES type we use to store docs
	 */
	public String getTypename() {
		return typename;
	}

	/**
	 * @param typename The ES type we use to store docs
	 */
	public void setTypename(String typename) {
		this.typename = typename;
	}

	/**
	 * @param start The river state
	 */
	public void setStart(boolean start) {
		this.start = start;
	}
	
	/**
	 * @param start The river state
	 */
	public boolean isStart() {
		return start;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	
	 
}
