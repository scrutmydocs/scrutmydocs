package fr.issamax.essearch.admin.river.data;

import java.io.Serializable;

/**
 * Manage Filesystem Rivers metadata
 * @author PILATO
 *
 */
public class FSRiver implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String type;
	private String name;
	private String url;
	private Long updateRate;
	
	public FSRiver() {
	}
	
	/**
	 * @param type The river type
	 * @param name The human readable name for this river
	 * @param url URL where to fetch content
	 * @param updateRate Update Rate (in seconds)
	 */
	public FSRiver(String type, String name, String url, Long updateRate) {
		this.type = type;
		this.name = name;
		this.url = url;
		this.updateRate = updateRate;
	}

	/**
	 * @return The river type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @param type The river type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return The human readable name for this river
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name The human readable name for this river
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return URL where to fetch content
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * @param url URL where to fetch content
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * @return Update Rate (in seconds)
	 */
	public Long getUpdateRate() {
		return updateRate;
	}
	
	/**
	 * @param updateRate Update Rate (in seconds)
	 */
	public void setUpdateRate(Long updateRate) {
		this.updateRate = updateRate;
	}
}
