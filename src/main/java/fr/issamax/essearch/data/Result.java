package fr.issamax.essearch.data;

import java.util.ArrayList;
import java.util.Collection;

public class Result {
	
	
	protected String title;
	
	protected Collection<String> fragments=new ArrayList<String>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Collection<String> getFragments() {
		return fragments;
	}

	public void setFragments(Collection<String> fragments) {
		this.fragments = fragments;
	}

	

}
