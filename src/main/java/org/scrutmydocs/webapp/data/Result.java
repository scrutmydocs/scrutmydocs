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

package org.scrutmydocs.webapp.data;

import java.util.ArrayList;
import java.util.Collection;

import org.elasticsearch.search.SearchHit;

public class Result {
	protected String title;
	protected Collection<String> fragments = new ArrayList<String>();
	protected SearchHit searchHit;
	protected String virtualPath;
	protected String contentType;
	
	public Result(SearchHit searchHit) {
		this.searchHit = searchHit;
	}

	public Result(String title, String virtualPath) {
		this.title = title;
		this.virtualPath = virtualPath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Collection<String> getFragments() {
		return fragments;
	}

	public void setFragments(Collection<String> fragments) {
		this.fragments = fragments;
	}

	public SearchHit getSearchHit() {
		return searchHit;
	}

	public void setSearchHit(SearchHit searchHit) {
		this.searchHit = searchHit;
	}

	public String getVirtualPath() {
		return virtualPath;
	}

	public void setVirtualPath(String virtualPath) {
		this.virtualPath = virtualPath;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
