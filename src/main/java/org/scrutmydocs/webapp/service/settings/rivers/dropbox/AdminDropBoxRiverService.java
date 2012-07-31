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

package org.scrutmydocs.webapp.service.settings.rivers.dropbox;

import org.scrutmydocs.webapp.api.settings.rivers.AbstractRiverHelper;
import org.scrutmydocs.webapp.api.settings.rivers.dropbox.data.DropBoxRiver;
import org.scrutmydocs.webapp.api.settings.rivers.dropbox.helper.DropBoxRiverHelper;
import org.scrutmydocs.webapp.configuration.ScrutMyDocsProperties;
import org.scrutmydocs.webapp.service.settings.rivers.AdminRiverAbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * River Service Implementation for DropBox Rivers
 * @author PILATO
 *
 */
@Component
public class AdminDropBoxRiverService extends AdminRiverAbstractService<DropBoxRiver> {
	private static final long serialVersionUID = 1L;

    @Autowired
    ScrutMyDocsProperties props;

	@Override
	public AbstractRiverHelper<DropBoxRiver> getHelper() {
		return new DropBoxRiverHelper(props.getDropboxKey(), props.getDropboxSecret());
	}

	@Override
	public DropBoxRiver buildInstance() {
		return new DropBoxRiver();
	}

}
