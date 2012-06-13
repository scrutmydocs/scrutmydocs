package fr.issamax.essearch.admin.river.action;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.river.fs.FsRiver;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("settingsAction")
@Scope("request")
public class SettingsAction {

	List<FsRiver> fsRivers = new ArrayList<FsRiver>();

	public SettingsAction() {

	}

	public List<FsRiver> getFsRivers() {
		return fsRivers;
	}

	public void setFsRivers(List<FsRiver> fsRivers) {
		this.fsRivers = fsRivers;
	}
}
