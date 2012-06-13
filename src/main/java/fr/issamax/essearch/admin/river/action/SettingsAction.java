package fr.issamax.essearch.admin.river.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.admin.river.data.FSRiver;
import fr.issamax.essearch.admin.river.service.RiverService;

@Component("settingsAction")
@Scope("session")
public class SettingsAction {

	@Autowired
	RiverService riverService;

	List<FSRiver> fsRivers = new ArrayList<FSRiver>();

	FSRiver fsRiverSelect = new FSRiver("", "", "", null);

	public void add() {

		riverService.update(fsRiverSelect);
		fsRivers.add(fsRiverSelect);
		fsRiverSelect = new FSRiver("", "", "", null);

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
