package fr.issamax.essearch.admin.river.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.admin.river.data.FSRiver;
import fr.issamax.essearch.admin.river.service.RiverService;
import fr.issamax.essearch.constant.ESSearchProperties;

@Component("settingsAction")
@Scope("session")
public class SettingsAction implements Serializable {

	private static final long serialVersionUID = -582985573033101693L;

	@Autowired
	RiverService riverService;

	List<FSRiver> fsRivers = new ArrayList<FSRiver>();

	FSRiver fsRiverSelect = new FSRiver(ESSearchProperties.INDEX_NAME,
			ESSearchProperties.INDEX_TYPE_DOC, "", "", "", null);

	public void add() {

		riverService.update(fsRiverSelect);
		fsRivers.add(fsRiverSelect);
		fsRiverSelect = new FSRiver(ESSearchProperties.INDEX_NAME,
				ESSearchProperties.INDEX_TYPE_DOC, "", "", "", null);

	}

	public String init() {

		fsRivers = riverService.get();
		return "settings";
	}

	public void resetFsRiverSelect() {
		fsRiverSelect = new FSRiver();
	}

	public void update() {
		riverService.update(fsRiverSelect);
	}

	public void reomove() {
		riverService.remove(fsRiverSelect);
	}

	public void stop() {
		fsRiverSelect.setStart(false);
//		riverService.stop();
	}

	public void start() {
		fsRiverSelect.setStart(true);
//		riverService.start();
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
