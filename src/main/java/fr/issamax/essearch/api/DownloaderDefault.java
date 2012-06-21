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

package fr.issamax.essearch.api;

import static fr.issamax.essearch.constant.ESSearchProperties.INDEX_NAME;
import static fr.issamax.essearch.constant.ESSearchProperties.INDEX_TYPE_DOC;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/download")
public class DownloaderDefault {

	@Autowired
	protected Client esClient;

	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public void get(@PathVariable final String id,HttpServletResponse response) throws IOException {
		

		
		GetResponse responseEs = esClient
				.prepareGet(INDEX_NAME, INDEX_TYPE_DOC, id)
				.execute().actionGet();
		if(!responseEs.isExists() )  {
			//TODO return a standard page who show a message like : this document is not available
			return;
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Object> attachment = (Map<String, Object>) responseEs
				.getSource().get("file");

		byte[] file = Base64.decode((String) attachment.get("content"));
		String name = (String) attachment.get("_name");
		String contentType = (String) attachment.get("_content_type");

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		file = Base64.decode((String) attachment.get("content"));
		response.setContentType(contentType);
		response.getOutputStream().write(file);
		response.getOutputStream().flush();
		response.getOutputStream().close();
		
//		return new ResponseEntity<byte[]>(file, headers, HttpStatus.CREATED);
		
	}
}