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

package fr.issamax.essearch.api.document.data;

import java.io.Serializable;

import fr.issamax.essearch.constant.ESSearchProperties;
import fr.issamax.essearch.util.StringTools;

public class Document implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id = null;
	private String index = null;
	private String type = null;
	private String name = null;
	private String contentType = null;
	private String content = null;
	
	/**
	 * Default constructor
	 */
	public Document() {
	}
	
	/**
	 * @param id Technical Unique ID for this document. May be null.
	 * @param index Index where will be stored your document (E.g.: mycompany)
	 * @param type Functionnal type of your document (E.g.: mybusinessunit)
	 * @param name Document fancy name
	 * @param contentType File ContentType. May be null. 
	 * @param content Base64 encoded file content
	 */
	public Document(String id, String index, String type, String name,
			String contentType, String content) {
		super();
		this.id = id;
		this.index = index;
		this.type = type;
		this.name = name;
		this.contentType = contentType;
		this.content = content;
	}
	
	
	/**
	 * @param id Technical Unique ID for this document. May be null.
	 * @param index Index where will be stored your document (E.g.: mycompany)
	 * @param type Functionnal type of your document (E.g.: mybusinessunit)
	 * @param name Document fancy name
	 * @param content Base64 encoded file content
	 */
	public Document(String id, String index, String type, String name, String content) {
		this(id, index, type, name, null, content);
	}
	
	/**
	 * @param index Index where will be stored your document (E.g.: mycompany)
	 * @param type Functionnal type of your document (E.g.: mybusinessunit)
	 * @param name Document fancy name
	 * @param content Base64 encoded file content
	 */
	public Document(String index, String type, String name, String content) {
		this(null, index, type, name, null, content);
	}
	
	/**
	 * @param name Document fancy name
	 * @param content Base64 encoded file content
	 */
	public Document(String name, String content) {
		this(null, ESSearchProperties.INDEX_NAME, ESSearchProperties.INDEX_TYPE_DOC, name, null, content);
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Document)) return false;
		
		Document doc = (Document) obj;
		
		if (this.id != doc.id && this.id != null && !this.id.equals(doc.id)) return false;
		if (this.index != doc.index && this.index != null && !this.index.equals(doc.index)) return false;
		if (this.type != doc.type && this.type != null && !this.type.equals(doc.type)) return false;
		if (this.name != doc.name && this.name != null && !this.name.equals(doc.name)) return false;
		if (this.contentType != doc.contentType && this.contentType != null && !this.contentType.equals(doc.contentType)) return false;
		if (this.content != doc.content && this.content != null && !this.content.equals(doc.content)) return false;

		return true;
	}
	
	@Override
	public String toString() {
		return StringTools.toString(this);
	}
}
