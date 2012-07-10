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

package org.scrutmydocs.webapp.constant;

public interface ESSearchProperties {

	public static final String INDEX_NAME = "docs";
	public static final String INDEX_TYPE_DOC = "doc";
	public static final String INDEX_TYPE_FOLDER = "folder";
	public static final String INDEX_TYPE_FS = "fsRiver";

	public static final String DOC_FIELD_NAME = "name";
	public static final String DOC_FIELD_DATE = "postDate";
	public static final String DOC_FIELD_PATH_ENCODED = "pathEncoded";
	public static final String DOC_FIELD_VIRTUAL_PATH = "virtualpath";
	public static final String DOC_FIELD_ROOT_PATH = "rootpath";
	
	public static final String DIR_FIELD_NAME = "name";
	public static final String DIR_FIELD_PATH_ENCODED = "pathEncoded";
	public static final String DIR_FIELD_VIRTUAL_PATH = "virtualpath";
	public static final String DIR_FIELD_ROOT_PATH = "rootpath";
	
	public static final String ES_META_INDEX = "essearchadmin";
	public static final String ES_META_RIVERS = "rivers";
	
}
