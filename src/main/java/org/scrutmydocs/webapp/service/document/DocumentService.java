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

package org.scrutmydocs.webapp.service.document;

import fr.pilato.elasticsearch.river.fs.river.SignTool;
import fr.pilato.elasticsearch.river.fs.util.FsRiverUtil;
import org.apache.tika.metadata.Metadata;
import org.apache.xmlbeans.impl.util.Base64;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.BytesStreamInput;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.api.document.data.Document;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

import static fr.pilato.elasticsearch.river.fs.river.TikaInstance.tika;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


@Component
public class DocumentService {

	private static final long serialVersionUID = 1L;

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	@Autowired
	Client client;

	@SuppressWarnings("unchecked")
	public Document get(String index, String type, String id) {
		GetResponse responseEs = client.prepareGet(index, type, id).execute()
				.actionGet();
		if (!responseEs.isExists()) {
			return null;
		}

		Document response = new Document();

		Map<String, Object> attachment = (Map<String, Object>) responseEs
				.getSource().get("file");

		response.setContent((String) attachment.get("content"));
		response.setName((String) attachment.get("_name"));
		response.setContentType((String) attachment.get("_content_type"));
		response.setId(responseEs.getId());
		response.setIndex(responseEs.getIndex());
		response.setType(responseEs.getType());

		return response;
	}

	public boolean delete(String index, String type, String id) {
		if (index == null) {
			index = SMDSearchProperties.INDEX_NAME;
		}
		if (type == null) {
			type = SMDSearchProperties.INDEX_TYPE_DOC;
		}

		DeleteResponse response = client.prepareDelete(index, type, index)
				.execute().actionGet();

		return response.isFound();
	}

	public Document push(Document document) throws RestAPIException {
		if (logger.isDebugEnabled()) logger.debug("push({})", document);
		if (document == null)
			return null;

		if (document.getIndex() == null || document.getIndex().isEmpty()) {
			document.setIndex(SMDSearchProperties.INDEX_NAME);
		}
		if (document.getType() == null || document.getType().isEmpty()) {
			document.setType(SMDSearchProperties.INDEX_TYPE_DOC);
		}
		try {
			IndexResponse response = client
					.prepareIndex(document.getIndex(), document.getType(),
							document.getId())
					.setSource(extractDocument(document)).execute()
					.actionGet();

			document.setId(response.getId());
		} catch (Exception e) {
			logger.warn("Can not index document {}", document.getName());
			throw new RestAPIException("Can not index document : "+ document.getName() + ": "+e.getMessage());
		}

		if (logger.isDebugEnabled()) logger.debug("/push()={}", document);
		return document;
	}

    public XContentBuilder extractDocument(Document document) throws IOException, NoSuchAlgorithmException {
        // Extracting content with Tika
        int indexedChars = 100000;
        Metadata metadata = new Metadata();

        String parsedContent;
        try {
            // Set the maximum length of strings returned by the parseToString method, -1 sets no limit
            parsedContent = tika().parseToString(new BytesStreamInput(
                    Base64.decode(document.getContent().getBytes()), false), metadata, indexedChars);
        } catch (Throwable e) {
            logger.debug("Failed to extract [" + indexedChars + "] characters of text for [" + document.getName() + "]", e);
            parsedContent = "";
        }

        XContentBuilder source = jsonBuilder().startObject();

        if (logger.isTraceEnabled()) {
            source.prettyPrint();
        }

        // File
        source
                .startObject(FsRiverUtil.Doc.FILE)
                .field(FsRiverUtil.Doc.File.FILENAME, document.getName())
                .field(FsRiverUtil.Doc.File.LAST_MODIFIED, new Date())
                .field(FsRiverUtil.Doc.File.INDEXING_DATE, new Date())
                .field(FsRiverUtil.Doc.File.CONTENT_TYPE, document.getContentType() != null ? document.getContentType() : metadata.get(Metadata.CONTENT_TYPE))
                .field(FsRiverUtil.Doc.File.URL, "file://" + (new File(".", document.getName())).toString());

        if (metadata.get(Metadata.CONTENT_LENGTH) != null) {
            // We try to get CONTENT_LENGTH from Tika first
            source.field(FsRiverUtil.Doc.File.FILESIZE, metadata.get(Metadata.CONTENT_LENGTH));
        } else {
            // Otherwise, we use our byte[] length
            source.field(FsRiverUtil.Doc.File.FILESIZE, Base64.decode(document.getContent().getBytes()).length);
        }
        source.endObject(); // File

        // Path
        source
                .startObject(FsRiverUtil.Doc.PATH)
                .field(FsRiverUtil.Doc.Path.ENCODED, SignTool.sign("."))
                .field(FsRiverUtil.Doc.Path.ROOT, ".")
                .field(FsRiverUtil.Doc.Path.VIRTUAL, ".")
                .field(FsRiverUtil.Doc.Path.REAL, (new File(".", document.getName())).toString())
                .endObject(); // Path

        // Meta
        source
                .startObject(FsRiverUtil.Doc.META)
                .field(FsRiverUtil.Doc.Meta.AUTHOR, metadata.get(Metadata.AUTHOR))
                .field(FsRiverUtil.Doc.Meta.TITLE, metadata.get(Metadata.TITLE) != null ? metadata.get(Metadata.TITLE) : document.getName())
                .field(FsRiverUtil.Doc.Meta.DATE, metadata.get(Metadata.DATE))
                .array(FsRiverUtil.Doc.Meta.KEYWORDS, Strings.commaDelimitedListToStringArray(metadata.get(Metadata.KEYWORDS)))
                .endObject(); // Meta

        // Doc content
        source.field(FsRiverUtil.Doc.CONTENT, parsedContent);

        // Doc as binary attachment
        source.field(FsRiverUtil.Doc.ATTACHMENT, document.getContent());

        // End of our document
        source.endObject();

        return source;
    }
}
