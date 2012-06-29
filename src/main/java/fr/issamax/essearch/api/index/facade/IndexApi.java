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

package fr.issamax.essearch.api.index.facade;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.issamax.essearch.api.common.RestAPIException;
import fr.issamax.essearch.api.index.data.Index;
import fr.issamax.essearch.api.index.data.RestResponseIndex;
import fr.issamax.essearch.api.index.service.RestIndexService;
import fr.issamax.essearch.constant.ESSearchProperties;

/**
 * Index RESTFul API
 * <ul>
 * <li>PUT : create a new index to hold documents
 * <li>DELETE : remove an index
 * </ul>
 * @author David Pilato
 *
 */
@Controller
@RequestMapping("/index")
public class IndexApi {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	RestIndexService restIndexService;

	@RequestMapping(method = RequestMethod.POST, value = "/{index}/{type}")
	public @ResponseBody
	RestResponseIndex create(@PathVariable String index, 
			@PathVariable String type,
			@RequestBody Index settings) {
		try {
			String analyzer = null;
			
			if (settings != null) {
				if (index == null) {
					index = settings.getIndex();
				} else {
					settings.setIndex(index);
				}
				if (type == null) {
					type = settings.getType();
				} else {
					settings.setType(type);
				}
				analyzer = settings.getAnalyzer();
			}
			restIndexService.createIndex(index, type, analyzer);
		} catch (RestAPIException e) {
			return new RestResponseIndex(e);
		}
		
		return new RestResponseIndex(settings);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{index}")
	public @ResponseBody
	RestResponseIndex create(@PathVariable String index, 
			@RequestBody Index settings) {
		return create(index, ESSearchProperties.INDEX_TYPE_DOC, settings);
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	RestResponseIndex create(@RequestBody Index settings) {
		return create(null, null, settings);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{index}")
	public @ResponseBody
	RestResponseIndex delete(@PathVariable String index) {
		restIndexService.delete(index);
		return new RestResponseIndex();
	}

}
