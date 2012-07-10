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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.scrutmydocs.webapp.api.common.RestAPIException;


public class RestResponse<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean ok;
	private String errors[];
	private T object;

	/**
	 * Default constructor : we suppose here that ok = true
	 */
	public RestResponse() {
		this.ok = true;
	}
	
	/**
	 * We can get an object back, so there is no error and ok=true
	 * @param object
	 */
	public RestResponse(T object) {
		this.object = object;
		this.ok = true;
	}


	/**
	 * We build a response when we have errors
	 * @param errors
	 */
	public RestResponse(String[] errors) {
		this.errors = errors;
		this.ok = false;
	}

	/**
	 * We build a response when we have a single exception
	 * @param e
	 */
	public RestResponse(RestAPIException e) {
		addError(e);
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String[] getErrors() {
		return errors;
	}

	public void setErrors(String[] errors) {
		this.errors = errors;
	}

	public Object getObject() {
		return object;
	}
	
	public void setObject(T object) {
		this.object = object;
	}
	
	/**
	 * Add an error to the error list
	 * @param e
	 */
	public void addError(RestAPIException e) {
		this.ok = false;
		Collection<String> errs = new ArrayList<String>();
		if (this.errors != null) Collections.addAll(errs, this.errors);
		errs.add(e.getMessage());
	}
}
