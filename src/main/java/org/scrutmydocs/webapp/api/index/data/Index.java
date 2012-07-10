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

package org.scrutmydocs.webapp.api.index.data;

import java.io.Serializable;

import org.scrutmydocs.webapp.constant.ESSearchProperties;


public class Index implements Serializable {
	private static final long serialVersionUID = 1L;

	private String index = null;
	private String type = null;
	private String analyzer = null;
	
	/**
	 * Default constructor :
	 * <br>Builds a docs/doc index/type with default analyzer (english)
	 */
	public Index() {
		this(ESSearchProperties.INDEX_NAME, ESSearchProperties.INDEX_TYPE_DOC, null);
	}
	
	/**
	 * @param index Index where will be stored your document (E.g.: mycompany)
	 * @param type Functionnal type of your document (E.g.: mybusinessunit)
	 * @param analyzer Analyzer we should apply on all docs (E.g: french)
	 */
	public Index(String index, String type, String analyzer) {
		super();
		this.index = index;
		this.type = type;
		this.analyzer = analyzer;
	}
	
	
	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the analyzer
	 */
	public String getAnalyzer() {
		return analyzer;
	}

	/**
	 * @param analyzer the analyzer to set
	 */
	public void setAnalyzer(String analyzer) {
		this.analyzer = analyzer;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Index)) return false;
		
		Index index = (Index) obj;
		
		if (this.index != index.index && this.index != null && !this.index.equals(index.index)) return false;
		if (this.type != index.type && this.type != null && !this.type.equals(index.type)) return false;
		if (this.analyzer != index.analyzer && this.analyzer != null && !this.analyzer.equals(index.analyzer)) return false;

		return true;
	}
	
}
