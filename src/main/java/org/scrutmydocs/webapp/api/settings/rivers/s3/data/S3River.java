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

package org.scrutmydocs.webapp.api.settings.rivers.s3.data;

import org.scrutmydocs.webapp.api.settings.rivers.abstractfs.data.AbstractFSRiver;
import org.scrutmydocs.webapp.util.StringTools;

/**
 * Manage Amazon S3 river metadata.
 * @author Laurent Broudoux
 */
public class S3River extends AbstractFSRiver {

   private String accessKey;
   private String secretKey;
   private String bucket;
   
   public S3River() {
      this(null, null, null, "bucket", "path/to/folder/", 60L);
   }
   
   public S3River(String id, String accessKey, String secretKey, String bucket, String pathPrefix, Long updateRate) {
      super(id, pathPrefix, updateRate);
      this.accessKey = accessKey;
      this.secretKey = secretKey;
      this.bucket = bucket;
   }
   
   /**
    * We implement here a "amazon-s3" river.
    */
   @Override
   public String getType() {
      return "amazon-s3";
   }
   
   @Override
   public String toString() {
      return StringTools.toString(this);
   }

   public String getAccessKey() {
      return accessKey;
   }

   public void setAccessKey(String accessKey) {
      this.accessKey = accessKey;
   }

   public String getSecretKey() {
      return secretKey;
   }

   public void setSecretKey(String secretKey) {
      this.secretKey = secretKey;
   }

   public String getBucket() {
      return bucket;
   }

   public void setBucket(String bucket) {
      this.bucket = bucket;
   }
}
