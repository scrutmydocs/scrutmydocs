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

package org.scrutmydocs.webapp.ui.admin.river.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.scrutmydocs.webapp.api.settings.rivers.fsriver.data.FSRiver;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.scrutmydocs.webapp.service.admin.river.AdminFSRiverService;
import org.scrutmydocs.webapp.service.admin.river.RiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component("settingsAction")
@Scope("session")
public class SettingsAction implements Serializable {

	private static final long serialVersionUID = -582985573033101693L;

	@Autowired RiverService riverService;
	@Autowired AdminFSRiverService adminService;

	List<FSRiver> fsRivers = new ArrayList<FSRiver>();

	FSRiver fsRiverSelect = new FSRiver("", SMDSearchProperties.INDEX_NAME,
			SMDSearchProperties.INDEX_TYPE_DOC, "", "", null, "standard", false);

	public void add() {
		riverService.start(fsRiverSelect);
		fsRivers.add(fsRiverSelect);
		fsRiverSelect = new FSRiver("", SMDSearchProperties.INDEX_NAME,
				SMDSearchProperties.INDEX_TYPE_DOC, "", "", null, "standard", false);
	}

	public String init() {
		fsRivers = adminService.get();
		return "settings";
	}

	public void resetFsRiverSelect() {
		fsRiverSelect =new FSRiver(null, SMDSearchProperties.INDEX_NAME,
				SMDSearchProperties.INDEX_TYPE_DOC, "", "", null, "standard", false);
	}

	public void update() {
		adminService.update(fsRiverSelect);
		riverService.start(fsRiverSelect);
		fsRivers = adminService.get();
	}

	public void remove() {
		riverService.stop(fsRiverSelect);
		adminService.remove(fsRiverSelect);
		fsRivers = adminService.get();
	}

	public void stop() {
		fsRiverSelect.setStart(false);
		adminService.update(fsRiverSelect);
		riverService.stop(fsRiverSelect);
	}

	public void start() {
		fsRiverSelect.setStart(true);
		adminService.update(fsRiverSelect);
		riverService.start(fsRiverSelect);
	}

	public List<FSRiver> getFsRivers() {
		return fsRivers;
	}

	public void setFsRivers(List<FSRiver> fsRivers) {
		this.fsRivers = fsRivers;
	}

	public FSRiver getFsRiverSelect() {
		return fsRiverSelect;
	}

	public void setFsRiverSelect(FSRiver fsRiverSelect) {
		this.fsRiverSelect = fsRiverSelect;
	}

}
