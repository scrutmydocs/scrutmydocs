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

package org.scrutmydocs.webapp.itest.api.settings.rivers;

import org.junit.Ignore;
import org.junit.Test;
import org.scrutmydocs.webapp.api.settings.rivers.drive.data.DriveRiver;
import org.scrutmydocs.webapp.api.settings.rivers.drive.data.RestResponseDriveRiver;
import org.scrutmydocs.webapp.api.settings.rivers.drive.data.RestResponseDriveRivers;


/**
 * Test for module "1/settings/rivers/drive/"
 * @author David Pilato
 */
public class DriveRiversApiTest extends AbstractFSRiversApiTest<DriveRiver, RestResponseDriveRiver, RestResponseDriveRivers> {
	@Override
	protected String apiVersion() {
		return "1";
	}
	

	@Override
	protected String type() {
		return "drive";
	}

	@Override
	protected DriveRiver buildRiverInstance(String name) {
		return new DriveRiver(name, "TOKEN", "SECRET", "REFRESH", "/thisdirshouldnotexist", 30L);
	}

	@Override
	protected Class<RestResponseDriveRiver> getClassForSingleResponse() {
		return RestResponseDriveRiver.class;
	}

	@Override
	protected Class<RestResponseDriveRivers> getClassForMultipleResponse() {
		return RestResponseDriveRivers.class;
	}
	
	/**
	 * TODO : Dropbox support is not yet implemented so we disabled "real" tests
	 */
	@Override
	@Test @Ignore
	public void delete_running_river_should_stop_it() throws Exception {
	}
	
	/**
	 * TODO : Dropbox support is not yet implemented so we disabled "real" tests
	 */
	@Override
	@Test @Ignore
	public void start_and_stop_river() throws Exception {
	}
	
	/**
	 * TODO : Dropbox support is not yet implemented so we disabled "real" tests
	 */
	@Override
	@Test @Ignore
	public void check_running_status_river() throws Exception {
	}
	
}
