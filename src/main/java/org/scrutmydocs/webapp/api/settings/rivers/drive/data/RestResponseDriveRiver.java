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

import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.api.common.data.RestResponse;

/**
 * A RestResponse for Google Drive river.
 * @author Laurent Broudoux
 */
public class RestResponseDriveRiver extends RestResponse<DriveRiver> {

   /**
    * Default serial version UID.
    */
   private static final long serialVersionUID = 1L;

   public RestResponseDriveRiver() {
      super();
   }
   
   public RestResponseDriveRiver(DriveRiver river) {
      super(river);
   }
   
   public RestResponseDriveRiver(RestAPIException e) {
      super(e);
   }
}
