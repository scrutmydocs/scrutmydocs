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

package org.scrutmydocs.webapp.api.settings.rivers.drive.data;

import org.scrutmydocs.webapp.api.settings.rivers.abstractfs.data.AbstractFSRiver;
import org.scrutmydocs.webapp.util.StringTools;

/**
 * Manage Google Drive river metadata.
 * @author Laurent Broudoux
 */
public class DriveRiver extends AbstractFSRiver {

   /**
    * Default serial version UID.
    */
   private static final long serialVersionUID = 1L;

   private String clientId;
   private String clientSecret;
   private String refreshToken;
   
   public DriveRiver() {
      this(null, null, null, "refresh", "folder", 60L);
   }
   
   public DriveRiver(String id, String clientId, String clientSecret, String refreshToken, String folder, Long updateRate) {
      super(id, folder, updateRate);
      this.clientId = clientId;
      this.clientSecret = clientSecret;
      this.refreshToken = refreshToken;
   }
   
   /**
    * We implement here a "google-drive" river
    */
   @Override
   public String getType() {
      return "google-drive";
   }
   
   @Override
   public String toString() {
      return StringTools.toString(this);
   }

   public String getClientId() {
      return clientId;
   }

   public void setClientId(String clientId) {
      this.clientId = clientId;
   }

   public String getClientSecret() {
      return clientSecret;
   }

   public void setClientSecret(String clientSecret) {
      this.clientSecret = clientSecret;
   }

   public String getRefreshToken() {
      return refreshToken;
   }

   public void setRefreshToken(String refreshToken) {
      this.refreshToken = refreshToken;
   }
}
