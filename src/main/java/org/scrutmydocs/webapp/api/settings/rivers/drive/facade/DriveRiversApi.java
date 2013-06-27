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

package org.scrutmydocs.webapp.api.settings.rivers.drive.facade;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.api.common.data.Api;
import org.scrutmydocs.webapp.api.common.facade.CommonBaseApi;
import org.scrutmydocs.webapp.api.settings.rivers.basic.data.BasicRiver;
import org.scrutmydocs.webapp.api.settings.rivers.drive.data.DriveRiver;
import org.scrutmydocs.webapp.api.settings.rivers.drive.data.RestResponseDriveRiver;
import org.scrutmydocs.webapp.api.settings.rivers.drive.data.RestResponseDriveRivers;
import org.scrutmydocs.webapp.service.settings.rivers.RiverService;
import org.scrutmydocs.webapp.service.settings.rivers.drive.AdminDriveRiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Rest API definitions for managing Google Drive rivers.
 * @author Laurent Broudoux
 */
@Controller
@RequestMapping("/1/settings/rivers/drive")
public class DriveRiversApi extends CommonBaseApi {

   /** Commons logger for diagnostic messages. */
   protected final Log logger = LogFactory.getLog(getClass());
   
   @Autowired
   protected RiverService riverService;
   @Autowired
   protected AdminDriveRiverService adminService;
   
   @Override
   public String helpMessage() {
      return "The /1/settings/rivers/drive API manage Google Drive rivers.";
   }

   @Override
   public Api[] helpApiList() {
      Api[] apis = new Api[7];
      apis[0] = new Api("/1/settings/rivers/drive", "GET", "Get all existing Google Drive rivers");
      apis[1] = new Api("/1/settings/rivers/drive/{name}", "GET", "Get details about a Google Drive river");
      apis[2] = new Api("/1/settings/rivers/drive", "PUT", "Create or update a Google Drive river");
      apis[3] = new Api("/1/settings/rivers/drive", "POST", "Create or update a Google Drive river");
      apis[4] = new Api("/1/settings/rivers/drive/{name}", "DELETE", "Delete an existing Google Drive river");
      apis[5] = new Api("/1/settings/rivers/drive/{name}/start", "GET", "Start a Google Drive river");
      apis[6] = new Api("/1/settings/rivers/drive/{name}/stop", "GET", "Stop a Google Drive river");
      return apis;
   }

   /**
    * Search for all Google Drive rivers.
    * @return Rest response containing list of available Drive rivers
    * @throws Exception if something goes wrong...
    */
   @RequestMapping(method = RequestMethod.GET)
   public @ResponseBody RestResponseDriveRivers get() throws Exception {
      List<DriveRiver> rivers = null;
      try {
         rivers = adminService.get();
         
         // For each river, we must look if it's running or not.
         for (BasicRiver river : rivers) {
            river.setStart(riverService.checkState(river));
         }
      } catch (Exception e){
         return new RestResponseDriveRivers(new RestAPIException(e));
      }
      return new RestResponseDriveRivers(rivers);
   }
   
   /**
    * Serah for a specified Google Drive river.
    * @param id Identifier of river to search for
    * @return Rest response representing Drive river
    * @throws Exception if something goes wrong...
    */
   @RequestMapping(value = "{id}", method = RequestMethod.GET)
   public @ResponseBody RestResponseDriveRiver get(@PathVariable final String id) throws Exception {
      DriveRiver river = null;
      try {
         river = adminService.get(id);
         
         // If found, look if it's running or not.
         if (river != null){
            river.setStart(riverService.checkState(river));
         }
      } catch (Exception e){
         return new RestResponseDriveRiver(new RestAPIException(e));
      }
      return new RestResponseDriveRiver(river);
   }
   
   /**
    * Create or Update a Google Drive river.
    * @return Rest response representing updated river.
    * @throws Exception if something goes wrong...
    */
   @RequestMapping(method = RequestMethod.PUT)
   public @ResponseBody RestResponseDriveRiver put(@RequestBody DriveRiver river) throws Exception {
      try {
         adminService.update(river);
      } catch (Exception e){
         return new RestResponseDriveRiver(new RestAPIException(e));
      }
      return new RestResponseDriveRiver(river);
   }
   
   /**
    * Create a Google Drive river.
    * @return Rest response representing updated river.
    * @throws Exception if something goes wrong...
    */
   @RequestMapping(method = RequestMethod.POST)
   public @ResponseBody RestResponseDriveRiver post(@RequestBody DriveRiver river) throws Exception {
      return put(river);
   }

   /**
    * Remove the specified Google Drive river.
    * @param id Identifier of river to search for
    * @return Rest response representing empty river
    * @throws Exception if something goes wrong...
    */
   @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
   public @ResponseBody RestResponseDriveRiver delete(@PathVariable final String id) throws Exception {
      try {
         adminService.remove(new DriveRiver(id, null, null, null, null, null));
      }
      catch (Exception e){
         return new RestResponseDriveRiver(new RestAPIException(e));
      }
      return new RestResponseDriveRiver();
   }
   
   /**
    * Start the specified Google Drive river.
    * @param id Identifier of river to search for
    * @return Rest response representing empty river
    * @throws Exception if something goes wrong...
    */
   @RequestMapping(value = "{id}/start", method = RequestMethod.GET)
   public @ResponseBody RestResponseDriveRiver start(@PathVariable final String id) throws Exception {
      DriveRiver river = null;
      try {
         river = adminService.get(id);
         if (river == null){
            return new RestResponseDriveRiver(new RestAPIException("River " + id + " does not exist."));
         }
         river.setStart(true);
         adminService.start(river);
      }
      catch (Exception e){
         return new RestResponseDriveRiver(new RestAPIException(e));
      } 
      return new RestResponseDriveRiver();
   }
   
   /**
    * Stop the specified Google Drive river.
    * @param id Identifier of river to search for
    * @return Rest response representing empty river
    * @throws Exception if something goes wrong...
    */
   @RequestMapping(value = "{id}/stop", method = RequestMethod.GET)
   public @ResponseBody RestResponseDriveRiver stop(@PathVariable final String id) throws Exception {
      DriveRiver river = null;
      try {
         river = adminService.get(id);
         if (river == null){
            return new RestResponseDriveRiver(new RestAPIException("River " + id + " does not exist."));
         }
         river.setStart(false);
         adminService.stop(river);
      }
      catch (Exception e){
         return new RestResponseDriveRiver(new RestAPIException(e));
      } 
      return new RestResponseDriveRiver();
   }
}
