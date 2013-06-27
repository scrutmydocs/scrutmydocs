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

package org.scrutmydocs.webapp.service.settings.rivers.drive;

import org.scrutmydocs.webapp.api.settings.rivers.AbstractRiverHelper;
import org.scrutmydocs.webapp.api.settings.rivers.drive.data.DriveRiver;
import org.scrutmydocs.webapp.api.settings.rivers.drive.helper.DriveRiverHelper;
import org.scrutmydocs.webapp.service.settings.rivers.AdminRiverAbstractService;
import org.springframework.stereotype.Component;

/**
 * An admin river service implementation for Google Drive rivers.
 * @author Laurent Broudoux
 */
@Component
public class AdminDriveRiverService extends AdminRiverAbstractService<DriveRiver> {

   /**
    * Default serial version UID.
    */
   private static final long serialVersionUID = 1L;

   @Override
   public AbstractRiverHelper<DriveRiver> getHelper() {
      return new DriveRiverHelper();
   }

   @Override
   public DriveRiver buildInstance() {
      return new DriveRiver();
   }

}
