package fr.issamax.essearch.admin.river.data;


/**
 * Manage Filesystem Rivers metadata
 * @author PILATO
 *
 */
public class FSRiver extends AbstractRiver {
	private static final long serialVersionUID = 1L;
	
	private String url;
	private Long updateRate;
	
	public FSRiver() {
		this("tmp", "/tmp", 60L);
	}
	
	/**
	 * @param url
	 * @param updateRate
	 */
	public FSRiver(String id, String url, Long updateRate) {
		super(id);
		this.url = url;
		this.updateRate = updateRate;
	}


	/**
	 * @param id The unique id of this river
	 * @param indexname The ES index where we store our docs
	 * @param typename The ES type we use to store docs
	 * @param type The river type
	 * @param name The human readable name for this river
	 * @param url URL where to fetch content
	 * @param updateRate Update Rate (in seconds)
	 */
	public FSRiver(String id, String indexname, String typename, String type, String name,
			String url, Long updateRate, boolean started) {
		super(id, indexname, typename, type, name, started);
		this.url = url;
		this.updateRate = updateRate;
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
