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

package org.scrutmydocs.webapp.api.settings.rivers.s3.helper;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.scrutmydocs.webapp.api.settings.rivers.abstractfs.helper.AbstractFSRiverHelper;
import org.scrutmydocs.webapp.api.settings.rivers.s3.data.S3River;

/**
 * We manage here the accessKey, secretKey and bucket from the JSON definition :
 * {
      "type": "amazon-s3",
         "amazon-s3": {
          "accessKey": "AAAAAAAAAAAAAAAA",
          "secretKey": "BBBBBBBBBBBBBBBB",
          "name": "My Amazon S3 feed",
          "bucket" : "myownbucket"
          "pathPrefix": "Work/",
          "update_rate": 900000,
          "includes": "*.doc,*.pdf",
          "excludes": "*.zip,*.gz"
       },
       "index": {
         "index": "drivedocs",
         "type": "doc",
         "bulk_size": 50
      }
   }'
 * @author Laurent Broudoux
 */
public class S3RiverHelper extends AbstractFSRiverHelper<S3River> {

   @Override
   public String type() {
      return "amazon-s3";
   }
   
   @Override
   public XContentBuilder addFSMeta(XContentBuilder xcb, S3River river) throws IOException {
      xcb.field("accessKey", river.getAccessKey())
         .field("secretKey", river.getSecretKey())
         .field("bucket", river.getBucket())
         .field("pathPrefix", river.getUrl());
      return xcb;
   }
   
   @Override
   public S3River parseFSMeta(S3River river, Map<String, Object> content) {
      river.setAccessKey(getSingleStringValue("amazon-s3.accessKey", content));
      river.setSecretKey(getSingleStringValue("amazon-s3.secretKey", content));
      river.setBucket(getSingleStringValue("amazon-s3.bucket", content));
      river.setUrl(getSingleStringValue("amazon-s3.pathPrefix", content));
      return null;
   }
}
