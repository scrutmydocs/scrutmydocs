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

package org.scrutmydocs.webapp.api.document.facade;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.api.common.data.Api;
import org.scrutmydocs.webapp.api.common.facade.CommonBaseApi;
import org.scrutmydocs.webapp.api.document.data.Document;
import org.scrutmydocs.webapp.api.document.data.RestResponseDocument;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.scrutmydocs.webapp.service.document.RestDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/doc")
public class DocumentApi extends CommonBaseApi {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	RestDocumentService restDocumentService;

	@Override
	public Api[] helpApiList() {
		Api[] apis = new Api[5];
		apis[0] = new Api("/doc", "POST", "Add a document to the search engine");
		apis[1] = new Api("/doc/{id}", "DELETE", "Delete a documentin the default index/type (doc/docs)");
		apis[1] = new Api("/doc/{index}/{id}", "DELETE", "Delete a document in the default type (doc)");
		apis[1] = new Api("/doc/{index}/{type}/{id}", "DELETE", "Delete a document ");
		apis[2] = new Api("/doc/{id}", "GET", "Get a document in the default index/type  (doc/docs)");
		apis[3] = new Api("/doc/{index}/{id}", "GET", "Get a document in a specific index with default type  (docs)");
		apis[4] = new Api("/doc/{index}/{type}/{id}", "GET", "Get a document in a specific index/type");
		return apis;
	}
	
	@Override
	public String helpMessage() {
		return "The /doc API helps you to manage your documents.";
	}
	
	/**
	 * Add a new document
	 * @param doc
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	RestResponseDocument push(@RequestBody Document doc) {
		try {
			doc = restDocumentService.push(doc);
		} catch (RestAPIException e) {
			return new RestResponseDocument(e);
		}
		RestResponseDocument response = new RestResponseDocument(doc);
		return response;
	}

	/**
	 * Delete an existing document
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public @ResponseBody
	RestResponseDocument delete(@PathVariable String id) {
		boolean result = restDocumentService.delete(null,null,id);
		RestResponseDocument response = new RestResponseDocument();
		response.setOk(result);
		return response;
	}


	/**
	 * Delete an existing document
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "{index}/{id}")
	public @ResponseBody
	RestResponseDocument delete(@PathVariable String index,@PathVariable String id) {
		boolean result = restDocumentService.delete(index,null,id);
		RestResponseDocument response = new RestResponseDocument();
		response.setOk(result);
		return response;
	}

	
	/**
	 * Delete an existing document
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "{index}/{type}/{id}")
	public @ResponseBody
	RestResponseDocument delete(@PathVariable String index,@PathVariable String type,@PathVariable String id) {
		boolean result = restDocumentService.delete(index,type,id);
		RestResponseDocument response = new RestResponseDocument();
		response.setOk(result);
		return response;
	}

	
	/**
	 * Get a document with its coordinates (index, type, id)
	 * @param index
	 * @param type
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{index}/{type}/{id}")
	public @ResponseBody
	Document get(@PathVariable String index, @PathVariable String type,
			@PathVariable String id) {
		return restDocumentService.get(index, type, id);
	}

	/**
	 * Get a document of type "doc" in a given index knowing its id
	 * @param index
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{index}/{id}")
	public @ResponseBody
	Document get(@PathVariable String index, @PathVariable String id) {
		return get(index, SMDSearchProperties.INDEX_TYPE_DOC, id);
	}

	/**
	 * Get a document in the default index/type (docs/doc) knowing its id
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public @ResponseBody
	Document get(@PathVariable String id) {
		return get(SMDSearchProperties.INDEX_NAME, SMDSearchProperties.INDEX_TYPE_DOC, id);
	}

}
