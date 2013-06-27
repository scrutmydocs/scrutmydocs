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

package org.scrutmydocs.webapp.api.settings.rivers.drive.helper;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.scrutmydocs.webapp.api.settings.rivers.abstractfs.helper.AbstractFSRiverHelper;
import org.scrutmydocs.webapp.api.settings.rivers.drive.data.DriveRiver;

/**
 * We manage here the clientId, clientSecret and refreshToken fields from the JSON definition :
 * {
     "type": "google-drive",
     "google-drive": {
       "clientId": "AAAAAAAAAAAAAAAA",
       "clientSecret": "BBBBBBBBBBBBBBBB",
       "refreshToken": "XXXXXXXXXXXXXXXX",
       "name": "My GDrive feed",
       "folder": "Work",
       "update_rate": 900000,
       "includes": "*.doc,*.pdf",
       "excludes": "*.zip,*.gz"
     },
     "index": {
       "index": "drivedocs",
       "type": "doc",
       "bulk_size": 50
     }
 * }
 * @author Laurent Broudoux
 */
public class DriveRiverHelper extends AbstractFSRiverHelper<DriveRiver> {

   @Override
   public String type() {
      return "google-drive";
   }
   
   @Override
   public XContentBuilder addFSMeta(XContentBuilder xcb, DriveRiver river) throws IOException {
      xcb.field("clientId", river.getClientId())
         .field("clientSecret", river.getClientSecret())
         .field("refreshToken", river.getRefreshToken())
         .field("folder", river.getUrl());
      return xcb;
   }
   
   @Override
   public DriveRiver parseFSMeta(DriveRiver river, Map<String, Object> content) {
      river.setClientId(getSingleStringValue("google-drive.clientId", content));
      river.setClientSecret(getSingleStringValue("google-drive.clientSecret", content));
      river.setRefreshToken(getSingleStringValue("google-drive.refreshToken", content));
      river.setUrl(getSingleStringValue("google-drive.folder", content));
      return river;
   }
}
