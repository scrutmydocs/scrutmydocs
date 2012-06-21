/*
 * Licensed to David Pilato and Malloum Laya (the "Authors") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Authors licenses this
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

package fr.issamax.essearch.admin.river.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.admin.river.data.FSRiver;
import fr.issamax.essearch.admin.river.service.AdminService;
import fr.issamax.essearch.admin.river.service.RiverService;
import fr.issamax.essearch.constant.ESSearchProperties;

@Component("settingsAction")
@Scope("session")
public class SettingsAction implements Serializable {

	private static final long serialVersionUID = -582985573033101693L;

	@Autowired RiverService riverService;
	@Autowired AdminService adminService;

	List<FSRiver> fsRivers = new ArrayList<FSRiver>();

	FSRiver fsRiverSelect = new FSRiver("", ESSearchProperties.INDEX_NAME,
			ESSearchProperties.INDEX_TYPE_DOC, "", "", "", null, "standard", false);

	public void add() {
		riverService.add(fsRiverSelect);
		fsRivers.add(fsRiverSelect);
		fsRiverSelect = new FSRiver("", ESSearchProperties.INDEX_NAME,
				ESSearchProperties.INDEX_TYPE_DOC, "", "", "", null, "standard", false);
	}

	public String init() {
		fsRivers = adminService.get();
		return "settings";
	}

	public void resetFsRiverSelect() {
		fsRiverSelect =new FSRiver(null, ESSearchProperties.INDEX_NAME,
				ESSearchProperties.INDEX_TYPE_DOC, "", "", "", null, "standard", false);
	}

	public void update() {
		adminService.update(fsRiverSelect);
		riverService.add(fsRiverSelect);
		fsRivers = adminService.get();
	}

	public void remove() {
		riverService.delete(fsRiverSelect);
		adminService.remove(fsRiverSelect);
		fsRivers = adminService.get();
	}

	public void stop() {
		fsRiverSelect.setStart(false);
		adminService.update(fsRiverSelect);
		riverService.delete(fsRiverSelect);
	}

	public void start() {
		fsRiverSelect.setStart(true);
		adminService.update(fsRiverSelect);
		riverService.add(fsRiverSelect);
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
