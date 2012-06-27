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

package fr.issamax.essearch.api.facade;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.issamax.essearch.api.RestAPIException;
import fr.issamax.essearch.api.data.Document;
import fr.issamax.essearch.api.data.RestResponse;
import fr.issamax.essearch.api.service.RestDocumentService;

@Controller
@RequestMapping("/docs")
public class DocumentApi {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	RestDocumentService restDocumentService;

	@RequestMapping(method = RequestMethod.POST, value = "/doc")
	public @ResponseBody
	RestResponse<Document> push(@RequestBody Document doc) {
		doc = restDocumentService.push(doc);
		RestResponse<Document> response = new RestResponse<Document>(doc);
		return response;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/doc/{id}")
	public @ResponseBody
	RestResponse<?> delete(@PathVariable String id) {
		restDocumentService.delete(id);
		return new RestResponse<Object>();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/doc/{index}/{type}/{id}")
	public @ResponseBody
	Document get(@PathVariable String index, @PathVariable String type,
			@PathVariable String id) {
		return restDocumentService.get(index, type, id);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/doc/{index}/{type}/")
	public @ResponseBody
	RestResponse<?> createIndex(@PathVariable String index,
			@PathVariable String type) {
		try {
			restDocumentService.createIndex(index, type);
		} catch (RestAPIException e) {
			return new RestResponse<Object>(e);
		}
		
		return new RestResponse<Object>();
	}

}
