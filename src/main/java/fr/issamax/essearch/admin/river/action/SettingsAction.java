package fr.issamax.essearch.admin.river.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.admin.river.data.FSRiver;

@Component("settingsAction")
@Scope("request")
public class SettingsAction {

	List<FSRiver> fsRivers = new ArrayList<FSRiver>();

	public SettingsAction() {

	}

	public List<FSRiver> getFsRivers() {
		return fsRivers;
	}

	public void setFsRivers(List<FSRiver> fsRivers) {
		this.fsRivers = fsRivers;
	}
	
	public void ajoueter(){
		
		this.fsRivers.add(new FSRiver("", "", "",null));
	}
}
