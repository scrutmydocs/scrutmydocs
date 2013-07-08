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

package org.scrutmydocs.webapp.api.settings.rivers.s3.facade;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.api.common.data.Api;
import org.scrutmydocs.webapp.api.common.facade.CommonBaseApi;
import org.scrutmydocs.webapp.api.settings.rivers.basic.data.BasicRiver;
import org.scrutmydocs.webapp.api.settings.rivers.s3.data.RestResponseS3River;
import org.scrutmydocs.webapp.api.settings.rivers.s3.data.RestResponseS3Rivers;
import org.scrutmydocs.webapp.api.settings.rivers.s3.data.S3River;
import org.scrutmydocs.webapp.service.settings.rivers.RiverService;
import org.scrutmydocs.webapp.service.settings.rivers.s3.AdminS3RiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Rest API definitions for managing Amazon S3 rivers.
 * @author Laurent Broudoux
 */
@Controller
@RequestMapping("/1/settings/rivers/s3")
public class S3RiversApi extends CommonBaseApi {

   /** A commons logger for diagnostic messages. */
   protected final Log logger = LogFactory.getLog(getClass());
   
   @Autowired
   protected RiverService riverService;
   @Autowired
   protected AdminS3RiverService adminService;
   
   @Override
   public Api[] helpApiList() {
      Api[] apis = new Api[7];
      apis[0] = new Api("/1/settings/rivers/s3", "GET", "Get all existing Amazon S3 rivers");
      apis[1] = new Api("/1/settings/rivers/s3/{name}", "GET", "Get details about an Amazon S3 river");
      apis[2] = new Api("/1/settings/rivers/s3", "PUT", "Create or update an Amazon S3 river");
      apis[3] = new Api("/1/settings/rivers/s3", "POST", "Create or update an Amazon S3 river");
      apis[4] = new Api("/1/settings/rivers/s3/{name}", "DELETE", "Delete an existing Amazon S3 river");
      apis[5] = new Api("/1/settings/rivers/s3/{name}/start", "GET", "Start a river");
      apis[6] = new Api("/1/settings/rivers/s3/{name}/stop", "GET", "Stop a river");
      return apis;
   }
   
   @Override
   public String helpMessage() {
      return "The /1/settings/rivers/s3 API manage Amazon S3 rivers.";
   }
   
   /**
    * Search for all Amazon S3 rivers.
    * @return Rest response containing list of available S3 rivers
    * @throws Exception if something goes wrong...
    */
   @RequestMapping(method = RequestMethod.GET)
   public @ResponseBody RestResponseS3Rivers get() throws Exception {
      List<S3River> rivers = null;
      
      return new RestResponseS3Rivers(rivers);
   }
   
   /**
    * Search for one specified S3 river.
    * @return Rest response representing S3 river
    * @throws Exception if something goes wrong...
    */
   @RequestMapping(value = "{id}", method = RequestMethod.GET)
   public @ResponseBody RestResponseS3River get(@PathVariable final String id) throws Exception {
      S3River river = null;
      try {
         river = adminService.get(id);
         if (river != null) {
            river.setStart(riverService.checkState(river));
         }
      } catch (Exception e) {
         return new RestResponseS3River(new RestAPIException(e));
      }
      
      return new RestResponseS3River(river);
   }

   /**
    * Create or Update a S3 river.
    * @return Rest response representing S3 river
    * @throws Exception if something goes wrong...
    */
   @RequestMapping(method = RequestMethod.PUT)
   public @ResponseBody RestResponseS3River put(@RequestBody S3River river) throws Exception {
      try {
         adminService.update(river);
      } catch (Exception e) {
         return new RestResponseS3River(new RestAPIException(e));
      }
      
      return new RestResponseS3River();
   }
   
   /**
    * Create or Update a S3 river.
    * @return Rest response representing S3 river
    * @throws Exception if something goes wrong...
    */
   @RequestMapping(method = RequestMethod.POST)
   public @ResponseBody RestResponseS3River push(@RequestBody S3River river) throws Exception {
      return put(river);
   }

   /**
    * Remove a S3 river.
    * @return Rest response representing empty S3 river
    * @throws Exception if something goes wrong...
    */
   @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
   public @ResponseBody RestResponseS3River delete(@PathVariable final String id) throws Exception {
      try {
         adminService.remove(new S3River(id, null, null, null, null, null));
      } catch (Exception e) {
         return new RestResponseS3River(new RestAPIException(e));
      }
      
      return new RestResponseS3River();
   }
   
   /**
    * Start a river.
    * @return Rest response representing empty S3 river
    * @throws Exception if something goes wrong...
    */
   @RequestMapping(value = "{id}/start", method = RequestMethod.GET)
   public @ResponseBody RestResponseS3River start(@PathVariable final String id) throws Exception {
      S3River river = null;
      try {
         river = adminService.get(id);
         if (river == null) {
            return new RestResponseS3River(new RestAPIException("River " + id + " does not exist."));
         }
         river.setStart(true);
         adminService.start(river);
      } catch (Exception e) {
         return new RestResponseS3River(new RestAPIException(e));
      }
      
      return new RestResponseS3River();
   }

   /**
    * Stop a river.
    * @return Rest response representing empty S3 river
    * @throws Exception if something goes wrong...
    */
   @RequestMapping(value = "{id}/stop", method = RequestMethod.GET)
   public @ResponseBody RestResponseS3River stop(@PathVariable final String id) throws Exception {
      BasicRiver river = null;
      try {
         river = adminService.get(id);
         if (river == null) {
            return new RestResponseS3River(new RestAPIException("River " + id + " does not exist."));
         }
         river.setStart(false);
         riverService.stop(river);
      } catch (Exception e) {
         return new RestResponseS3River(new RestAPIException(e));
      }
      
      return new RestResponseS3River();
   }
}
